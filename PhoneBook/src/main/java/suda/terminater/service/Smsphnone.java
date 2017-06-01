package suda.terminater.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.text.format.Time;

import com.suda.xiangmu.sqlite.MyDbHelper;
/**
 * 是不需要的监听短信
 * @author lipeng
 *
 */
@SuppressWarnings("deprecation")
public class Smsphnone extends Service{
   String number="";
   String body="";
   MyDbHelper mydbhelper;
   boolean bool=false;
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		 this.number=intent.getStringExtra("number");
		 this.body=intent.getStringExtra("body");
		
		 mydbhelper=new MyDbHelper(Smsphnone.this);
		 mydbhelper.open();
		 Cursor cur=mydbhelper.querData("stranger");
		 cur.moveToFirst();
		 while(!cur.isAfterLast()){
			 if(cur.getString(0).equals(number)){
				 bool=true;break;
			 }
			 cur.moveToNext();
		 }
		 if(bool){
			 mydbhelper.updataData(number, body); 
		 }
		 cur.close();
		 mydbhelper.close();
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
