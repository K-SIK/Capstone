package kr.co.hanbit.foodai

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.hanbit.foodai.databinding.FragmentPhotoBinding


// TODO: 메인 액티비티로부터 전달받은 값으로 탐지된 이미지와 음식 리스트 출력
class PhotoFragment : Fragment() {
    // 메인 액티비티
    var mainActivity: MainActivity? = null
    // 바인딩 될 레이아웃
    lateinit var binding: FragmentPhotoBinding

    // 액티비티가 프래그먼트를 요청하면 onCreateView() 메서드를 통해 뷰를 만들어서 보여줌(리사이클러뷰의 onCreateViewHolder 메서드와 유사)
    // 파라미터 1: 레이아웃 파일을 로드하기 위한 레이아웃 인플레이터를 기본 제공
    // 파라미터 2: 프래그먼트 레이아웃이 배치되는 부모 레이아웃 (액티비티의 레이아웃)
    // 파라미터 3: 상태값 저장을 위한 보조 도구. 액티비티의 onCreate의 파라미터와 동일.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_list, container, false)
        binding = FragmentPhotoBinding.inflate(inflater, container, false)

        // TODO: 이미지/리사이클러 뷰 반환값 출력 (10/24 - )
        val imageByteArray = arguments?.getByteArray("imageByteArray")
        val image = imageByteArray?.let { BitmapFactory.decodeByteArray(imageByteArray, 0, it.size) }
        val boxesList = arguments?.getFloatArray("boxesList")
        val foodList = arguments?.getStringArray("foodList")
        if (foodList != null) {
            for (food in foodList){
                Log.i("FoodList", food)
            }
        }
        // 이미지뷰 출력
        val w = image?.width!!
        val h = image?.height!!
        val tmpBitmap = Bitmap.createBitmap(w, h, image?.config!!)
        var canvas = Canvas(tmpBitmap)
        canvas.drawBitmap(image, 0f, 0f, null)
        for (i in 0 until foodList?.size!!) {
            val xmin = boxesList?.get(4 * i)!!
            val ymin = boxesList?.get(4 * i + 1)!!
            val xmax = boxesList?.get(4 * i + 2)!!
            val ymax = boxesList?.get(4 * i + 3)!!

            canvas = drawOnCanvas(canvas,w*xmin, h*ymin, w*xmax, h*ymax)
        }
        binding.imageView.setImageDrawable(BitmapDrawable(resources, tmpBitmap))

        // 리사이클러 뷰 출력
        val data = loadData(foodList)
        Log.d("PhotoFragment", "Data loaded")

        val adapter = PhotoItemAdapter()
        adapter.listData = data
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

        return binding.root
    }

    // 목록의 아이템 클래스의 리스트를 반환하는 함수
    fun loadData(foodList: Array<String>?): MutableList<PhotoItem>{
        // 리턴할 MutableList 컬렉션
        val data: MutableList<PhotoItem> = mutableListOf()
        // TODO: 모델 반환값 받고 리스트에 저장
        var i = 0
        if (foodList != null) {
            for (food in foodList){
                i += 1
                // val detectedFood = food
                // val userInput = ""
                // 아이템 인스턴스 생성 후 반환할 리스트에 추가
                val photoItem = PhotoItem(i, food, "")
                data.add(photoItem)
            }
        }

        return data
    }



    private fun drawOnCanvas(canvas: Canvas?, xmin: Float, ymin: Float, xmax: Float, ymax: Float): Canvas{
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = Color.GREEN
        val rect = RectF(xmin, ymin, xmax, ymax)

        canvas?.drawRect(rect, paint)

        return canvas!!
    }
}

// View를 상속받는 클래스
class CustomView(context: Context): View(context){
    // onDraw 메서드 오버라이딩
    override fun onDraw(canvas: Canvas?){
        super.onDraw(canvas)


    }
}