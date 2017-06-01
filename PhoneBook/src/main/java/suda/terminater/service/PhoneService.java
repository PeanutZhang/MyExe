package suda.terminater.service;

import java.lang.reflect.Method;
import java.util.Calendar;

import suda.terminater.R;
import suda.terminater.district.TelDao;
import suda.terminater.interfac.Record;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.suda.xiangmu.sqlite.MyDbHelper;

public class PhoneService extends Service{
	private TelephonyManager telephony;
	  MyDbHelper mydbhelper;
	  int booolsms=1;
	  int boool=1;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		  booolsms=intent.getIntExtra("bool", 2);
		  boool=booolsms;
		this.telephony=(TelephonyManager) super.getSystemService(Context.TELEPHONY_SERVICE);
		this.telephony.listen(new PhoneStateListenerlmpl(), PhoneStateListener.LISTEN_CALL_STATE);
		return super.onStartCommand(intent, flags, startId);
	}
	
	private class PhoneStateListenerlmpl extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			 AudioManager mAudioManager=(AudioManager)PhoneService.this.getSystemService(Context.AUDIO_SERVICE);
			
			switch(state){
			case TelephonyManager.CALL_STATE_IDLE:
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				//incomingNumber.equals(PhoneService.this.phoneNumber)
				{    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT); 
					  
	                 if(judgement(incomingNumber)){//拦截指定的电话号码     
	                     //先静音处理     
	                        
	              //       Log.e("msg", "Turn Ringtone Silent");     
	                          
	                     try {     
	                       //挂断电话   方法一  
	                         Method method = Class.forName(  
	                                "android.os.ServiceManager").getMethod(  
	                                "getService", String.class);  
	                            // 获取远程TELEPHONY_SERVICE的IBinder对象的代理  
	                            IBinder binder = (IBinder) method.invoke(null,  
	                                new Object[] { Context.TELEPHONY_SERVICE });  
	                            // 将IBinder对象的代理转换为ITelephony对象  
	                            ITelephony telephony = ITelephony.Stub  
	                                .asInterface(binder);  
	                            // 挂断电话  
	                            telephony.endCall();  Log.e("msg", "end");   
	                        //挂断电话   方法二  
	                     } catch (Exception e) {     
	                         e.printStackTrace();     
	                     }  
	                     //再恢复正常铃声     
	                     data(incomingNumber);
	 	 	            sendSMS(incomingNumber);
	 	               	record(incomingNumber);  //****拦截记录***
	 	               	notification(incomingNumber);//*****状态提示符****
	                 }     
	                 mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				}
				break;
				
			}
			  
		}
		
	}
	
	private boolean judgement(String number){
		SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
		Calendar c=Calendar.getInstance();
		
         boolean stranbool=false;
         String numberphone="";//手机号码（陌生电话表）
         String context="";//短信内容（陌生电话表）
         int num=0;  //拦截次数
         int startthour=0;//第一次拦截时间
         int starttminute=0;//
         String answer=sp.getString("answer", "");
         
		int hour=c.get(Calendar.HOUR_OF_DAY);  //当前小时
		int minue=c.get(Calendar.MINUTE);      //当前分钟
		int starthour=sp.getInt("starthour", 0);//开始小时
		int startminue=sp.getInt("startminue", 0);//开始分钟
		int endhour=sp.getInt("endhour", 0);//结束时间
		int endminue=sp.getInt("endminue", 0);//结束分钟
		int model=sp.getInt("imageid", 0);//获得拦截模式
		
		int phonecount=sp.getInt("phonecount", 3);
		
		boolean timebreak=false;//判断是否在拦截时间段
		/* 判断是否在拦截时间段，如果是拦截否不拦截      */
		if(starthour==25){  //全天拦截模式
			timebreak=true;
		}else if(starthour<endhour){  //拦截开始时间小于结束时间
			if(hour>starthour&&hour<endhour){  //在拦截时间段
				timebreak=true;
			}else if(hour==endhour){
				if(startminue<=minue&&minue<=endminue){
					timebreak=true; //在拦截时间段
				}else{
					return false; //不在拦截时间段
				}
			}
		}else if(starthour>endhour){  //拦截开始时间大于结束时间
			if(hour<endhour){
				timebreak=true; //在拦截时间段
			}else if(hour==endhour){
				if(startminue<=minue&&minue<=endminue){
					timebreak=true; //在拦截时间段
				}else{
					return false; //不在拦截时间段
				}
			}
		}else if(starthour==endhour){ //拦截开始时间等于结束时间
			if(startminue<=minue&&minue<=endminue){
				timebreak=true; //在拦截时间段
			}else{
				return false; //不在拦截时间段
			}
		}
		
		if(timebreak){ //如果是在拦截时间段，判断是否拦截
			mydbhelper=new MyDbHelper(PhoneService.this);  //打开数据库
			switch(model){
			case 0:  //关闭拦截规则
				return false;
			case 1: //拦截黑名单里的名单
				mydbhelper.open();  
				Cursor cur=mydbhelper.querData("black");
				cur.moveToFirst();
				while(!cur.isAfterLast()){
					String phonenumber=cur.getString(0);
					if(number.equals(phonenumber)){
						return true;
					}
					cur.moveToNext();
				}
				mydbhelper.close();
				return false;
			case 2:  //智能拦截陌生人
			{ mydbhelper.open();
			       Cursor bookcur=mydbhelper.querData("addbook");
			       bookcur.moveToFirst();
			       while(!bookcur.isAfterLast()){  //判断是否陌生人
			    	   String booknumber=bookcur.getString(0);
			    	   if(number.equals(booknumber)){
			    		   mydbhelper.close();
			    		   return false;
			    	   }
			    	   bookcur.moveToNext();
			       }
			       bookcur=mydbhelper.querData("stranger");
			       bookcur.moveToFirst();
			       while(!bookcur.isAfterLast()){  //陌生电话是否有其记录
			    	   if(bookcur.getString(0).equals(number)){
			    		   numberphone=numberphone.replace(numberphone, number);
			    		   context=context.replace(context, bookcur.getString(5));
			    		   num=bookcur.getInt(2);
			    		   startthour=bookcur.getInt(3);
			    		   starttminute=bookcur.getInt(4);
			    		   stranbool=true;break;
			    	   }
			    	   bookcur.moveToNext();
			       }
			       
			       if(stranbool){ //陌生电话有其记录
			    	  if((subtime(startthour,starttminute)&&num>=phonecount)||context.equals(answer)){
			    		  mydbhelper.close();
			    		  return false;
			    	  }
			       }else{
			    	   mydbhelper.close();
			    	   
			    	   return true;
			       }
			     
					mydbhelper.close();
					   
					return true;
			}
			case 3:  //只接受白名单里的人
			{ mydbhelper.open();
			Cursor ccur=mydbhelper.querData("white");
			ccur.moveToFirst();
			while(!ccur.isAfterLast()){
				String phonenumber=ccur.getString(0);
				if(number.equals(phonenumber)){
					return false;
				}
				ccur.moveToNext();
			}
			mydbhelper.close();
			return true;
			}
				
			case 4:  //拦截所有人
				 return true;
			case 5:{ //按地区拦截
				boolean bool=rigion(number);
				return bool;
			     }
			}
			
		}
		return true;
	}
	
	public boolean rigion(String number){ //按地区拦截
		
		if(number.length()>8){ //防止空号
			SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
			String oldAddr=sp.getString("provincename", "请输入地区")+sp.getString("cityname", "");
			int model=sp.getInt("spinner", 0);
			
			String str=number.substring(0, 7);
			String nowAddr=TelDao.query(str);
			
			if(model==0){ //只拦截一下地区
				 if(oldAddr.equals(nowAddr)){//是这个地区
	  					return false; //不拦截
	  				}else{  //不是这个地区
	  					return true; //拦截
	  				}
			   
			}else if(model==1){//只接听一下地区
				 if(oldAddr.equals(nowAddr)){
						return true;
					}else{
						return false;
					} 
			}
			
		}else{ return false;}
		
		return false;
	}
	
	private void data(String number){    //******陌生电话拦截记录******
		int ncount=0,max=0;
		boolean bool=false;//判断陌生拦截是否有此号码
		String phonenumber="";
		SharedPreferences id=this.getSharedPreferences("rule_record",MODE_PRIVATE);
		int ruleid=id.getInt("imageid", 0);
		if(ruleid==2){ //是陌生模式时才启动
        Calendar c=Calendar.getInstance();
		int stime=c.get(Calendar.HOUR_OF_DAY);
		int sminute=c.get(Calendar.MINUTE); //开始时间
		
		mydbhelper=new MyDbHelper(PhoneService.this);
		mydbhelper.open();
		Cursor cur=mydbhelper.querData("stranger");
		cur.moveToFirst();
		
	    while(!cur.isAfterLast()){   //判断是否有此号码
	    	if(number.equals(cur.getString(0))){
	    		bool=true;
	    		phonenumber=phonenumber.replace(phonenumber, cur.getString(0));
	    		ncount=cur.getInt(2);
	    		break;
	    	}
	    	cur.moveToNext();
	    }
	    
	   if(boool==2){
		    if(bool){ //当有此号码时
		    	ncount=ncount+1;
		    	mydbhelper.updataData(phonenumber, ncount);
		    }else{ //当没有此号码时
		    	cur.moveToFirst();
		    	ncount=0;
		    	while(!cur.isAfterLast()){   //判断陌生电话拦截次数是不大于规定值
		    		ncount=cur.getInt(1);
					if(ncount>=max){
						max=ncount;
					}
					cur.moveToNext();
				}
		    	
				if(max>=10){  //大于最大值
					reorder();//删除第一个,只保留第10个陌生记录信息
					mydbhelper.insertData(number, 10, 1, stime,sminute, "", "stranger");
				}else{
					max=max+1;
					mydbhelper.insertData(number, max, 1, stime, sminute,"", "stranger");
				}
				
		       }
		   
		    boool=1;
		    mydbhelper.close();
	     }
		}
		 
	}
	private void reorder(){ //删除第一个
		int n;
		String number="";
		mydbhelper.deleteData(1);
		Cursor cur=mydbhelper.querData("stranger");
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			number=number.replace(number, cur.getString(0));
			n=cur.getInt(1);
			if(n!=1){
				n=n-1;
				mydbhelper.updataData(n, number);
			}
			cur.moveToNext();
		}
		cur.close();
	}
	
	
    private boolean subtime(int shour,int sminute){ //第一次记录时间与现在时间相减
    	int time=0;
    	int n;
		SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
		n=sp.getInt("phonebreak", 0);
    	Calendar c=Calendar.getInstance();
		int hour=c.get(Calendar.HOUR_OF_DAY);
		int minute=c.get(Calendar.MINUTE);
		if(sminute>minute){
			time=(hour-shour-1)*60+(minute+60-sminute);
		}else{
			time=(hour-shour)*60+(minute-sminute);
		}
		if(time>=n){return false;}
		else{ return true;}
    }
	
	private void sendSMS(String number){    //******陌生号码拦截发送短信*****
		int count=0; //多少次之后发送短信
		SharedPreferences id=this.getSharedPreferences("rule_record",MODE_PRIVATE);
		int ruleid=id.getInt("imageid", 0);
		String quest=id.getString("query", "你好！");
		count=id.getInt("smscount", 0);
		
		mydbhelper=new MyDbHelper(PhoneService.this);
		mydbhelper.open();
		
		if(ruleid==2&&booolsms==2){ //是陌生模式时才启动
			Cursor cur=mydbhelper.querData("stranger");
			cur.moveToFirst();
		while(!cur.isAfterLast()){
			if(cur.getInt(2)==count&&number.equals(cur.getString(0))){ //此号码需在陌生号码表中，和此表中拦截次数向比较
				PendingIntent pi=PendingIntent.getActivity(this,
						0, new Intent(this,PhoneService.class),0);
				SmsManager sms=SmsManager.getDefault();
				sms.sendTextMessage(number, null, quest, pi, null);
				break;
			}
			cur.moveToNext();
		}
		cur.close();
		booolsms=1;//防止多发
		
		}else{} ;
		mydbhelper.close();
	}
	
	private void record(String phonenumber){  //*******拦截记录****
		boolean bool=false;   //*****判断号码是不拦截过*********
		int frequency=0;
		
		mydbhelper=new MyDbHelper(PhoneService.this);
		mydbhelper.open();
		Calendar c=Calendar.getInstance();
		int year=c.get(Calendar.YEAR);
		int month=c.get(Calendar.MONTH);
		int day=c.get(Calendar.DAY_OF_WEEK);
		int hour=c.get(Calendar.HOUR_OF_DAY);
		int minute=c.get(Calendar.MINUTE);
		String data=String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(day);
		String time=String.valueOf(hour)+":"+String.valueOf(minute);
		Cursor cur=mydbhelper.querData("record");
		cur.moveToFirst();
		while(!cur.isAfterLast()){    //*****判断是不以拦截过******
			if(phonenumber.equals(cur.getString(0))){
				frequency=cur.getInt(1);
				frequency++;//统计拦截次数
				bool=true;break;//是拦截过
			}
			cur.moveToNext();
		}
		if(bool){    //此号码已拦截过
			mydbhelper.updataData(time,frequency, data, phonenumber, "record");
		}else{
		mydbhelper.insertData(phonenumber, 1, time, data, "record");
		}
		mydbhelper.close();
	}
	
	private void notification(String phone){
		Intent intent=new Intent(PhoneService.this,Record.class);
		PendingIntent pi=PendingIntent.getActivity(PhoneService.this, 0, intent, 0);
		
		NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notify=new Notification.Builder(PhoneService.this)
		.setAutoCancel(true)
		.setTicker("未接电话")
		.setSmallIcon(R.drawable.p13)
		.setContentText("一条新通知")
		.setContentText("终结者拦截号码为:"+phone)
		.setDefaults(Notification.DEFAULT_LIGHTS)
		.setContentIntent(pi)
		.build();
		nm.notify(0x123, notify);
	}
}
