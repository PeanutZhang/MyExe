package suda.terminater.interfac;

import java.io.InputStream;

import suda.terminater.R;
import suda.terminater.district.FileUtil;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
/**
 * 本类为主页面，使用选项卡布局包含拦截记录、黑白名单、拦截记录。
 * @author lipeng
 *
 */
public class Main extends TabActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maininterface);
		
		Resources rs=getResources();   //获得资源管理器
		TabHost tabhost=getTabHost();   
		TabHost.TabSpec tabspec;
		Intent intent;
		
	    intent=new Intent(Main.this,Record.class); //跳转到拦截记录页面
	    tabspec=tabhost.newTabSpec("拦截记录")
	    		       .setIndicator("", rs.getDrawable(R.drawable.m3))
	    		       .setContent(intent);
	    tabhost.addTab(tabspec);
	    
	    intent=new Intent(Main.this,HeiBai.class); //跳转到黑白名单管理页面
	    tabspec=tabhost.newTabSpec("黑白名单")
	    		       .setIndicator("", rs.getDrawable(R.drawable.m1))
	    		       .setContent(intent);
	    tabhost.addTab(tabspec);
	    
	    intent=new Intent(Main.this,Rule.class); //跳转到拦截规则设置页面
	    tabspec=tabhost.newTabSpec("拦截规则")
	    		       .setIndicator("", rs.getDrawable(R.drawable.m2))
	    		       .setContent(intent);
	    tabhost.addTab(tabspec);
	    
		tabhost.setCurrentTab(0); //****设置拦截记录页面为第一个显示页面****
	}
	
}
