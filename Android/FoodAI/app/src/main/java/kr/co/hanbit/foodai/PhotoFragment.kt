package kr.co.hanbit.foodai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.hanbit.foodai.databinding.FragmentPhotoBinding


// TODO: 메인 액티비티로부터 전달받은 값으로 탐지된 이미지와 음식 리스트 출력
class PhotoFragment : Fragment() {

    val binding by lazy {FragmentPhotoBinding.inflate(layoutInflater)}

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // 데이터 가져오기
//        val data: MutableList<PhotoItem> = loadData()
//        // 어댑터 생성 및 데이터 전달
//        var adapter = PhotoItemAdapter()
//        adapter.listData = data
//        // 리사이클러 뷰에 어댑터, 레이아웃 매니저 전달
//        binding.recyclerView.adapter = adapter
//        binding.recyclerView.layoutManager = LinearLayoutManager(this.activity)
//
//    }

    override fun onStart() { // onResume()?
        super.onStart()
        // 데이터 가져오기
        val data: MutableList<PhotoItem> = loadData()
        // 어댑터 생성 및 데이터 전달
        var adapter = PhotoItemAdapter()
        adapter.listData = data
        // 리사이클러 뷰에 어댑터, 레이아웃 매니저 전달
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.activity)

    }

    // 액티비티가 프래그먼트를 요청하면 onCreateView() 메서드를 통해 뷰를 만들어서 보여줌(리사이클러뷰의 onCreateViewHolder 메서드와 유사)
    // 파라미터 1: 레이아웃 파일을 로드하기 위한 레이아웃 인플레이터를 기본 제공
    // 파라미터 2: 프래그먼트 레이아웃이 배치되는 부모 레이아웃 (액티비티의 레이아웃)
    // 파라미터 3: 상태값 저장을 위한 보조 도구. 액티비티의 onCreate의 파라미터와 동일.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    // 목록의 아이템 클래스의 리스트를 반환하는 함수
    fun loadData(): MutableList<PhotoItem>{
        // 리턴할 MutableList 컬렉션
        val data: MutableList<PhotoItem> = mutableListOf()
        // TODO: 모델 반환값 받고 리스트에 저장
        // 예시: 100개의 가상 데이터 만들기
        for (no in 1..100){
            val detectedFood = ""
            val userInput = ""
            // 아이템 인스턴스 생성 후 반환할 리스트에 추가
            var photoItem = PhotoItem(no, detectedFood, userInput)
            data.add(photoItem)
        }

        return data
    }
}