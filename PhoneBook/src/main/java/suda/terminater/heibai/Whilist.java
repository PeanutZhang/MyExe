package suda.terminater.heibai;

import java.util.ArrayList;
import java.util.List;

import suda.terminater.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.suda.xiangmu.sqlite.MyDbHelper;
/**
 * 此类完成列表添加黑、白名单从联系人中
 * 以对话框形式出现
 * @author lipeng
 *
 */
public class Whilist extends Activity{
    ListView listview;
    List<whilepeopl>list=new ArrayList<whilepeopl>();
    MyDbHelper mydbhelper;
    String table="white";
    int i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whilist);
		listview=(ListView) findViewById(R.id.whilelistpeo);
		SharedPreferences ps=Whilist.this.getSharedPreferences("book",MODE_PRIVATE);
		 i=ps.getInt("group", 0);
		if(i==0){                              //判断黑白名单
			table=table.replace(table, "white");
		}
		else{
			table=table.replace(table, "black");
		}
		
		getdata();
		
		BaseAdapter ba=new BaseAdapter(){

			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LinearLayout ll=new LinearLayout(Whilist.this);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ll.setGravity(Gravity.CENTER_VERTICAL);
				ImageView image=new ImageView(Whilist.this);
				image.setImageDrawable(getResources().getDrawable(R.drawable.e1));
				ll.addView(image);
				TextView tvn=new TextView(Whilist.this);
				tvn.setText(list.get(position).getName().toString());
				tvn.setGravity(Gravity.LEFT);
				tvn.setTextColor(getResources().getColor(R.color.blak));
				ll.addView(tvn);
				TextView tv=getTextView();
				tv.setText(list.get(position).getNumber().toString());
				tv.setTextColor(getResources().getColor(R.color.white));
				tv.setGravity(Gravity.CENTER);
				tv.setTextColor(getResources().getColor(R.color.blak));
				ll.addView(tv);
				return ll;
			}
			
		};
		listview.setAdapter(ba);
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LinearLayout ll=(LinearLayout) arg1;
				TextView tvn=(TextView) ll.getChildAt(1);
				TextView tvm=(TextView) ll.getChildAt(2);
				final String name=tvn.getText().toString();
				final String number=tvm.getText().toString();
				mydbhelper=new MyDbHelper(Whilist.this);
				/*mydbhelper.insertData(number, name, table);
				mydbhelper.close();*/
				
				
				if(i==0){ //添加到白名单中
					if(judge(number,"black")){  //判断黑名单中是否存在
						AlertDialog.Builder build=new AlertDialog.Builder(Whilist.this);
						build.setTitle("此号已在黑名单中是否添加");
						build.setIcon(R.drawable.p13);
						build.setPositiveButton("确定",new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MyDbHelper mydb=new MyDbHelper(Whilist.this);
								mydb.open();
								mydb.insertData(number, name, "white");
								mydb.deleteData(number, "black");
								mydb.close();
						Toast.makeText(Whilist.this, "添加成功！", Toast.LENGTH_SHORT).show();
						        Whilist.this.finish();
							}
							
						});
						build.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Whilist.this.finish();
							}
						});
						build.create().show();
					}else{       //******此号不是在黑名单中***** 
						mydbhelper.open();
						mydbhelper.insertData(number, name, "white");
						mydbhelper.close();
						Whilist.this.finish();
						Toast.makeText(Whilist.this, "添加成功！", Toast.LENGTH_SHORT).show();
					}
					
				}else if(i==1){     //添加到黑名单中
                    if(judge(number,"white")){  //判断白名单中是否存在
                    	AlertDialog.Builder builde=new AlertDialog.Builder(Whilist.this);
						builde.setTitle("此号已在白名单中是否添加");
						builde.setIcon(R.drawable.p13);
						builde.setPositiveButton("确定",new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MyDbHelper mydb=new MyDbHelper(Whilist.this);
								mydb.open();
								mydb.insertData(number, name, "black");
								mydb.deleteData(number, "white");
								mydb.close();
						Toast.makeText(Whilist.this, "添加成功！", Toast.LENGTH_SHORT).show();
						Whilist.this.finish();
							}
							
						});
						builde.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Whilist.this.finish();
							}
						});
						builde.create().show();
					}else{       //******此号不是在黑名单中***** 
						mydbhelper.open();
						mydbhelper.insertData(number, name, "black");
						mydbhelper.close();
						Toast.makeText(Whilist.this, "添加成功！", Toast.LENGTH_SHORT).show();
						Whilist.this.finish();
					}
				}
				Whilist.this.finish();
			}
			
		});
	}
	private boolean judge(String phone,String table){  //判断是不在黑白名单中
		  mydbhelper.open();
		  Cursor cur=mydbhelper.querData(table);
		  cur.moveToFirst();
		  while(!cur.isAfterLast()){
			  if(phone.equals(cur.getString(0))){
				  mydbhelper.close();
				  return true;
			  }
			  cur.moveToNext();
		  }
		return false;
	}
	private void getdata(){
		mydbhelper=new MyDbHelper(Whilist.this);
		mydbhelper.open();
		Cursor cur=mydbhelper.querData("addbook");
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			whilepeopl people=new whilepeopl();
			people.setNumber(cur.getString(0));
			people.setName(cur.getString(1));
			list.add(people);
			cur.moveToNext();
		}
		cur.close();
		mydbhelper.close();
	}

	TextView getTextView() {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, 64);
		TextView textView = new TextView(
				Whilist.this);
		textView.setLayoutParams(lp);
		textView.setPadding(5, 5, 5,5);
		textView.setTextSize(15);
		textView.setTextColor(Color.BLACK);
		return textView;
	}
}
class whilepeopl{
	private String name;
	private String number;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
}