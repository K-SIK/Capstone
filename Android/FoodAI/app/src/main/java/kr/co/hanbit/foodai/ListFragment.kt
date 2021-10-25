package kr.co.hanbit.foodai

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
import java.io.IOException


class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


    // Uri를 이용해서 미디어스토어에 저장된 이미지를 읽어오는 메서드
    // 파라미터로 Uri를 받아 결괏괎을 Bitmap으로 반환
    fun loadBitmap(photoUri: Uri): Bitmap?{
        var image: Bitmap? = null
        // API 버전이 27 이하이면 MediaStore에 있는 getBitmap 메서드를 사용하고, 27보다 크면 ImageDecoder 사용
        try{
            image = if (Build.VERSION.SDK_INT > 27){
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(this.requireActivity().contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            }else{
                MediaStore.Images.Media.getBitmap(this.requireActivity().contentResolver, photoUri)
            }
        }catch(e: IOException){
            e.printStackTrace()
        }

        return image
    }

}