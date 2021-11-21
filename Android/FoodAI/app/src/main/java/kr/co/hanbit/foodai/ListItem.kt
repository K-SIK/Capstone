package kr.co.hanbit.foodai

// ListItem 클래스의 INSERT, SELECT, UPDATE, DELETE에 모두 사용
data class ListItem(var no: Long?, var datetime: String, var imageString: String, var foodList: Array<String>, var diary: String?, var favorite: String){

}
