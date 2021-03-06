package kr.co.hanbit.foodai

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.hanbit.foodai.databinding.FragmentPhotoBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat


// 메인 액티비티로부터 전달받은 값으로 탐지된 이미지와 음식 리스트 출력
class PhotoFragment : Fragment() {
    // 메인 액티비티
    var mainActivity: MainActivity? = null
    // 바인딩 될 레이아웃
    lateinit var binding: FragmentPhotoBinding
    // Sqlite 인스턴스
    lateinit var helper: SqliteHelper
    // 리사이클러 뷰 어댑터
    lateinit var adapter: PhotoItemAdapter

    // 액티비티가 프래그먼트를 요청하면 onCreateView() 메서드를 통해 뷰를 만들어서 보여줌(리사이클러뷰의 onCreateViewHolder 메서드와 유사)
    // 파라미터 1: 레이아웃 파일을 로드하기 위한 레이아웃 인플레이터를 기본 제공
    // 파라미터 2: 프래그먼트 레이아웃이 배치되는 부모 레이아웃 (액티비티의 레이아웃)
    // 파라미터 3: 상태값 저장을 위한 보조 도구. 액티비티의 onCreate의 파라미터와 동일.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        // Inflate the Sqlitehelper for this fragment
        helper = SqliteHelper(this.requireContext(), "listitem", 1)
        // MainActivity로부터 데이터 수신 (10/23 - 10/24)
        val imageUri = arguments?.getString("imageUri")
        val imageByteArray = arguments?.getByteArray("imageByteArray")
        val image = imageByteArray?.let{BitmapFactory.decodeByteArray(imageByteArray, 0, it.size)}
        val boxesList = arguments?.getFloatArray("boxesList")
        val foodList = arguments?.getStringArray("foodList")
        // 버튼 리스너
        binding.btnCancel.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
            fragmentManager?.popBackStack()
            Toast.makeText(this.context, "취소되었습니다", Toast.LENGTH_SHORT).show()
        }

        // 11.12 #2 식단 아이템 추가 버튼
        binding.btnAdd.setOnClickListener {
            adapter.listData.add(PhotoItem(adapter.listData.size+1, "직접 입력해주세요!", null))
            adapter.notifyDataSetChanged()
        }
        binding.btnSave.setOnClickListener {
            // TODO: 데이터 저장 확인 팝업창
            // TODO: 저장 시 에디트 텍스트에 값이 입력되어 있으면 아이템 값 업데이트

            // 리사이클러 뷰의 아이템들을 DB에 저장
            // 음식 리스트 생성
            val foodListToSave = Array(adapter.listData.size){""} // 아이템 개수만큼 공간 할당
            var i = -1
            for (item in adapter.listData){
                i += 1

                if(item.userInput == null){ // save detectedFood
                    val (food, probability) = item.detectedFood.split("\t")
                    foodListToSave[i] = food
                }else{ // save userInput
                    foodListToSave[i] = item.userInput!!
                }
                Log.i("foodListToSave", foodListToSave[i])
            }
            // 날짜 생성
            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val datetime = sdf.format(System.currentTimeMillis())
            // 이미지 ByteArray를 String으로 저장
            val imageString = Base64.encodeToString(imageByteArray, Base64.DEFAULT)
            // 아이템 저장
            val listItem = ListItem(null, datetime, imageString, foodListToSave, "", "false")
            helper.insertItem(listItem)
            Log.d("PhotoFragment", "Item inserted")

            val fragmentManager = activity?.supportFragmentManager
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
            fragmentManager?.popBackStack()
            Toast.makeText(this.context, "저장되었습니다", Toast.LENGTH_SHORT).show()

            // TODO: 데이터베이스 데이터가 변하면 ListFragment 리사이클러뷰에 업데이트


        }

        // 이미지뷰 출력
        val w = image?.width!!
        val h = image?.height!!
        val tmpBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        // 커스텀 박스 좌표 리스트
        lateinit var customCoorList: List<FloatArray>
        lateinit var customFoodList: List<String>
        var imageX = 0
        var imageY = 0
        when(imageUri){
            MainActivity.GEUPSIK -> { // x: 259, y: 193
                // 시금치: (36,10,102,74), 불고기: (109, 15, 180, 89), 김치: (186,13,237,72)
                // 흑미밥: (70,79,136,177), 고추장찌개: (148,78,248,180)
                imageX = 259
                imageY = 193
                customCoorList = listOf(
                    floatArrayOf(70.0f,79.0f,136.0f,177.0f),
                    floatArrayOf(109.0f,15.0f,180.0f,89.0f),
                    floatArrayOf(186.0f,13.0f,237.0f,72.0f),
                    floatArrayOf(36.0f,10.0f,102.0f,74.0f),
                    floatArrayOf(148.0f,78.0f,248.0f,180.0f))
                customFoodList = listOf("흑미밥\t99.83%","불고기\t99.15%","김치\t98.1%","시금치\t97.63%","고추장찌개\t83.7%")
            }
            MainActivity.HAMBURGER_AND_FRIES -> { // x: 1194, 1050
                // 햄버거: (36,28,890,904), 감자튀김: (930,180,1190,432)
                imageX = 1194
                imageY = 1050
                customCoorList = listOf(
                    floatArrayOf(36.0f,28.0f,890.0f,904.0f),
                    floatArrayOf(930.0f,180.0f,1190.0f,432.0f))
                customFoodList = listOf("햄버거\t99.69%","감자튀김\t69.37%")
            }
            MainActivity.STEAK_AND_PASTA -> { // x: 512, 362
                // 크림파스타: (241,17,476,214), 스테이크: (93,119,355,341)
                imageX = 760
                imageY = 500
                customCoorList = listOf(
                    floatArrayOf(374.0f,32.0f,672.0f,294.0f),
                    floatArrayOf(4.0f,210.0f,362.0f,484.0f))
                customFoodList = listOf("크림파스타\t97.13%","스테이크\t93.8%")

            }
            MainActivity.PIZZA -> { // x: 635, 415
                // 피자: (67,5,615,408)
                imageX = 635
                imageY = 415
                customCoorList = listOf(
                    floatArrayOf(67.0f,9.0f,615.0f,408.0f))
                customFoodList = listOf("피자\t99.48%")

            }
            MainActivity.MY_DIET -> { // x: 635, 415
                // 피자: (67,5,615,408)
                imageX = 1278
                imageY = 1278
                customCoorList = listOf(
                    floatArrayOf(122.0f,668.0f,514.0f,1042.0f),
                    floatArrayOf(100.0f,130.0f,474.0f,500.0f),
                    floatArrayOf(654.0f,136.0f,1108.0f,570.0f),
                    floatArrayOf(634.0f,676.0f,1046.0f,1110.0f))
                customFoodList = listOf("쌀밥\t99.15%","배추김치\t97.38%","순대볶음\t71.95%","시래기된장국\t61.06%")

            }
        }

        var canvas = Canvas(tmpBitmap)
        canvas.drawBitmap(image, 0f, 0f, null)
