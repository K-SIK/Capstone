package kr.co.hanbit.foodai

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import kr.co.hanbit.foodai.databinding.ActivityMainBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.io.ByteArrayOutputStream

class MainActivity : BaseActivity() {

    // 권한 관련 상수 선언
    companion object{
        const val PERM_STORAGE = 99 // 외부 저장소 권한 요청
        const val PERM_CAMERA = 100 // 카메라 권한 요청
        const val REQ_CAMERA = 101  // 카메라 호출
        const val REQ_STORAGE = 102 // 갤러리 호출
        const val POPUP_ACTIVITY = 103 // 팝업 액티비티 호출
    }

    // 전역 프로퍼티
    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    var photoUri: Uri? = null
    var photoFragment: PhotoFragment? = null
    lateinit var foodDetector: FoodDetector


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 하단 탭이 눌렸을 때 화면을 전환하기 위해선 이벤트 처리하기 위해 BottomNavigationView 객체 생성
        val bnv_main = binding.bnvMain

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        bnv_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.tabPhoto -> {
                    // 카메라 권한 요청
                    requirePermissions(arrayOf(Manifest.permission.CAMERA), PERM_CAMERA)
                    if (photoFragment != null){
                        val fragmentManager = supportFragmentManager
                        fragmentManager.beginTransaction().remove(photoFragment!!).commit()
                        fragmentManager.popBackStack()
                        Toast.makeText(this.context, "작업 초기화!", Toast.LENGTH_SHORT).show()
                    }
                    // TODO: PhotoFragment에 모델 반환값 전달 (10/23 - )
                    Log.d("tabPhoto", "PhotoFragment called")
                }
                R.id.tabAR -> {
                    // TODO: AR 기능
                    Toast.makeText(this@MainActivity, "'AR' 기능 구현 전!", Toast.LENGTH_SHORT).show()
                }
                R.id.tabHome -> {
                    // 다른 프래그먼트 화면으로 이동하는 기능
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, homeFragment).commit()
                }
                R.id.tabList -> {
                    val listFragment = ListFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, listFragment).commit()
                }
                R.id.tabRecommendation -> {
                    val recommendationFragment = RecommendationFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, recommendationFragment).commit()
                }
            }
            true
        }
            selectedItemId = R.id.tabHome
        }

        // 외부 저장소(갤러리) 권한 요청
        requirePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_STORAGE)
    }

    // 권한 요청 승인 시 호출
    override fun permissionGranted(requestCode: Int){
        when(requestCode){
            // 외부 저장소 권한
            PERM_STORAGE -> {

            }
            // 카메라 권한
            PERM_CAMERA -> {
                // 카메라/갤러리 선택
                val intent = Intent(this@MainActivity, PopupActivity::class.java)
                startActivityForResult(intent, POPUP_ACTIVITY)
            }
            // 카메라 호출
            REQ_CAMERA -> {

            }
        }
    }
    // 권한 요청 거부 시 호출
    override fun permissionDenied(requestCode: Int) {
        when(requestCode){
            // 외부 저장소 권한
            PERM_STORAGE -> {
                Toast.makeText(baseContext,
                                "외부 저장소 권한을 승인해야 앱을 사용할 수 있습니다.",
                                Toast.LENGTH_LONG).show()
                finish()
            }
            // 카메라 권한
            PERM_CAMERA -> {
                Toast.makeText(baseContext,
                                "카메라 권한을 승인해야 카메라를 사용할 수 있습니다.",
                                Toast.LENGTH_LONG).show()

            }
            // 카메라 호출
            REQ_CAMERA -> {

            }
        }
    }

