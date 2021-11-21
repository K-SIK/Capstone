package kr.co.hanbit.foodai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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
            // DB에서 아이템 가져와서 값 설정
            val imageByteArray = Base64.decode(listItem.imageString, Base64.DEFAULT)
            binding.imageViewList.setImageBitmap(BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size))
            binding.textDatetime.text = listItem.datetime
            binding.textFoodList.text = helper!!.convertArrayToString(listItem.foodList, ",")
            binding.editTextDiary.setText(listItem.diary)
            if(listItem.favorite == "true"){
                binding.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on)
            }else{
                binding.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off)
            }
            // 저장 버튼
            binding.btnSaveListItem.setOnClickListener {
                // TODO: 저장하시겠습니까? 팝업창

                // 아이템 수정 (한줄 일기 업데이트)
                // listItem.diary = binding.editTextDiary.text.toString()
                helper!!.updateItem(ListItem(listItem.no, listItem.datetime, listItem.imageString,
                    listItem.foodList, binding.editTextDiary.text.toString(), listItem.favorite))
                // 리사이클러뷰에 업데이트
                listData.clear()
                listData.addAll(helper!!.selectAllItem())
                notifyDataSetChanged()
                Toast.makeText(context, "저장되었습니다", Toast.LENGTH_SHORT).show()
            }
            // 삭제 버튼
            binding.btnDeleteListItem.setOnClickListener {
                // TODO: 삭제하시겠습니다? 팝업창

                // 아이템 삭제
                helper!!.deleteItem(listItem)
                // 리사이클러뷰에 업데이트
                listData.clear()
                listData.addAll(helper!!.selectAllItem())
                notifyDataSetChanged()
                Toast.makeText(context, "삭제되었습니다", Toast.LENGTH_SHORT).show()
            }
            // 즐겨찾기 버튼
            binding.btnFavorite.setOnClickListener {
                if(listItem.favorite == "true"){
                    binding.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off)
                    listItem.favorite = "false"
                    Toast.makeText(binding.root.context, "즐겨찾기 해제되었습니다.", Toast.LENGTH_SHORT).show()
                }else{
                    binding.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on)
                    listItem.favorite = "true"
                    Toast.makeText(binding.root.context, "즐겨찾기 등록되었습니다.", Toast.LENGTH_SHORT).show()
                }
                // 데이터 변경 알리기
                helper?.updateItem(listItem)
                notifyDataSetChanged()
            }

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


}