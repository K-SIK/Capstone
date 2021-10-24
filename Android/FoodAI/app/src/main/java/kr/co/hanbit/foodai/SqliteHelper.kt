package kr.co.hanbit.foodai

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// PhotoFragment와 ListFragment를 연결하여 ListFragment의 RecyclerView에 출력될 DB
class SqliteHelper(context: Context, name: String, version: Int):SQLiteOpenHelper(context, name, null, version) {
    // Q. DB에 리스트 형태의 데이터를 저장할 수 있나??
    /* params: no: Long, datetime: String, imageUri: String, foodList: Array<String>, diary: String*/

    // 데이터베이스 최초 생성 시 호출
    override fun onCreate(db: SQLiteDatabase?) {
        // 테이블 생성 쿼리
        val create = "create table listitem" +
                "(" +
                "no integer primary key" // 순서
                "datetime text, " +      // 날짜/시간
                "imageuri text, " +      // 이미지 주소
                "foodlist blob" +        // 음식 리스트
                "diary text" +           // 일기 및 기록장
                ")"
        // DB의 execSQL 메서드에 전달하여 쿼리 실행
        db?.execSQL(create)
    }
    // SqliteHelper에 전달되는 버전 정보가 변경되었을 때 현재 생성되어 있는 데이터베이스의 버전보다 높으면 호출
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // TODO("Not yet implemented")
    }

}