package suda.terminater.heibai;

import suda.terminater.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.suda.xiangmu.sqlite.MyDbHelper;
/**
 * 此类完成对数据成员操作
 * 以对话框形式出现
 * @author lipeng
 *
 */
public class Whilechilde extends Activity{
    ListView listview;
    String []name=new String[]{"拨打电话","发短信","修改备注","删除"};
    String number;
    MyDbHelper mydbhelper;
    String table="white";
    int i;
    BaseAdapter ba;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whilechilde);
		listview=(ListView) findViewById(R.id.whilechild_list);
		ba=new BaseAdapter(){

			@Override
			public int getCount() {
				return name.length;
			}
			public Object getItem(int position) { return position;}
			public long getItemId(int position) {return position;}
			public View getView(int position, View convertView, ViewGroup parent) {
				LinearLayout ll=new LinearLayout(Whilechilde.this);
				ll.setGravity(Gravity.CENTER_VERTICAL);
				TextView tv=new TextView(Whilechilde.this);
				tv.setPadding(10, 18, 15, 18);
				tv.setText(name[position]);
				tv.setTextSize(18);
				tv.setTextColor(getResources().getColor(R.color.blak));
				ll.addView(tv);
				return ll;
			}
			
		};
        listview.setAdapter(ba);		
		SharedPreferences sp=getSharedPreferences("book", MODE_PRIVATE);
	    number=sp.getString("number", "110");
		i=sp.getInt("group", 0);
		if(i==0){                              //判断黑白名单
			table=table.replace(table, "white");
		}
		else{
			table=table.replace(table, "black");
		}
		Whilechilde.this.getWindow().setTitle(number);
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch(arg2){
				case 0:  //打电话
					Uri uri=Uri.parse("tel:"+number);
					Intent intent=new Intent(Intent.ACTION_VIEW,uri);
					startActivity(intent);
					 Whilechilde.this.finish();
					break;
				case 1:  //发短信
					Uri smsToUri = Uri.parse("smsto:"+number);
					Intent mIntent = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri );
					startActivity( mIntent );
					 Whilechilde.this.finish();
					break;
				case 2:  //修改备注
					Intent tem=new Intent();
					tem.setClass(getApplicationContext(), CommiteAlter.class);
					startActivity(tem);
					Whilechilde.this.finish();
					break;
				case 3:  //删除联系人
					 mydbhelper=new MyDbHelper(Whilechilde.this);
					 mydbhelper.open();
					 mydbhelper.deleteData(number, table);
					 mydbhelper.close();
					 Whilechilde.this.finish();
					break;
				}
			}
			
		});
	
	}
      
}