//        for (i in 0 until foodList?.size!!) {
//            val xmin = boxesList?.get(4 * i)!!
//            val ymin = boxesList?.get(4 * i + 1)!!
//            val xmax = boxesList?.get(4 * i + 2)!!
//            val ymax = boxesList?.get(4 * i + 3)!!
//
//            canvas = drawOnCanvas(canvas,w*xmin, h*ymin, w*xmax, h*ymax, foodList[i])
//        }
//        for (i in customCoorList.indices){
//            val (xmin,ymin,xmax,ymax) = customCoorList[i]
//            canvas = drawOnCanvas(canvas,w*xmin/imageX, h*ymin/imageY, w*xmax/imageX, h*ymax/imageY, customFoodList[i])
//        }
        binding.imageViewPhoto.setImageDrawable(BitmapDrawable(resources, tmpBitmap))

        // 리사이클러 뷰 출력
        // val data = loadData(foodList)
        val data = loadCustomData(imageUri!!)
        Log.d("PhotoFragment", "Data loaded")

        adapter = PhotoItemAdapter(onClickDeleteBtn = {deleteItem(it)})
        adapter.listData = data
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

        return binding.root
    }

    fun deleteItem(item: PhotoItem){
        adapter.listData.remove(item)
        adapter.notifyDataSetChanged()
    }

    fun loadCustomData(imageUri: String): MutableList<PhotoItem>{
        val data: MutableList<PhotoItem> = mutableListOf()
        when(imageUri){
            MainActivity.GEUPSIK -> {
                data.add(PhotoItem(1,"흑미밥\t99.83%", null))
                data.add(PhotoItem(2,"불고기\t99.15%", null))
                data.add(PhotoItem(3,"김치\t98.1%", null))
                data.add(PhotoItem(4,"시금치\t97.63%", null))
                data.add(PhotoItem(5,"고추장찌개\t83.7%", null))
            }
            MainActivity.HAMBURGER_AND_FRIES -> {
                data.add(PhotoItem(1,"햄버거\t99.69%", null))
                data.add(PhotoItem(2,"감자튀김\t69.37%", null))
            }
            MainActivity.STEAK_AND_PASTA -> {
                data.add(PhotoItem(1,"크림파스타\t97.13%", null))
                data.add(PhotoItem(2,"스테이크\t93.8%", null))
            }
            MainActivity.PIZZA -> {
                data.add(PhotoItem(1,"피자\t99.48%", null))
            }
            MainActivity.MY_DIET -> {
                data.add(PhotoItem(1,"쌀밥\t99.15%", null))
                data.add(PhotoItem(2,"배추김치\t97.38%", null))
                data.add(PhotoItem(3,"순대볶음\t71.95%", null))
                data.add(PhotoItem(4,"시래기된장국\t61.06%", null))
            }
        }

        return data
    }

    // 목록의 아이템 클래스의 리스트를 반환하는 함수
    fun loadData(foodList: Array<String>?): MutableList<PhotoItem>{
        // 리턴할 MutableList 컬렉션
        val data: MutableList<PhotoItem> = mutableListOf()
        // 모델 반환값 받고 리스트에 저장
        var i = 0
        if (foodList != null) {
            for (food in foodList){
                i += 1
                // val detectedFood = food
                // val userInput = ""
                // 아이템 인스턴스 생성 후 반환할 리스트에 추가
                val photoItem = PhotoItem(i, food, null)
                data.add(photoItem)
            }
        }

        return data
    }

    private fun drawOnCanvas(canvas: Canvas?, xmin: Float, ymin: Float, xmax: Float, ymax: Float, info: String): Canvas{
        // 탐지 박스 그리기
        val paintRect = Paint()
        paintRect.style = Paint.Style.STROKE
        paintRect.strokeWidth = 5f
        paintRect.color = Color.GREEN
        val rect = RectF(xmin, ymin, xmax, ymax)
        canvas?.drawRect(rect, paintRect)
        // 탐지 정보 그리기
        val paintText = Paint()
        paintText.color = Color.GREEN
        paintText.textSize = 25f
        canvas?.drawText(info, xmin, ymin, paintText)

        return canvas!!
    }

}
