package suda.terminater.interfac;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import suda.terminater.R;
import suda.terminater.district.FileUtil;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.suda.xiangmu.sqlite.MyDbHelper;
/**
 * 拦截记录页面，记录本软件拦截过的电话
 * @author lipeng
 *
 */
public class Record extends Activity{
    ListView list;
    BaseAdapter adapter;
    List<String>phonelist=new ArrayList<String>();//存放电话号码和拦截次数
    List<String>datelist=new ArrayList<String>();//存放拦截时间
    MyDbHelper myDbHelper;
    private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				showDialog(1);
				break;
			case 2:
				removeDialog(1);
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_interface);
	    
		list=(ListView) findViewById(R.id.record_list);
		createDb();
		getData();  //取出数据库中拦截记录表中的数据
		setData();  //将得到的数据放入BaseAdapter容器中
		
		
	}
	/** 将数据库导入apk文件中 */
	private void createDb() {  //根据手机号码前7位判断手机号码归属地
		new Thread() {
			public void run() {
				String name = "tel.db";
				InputStream in = getResources().openRawResource(R.raw.lusoft);
				InputStream read=getResources().openRawResource(R.raw.rigion);
				FileUtil.save(in, read,name,handler);

			};
		}.start();

	}
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case 1:
			dialog = new ProgressDialog(this);
			dialog.setTitle("数据库导入中");
			break;

		default:
			break;
		}
		
		return dialog;
	}
	private void setData(){  //将得到的数据放入BaseAdapter容器中
		
		adapter=new BaseAdapter() {
			int count=0;
			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				count=arg0;
				LayoutInflater inflate=LayoutInflater.from(Record.this);
				View view=inflate.inflate(R.layout.record_dialog, null);
				TextView phonenumber=(TextView) view.findViewById(R.id.recordtext);
				TextView date=(TextView) view.findViewById(R.id.record_time);
				ImageButton sms=(ImageButton) view.findViewById(R.id.record_Sms);
				ImageButton phone=(ImageButton) view.findViewById(R.id.record_tel);
				ImageButton del=(ImageButton) view.findViewById(R.id.record_del);
				
				phonenumber.setText(phonelist.get(arg0));
				date.setText(datelist.get(arg0));
				
				sms.setOnClickListener(new View.OnClickListener() {  //***发送短信点击事件***
					
					@Override
					public void onClick(View arg0) {
						Uri smsToUri = Uri.parse("smsto:"+phonelist.get(count));
						Intent mIntent = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri );
						startActivity( mIntent );
					}
				});
				
                phone.setOnClickListener(new View.OnClickListener() {  //***拨打电话点击事件***
					
					@Override
					public void onClick(View arg0) {
						Uri uri=Uri.parse("tel:"+phonelist.get(count));
						Intent intent=new Intent(Intent.ACTION_VIEW,uri);
						startActivity(intent);
					}
				});
                
               del.setOnClickListener(new View.OnClickListener() {   //***删除记录事件***
					
					@Override
					public void onClick(View arg0) {
						myDbHelper=new MyDbHelper(Record.this);
						myDbHelper.open();  
						myDbHelper.deleteData(phonelist.get(count), "record");
						myDbHelper.close();
						
						getData();  //刷新页面
						setData(); 
					}
				});
				
				return view;
			}
			
			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				return phonelist.size();
			}
		};
		list.setAdapter(adapter);
	}
	
	private void getData(){ //将拦截记录表中的数据读出放到这两个容器中
		phonelist.clear(); //清容器中的数据
		datelist.clear();
		
		myDbHelper=new MyDbHelper(Record.this);
		myDbHelper.open();         // 打开数据库
		Cursor cursor=myDbHelper.querData("record");
		
		while(cursor.moveToNext()){
			phonelist.add(cursor.getString(0));
			datelist.add(cursor.getString(3)+"   "+cursor.getString(2));
		}
		cursor.close();
		myDbHelper.close();
	}
	//***************菜单及其事件***************

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {   //加载xml菜单布局
        MenuInflater inflate=new MenuInflater(Record.this);
        inflate.inflate(R.menu.record_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {  //事件响应
        switch(item.getItemId()){
        case R.id.mrecord_delete:
        	myDbHelper=new MyDbHelper(Record.this);
        	myDbHelper.open();
        	myDbHelper.clearData("record");
        	myDbHelper.close();
        	getData();  //刷新页面
			setData(); 
        	break;
        case R.id.mrecord_quit:
        	System.exit(0);
        	break;
        }
		return super.onOptionsItemSelected(item);
	}
     
	
}
