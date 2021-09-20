package kr.co.hanbit.foodai


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import kr.co.hanbit.foodai.databinding.ActivityPopupBinding

class PopupActivity : Activity() {

    val binding by lazy {ActivityPopupBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE) // -> Activity 상속 필요
        setContentView(binding.root)

    }
    fun openCamera(view: View){
        val intent = Intent()
        intent.putExtra("result", "openCamera")
        setResult(RESULT_OK, intent)

        finish()
    }
    fun openGallery(view: View){
        val intent = Intent()
        intent.putExtra("result", "openGallery")
        setResult(RESULT_OK, intent)

        finish()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // 바깥 배경 클릭 시 닫히지 않도록 함
        return event?.getAction() != MotionEvent.ACTION_OUTSIDE
    }
}