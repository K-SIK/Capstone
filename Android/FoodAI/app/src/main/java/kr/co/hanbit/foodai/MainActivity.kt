package kr.co.hanbit.foodai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.co.hanbit.foodai.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 하단 탭이 눌렸을 때 화면을 전환하기 위해선 이벤트 처리하기 위해 BottomNavigationView 객체 생성
        var bnv_main = binding.bnvMain

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        bnv_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.tabPhoto -> {
                    // TODO: 카메라/갤러리 불러오기
                    Toast.makeText(this@MainActivity, "'사진 가져오기' 기능 구현 전!", Toast.LENGTH_SHORT).show()
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
    }
}