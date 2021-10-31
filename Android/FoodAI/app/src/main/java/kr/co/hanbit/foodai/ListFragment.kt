package kr.co.hanbit.foodai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.hanbit.foodai.databinding.FragmentListBinding
import kr.co.hanbit.foodai.databinding.FragmentPhotoBinding
import java.io.IOException


class ListFragment : Fragment() {
    // 메인 액티비티
    var mainActivity: MainActivity? = null
    // 바인딩 될 레이아웃
    lateinit var binding: FragmentListBinding
    // Sqlite 인스턴스
    lateinit var helper: SqliteHelper
    // 리사이클러 뷰 어댑터
    lateinit var adapter: ListItemAdapter
    // 컨텍스트
    // lateinit var context: Context


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        // Inflate the Sqlitehelper for this fragment
        helper = SqliteHelper(this.requireContext(), "listitem", 1)

        // DB에서 데이터 가져오기
        adapter = ListItemAdapter()
        adapter.helper = helper
        adapter.context = this.context
        adapter.listData.addAll(helper.selectItem())
//        for (item in helper.selectItem()){
//            val (no, datetime, imageUri, foodList, diary) = item
//            val image =
//        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)





        return binding.root
    }

//    // DB에 있는 아이템들을 가져오는 메서드
//    fun loadData(): MutableList<ListItem>{
//        val data: MutableList<ListItem> = mutableListOf()
//
//
//
//        return data
//    }


    // Uri를 이용해서 미디어스토어에 저장된 이미지를 읽어오는 메서드
    // 파라미터로 Uri를 받아 결괏괎을 Bitmap으로 반환
    fun loadBitmap(photoUri: Uri): Bitmap?{
        var image: Bitmap? = null
        // API 버전이 27 이하이면 MediaStore에 있는 getBitmap 메서드를 사용하고, 27보다 크면 ImageDecoder 사용
        try{
            image = if (Build.VERSION.SDK_INT > 27){
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(this.requireActivity().contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            }else{
                MediaStore.Images.Media.getBitmap(this.requireActivity().contentResolver, photoUri)
            }
        }catch(e: IOException){
            e.printStackTrace()
        }

        return image
    }

}