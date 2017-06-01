package suda.terminater.rule;

import suda.terminater.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
/**
 * 本类完成对陌生人拦截设置
 * @author lipeng
 *
 */
public class Stranger_set extends Activity{
	TextView smstall,timestall,smstext,phonetext,timetext,quit;
    LinearLayout smslinear,phonelinear,timelinear;
    String []smstime=new String[10];
    String []phonetime=new String[10];
	int n=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stranger_interface);
		findView(); //加载组件
		getTextcontent(); //获取本地文件的数据如次数和时间段
		getdata(); //对数组加字
		onClick();//事件响应
	}
	
	public void findView(){
		quit=(TextView) findViewById(R.id.strange_quit);  //返回键
		smstall=(TextView) findViewById(R.id.strange_smsinstall);  //回复短信设置
		timestall=(TextView) findViewById(R.id.strange_timeinstall);// 时间设置
	    smstext=(TextView) findViewById(R.id.strange_smssinstall);//多少次发送短信
	    phonetext=(TextView) findViewById(R.id.strange_phoneinstall);//多少次接听
	    timetext=(TextView) findViewById(R.id.strange_timespaneinstall);//拦截时间段设置
	    smslinear=(LinearLayout) findViewById(R.id.strange_smslinear);//  发送短信事件
	    phonelinear=(LinearLayout) findViewById(R.id.strange_phonelinear);// 接听事件
	    timelinear=(LinearLayout) findViewById(R.id.strange_timelinear);//  拦截时间段事件
	    
	}
	private void getdata(){   // ****初始化数组****
		int i;
		for(i=0;i<smstime.length;i++){
			smstime[i]="第："+(i+1)+" 次后发送短信";
		}
		for(i=0;i<phonetime.length;i++){
			phonetime[i]="第："+(i+1)+"次后可以接通";
		}
	}
	
	private void getTextcontent(){
		int n;
		SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
		n=sp.getInt("smscount", 0);
		String str=""+n+"次";
		smstext.setText(str);
		n=sp.getInt("phonecount", 0);
		str=str.replace(str, ""+n+"次");
		phonetext.setText(str);
		n=sp.getInt("phonebreak", 0);
		str=str.replace(str, ""+n+"分钟");
		timetext.setText(str);
	}
	
	public void onClick(){
		
		quit.setOnClickListener(new OnClickListener(){ //返回事件

			@Override
			public void onClick(View arg0) {
				Stranger_set.this.finish();
			}
		});
		smstall.setOnClickListener(new OnClickListener(){ //发送信息设置，提问问题

			@Override
			public void onClick(View arg0) {
				new Stranger_smsmanager(Stranger_set.this).Dialog(); //一对话框的形式出现
			}
		});
		timestall.setOnClickListener(new OnClickListener(){ //时间设置事件,跳转到时间设置页面

			@Override
			public void onClick(View arg0) {
				  Intent intent=new Intent(Stranger_set.this,Time_set.class);
			      startActivity(intent);
			}
		});
		smslinear.setOnClickListener(new OnClickListener(){ //发送信息设置，提问问题

			@Override
			public void onClick(View arg0) {
				int z=-1;
				AlertDialog.Builder builder=new AlertDialog.Builder(Stranger_set.this);
				builder.setTitle("请选择");
				builder.setIcon(R.drawable.p13);
				builder.setSingleChoiceItems(smstime, z,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					   n=which+1;	
					}
				});
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
						SharedPreferences.Editor edit=sp.edit();
						edit.putInt("smscount", n);
						edit.commit();
						getTextcontent();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				builder.create().show();
			}
		});
		phonelinear.setOnClickListener(new OnClickListener(){ //多少次接通

			@Override
			public void onClick(View arg0) {
				int z=-1;
				AlertDialog.Builder builder=new AlertDialog.Builder(Stranger_set.this);
				builder.setTitle("请选择");
				builder.setIcon(R.drawable.p13);
				builder.setSingleChoiceItems(phonetime, z,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					   n=which+1;	
					}
				});
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
						SharedPreferences.Editor edit=sp.edit();
						edit.putInt("phonecount", n);
						edit.commit();
						getTextcontent();
						
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				builder.create().show();
				
			}
		});
		timelinear.setOnClickListener(new OnClickListener(){  //接通的时间段

			@Override
			public void onClick(View arg0) {
				 Dialog dialog=new TimePickerDialog(Stranger_set.this,new TimePickerDialog.OnTimeSetListener() {
						
						@Override
						public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
							n=arg1*60+arg2;
							SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
							SharedPreferences.Editor edit=sp.edit();
							edit.putInt("phonebreak", n);
							edit.commit();
							getTextcontent();
						}
					},0,0,true);
					 dialog.show();
			}
			
		});
	}
      
}
