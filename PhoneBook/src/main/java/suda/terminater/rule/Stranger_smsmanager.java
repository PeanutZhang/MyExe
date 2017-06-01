package suda.terminater.rule;

import suda.terminater.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.widget.EditText;
/**
 * 本类以对话框的形式出现完成发送短信的查看和设置
 * @author lipeng
 *
 */
public class Stranger_smsmanager {
	private Context context;
	private String query;
	private String answer;
       public Stranger_smsmanager(Context context){
    	   this.context=context;
       }
       
       public void Dialog(){ //一级对画框
    	   getData();//获得数据
    	   AlertDialog.Builder builder=new AlertDialog.Builder(context);
    	   builder.setTitle("发送短信管理")
    	          .setMessage(query+"\n"+answer)
    	          .setIcon(context.getResources().getDrawable(R.drawable.p13));
    	   builder.setPositiveButton("答案设置", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				answer_set();
			}
		  });
    	   builder.setNegativeButton("问题设置", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				query_set();
			}
		});
    	   builder.create().show();
       }
       
       private void getData(){  //获得数据
    	   SharedPreferences sp=context.getSharedPreferences("rule_record", Context.MODE_PRIVATE);
    	   query=sp.getString("query", "对不起，你的问题还没设置！");
    	   answer=sp.getString("answer", "对不起，你的答案还没设置！");
       }
       
       private void query_set(){  //问题设置对话框
    	   final EditText tv=new EditText(context);
    	   tv.setTextColor(context.getResources().getColor(R.color.blak));
    	   tv.setTextSize(20);
    	   AlertDialog.Builder builder=new AlertDialog.Builder(context);
    	   builder.setTitle("问题设置")
    	          .setIcon(R.drawable.p13)
    	          .setView(tv);
    	   builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				SharedPreferences sp=context.getSharedPreferences("rule_record", Context.MODE_PRIVATE);
		    	   SharedPreferences.Editor edit=sp.edit();
		    	   edit.putString("query", tv.getText().toString());
		    	   edit.commit();
			}
		  });
    	   builder.setNegativeButton("取消", new OnClickListener() {
   			
   			@Override
   			public void onClick(DialogInterface arg0, int arg1) {
   				
   			}
   		});
    	   builder.create().show();
       }
       
       private void answer_set(){   //答案设置对话框
    	   final EditText tv=new EditText(context);
    	   tv.setTextColor(context.getResources().getColor(R.color.blak));
    	   tv.setTextSize(20);
    	   AlertDialog.Builder builder=new AlertDialog.Builder(context);
    	   builder.setTitle("答案设置")
    	          .setIcon(R.drawable.p13)
    	          .setView(tv);
    	   builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				 SharedPreferences sp=context.getSharedPreferences("rule_record", Context.MODE_PRIVATE);
		    	   SharedPreferences.Editor edit=sp.edit();
		    	   edit.putString("answer", tv.getText().toString());
		    	   edit.commit();
			}
		  });
    	   builder.setNegativeButton("取消", new OnClickListener() {
   			
   			@Override
   			public void onClick(DialogInterface arg0, int arg1) {
   				
   			}
   		});
    	   builder.create().show();
       }
}
