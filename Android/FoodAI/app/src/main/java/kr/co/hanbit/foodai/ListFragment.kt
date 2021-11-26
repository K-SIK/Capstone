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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kr.co.hanbit.foodai.databinding.FragmentListBinding
import kr.co.hanbit.foodai.databinding.FragmentPhotoBinding
import java.io.IOException


class ListFragment : Fragment() {
    companion object{
        const val GET_ALL_LIST = 0
        const val GET_FAVORITE_LIST = 1
    }


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
    // 수정할 데이터를 임시 저장
    var updateListItem: ListItem? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        // Inflate the Sqlitehelper for this fragment
        helper = SqliteHelper(this.requireContext(), "listitem", 1)
        // 리사이클러 뷰 어댑터 설정
        adapter = ListItemAdapter()
        adapter.helper = helper
        adapter.context = this.context
        val dividerItemDecoration = DividerItemDecoration(binding.recyclerView.context, LinearLayoutManager(this.context).orientation)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        // 초기 화면은 전체 식단 출력
        var data: MutableList<ListItem>
        data = loadData(GET_ALL_LIST)
        setData(data)

        // 탭 레이아웃 리스너
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> {
                        data = loadData(GET_ALL_LIST)
                    }
                    1 -> {
                        data = loadData(GET_FAVORITE_LIST)
                    }
                }
                setData(data)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })


        return binding.root
    }

    // DB에 저장된 아이템 리스트 불러오기
    private fun loadData(tabNumber: Int): MutableList<ListItem>{
        var data: MutableList<ListItem> = mutableListOf()
        when(tabNumber){
            GET_ALL_LIST -> {
                data = helper.selectAllItem()
            }
            GET_FAVORITE_LIST -> {
                data = helper.selectFavoriteItem()
            }
        }

        return data
    }

    // 리사이클러 뷰에 아이템 출력하기
    private fun setData(data: List<ListItem>){
        adapter.listData.clear()
        adapter.listData.addAll(data)
        adapter.notifyDataSetChanged()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

}