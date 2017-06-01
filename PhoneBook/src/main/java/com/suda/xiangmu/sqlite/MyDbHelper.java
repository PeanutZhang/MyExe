package com.suda.xiangmu.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper {
	private Context mcontext=null;
	private static final String DB_NAME="list.db";
	private static final String TABLE_RECORD=  //拦截记录
			"create table record(number text primary key, frquency int, time text,day text)";
	private static final String TABLE_BLACK=    //黑名单
			"create table black(number text primary key, name text,frequency text)";
	private static final String TABLE_WHITE=    //白名单
			"create table white(number text primary key, name text)";
	private static final String TABLE_ADDRESSBOOK=     //通讯录
			"create table addbook(number text, name text)";
	private static final String TABLE_STRANGER=  //陌生拦截记录
			"create table stranger(number text primary key,num int,count int,starthour int,startminute int,context text)";
	//数据库版本
	private static final int DB_VERSION=1;
	private SQLiteDatabase sqlitedatabase;
	private MySQLiteOpenHelper mysqliteopen;
	public MyDbHelper(Context context){
		this.mcontext=context;
	}
	
	private static class MySQLiteOpenHelper extends SQLiteOpenHelper{

		public MySQLiteOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_RECORD);     //创建表
			db.execSQL(TABLE_BLACK);
			db.execSQL(TABLE_WHITE);
			db.execSQL(TABLE_ADDRESSBOOK);
			db.execSQL(TABLE_STRANGER);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			db.execSQL("drop table if exists record");    //删除表
			db.execSQL("drop table if exists black");
			db.execSQL("drop table if exists white");
			db.execSQL("drop table if exists addbook");
			db.execSQL("drop table if exists stranger");
			onCreate(db);
		}
		
	}
	public void open(){         //打开数据库
		mysqliteopen=new MySQLiteOpenHelper(mcontext,DB_NAME,null,DB_VERSION);
		sqlitedatabase=mysqliteopen.getWritableDatabase();
	}
	public void close(){       //删除数据库
		mysqliteopen.close();
	}
	//插入数据01 保存信息
	public long insertData(String number,String name,String table){
		ContentValues v=new ContentValues();
		v.put("number", number);
		v.put("name", name);
		return sqlitedatabase.insert(table, null, v);
	}
	
	public long insertData(String number,String name,String frequency,String table){
		ContentValues v=new ContentValues();
		v.put("number", number);
		v.put("name", name);
		v.put("frequency",frequency );
		return sqlitedatabase.insert(table, null, v);
	}
	
	//插入数据03 重载
	public long insertData(String number,int count,String time,String day,String table){
		ContentValues v=new ContentValues();
		v.put("number", number);
		v.put("frquency", count);
		v.put("time", time);
		v.put("day", day);
		return sqlitedatabase.insert(table, null, v);
	}
	/**
	 * 插入数据03 重载
	 * @param number  电话号码
	 * @param count  拦截次数
	 * @param startime  第一次拦截时间
	 * @param num    手机号码序号
	 * @param smscontent 短信内容
	 * @param table  表名
	 * @return
	 */
	public long insertData(String number,int num,int count,int startime,int startminute,String smscontent,String table){
		ContentValues v=new ContentValues();
		v.put("number", number);
		v.put("count", count);
		v.put("starthour", startime);
		v.put("startminute", startminute);
		v.put("num", num);
		v.put("context",smscontent);
		return sqlitedatabase.insert(table, null, v);
	}
	//更新名单信息
	public boolean updataData(String number,String name,String table){
		String str="update "+table+" set name=? where number=?";
		sqlitedatabase.execSQL(str,new Object[]{name,number});
		return true;
	}
	//更新名单信息
	public void updataData(String number,String context){
		String str="update stranger set context=? where number=?";
		sqlitedatabase.execSQL(str,new Object[]{context,number});
	}
	//更新名单信息
	public void updataData(String number,int count){
		String str="update stranger set count=? where number=?";
		sqlitedatabase.execSQL(str,new Object[]{count,number});
	}
	//更新特记录
	public void updataData(int nownum,String phone){
		String str="update stranger set num=? where number=?";
		sqlitedatabase.execSQL(str,new Object[]{nownum,phone});
	}
	 //更新数据(记录表)
    public void updataData(String time,String data,String phone,String table){
    	String str="update "+table+"  set time=? , day=? where number=?";
    	sqlitedatabase.execSQL(str, new Object[]{time,data,phone});
    }
    public void updataData(String time,int frequency,String data,String phone,String table){
    	String str="update "+table+"  set frquency=? , time=? , day=? where number=?";
    	sqlitedatabase.execSQL(str, new Object[]{frequency,time,data,phone});
    }
	// 删除数据
    public void deleteData(String number,String table){
    	String str="delete from "+table+" where number=?";
    	sqlitedatabase.execSQL(str,new Object[]{number});
    }
    // 删除数据
    public void deleteData(int num){
    	String str="delete from stranger where num=?";
    	sqlitedatabase.execSQL(str,new Object[]{num});
    }
    //清空数据库指定表
    public void clearData(String table){
    	String str="delete from "+table;
    	sqlitedatabase.execSQL(str);
    }
    //查询数据
    public Cursor querData(String table){
    	Cursor cur=null;
    	cur=sqlitedatabase.rawQuery("select * from "+table, null);
    	return cur;
    }
}
