package kr.co.hanbit.foodai

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.model.Model
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder


/*텐서플로 라이트 서포트 라이브러리의 model.Model 클래스를 이용한 Classifier 클래스 구현*/
class FoodDetector(context: Context) {
    /* 상수 선언 */
    companion object{
        // 2-1. 모델 로드: tflite 모델을 assets 디렉터리에 추가
        // 2-2. 모델 로드: 모델 파일명을 상수로 선언
        private const val MODEL_NAME = "yolov3_dynamic_range.tflite"
        // 5-1. 추론 결과 해석: 분류 클래스 라벨을 포함하는 txt 파일을 assets 디렉터리에 추가
        // 5-2. 추론 결과 해석: 라벨 파일명을 상수로 선언
        private const val LABEL_FILE = "coco.names"
    }
    /* 프로퍼티 선언 */
    var context: Context = context

    // ===========================================================================================
    // 2-4. 모델 로드: interpreter 프로퍼티 선언
    lateinit var model: Interpreter
    // Model 클래스 사용 시 Interpreter를 직접 생성할 필요가 없음
    // var model: Model
    // ============================================================================================

    // 3-1. 입력 이미지 전처리: 모델의 입력 이미지를 저장할 프로퍼티 선언
    lateinit var inputImage: TensorImage
    // 3-2. 입력 이미지 전처리: 모델의 입력 형상 프로퍼티 선언
    var modelInputChannel: Int = 0
    var modelInputWidth: Int = 0
    var modelInputHeight: Int = 0
    // 4-1. 추론: 모델의 추론된 출력 값을 저장할 프로퍼티 선언
    lateinit var outputBuffer: TensorBuffer
    lateinit var outputBufferBoxes: TensorBuffer
    lateinit var outputBufferScores: TensorBuffer
    lateinit var outputBufferClasses: TensorBuffer
    lateinit var outputBufferNums: TensorBuffer
    // 5-3. 추론 결과 해석: 라벨 목록을 저장하는 프로퍼티 선언
    private lateinit var labels: List<String>


    init{
        // ========================================================================================
        // 2-3. 모델 로드: tflite 파일 로드 (Interpreter)
        val modelFile: ByteBuffer? = FileUtil.loadMappedFile(context, MODEL_NAME)
        modelFile?.order(ByteOrder.nativeOrder())?:throw IOException()
        model = Interpreter(modelFile)
        // Model 클래스가 tflite 파일 로드부터 추론까지 모두 수행
        // model = Model.createModel(context, MODEL_NAME)
        /* 모델 성능 개선 */
        // model = createMultiThreadModel(2) // CPU 멀티스레드 모델
        // model = createGPUModel()          // GPU 위임 모델
        // model = createNNAPIModel()        // NNAPI 위임 모델
        // ========================================================================================

        // 3-4. 입력 이미지 전처리: 메서드 호출
        initModelShape()
        // 5-4. 추론 결과 해석: 라벨 파일 로드
        labels = FileUtil.loadLabels(context, LABEL_FILE)
    }

    // 3-3. 입력 이미지 전처리: 메서드 정의
    // 모델의 입력 형상과 데이터 타입을 프로퍼티에 저장
    private fun initModelShape(){
        // ========================================================================================
        // val inputTensor = interpreter.getInputTensor(0)
        val inputTensor = model.getInputTensor(0)
        // ========================================================================================
        val shape = inputTensor.shape()
        modelInputChannel = shape[0]
        modelInputWidth = shape[1]
        modelInputHeight = shape[2]
        // 모델의 입력값을 저장할 TensorImage 생성
        inputImage = TensorImage(inputTensor.dataType())

        // 4-2. 추론: 모델의 출력값을 저장할 TensorBuffer 생성
        // ========================================================================================
        // 모델 출력값 참조
        // val outputTensor = interpreter.getOutputTensor(0)
        // val outputTensor = model.getOutputTensor(0)
        val outputTensorBoxes = model.getOutputTensor(0)
        val outputTensorScores = model.getOutputTensor(1)
        val outputTensorClasses = model.getOutputTensor(2)
        val outputTensorNums = model.getOutputTensor(3)
        // ========================================================================================
        // 모델의 반환값을 저장할 텐서 버퍼 생성 (텐서버퍼는 현재 FLOAT32와 UINT8 자료형만 지원)
        // outputBuffer = TensorBuffer.createFixedSize(outputTensor.shape(), outputTensor.dataType())
        Log.i("FoodDetector", "${outputTensorBoxes.shape()}, ${outputTensorBoxes.dataType()}")
        Log.i("FoodDetector", "${outputTensorScores.shape()}, ${outputTensorScores.dataType()}")
        Log.i("FoodDetector", "${outputTensorClasses.shape()}, ${outputTensorClasses.dataType()}")
        Log.i("FoodDetector", "${outputTensorNums.shape()}, ${outputTensorNums.dataType()}")
        outputBufferBoxes = TensorBuffer.createFixedSize(intArrayOf(1,100,4), DataType.FLOAT32)
        outputBufferScores = TensorBuffer.createFixedSize(intArrayOf(1,100), DataType.FLOAT32)
        outputBufferClasses = TensorBuffer.createFixedSize(intArrayOf(1,100,2), DataType.FLOAT32)
        outputBufferNums = TensorBuffer.createFixedSize(intArrayOf(1), DataType.FLOAT32)
    }

