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
 * ����Ϊ��ҳ�棬ʹ��ѡ����ְ������ؼ�¼���ڰ����������ؼ�¼��
 * @author lipeng
 *
 */
public class Main extends TabActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maininterface);
		
		Resources rs=getResources();   //�����Դ������
		TabHost tabhost=getTabHost();   
		TabHost.TabSpec tabspec;
		Intent intent;
		
	    intent=new Intent(Main.this,Record.class); //��ת�����ؼ�¼ҳ��
	    tabspec=tabhost.newTabSpec("���ؼ�¼")
	    		       .setIndicator("", rs.getDrawable(R.drawable.m3))
	    		       .setContent(intent);
	    tabhost.addTab(tabspec);
	    
	    intent=new Intent(Main.this,HeiBai.class); //��ת���ڰ���������ҳ��
	    tabspec=tabhost.newTabSpec("�ڰ�����")
	    		       .setIndicator("", rs.getDrawable(R.drawable.m1))
	    		       .setContent(intent);
	    tabhost.addTab(tabspec);
	    
	    intent=new Intent(Main.this,Rule.class); //��ת�����ع�������ҳ��
	    tabspec=tabhost.newTabSpec("���ع���")
	    		       .setIndicator("", rs.getDrawable(R.drawable.m2))
	    		       .setContent(intent);
	    tabhost.addTab(tabspec);
	    
		tabhost.setCurrentTab(0); //****�������ؼ�¼ҳ��Ϊ��һ����ʾҳ��****
	}
	
}
