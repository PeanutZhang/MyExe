package suda.terminater.district;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TelDao {
	private static SQLiteDatabase db;
	private TelDao(){}
	static{
		db = SQLiteDatabase.openOrCreateDatabase(FileUtil.PATH+"tel.db", null);
		/*if(db==null||!db.isOpen()){
			db = SQLiteDatabase.openOrCreateDatabase(FileUtil.PATH+"tel.db", null);
		}*/
	}
	
	public static void close() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}
	public static String query(String phone){
		String address = null;
		
		Cursor c = db.query("tbl_telephone", new String[]{"telAddress"}, "telphone=?", new String[]{phone}, null, null, null);
		if(c!=null&&c.getCount()>0){
			c.moveToNext();
			address = c.getString(c.getColumnIndex("telAddress"));
			c.close();
		}
		return address;
	}
	
}
