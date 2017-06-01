package suda.terminater.district;

import java.util.List;

import suda.terminater.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 省列表
 * @author lipeng
 *
 */
public class Province extends Activity{
     
	List<String>list=Region_djust.getProvice();
	TextView quit;//退出
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.province);
		
		ListView lv=(ListView) findViewById(R.id.province_list);
		quit=(TextView) findViewById(R.id.province_quit);
		
		BaseAdapter ba=new BaseAdapter() {
			
			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
			   TextView tv=new TextView(Province.this);
			   tv.setText(list.get(arg0));
			   tv.setTextSize(18);
			   tv.setPadding(5, 7, 0, 7);
			   tv.setTextColor(getResources().getColor(R.color.blak));
				return tv;
			}
			
			@Override
			public long getItemId(int arg0) {
				return 0;
			}
			
			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				return list.size();
			}
		};
		lv.setAdapter(ba);
		
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
				 SharedPreferences.Editor edit=sp.edit();
				 edit.putInt("provinceid", arg2);
				 if(arg2<list.size()-4){
					//edit.putString("provincename", list.get(arg2)+"-");
				 }else{
					 edit.putString("provincename", list.get(arg2));
					 edit.putString("cityname","");
					 Province.this.finish();
				 }
				 edit.commit();
				 if(arg2<list.size()-4){
					 Intent intent=new Intent(Province.this,City.class);
					 startActivity(intent);
					 Province.this.finish();
				 }
			}
			
		});
		
		quit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Province.this.finish();
			}
			
		});
	}
        
}