    // TODO: 입력 이미지 전처리
    // 3-4. 입력 이미지 전처리: TensorImage에 bitmap 이미지 입력 및 이미지 전처리 로직 정의
    // Bitmap 이미지를 입력 받아 전처리하고 이를 TensorImage 형태로 반환
    private fun loadImage(bitmap: Bitmap?): TensorImage{
        // TensorImage에 이미지 데이터 저장
        // 7-2. 추가 - 데이터 포맷 변환: bitmap의 데이터 포맷이 ARGB_8888이 아닌 경우 변환
        if (bitmap != null) {
            if(bitmap.config != Bitmap.Config.ARGB_8888)
                inputImage.load(convertBitmap2ARGB8888(bitmap))
            else
                inputImage.load(bitmap)
        }
        // inputImage?.load(bitmap)

        // 전처리 ImageProcessor 정의
        val imageProcessor =
            ImageProcessor.Builder()                            // Builder 생성
                .add(ResizeOp(modelInputWidth,modelInputHeight, // 이미지 크기 변환
                    ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                .add(NormalizeOp(0.0f, 255.0f))    // 이미지 정규화
                .build()                                       // ImageProcessor 생성
        // 이미지를 전처리하여 TensorImage 형태로 반환
        return imageProcessor.process(inputImage)
    }

    // TODO: 입력 이미지 변환
    // 7-1. 추가 - 데이터 포맷 변환: loadImage 메서드의 bitmap 변수가 ARGB_8888이 아닌 경우 변환
    private fun convertBitmap2ARGB8888(bitmap: Bitmap): Bitmap{
        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    // 4-3. 추론: 추론 메서드 정의
    fun detect(image: Bitmap?): Pair<String, Float>{
        // 전처리된 입력 이미지 load
        // image: Bitmap / inputImage: TensorImage
        inputImage = loadImage(image)
        Log.d("FoodDetector", "Image loading successed")
        // ========================================================================================
        // interpreter.run(inputImage.buffer, outputBuffer.buffer.rewind())
        // Model 클래스의 파라미터는 각각 Object의 배열과 Object의 Map을 요구
        val inputs = arrayOf<Any>(inputImage.buffer as Any)
        Log.d("FoodDetector", "Input buffer created")
        // TODO: 객체 탐지 모델 추론과 반환값 (10/17)
        // 반환값을 담을 버퍼와 자리 생성
        val outputs = mutableMapOf<Int, Any>()
        outputs.put(0, outputBufferBoxes.buffer.rewind() as Any)
        outputs.put(1, outputBufferScores.buffer.rewind() as Any)
        outputs.put(2, outputBufferClasses.buffer.rewind() as Any)
        outputs.put(3, outputBufferNums.buffer.rewind() as Any)

        // val outputs = mutableListOf<TensorBuffer>()
        // outputs.put(0, outputBuffer.buffer.rewind() as Any)
        Log.d("FoodDetector", "Output buffer created")
        /* ==========10/16 수정 - 다중 반환값을 받기 위해 Model 클래스 대신 Interpreter 클래스 사용 ====== */
        model.runForMultipleInputsOutputs(inputs, outputs)
        // model.run(inputs, outputs as @NonNull Map<Int, Any>)
        Log.d("FoodDetector", "Model run successed")
        Log.i("Model return value", "${outputBufferBoxes.floatArray[0]}")
        Log.i("Model return value", "${outputBufferScores.floatArray[0]}")
        Log.i("Model return value", "${outputBufferClasses.floatArray[0]}")
        Log.i("Model return value", "${outputBufferNums.floatArray[0]}")
        // ========================================================================================

        // TODO: 반환값 전처리 (10/18 - )
        // 5-5. 추론 결과 해석: 모델이 반환한 인덱스를 라벨에 매핑하여 반환
        val output = TensorLabel(labels, outputBuffer).getMapWithFloatValue() // Map<String, Float>

        return argmax(output)

    }

    // TODO: 반환값 전처리
    // 5-6. 추론 결과 해석: Map에서 확률이 가장 높은 클래스명과 확률 쌍을 찾아서 반환하는 메서드 정의
    private fun argmax(map: Map<String, Float>): Pair<String, Float>{
        var maxKey = ""
        var maxVal = -1.0f

        for(entry in map.entries){
            var f = entry.value
            if(f > maxVal){
                maxKey = entry.key
                maxVal = f
            }
        }

        return Pair(maxKey, maxVal)
    }

    // 추론 성능 개선: CPU 멀티 스레드
    private fun createMultiThreadModel(nThreads: Int): Model{
        try {
            val optionsBuilder = Model.Options.Builder()
            optionsBuilder.setNumThreads(nThreads)
            return Model.createModel(context, MODEL_NAME, optionsBuilder.build())
        }catch(ioe: IOException){
            throw ioe
        }
    }

    // 추론 성능 개선: GPU 위임
    private fun createGPUModel(): Model {
        try {
            val optionsBuilder = Model.Options.Builder()
            val compatList = CompatibilityList()

            if (compatList.isDelegateSupportedOnThisDevice)
                optionsBuilder.setDevice(Model.Device.GPU)

            return Model.createModel(context, MODEL_NAME, optionsBuilder.build())
        }catch(ioe: IOException){
            throw ioe
        }
    }

    // 추론 성능 개선: NNAPI 위임
    private fun createNNAPIModel(): Model{
        try{
            val optionsBuilder = Model.Options.Builder()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                optionsBuilder.setDevice(Model.Device.NNAPI)

            return Model.createModel(context, MODEL_NAME, optionsBuilder.build())
        }catch(ioe: IOException){
            throw ioe
        }
    }

    // 6. 자원 해제: 자원 해제 메서드 정의
    fun finish(){
        // ========================================================================================
        // if(interpreter != null)
        //     interpreter.close()
        if(model != null)
            model.close()
        // ========================================================================================
    }

}