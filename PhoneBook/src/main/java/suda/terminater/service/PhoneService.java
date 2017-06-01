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
					  
	                 if(judgement(incomingNumber)){//����ָ���ĵ绰����     
	                     //�Ⱦ�������     
	                        
	              //       Log.e("msg", "Turn Ringtone Silent");     
	                          
	                     try {     
	                       //�Ҷϵ绰   ����һ  
	                         Method method = Class.forName(  
	                                "android.os.ServiceManager").getMethod(  
	                                "getService", String.class);  
	                            // ��ȡԶ��TELEPHONY_SERVICE��IBinder����Ĵ���  
	                            IBinder binder = (IBinder) method.invoke(null,  
	                                new Object[] { Context.TELEPHONY_SERVICE });  
	                            // ��IBinder����Ĵ���ת��ΪITelephony����  
	                            ITelephony telephony = ITelephony.Stub  
	                                .asInterface(binder);  
	                            // �Ҷϵ绰  
	                            telephony.endCall();  Log.e("msg", "end");   
	                        //�Ҷϵ绰   ������  
	                     } catch (Exception e) {     
	                         e.printStackTrace();     
	                     }  
	                     //�ٻָ���������     
	                     data(incomingNumber);
	 	 	            sendSMS(incomingNumber);
	 	               	record(incomingNumber);  //****���ؼ�¼***
	 	               	notification(incomingNumber);//*****״̬��ʾ��****
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
         String numberphone="";//�ֻ����루İ���绰��
         String context="";//�������ݣ�İ���绰��
         int num=0;  //���ش���
         int startthour=0;//��һ������ʱ��
         int starttminute=0;//
         String answer=sp.getString("answer", "");
         
		int hour=c.get(Calendar.HOUR_OF_DAY);  //��ǰСʱ
		int minue=c.get(Calendar.MINUTE);      //��ǰ����
		int starthour=sp.getInt("starthour", 0);//��ʼСʱ
		int startminue=sp.getInt("startminue", 0);//��ʼ����
		int endhour=sp.getInt("endhour", 0);//����ʱ��
		int endminue=sp.getInt("endminue", 0);//��������
		int model=sp.getInt("imageid", 0);//�������ģʽ
		
		int phonecount=sp.getInt("phonecount", 3);
		
		boolean timebreak=false;//�ж��Ƿ�������ʱ���
		/* �ж��Ƿ�������ʱ��Σ���������ط�����      */
		if(starthour==25){  //ȫ������ģʽ
			timebreak=true;
		}else if(starthour<endhour){  //���ؿ�ʼʱ��С�ڽ���ʱ��
			if(hour>starthour&&hour<endhour){  //������ʱ���
				timebreak=true;
			}else if(hour==endhour){
				if(startminue<=minue&&minue<=endminue){
					timebreak=true; //������ʱ���
				}else{
					return false; //��������ʱ���
				}
			}
		}else if(starthour>endhour){  //���ؿ�ʼʱ����ڽ���ʱ��
			if(hour<endhour){
				timebreak=true; //������ʱ���
			}else if(hour==endhour){
				if(startminue<=minue&&minue<=endminue){
					timebreak=true; //������ʱ���
				}else{
					return false; //��������ʱ���
				}
			}
		}else if(starthour==endhour){ //���ؿ�ʼʱ����ڽ���ʱ��
			if(startminue<=minue&&minue<=endminue){
				timebreak=true; //������ʱ���
			}else{
				return false; //��������ʱ���
			}
		}
		
		if(timebreak){ //�����������ʱ��Σ��ж��Ƿ�����
			mydbhelper=new MyDbHelper(PhoneService.this);  //�����ݿ�
			switch(model){
			case 0:  //�ر����ع���
				return false;
			case 1: //���غ������������
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
			case 2:  //��������İ����
			{ mydbhelper.open();
			       Cursor bookcur=mydbhelper.querData("addbook");
			       bookcur.moveToFirst();
			       while(!bookcur.isAfterLast()){  //�ж��Ƿ�İ����
			    	   String booknumber=bookcur.getString(0);
			    	   if(number.equals(booknumber)){
			    		   mydbhelper.close();
			    		   return false;
			    	   }
			    	   bookcur.moveToNext();
			       }
			       bookcur=mydbhelper.querData("stranger");
			       bookcur.moveToFirst();
			       while(!bookcur.isAfterLast()){  //İ���绰�Ƿ������¼
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
			       
			       if(stranbool){ //İ���绰�����¼
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
			case 3:  //ֻ���ܰ����������
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
				
			case 4:  //����������
				 return true;
			case 5:{ //����������
				boolean bool=rigion(number);
				return bool;
			     }
			}
			
		}
		return true;
	}
	
	public boolean rigion(String number){ //����������
		
		if(number.length()>8){ //��ֹ�պ�
			SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
			String oldAddr=sp.getString("provincename", "���������")+sp.getString("cityname", "");
			int model=sp.getInt("spinner", 0);
			
			String str=number.substring(0, 7);
			String nowAddr=TelDao.query(str);
			
			if(model==0){ //ֻ����һ�µ���
				 if(oldAddr.equals(nowAddr)){//���������
	  					return false; //������
	  				}else{  //�����������
	  					return true; //����
	  				}
			   
			}else if(model==1){//ֻ����һ�µ���
				 if(oldAddr.equals(nowAddr)){
						return true;
					}else{
						return false;
					} 
			}
			
		}else{ return false;}
		
		return false;
	}
	
	private void data(String number){    //******İ���绰���ؼ�¼******
		int ncount=0,max=0;
		boolean bool=false;//�ж�İ�������Ƿ��д˺���
		String phonenumber="";
		SharedPreferences id=this.getSharedPreferences("rule_record",MODE_PRIVATE);
		int ruleid=id.getInt("imageid", 0);
		if(ruleid==2){ //��İ��ģʽʱ������
        Calendar c=Calendar.getInstance();
		int stime=c.get(Calendar.HOUR_OF_DAY);
		int sminute=c.get(Calendar.MINUTE); //��ʼʱ��
		
		mydbhelper=new MyDbHelper(PhoneService.this);
		mydbhelper.open();
		Cursor cur=mydbhelper.querData("stranger");
		cur.moveToFirst();
		
	    while(!cur.isAfterLast()){   //�ж��Ƿ��д˺���
	    	if(number.equals(cur.getString(0))){
	    		bool=true;
	    		phonenumber=phonenumber.replace(phonenumber, cur.getString(0));
	    		ncount=cur.getInt(2);
	    		break;
	    	}
	    	cur.moveToNext();
	    }
	    
	   if(boool==2){
		    if(bool){ //���д˺���ʱ
		    	ncount=ncount+1;
		    	mydbhelper.updataData(phonenumber, ncount);
		    }else{ //��û�д˺���ʱ
		    	cur.moveToFirst();
		    	ncount=0;
		    	while(!cur.isAfterLast()){   //�ж�İ���绰���ش����ǲ����ڹ涨ֵ
		    		ncount=cur.getInt(1);
					if(ncount>=max){
						max=ncount;
					}
					cur.moveToNext();
				}
		    	
				if(max>=10){  //�������ֵ
					reorder();//ɾ����һ��,ֻ������10��İ����¼��Ϣ
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
	private void reorder(){ //ɾ����һ��
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
	
	
    private boolean subtime(int shour,int sminute){ //��һ�μ�¼ʱ��������ʱ�����
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
	
	private void sendSMS(String number){    //******İ���������ط��Ͷ���*****
		int count=0; //���ٴ�֮���Ͷ���
		SharedPreferences id=this.getSharedPreferences("rule_record",MODE_PRIVATE);
		int ruleid=id.getInt("imageid", 0);
		String quest=id.getString("query", "��ã�");
		count=id.getInt("smscount", 0);
		
		mydbhelper=new MyDbHelper(PhoneService.this);
		mydbhelper.open();
		
		if(ruleid==2&&booolsms==2){ //��İ��ģʽʱ������
			Cursor cur=mydbhelper.querData("stranger");
			cur.moveToFirst();
		while(!cur.isAfterLast()){
			if(cur.getInt(2)==count&&number.equals(cur.getString(0))){ //�˺�������İ��������У��ʹ˱������ش�����Ƚ�
				PendingIntent pi=PendingIntent.getActivity(this,
						0, new Intent(this,PhoneService.class),0);
				SmsManager sms=SmsManager.getDefault();
				sms.sendTextMessage(number, null, quest, pi, null);
				break;
			}
			cur.moveToNext();
		}
		cur.close();
		booolsms=1;//��ֹ�෢
		
		}else{} ;
		mydbhelper.close();
	}
	
	private void record(String phonenumber){  //*******���ؼ�¼****
		boolean bool=false;   //*****�жϺ����ǲ����ع�*********
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
		while(!cur.isAfterLast()){    //*****�ж��ǲ������ع�******
			if(phonenumber.equals(cur.getString(0))){
				frequency=cur.getInt(1);
				frequency++;//ͳ�����ش���
				bool=true;break;//�����ع�
			}
			cur.moveToNext();
		}
		if(bool){    //�˺��������ع�
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
		.setTicker("δ�ӵ绰")
		.setSmallIcon(R.drawable.p13)
		.setContentText("һ����֪ͨ")
		.setContentText("�ս������غ���Ϊ:"+phone)
		.setDefaults(Notification.DEFAULT_LIGHTS)
		.setContentIntent(pi)
		.build();
		nm.notify(0x123, notify);
	}
}
