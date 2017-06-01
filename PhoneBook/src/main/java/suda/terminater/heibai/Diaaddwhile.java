package suda.terminater.heibai;
import suda.terminater.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.suda.xiangmu.sqlite.MyDbHelper;

/**
 * 此类完成手动添加人员到黑白名单
 * @author lipeng
 *
 */
public class Diaaddwhile extends Activity{
	MyDbHelper mydbhelper;
	String table="white";
	int i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv=new TextView(Diaaddwhile.this);
		tv.setText("");
		tv.setBackgroundResource(getResources().getColor(R.color.light));
		setContentView(tv);
		
		SharedPreferences ps=Diaaddwhile.this.getSharedPreferences("book",MODE_PRIVATE);
		i=ps.getInt("group", 0);
				
		AlertDialog.Builder builder=new AlertDialog.Builder(Diaaddwhile.this);
		LayoutInflater factory=LayoutInflater.from(Diaaddwhile.this);
		final View view=factory.inflate(R.layout.dialogpeoadd, null);
		builder.setView(view);
		builder.setTitle("添加号码到白名单");
		builder.setIcon(R.drawable.p13);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mydbhelper=new MyDbHelper(Diaaddwhile.this);
				mydbhelper.open();   //*******打开数据库*******
				EditText name=(EditText) view.findViewById(R.id.edit_whilename);
				EditText number=(EditText) view.findViewById(R.id.edit_whilenumb);
				final String strname=name.getText().toString();
				final String strnumber=number.getText().toString();
				if(strname.equals("")||strnumber.equals("")){
					Toast toast=Toast.makeText(Diaaddwhile.this,"姓名与号码不能为空",Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					mydbhelper.close();
					Diaaddwhile.this.finish();
				}else{
				if(i==0){                       //********* 添加白名单******      
					if(judge(strnumber,"black")){   //******此号在黑名单中***** 
						AlertDialog.Builder build=new AlertDialog.Builder(Diaaddwhile.this);
						build.setTitle("此号已在黑名单中是否添加");
						build.setIcon(R.drawable.p13);
						build.setPositiveButton("确定",new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MyDbHelper mydb=new MyDbHelper(Diaaddwhile.this);
								mydb.open();
								mydb.insertData(strnumber, strname, "white");
								mydb.deleteData(strnumber, "black");
								mydb.close();
								Diaaddwhile.this.finish();
						Toast.makeText(Diaaddwhile.this, "添加成功！", Toast.LENGTH_SHORT).show();

							}
							
						});
						build.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Diaaddwhile.this.finish();
							}
						});
						build.create().show();
					}else{       //******此号不是在黑名单中***** 
						mydbhelper.open();
						mydbhelper.insertData(strnumber, strname, "white");
						mydbhelper.close();
						Diaaddwhile.this.finish();
						Toast.makeText(Diaaddwhile.this, "添加成功！", Toast.LENGTH_SHORT).show();
					}
				}
				else if(i==1){        //********* 添加黑名单****** 
					if(judge(strnumber,"white")){   //******此号是在白名单中***** 
						AlertDialog.Builder builde=new AlertDialog.Builder(Diaaddwhile.this);
						builde.setTitle("此号已在白名单中是否添加");
						builde.setIcon(R.drawable.p13);
						builde.setPositiveButton("确定",new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MyDbHelper mydb=new MyDbHelper(Diaaddwhile.this);
								mydb.open();
								mydb.insertData(strnumber, strname,"0" ,"black");
								mydb.deleteData(strnumber, "white");
								mydb.close();
								Diaaddwhile.this.finish();
								Toast.makeText(Diaaddwhile.this, "添加成功！", Toast.LENGTH_SHORT).show();
							}
							
						});
						builde.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Diaaddwhile.this.finish();
							}
						});
						builde.create().show();
					}else{       //******此号不是在白名单中***** 
					   mydbhelper.open();
					   mydbhelper.insertData(strnumber, strname,"0" ,"black");
					   mydbhelper.close();
					   Diaaddwhile.this.finish();
					Toast.makeText(Diaaddwhile.this, "添加成功！", Toast.LENGTH_SHORT).show();

					    }
					
				}
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Diaaddwhile.this.finish();
			}
		});
		builder.create().show();
	}
	private boolean judge(String phone,String table){  //判断是不在黑白名单中
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
        
}
