package suda.terminater.district;

import java.util.ArrayList;
import java.util.List;

import suda.terminater.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class City extends Activity{
    
	List<ArrayList<String>>list=Region_djust.getCity();
	List<String>item=Region_djust.getProvice();
	TextView quit;
	int n=0;//确定省市
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city);
		ListView lv=(ListView) findViewById(R.id.city_list);
		quit=(TextView) findViewById(R.id.city_quit);
		
		SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
		  n=sp.getInt("provinceid", 0);
		  quit.setText(item.get(n)+"省");
        BaseAdapter ba=new BaseAdapter(){
			
			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
			   TextView tv=new TextView(City.this);
			   tv.setText(list.get(n).get(arg0));
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
				return list.get(n).size();
			}
		};
		lv.setAdapter(ba);
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
				 SharedPreferences.Editor edit=sp.edit();
				 edit.putInt("cityid", arg2);
				 edit.putString("cityname",list.get(n).get(arg2));
				 edit.putString("provincename", item.get(n)+"-");
				 edit.commit();
				 City.this.finish();
			}
			
		});
		quit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				City.this.finish();
			}
			
		});
	}
       
}