//    fun setViews(){
//        val intent = Intent(this, PopupActivity::class.java)
//        startActivityForResult(intent, POPUP_ACTIVITY)
//    }

    // 촬영한 이미지를 저장할 Uri를 미디어스토어에 생성하는 메서드
    fun createImageUri(filename: String, mimeType: String): Uri?{
        // ContentValues 클래스를 사용해 파일명과 파일의 타입을 입력한 후,
        // ContentResolver의 insert() 메서드를 통해 저장
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        // 파라미터: 저장소명, 저장할 콘텐트
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    // 실질적인 카메라 호출 메서드
    fun openCamera(){
        // 카메라 호출 시 보낼 인텐트
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // 촬영한 이미지를 바로 사용하지 않고 Uri로 생성하여 미디어스토어에 저장하고 사용
        createImageUri(newFileName(), "image/jpg")?.let{uri->
            photoUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, REQ_CAMERA)
        }
    }

    // 새로운 파일명을 만들어주는 메서드 (파일명이 중복되지 않도록 현재 시각 사용)
    fun newFileName(): String{
        // SimpleDateFormat(java.text) 사용
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        // 연월일_시간.jpg
        return "$filename.jpg"
    }

    // Uri를 이용해서 미디어스토어에 저장된 이미지를 읽어오는 메서드
    // 파라미터로 Uri를 받아 결괏괎을 Bitmap으로 반환
    fun loadBitmap(photoUri: Uri): Bitmap?{
        var image: Bitmap? = null
        // API 버전이 27 이하이면 MediaStore에 있는 getBitmap 메서드를 사용하고, 27보다 크면 ImageDecoder 사용
        try{
            image = if (Build.VERSION.SDK_INT > 27){
                val source: ImageDecoder.Source =
                        ImageDecoder.createSource(this.contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            }else{
                MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
            }
        }catch(e: IOException){
            e.printStackTrace()
        }

        return image
    }

    // 갤러리 열람 메서드
    fun openGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT).setType("image/*")
        // val intent = Intent(Intent.ACTION_PICK)
        // intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQ_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK){
            when(requestCode){
                POPUP_ACTIVITY -> {
                    val selection = data?.getStringExtra("result")
                    if(selection == "openCamera"){ // 카메라
                        // 카메라 호출
                        Toast.makeText(baseContext, "카메라 선택", Toast.LENGTH_SHORT).show()
                        openCamera()

                    }else if(selection == "openGallery") { // 갤러리
                        // 갤러리 호출
                        Toast.makeText(baseContext, "갤러리 선택", Toast.LENGTH_SHORT).show()
                        openGallery()
                    }
                }
                REQ_CAMERA -> {
                    Toast.makeText(baseContext, "사진 촬영 완료!", Toast.LENGTH_SHORT).show()
                    lateinit var result: Triple<Bitmap?, FloatArray, Array<String>>
                    photoUri?.let{ uri ->
                        val capturedImage = loadBitmap(uri)
                        // 비트맵을 모델에 전달하여 추론 수행
                        val (boxesList, foodList) = callFoodDetector(capturedImage)
                        setPhotoFragment(Triple(uri, boxesList, foodList))
                        photoUri = null
                    }

                }
                REQ_STORAGE -> {
                    Toast.makeText(baseContext, "사진 선택 완료!", Toast.LENGTH_SHORT).show()
                    val selectedImageUri: Uri = data?.data as Uri
                    val selectedImage = loadBitmap(selectedImageUri)
                    // 비트맵을 모델에 전달하여 추론 수행
                    val (boxesList, foodList) = callFoodDetector(selectedImage)
                    setPhotoFragment(Triple(selectedImageUri, boxesList, foodList))

                }

            }
        }else if(resultCode == RESULT_CANCELED){
            when (requestCode){
                POPUP_ACTIVITY -> {

                }
                REQ_CAMERA -> {

                }
                REQ_STORAGE -> {

                }

            }
        }else{

        }
    }

    private fun setPhotoFragment(data: Triple<Uri, FloatArray?, Array<String>>){
        photoFragment = PhotoFragment()
        Log.d("MainActivity", "set Photo Fragment")
        Toast.makeText(this, "PhotoFragment 호출", Toast.LENGTH_SHORT).show()

        // 번들을 생성하고 전달할 값을 담는다.
        var bundle = Bundle()
        val (imageUri, boxesList, foodList) = data
        // 이미지
//        val stream = ByteArrayOutputStream()
//        image?.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        val imageByteArray = stream.toByteArray()
//        bundle.putByteArray("imageByteArray", imageByteArray)
        bundle.putString("imageUri", imageUri.toString())
        // 박스 좌표 리스트
        bundle.putFloatArray("boxesList", boxesList)
        // 음식+확률 문자열 리스트
        bundle.putStringArray("foodList", foodList)

        // 값이 담긴 번들을 프래그먼트의 arguments에 담는다.
        photoFragment!!.arguments = bundle

        supportFragmentManager.beginTransaction().replace(R.id.fl_container, photoFragment!!).commit()
    }

    private fun callFoodDetector(bitmap: Bitmap?): Pair<FloatArray, Array<String>>{
        foodDetector = FoodDetector(this)
        // TODO: 모델 반환값 가공 및 반환
        Log.d("MainActivity", "called FoodDetector")
        // 반환값: Image 비트맵, 객체 탐지 박스좌표 List, detectedFood(+probability) List
        val output: Pair<FloatArray, Array<String>> = foodDetector.detect(bitmap)
        Log.d("MainActivity", "return model output")
        return output
    }

    override fun onDestroy() {
        super.onDestroy()
        // lateinit 검사
        if(::foodDetector.isInitialized)
            foodDetector.finish()
    }
}