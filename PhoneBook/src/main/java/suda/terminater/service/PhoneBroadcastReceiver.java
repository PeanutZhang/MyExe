package suda.terminater.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
/**
 * ���㲥����������
 * @author lipeng
 *
 */
public class PhoneBroadcastReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		int i=2;
		//Toast.makeText(context, "�㲥����", Toast.LENGTH_SHORT).show();
		if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){ //ȥ�����
			
		}else{
			Intent pit=new Intent(context,PhoneService.class);
			pit.putExtra("bool", i);
			//pit.putExtra("mIncomingNumber", mIncomingNumber);
			context.startService(pit);
		}
		
	}

}
