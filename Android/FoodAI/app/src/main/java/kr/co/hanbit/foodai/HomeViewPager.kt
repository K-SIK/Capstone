package kr.co.hanbit.foodai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2

 class HomeViewPager : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_view_pager)

        val viewPager: ViewPager2 = findViewById(R.id.view_Pager)

        val fragmentList = listOf(HomeFragment1(), HomeFragment2(), HomeFragment3(), HomeFragment4())
        val adapter = FragmentAdapter(this)
        // 어댑터 생성. 아이템 리스트를 파라미터로 넣어준다.

        adapter.fragmentList = fragmentList
        viewPager.adapter = adapter

    }
}
