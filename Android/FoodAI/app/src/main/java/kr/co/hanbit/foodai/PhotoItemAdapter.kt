package kr.co.hanbit.foodai

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kr.co.hanbit.foodai.databinding.PhotoitemRecyclerBinding
import kotlin.coroutines.coroutineContext

class PhotoItemAdapter(
    val onClickDeleteBtn: (item: PhotoItem) -> Unit
): RecyclerView.Adapter<PhotoItemAdapter.PhotoItemHolder>() {
    // 존재하는 데이터 리스트
    var listData = mutableListOf<PhotoItem>()
    // SQliteHelper 프로퍼티
    var helper: SqliteHelper? = null

    // 뷰 홀더 내부 클래스
    inner class PhotoItemHolder(val binding: PhotoitemRecyclerBinding): RecyclerView.ViewHolder(binding.root){
        // setPhotoItem 메서드로 넘어온 PhotoItem 임시 저장
        var mPhotoItem: PhotoItem? = null
        init{

        }
        // 아이템(레이아웃)에 데이터를 세팅하는 메서드 - onBindViewHolder에서 호출
        fun setPhotoItem(photoItem: PhotoItem){
            binding.textNo.text = "${photoItem.no}"
            binding.radioDetectedFood.text = "${photoItem.detectedFood}"
            // binding.editUserInput.text = "${photoItem.userInput}"
            itemView.setOnClickListener {
                if(binding.radioDetectedFood.isChecked){
                    binding.editUserInput.text = null
                    binding.editUserInput.isClickable = false
                    binding.editUserInput.isFocusable = false
                }
                else if (binding.radioUserInput.isChecked){
                    binding.editUserInput.isClickable = true
                    binding.editUserInput.isFocusable = true
                }
            }
        }

    }

    // 스마트폰의 한 화면에 그려지는 아이템 개수만큼 아이템 레이아웃 생성
    // 안드로이드는 ViewHolder 클래스를 메모리에 저장했다가 요청이 있을 때마다 메서드를 실행하여 꺼내서 사용한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemHolder {
        // 어댑터에서 사용하는 바인딩
        val binding = PhotoitemRecyclerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return PhotoItemHolder(binding)
    }
    // 생성된 레이아웃에 값 입력 후 목록에 출력 (생성된 뷰 홀더를 출력)
    override fun onBindViewHolder(holder: PhotoItemHolder, position: Int) {
        val photoItem = listData.get(position)
        holder.setPhotoItem(photoItem)
        // 아이템 수정 리스너
        holder.binding.btnModifyItem.setOnClickListener {
            photoItem.userInput = holder.binding.editUserInput.text.toString()
            Toast.makeText(holder.itemView.context, "아이템이 수정되었습니다.", Toast.LENGTH_SHORT).show()
        }
        // 아이템 삭제 리스너
        holder.binding.btnDeleteItem.setOnClickListener {
            onClickDeleteBtn.invoke(photoItem)
            Toast.makeText(holder.itemView.context, "아이템이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    // 목록에 보여줄 아이템 개수
    override fun getItemCount(): Int {
        return listData.size
    }
}