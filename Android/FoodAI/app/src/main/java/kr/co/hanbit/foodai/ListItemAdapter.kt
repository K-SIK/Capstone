package kr.co.hanbit.foodai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import kr.co.hanbit.foodai.databinding.ListitemRecyclerBinding
import java.io.IOException

class ListItemAdapter: RecyclerView.Adapter<ListItemAdapter.ListItemHolder>() {

    // 존재하는 데이터 리스트
    var listData = mutableListOf<ListItem>()
    // SQliteHelper 프로퍼티
    var helper: SqliteHelper? = null
    // 컨텍스트(ListFragment.activity)
    var context: Context? = null

    inner class ListItemHolder(val binding: ListitemRecyclerBinding): RecyclerView.ViewHolder(binding.root){

        fun setListItem(listItem: ListItem){
            // TODO: DB에서 아이템 가져와서 값 설정
            binding.imageViewList.setImageBitmap(loadBitmap(listItem.imageUri.toUri()))
            binding.textDatetime.text = listItem.datetime
            binding.textFoodList.text = helper!!.convertArrayToString(listItem.foodList, ",")
        }
    }

    // 스마트폰의 한 화면에 그려지는 아이템 개수만큼 아이템 레이아웃 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val binding = ListitemRecyclerBinding.inflate(LayoutInflater.from(parent.context),
                                                        parent, false)
        return ListItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val listItem = listData.get(position)
        holder.setListItem(listItem)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    // Uri를 이용해서 미디어스토어에 저장된 이미지를 읽어오는 메서드
    // 파라미터로 Uri를 받아 결괏괎을 Bitmap으로 반환
    fun loadBitmap(photoUri: Uri): Bitmap?{
        var image: Bitmap? = null
        // API 버전이 27 이하이면 MediaStore에 있는 getBitmap 메서드를 사용하고, 27보다 크면 ImageDecoder 사용
        try{
            image = if (Build.VERSION.SDK_INT > 27){
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(this.context!!.contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            }else{
                MediaStore.Images.Media.getBitmap(this.context!!.contentResolver, photoUri)
            }
        }catch(e: IOException){
            e.printStackTrace()
        }

        return image
    }

}