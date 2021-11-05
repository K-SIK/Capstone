package kr.co.hanbit.foodai

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    var fragmentList = listOf<Fragment>()

    // 페이지 개수 결정하기 위한 프래그먼트 개수 리턴
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    // 페이지 요청시 해당 position 의 프래그먼트 1개 리턴
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}