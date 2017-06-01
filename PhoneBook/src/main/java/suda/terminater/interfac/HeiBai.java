package suda.terminater.interfac;

import java.util.ArrayList;
import java.util.List;

import suda.terminater.R;
import suda.terminater.heibai.Text;
import suda.terminater.heibai.Whilechilde;
import suda.terminater.heibai.Whilepeople;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suda.xiangmu.sqlite.MyDbHelper;
/**
 * ������Ϊ�ڰ���������
 * ��Ҫ�����۵��б��һЩҳ����ת
 * @author lipeng
 *
 */
public class HeiBai extends Activity{
	 private MyDbHelper mydbhelper;
	    private Text text;
	    private List<List<Object>>list;
	    private ExpandableListAdapter ae;
	    private ExpandableListView listview;
	    private int addbookleng=1;
	    public String []addbook=new String[addbookleng];
	    List<Object>lis;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.expendlist);
			list=new ArrayList<List <Object>>();
		    list.add(new ArrayList<Object>());
		    lis=new ArrayList<Object>();
		    list.add(1, lis);
			listview=(ExpandableListView) findViewById(R.id.list);
			//getPhone();
			getlistdata();
			Expand();
		}
	   
       public void fresh(){
    	   getlistdata();
			Expand();
       }
       
		@Override
	protected void onResume() {
			 fresh();
		super.onResume();
	}

		public void Expand(){       //����
			 ae=new ExpandableListAdapter(){
					String []generalsTypes=new String[]{"������","������"};
					
		         
					TextView getTextView() {
						@SuppressWarnings("deprecation")
						AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
								ViewGroup.LayoutParams.FILL_PARENT, 64);
						TextView textView = new TextView(
								HeiBai.this);
						textView.setLayoutParams(lp);
						textView.setGravity(Gravity.CENTER_VERTICAL);
						textView.setPadding(36, 0, 0, 0);
						textView.setTextSize(20);
						textView.setTextColor(Color.BLACK);
						return textView;
					}
					@Override
					public Object getChild(int groupPosition, int childPosition) { //�˷�����д
						
                     text=(Text) list.get(groupPosition).get(childPosition);
				        String str=text.getNumber();
						return str;
					
					}

					@Override
					public long getChildId(int groupPosition, int childPosition) {
						// TODO Auto-generated method stub
						return childPosition;
					}

					@Override
					public View getChildView(int groupPosition, int childPosition,
							boolean isLastChild, View convertView, ViewGroup parent) {
						LinearLayout ll=new LinearLayout(HeiBai.this);
						ll.setOrientation(LinearLayout.VERTICAL);
						if((groupPosition==0&&childPosition==0)||(groupPosition==1&&childPosition==0)){
							ll.setOrientation(LinearLayout.HORIZONTAL);
							ll.setGravity(Gravity.CENTER_VERTICAL);
							ImageView ivt=new ImageView(HeiBai.this);
							ivt.setImageDrawable(getResources().getDrawable(R.drawable.p5));
							ll.addView(ivt);
							TextView tvv=new TextView(HeiBai.this);
							tvv.setTextColor(getResources().getColor(R.color.blak));
							tvv.setText("�����Ա");
							tvv.setTextSize(18);
							ll.addView(tvv);
							ll.setWeightSum(100);
						}
						else if(groupPosition==0){

							TextView tv=getTextView();
							tv.setText(((Text) list.get(groupPosition).get(childPosition)).getName());
							tv.setTextSize(20);
							tv.setTextColor(Color.BLACK);
							ll.addView(tv);
							
							TextView tnumber=new TextView(HeiBai.this);
							tnumber.setText(((Text) list.get(groupPosition).get(childPosition)).getNumber());
							tv.setTextSize(16);
							ll.addView(tnumber);
							
						}
						else if(groupPosition==1){
							TextView tv=getTextView();
							tv.setText(((Text) list.get(groupPosition).get(childPosition)).getName());
							tv.setTextSize(20);
							tv.setTextColor(Color.BLACK);
							ll.addView(tv);
							
							TextView tnumber=new TextView(HeiBai.this);
							String str=((Text) list.get(groupPosition).get(childPosition)).getNumber();
							str+="    |    �����أ��绰";
							str+=((Text) list.get(groupPosition).get(childPosition)).getFrequency();
							tnumber.setText(str);
							tv.setTextSize(16);
							ll.addView(tnumber);
						}
						return ll;
					}

					@Override
					public int getChildrenCount(int groupPosition) {
						return list.get(groupPosition).size();
					}

					@Override
					public long getCombinedChildId(long groupId, long childId) {
						// TODO Auto-generated method stub
						return childId;
					}

					@Override
					public long getCombinedGroupId(long groupId) {
						// TODO Auto-generated method stub
						return groupId;
					}

					@Override
					public Object getGroup(int groupPosition) {
						return generalsTypes[groupPosition];
					}

					@Override
					public int getGroupCount() {
						return generalsTypes.length;
					}

					@Override
					public long getGroupId(int groupPosition) {
						return groupPosition;
					}

					@Override
					public View getGroupView(int groupPosition, boolean isExpanded,
							View convertView, ViewGroup parent) {
						LinearLayout ll=new LinearLayout(HeiBai.this);
						ll.setOrientation(LinearLayout.HORIZONTAL);
						ll.setGravity(Gravity.CENTER_VERTICAL);
						TextView tv=getTextView();
						tv.setText(getGroup(groupPosition).toString());
					    tv.setTextColor(Color.BLACK);
					    ll.addView(tv);
						return ll;
					}

					@Override
					public boolean hasStableIds() {
						// TODO Auto-generated method stub
						return true;
					}

					@Override
					public boolean isChildSelectable(int groupPosition,
							int childPosition) {
						return true;
					}

					@Override
					public boolean isEmpty() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public void onGroupCollapsed(int groupPosition) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onGroupExpanded(int groupPosition) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void registerDataSetObserver(DataSetObserver observer) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void unregisterDataSetObserver(DataSetObserver observer) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public boolean areAllItemsEnabled() {
						// TODO Auto-generated method stub
						return false;
					}
				};
				listview.setAdapter(ae);
				
				listview.setOnChildClickListener(new OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent, View v,
							int groupPosition, int childPosition, long id) {
						
						String str= ((Text) list.get(groupPosition).get(childPosition)).getNumber();
					    SharedPreferences sp=HeiBai.this.getSharedPreferences("book", MODE_PRIVATE);
					    SharedPreferences.Editor editor=sp.edit();
						    editor.clear();
						    editor.putString("number", str);
						    editor.putInt("group", groupPosition);
						    editor.commit(); //��ʽ�ύ��������Ч
						
						if(childPosition==0){
							
							if(groupPosition==0||groupPosition==1){
	               				Intent intent=new Intent();
	               				intent.setClass(HeiBai.this, Whilepeople.class);
	               				startActivity(intent);
							}
	                  
							return true;
						}
						else{
						    //���һЩ���ݵ������ļ���
						   
						    Intent inten =new Intent();
						    inten.setClass(HeiBai.this, Whilechilde.class);
						    startActivity(inten);
						}
						return false;
					}
				});
		}
		/**
		 * �Ѻڰ�������Ϣ����ExpandList��
		 */
		public  void getlistdata(){
			list.get(0).clear();
			list.get(1).clear();
			text=new Text();
			text.setName("����");
			text.setNumber("15677885646");
			list.get(0).add(text);
			text.setFrequency("3");
			list.get(1).add(text);
			mydbhelper=new MyDbHelper(HeiBai.this);
			mydbhelper.open();
			Cursor cur=mydbhelper.querData("white");//��ȡ������������
			cur.moveToFirst();
			while(!cur.isAfterLast()){
				text=new Text();
				text.setName(cur.getString(1));
				text.setNumber(cur.getString(0));
				list.get(0).add(text);
				cur.moveToNext();
			}
			
			cur=mydbhelper.querData("black");   //��ȡ����������
			cur.moveToFirst();
			while(!cur.isAfterLast()){
				text=new Text();
				text.setName(cur.getString(1));
				text.setNumber(cur.getString(0));
				text.setFrequency(cur.getString(2));
				list.get(1).add(text);
				cur.moveToNext();
			}
			mydbhelper.close();
		}
		/**
		 * �˷�����ɽ�ͨѶ¼�е����������ݿ�
		 * ���������ݿ�
		 */
		public void getPhone(){   
			mydbhelper=new MyDbHelper(HeiBai.this);
			mydbhelper.open();
			mydbhelper.clearData("addbook");  //ɾ��ͨѶ¼
			ContentResolver cr=getContentResolver();
			
			List<String>phones=new ArrayList<String>();//��ֹ��һ�������ظ����
			boolean bool=false;
			
			Cursor cursor=cr.query(ContactsContract.Contacts.CONTENT_URI,
					null, null
					, null, null);
			 while(cursor.moveToNext())
		        {
		           //ȡ����ϵ������
		           int nameFieldColumnIndex= cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
		            String contact= cursor.getString(nameFieldColumnIndex);
		           //ȡ�õ绰����
		            String ContactId= cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
		            Cursor phone= cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		            		null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ "=" + ContactId,null,null);
		           
		            while(phone.moveToNext())
		            {
		                String PhoneNumber= phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		                PhoneNumber=PhoneNumber.concat("0");
		                mydbhelper.insertData(PhoneNumber, contact, "addbook");	
		               // string+= ( contact+":" + PhoneNumber+ "\n");
		            }
		            phone.close();
		        }
	        
			cursor.close();
			mydbhelper.close();
		}
		//***************�˵��¼�*******************

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.hbmenu, menu);
			return super.onCreateOptionsMenu(menu);
		}

		@Override
		public boolean onMenuItemSelected(int featureId, MenuItem item) {
			int item_id=item.getItemId();
			switch(item_id){
			case R.id.hbdrop:  //�˳�����
				HeiBai.this.finish();
				break;
			case R.id.clearhei:  //��պ�������
				mydbhelper=new MyDbHelper(HeiBai.this);
				mydbhelper.open();
				mydbhelper.clearData("black");
				mydbhelper.close();
				break;
			case R.id.clearbai:  //��հ�����
				mydbhelper=new MyDbHelper(HeiBai.this);
				mydbhelper.open();
				mydbhelper.clearData("white");
				mydbhelper.close();
				break;
			case R.id.aaddbook:   //����ͨѶ¼
				new Thread(){

					@Override
					public void run() {
						getPhone();  //��Ϊ�˷�����ʱ�ϳ�������Ҫһ���߳�
					}
					
				}.start();
				break;
			}
			return super.onMenuItemSelected(featureId, item);
		}
		
	}



