package kr.co.hanbit.foodai
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.R
import kr.co.hanbit.foodai.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        val fragmentList = listOf(HomeFragment1(), HomeFragment2(), HomeFragment3(), HomeFragment4())

        val binding by lazy {
            FragmentHomeBinding.inflate(layoutInflater)
        }
        val adapter = FragmentAdapter(requireActivity())
        adapter.fragmentList = fragmentList
        binding.viewPager.adapter = adapter

        return binding.root
    }

}