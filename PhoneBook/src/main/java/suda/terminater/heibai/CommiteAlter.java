package suda.terminater.heibai;

import suda.terminater.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.suda.xiangmu.sqlite.MyDbHelper;
/**
 * 此类完成修改备注
 * 以对话框形式出现
 * @author lipeng
 *
 */
public class CommiteAlter extends Activity{
	MyDbHelper mydbhelper;
	String number;
	String table="white";
	int i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commite);
		SharedPreferences sp=getSharedPreferences("book", MODE_PRIVATE);
	    number=sp.getString("number", "110");
	    i=sp.getInt("group", 0);
	    if(i==0){                              //判断黑白名单
			table=table.replace(table, "white");
		}
		else{
			table=table.replace(table, "black");
		}
		AlertDialog.Builder builder=new AlertDialog.Builder(CommiteAlter.this);
		LayoutInflater factory=LayoutInflater.from(CommiteAlter.this);
		final View view=factory.inflate(R.layout.comment, null);
		builder.setView(view);
		TextView tv=(TextView)view.findViewById(R.id.coment_text);
		tv.setText(number);
		builder.setIcon(R.drawable.e1);
		builder.setTitle("修改备注");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				CommiteAlter.this.finish();
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				  EditText et=(EditText) view.findViewById(R.id.comment_edit);
				  String str=et.getText().toString();
				  mydbhelper=new MyDbHelper(CommiteAlter.this);
				  mydbhelper.open();
				  mydbhelper.updataData(number, str, table);
				  mydbhelper.close();
				  CommiteAlter.this.finish();
			}
		});
		builder.create().show();
	}
       
}
