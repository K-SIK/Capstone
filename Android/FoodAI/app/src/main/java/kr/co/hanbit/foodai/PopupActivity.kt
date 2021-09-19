package kr.co.hanbit.foodai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import kr.co.hanbit.foodai.databinding.ActivityPopupBinding

class PopupActivity : AppCompatActivity() {

    val binding by lazy {ActivityPopupBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

    }
    fun openCamera(){
        val intent = Intent()
        intent.putExtra("result", "openCamera")
        setResult(RESULT_OK, intent)

        finish()
    }
    fun openGallery(){
        val intent = Intent()
        intent.putExtra("result", "openGallery")
        setResult(RESULT_OK, intent)

        finish()
    }
}