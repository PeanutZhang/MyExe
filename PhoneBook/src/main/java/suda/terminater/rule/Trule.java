package suda.terminater.rule;

import suda.terminater.R;
import suda.terminater.district.Province;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
/**
 * 本类完成拦截规则设置如按地区拦截拦截时间设置
 * @author lipeng
 *
 */
public class Trule extends Activity{
      LinearLayout time_set;
      Spinner spinner;
      TextView stime,etime,yunkum,quit,yunmanager;
      EditText rigon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		findView();
		getdata();
		 Onclik();
	}

	@Override
	protected void onResume() {
		getdata();
		super.onResume();
	}


	public void findView(){
		
		time_set=(LinearLayout) findViewById(R.id.linearlayout);
		spinner=(Spinner) findViewById(R.id.setting_spinner);
		rigon=(EditText) findViewById(R.id.sett_local);
		stime=(TextView) findViewById(R.id.time_hour);//time_hour
		etime=(TextView) findViewById(R.id.time_second);
		quit=(TextView) findViewById(R.id.trule_quit);
		yunmanager=(TextView) findViewById(R.id.setting_yun);
	}
	
	public void getdata(){
	    SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
	    if(sp.getInt("starthour", 0)==25){
	    	stime.setText("全天拦截");
	    	etime.setText("");
	    }else if(sp.getInt("starthour", 0)<25){
	    	String st=""+sp.getInt("starthour", 0)+":"+sp.getInt("startminue", 0)+"--";
	    	String et=""+sp.getInt("endhour", 0)+":"+sp.getInt("endminue", 0);
	    	stime.setText(st);
	    	etime.setText(et);
	    }
	    spinner.setSelection(sp.getInt("spinner", 0));
	    String address=sp.getString("provincename", "请输入地区")+sp.getString("cityname", "");
	    rigon.setText(address);
	    
	}
	
	public void Onclik(){
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){//选择地区拦截模式如只拦截摸个地区或只接听
			                                                             //莫个地区

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
				SharedPreferences.Editor edit=sp.edit();
				edit.putInt("spinner", arg2);
				edit.commit();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		quit.setOnClickListener(new OnClickListener() { //返回键事件
			
			@Override
			public void onClick(View arg0) {
				Trule.this.finish();
			}
		});
		yunmanager.setOnClickListener(new OnClickListener() {  //云库管理
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		time_set.setOnClickListener(new OnClickListener() {  //拦截时间管理
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(Trule.this,Time_set.class);
				startActivity(intent);
			}
		});
		rigon.setOnClickListener(new OnClickListener() { //拦截地区选择如可选择江苏--苏州
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(Trule.this,Province.class);
				startActivity(intent);
			}
		});
	}
	

     
}
