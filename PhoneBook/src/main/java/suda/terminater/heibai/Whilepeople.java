package suda.terminater.heibai;

import suda.terminater.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
/**
 * ������ɺڡ����������
 * ��һ��
 * @author lipeng
 *
 */
public class Whilepeople extends Activity{
	ListView listview;
	BaseAdapter ba;
	String []name=new String[]{"�ֶ����","��ͨ������ϵ����ѡ��"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booklist);
		listview=(ListView) findViewById(R.id.booklist);
		ba=new BaseAdapter(){

			@Override
			public int getCount() {
				return name.length;
			}
			public Object getItem(int arg0) {	return null;}
			public long getItemId(int position) { return 0;}
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LinearLayout ll=new LinearLayout(Whilepeople.this);
				ll.setGravity(Gravity.CENTER_VERTICAL);
				TextView tv=new TextView(Whilepeople.this);
				tv.setText(name[position]);
				tv.setTextSize(18);
				tv.setTextColor(getResources().getColor(R.color.blak));
				tv.setPadding(10, 15, 10, 15);
				ll.addView(tv);
				return ll;
			}
			
		};
		listview.setAdapter(ba);
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2==0){    //�ն����
					   Intent intent=new Intent();
				        intent.setClass(Whilepeople.this, Diaaddwhile.class);
				        startActivity(intent);
				    	Whilepeople.this.finish();
				}else if(arg2==1){    //��ͨ������ϵ����ѡ��
					Intent intent=new Intent();
					intent.setClass(getApplicationContext(), Whilist.class);
					startActivity(intent);
					Whilepeople.this.finish();
				}
				
			}
			
		});
		
	}

}

