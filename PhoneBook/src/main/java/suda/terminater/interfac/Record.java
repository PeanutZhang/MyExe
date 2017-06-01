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
 * ���ؼ�¼ҳ�棬��¼��������ع��ĵ绰
 * @author lipeng
 *
 */
public class Record extends Activity{
    ListView list;
    BaseAdapter adapter;
    List<String>phonelist=new ArrayList<String>();//��ŵ绰��������ش���
    List<String>datelist=new ArrayList<String>();//�������ʱ��
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
		getData();  //ȡ�����ݿ������ؼ�¼���е�����
		setData();  //���õ������ݷ���BaseAdapter������
		
		
	}
	/** �����ݿ⵼��apk�ļ��� */
	private void createDb() {  //�����ֻ�����ǰ7λ�ж��ֻ����������
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
			dialog.setTitle("���ݿ⵼����");
			break;

		default:
			break;
		}
		
		return dialog;
	}
	private void setData(){  //���õ������ݷ���BaseAdapter������
		
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
				
				sms.setOnClickListener(new View.OnClickListener() {  //***���Ͷ��ŵ���¼�***
					
					@Override
					public void onClick(View arg0) {
						Uri smsToUri = Uri.parse("smsto:"+phonelist.get(count));
						Intent mIntent = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri );
						startActivity( mIntent );
					}
				});
				
                phone.setOnClickListener(new View.OnClickListener() {  //***����绰����¼�***
					
					@Override
					public void onClick(View arg0) {
						Uri uri=Uri.parse("tel:"+phonelist.get(count));
						Intent intent=new Intent(Intent.ACTION_VIEW,uri);
						startActivity(intent);
					}
				});
                
               del.setOnClickListener(new View.OnClickListener() {   //***ɾ����¼�¼�***
					
					@Override
					public void onClick(View arg0) {
						myDbHelper=new MyDbHelper(Record.this);
						myDbHelper.open();  
						myDbHelper.deleteData(phonelist.get(count), "record");
						myDbHelper.close();
						
						getData();  //ˢ��ҳ��
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
	
	private void getData(){ //�����ؼ�¼���е����ݶ����ŵ�������������
		phonelist.clear(); //�������е�����
		datelist.clear();
		
		myDbHelper=new MyDbHelper(Record.this);
		myDbHelper.open();         // �����ݿ�
		Cursor cursor=myDbHelper.querData("record");
		
		while(cursor.moveToNext()){
			phonelist.add(cursor.getString(0));
			datelist.add(cursor.getString(3)+"   "+cursor.getString(2));
		}
		cursor.close();
		myDbHelper.close();
	}
	//***************�˵������¼�***************

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {   //����xml�˵�����
        MenuInflater inflate=new MenuInflater(Record.this);
        inflate.inflate(R.menu.record_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {  //�¼���Ӧ
        switch(item.getItemId()){
        case R.id.mrecord_delete:
        	myDbHelper=new MyDbHelper(Record.this);
        	myDbHelper.open();
        	myDbHelper.clearData("record");
        	myDbHelper.close();
        	getData();  //ˢ��ҳ��
			setData(); 
        	break;
        case R.id.mrecord_quit:
        	System.exit(0);
        	break;
        }
		return super.onOptionsItemSelected(item);
	}
     
	
}
