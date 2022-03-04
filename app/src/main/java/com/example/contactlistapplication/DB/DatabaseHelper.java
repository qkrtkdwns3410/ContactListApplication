package com.example.contactlistapplication.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

/**
 *packageName    : com.example.contactlistapplication.DB
 * fileName       : DatabaseHelper
 * author         : letscombine
 * date           : 2022-03-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-03-04        letscombine       최초 생성
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	  public static final String DATABASE_NAME = "Student.db";  // 1. 데이터베이스 네임
	  public static final String TABLE_NAME = "students";        //2. 테이블 네임
	  // 3. 테이블 항목 네임
	  public static final String COL_1 = "ID";
	  public static final String COL_2 = "Name";
	  public static final String COL_3 = "Surname";
	  public static final String COL_4 = "Marks";
	  
	  public DatabaseHelper(@Nullable Context context) {
			super(context, DATABASE_NAME, null, 1);
			
	  }
	  
	  @Override
	  public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, SURNAME TEXT, MARKS TEXT)");
			
	  }
	  
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
			
	  }
	  
	  // 데이터 추가하기
	  public boolean insertData(String name, String surname, String marks) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put(COL_2, name);
			contentValues.put(COL_3, surname);
			contentValues.put(COL_4, marks);
			long result = db.insert(TABLE_NAME, null, contentValues);
			if (result == -1)
				  return false;
			else
				  return true;
	  }
	  
	  // 데이터베이스 보여주기(읽어오기)
	  public Cursor getAllData() {
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
			return res;
	  }
	  
	  //데이터베이스 수정하기(업데이트)
	  public boolean updateDatat(String id, String name, String surname, String marks) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put(COL_1, id);
			contentValues.put(COL_2, name);
			contentValues.put(COL_3, surname);
			contentValues.put(COL_4, marks);
			db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});
			return true;
	  }
}

























