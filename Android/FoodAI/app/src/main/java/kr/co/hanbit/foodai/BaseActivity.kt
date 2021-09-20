package kr.co.hanbit.foodai

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

// 액티비티이지만 직접 실행되는 것을 방지하고 상속받은 액티비티(구현체)에서만
// 사용할 수 있게 '추상 클래스'로 정의
abstract class BaseActivity: AppCompatActivity() {
    // 상속받은 액티비티에게 구현을 강제함
    abstract fun permissionGranted(requestCode :Int)
    abstract fun permissionDenied(requestCode :Int)

    // 자식 액티비티에서 권한 요청 시 직접 호출하는 메서드
    // 파라미터: 요청권한 배열, 리퀘스트 코드
    fun requirePermissions(permissions: Array<String>, requestCode: Int){
        // 안드로이드 버전 체크
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){ // 6.0 미만
            permissionGranted(requestCode)
        }
        else{ // 권한 체크를 해야하는 버전
            // 권한이 모두 승인되었는 지 확인
            val isAllPermissionsGranted = permissions.all{
                checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
            }
            // 권한 승인여부에 따라 분기
            if (isAllPermissionsGranted){ // 모두 승인되었다면,
                permissionGranted(requestCode)
            }
            else{ // 미승인 권한이 있다면,
                ActivityCompat.requestPermissions(this, permissions, requestCode)
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.all{ it == PackageManager.PERMISSION_GRANTED}){
            permissionGranted(requestCode)
        }
        else{
            permissionDenied(requestCode)
        }
    }

}