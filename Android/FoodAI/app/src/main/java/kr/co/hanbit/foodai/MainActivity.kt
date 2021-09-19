package kr.co.hanbit.foodai

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.co.hanbit.foodai.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    // 권한 관련 상수 선언
    companion object{
        const val PERM_STORAGE = 99 // 외부 저장소 권한 요청
        const val PERM_CAMERA = 100 // 카메라 권한 요청
        const val REQ_CAMERA = 101  // 카메라 호출
        const val REQ_STORAGE = 102 // 갤러리 호출
    }

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
                    // 카메라 권한 요청
                    requirePermissions(arrayOf(Manifest.permission.CAMERA), PERM_CAMERA)
                    // TODO: 카메라/갤러리 선택

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
                // setViews() // 카메라 권한 요청
            }
            // 카메라 권한
            PERM_CAMERA -> {

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
//    // 카메라 권한 요청
//    fun setViews(){
//        // 승인 되었을 경우에만 permissionGranted() 에서 카메라 요청
//        binding.bnvMain.
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }
}