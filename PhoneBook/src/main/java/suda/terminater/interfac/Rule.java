package suda.terminater.interfac;

import suda.terminater.R;
import suda.terminater.rule.Stranger_set;
import suda.terminater.rule.Trule;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
/**
 * 拦截规则页面
 * @author lipeng
 *
 */
public class Rule extends Activity{
	 ListView listview;
	    String []label=new String[]{"关闭拦截规则                        ","拦截黑名单里的人                ",
	    		"智能拦截陌生人                    ","只接受白名单里的人            ","拦截所有人                          ","按地区拦截                          "};
	    int[]image=new int[]{R.drawable.y7,R.drawable.y8,R.drawable.y8,R.drawable.y8,
	    		R.drawable.y8,R.drawable.y8};
	    int imageid=0;  //记录选择的规则
	     BaseAdapter ba;
	     
	     @SuppressLint("HandlerLeak")
		Handler hand=new Handler(){  //刷新页面

			@Override
			public void handleMessage(Message msg) {
				if(msg.what==1){
					updata();//本方法重复刷新页面
				}
				super.handleMessage(msg);
			}
	    	 
	     };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rulelist);
		listview=(ListView) findViewById(R.id.rule_list);
		updata();//本方法重复刷新页面
	}
	 private void updata(){  //本方法重复刷新页面
	    	changedata();
	    	ba=new BaseAdapter(){

				@Override
				public int getCount() {
					
					return label.length;
				}

				@Override
				public Object getItem(int arg0) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public long getItemId(int arg0) {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public View getView(final int arg0, View arg1, ViewGroup arg2) {
						arg1=getLayoutInflater().inflate(R.layout.record_relate, null);
						final ImageView ivt=(ImageView) arg1.findViewById(R.id.record_image);
						final Button tv=(Button) arg1.findViewById(R.id.record_text);
						ImageButton ib=(ImageButton) arg1.findViewById(R.id.record_ivt);
						ib.setPadding(20, 15, 0, 0);
						if(arg0!=0){ib.setImageDrawable(getResources().getDrawable(R.drawable.y9));}
						ib.setBackgroundColor(getResources().getColor(R.color.wwhite));
						
						ivt.setImageDrawable(getResources().getDrawable(image[arg0]));
						tv.setText(label[arg0]);
						tv.setPadding(10, 5, 50, 5);
						tv.setTextSize(18);
						tv.setBackgroundColor(getResources().getColor(R.color.wwhite));
						tv.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(imageid!=arg0){
								image[arg0]=R.drawable.y7;
								image[imageid]=R.drawable.y8;
								imageid=arg0;
								ivt.setImageDrawable(getResources().getDrawable(image[arg0]));
								SharedPreferences cf=getSharedPreferences("rule_record", MODE_PRIVATE);
								SharedPreferences.Editor editor=cf.edit();
								editor.putInt("imageid", imageid);
								editor.commit();
								hand.sendEmptyMessage(1); //刷新页面
								}
							
							String text=tv.getText().toString();
							Toast.makeText(Rule.this, text, Toast.LENGTH_SHORT).show();
							}
						});
					  if(arg0==2){  //*****是否陌生拦截**
		                            ib.setOnClickListener(new View.OnClickListener() { //图片按钮
								
								@Override
								public void onClick(View v) {
									  Intent intent=new Intent(Rule.this,Stranger_set.class);
			 						  startActivity(intent);
								}
							});
					  }else{
							ib.setOnClickListener(new View.OnClickListener() { //图片按钮
								
								@Override
								public void onClick(View v) {
		 						  Intent intent=new Intent(Rule.this,Trule.class);
		 						  startActivity(intent);
								}
							});
					  }
					
					return arg1;
				}
				
			};
			listview.setAdapter(ba);
			listview.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					
				}
				
			});
	    }
	
	  private void changedata(){  //读取本地文件
	    	SharedPreferences cf=getSharedPreferences("rule_record", MODE_PRIVATE);
	    	int i;
	        i =cf.getInt("imageid", 0);
	        if(i!=imageid){
	        	image[i]=R.drawable.y7;
	     		image[imageid]=R.drawable.y8;
	     		imageid=i;
	        }else{
	        	
	        }
	    }
        //********************菜单及其响应事件************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflate=new MenuInflater(Rule.this);
		inflate.inflate(R.menu.rule_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
        switch(item.getItemId()){
        case R.id.mrule_quit:
        	System.exit(0);
        	break;
        }
       
		return super.onOptionsItemSelected(item);
	}
	  
}
