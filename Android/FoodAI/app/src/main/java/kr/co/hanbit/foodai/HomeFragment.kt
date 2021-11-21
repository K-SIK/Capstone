package kr.co.hanbit.foodai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.R
import androidx.viewpager.widget.PagerAdapter

import androidx.viewpager.widget.ViewPager
import kr.co.hanbit.foodai.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)

        val fragmentList = listOf(HomeFragment1(), HomeFragment2(), HomeFragment3(), HomeFragment4())
        // Inflate the layout for this fragment
        // HomeViewPager()
        val binding by lazy {
            FragmentHomeBinding.inflate(layoutInflater)
        }
        val adapter = FragmentAdapter(requireActivity())
        adapter.fragmentList = fragmentList
        binding.viewPager.adapter = adapter
            // FragmentAdapter(requireActivity().childFragmentManager, 1)
        // 어댑터 생성. 아이템 리스트를 파라미터로 넣어준다.


        // R.id.constraintLayoutHome.viewPager
        // R.id.viewPager
        // R.id.fragment_home.viewPager

        return binding.root
    }

}