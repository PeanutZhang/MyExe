package com.example.zyh.myexe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chaychan.viewlib.NumberRunningTextView;

public class RunningNumberActivity extends Activity {

    protected NumberRunningTextView tvMoney;
    protected NumberRunningTextView tvNum;
    int money = 18888;
    int num = 666666;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_running_number);
        initView();
    }

    public void setNum(View view) {

       tvMoney.setContent(++money+"");
        tvNum.setContent(++num+"");


    }

    private void initView() {
        tvMoney = (NumberRunningTextView) findViewById(R.id.tv_money);
        tvNum = (NumberRunningTextView) findViewById(R.id.tv_num);
    }

    public void toast(View view) {

       String mony= tvMoney.getText().toString();
        Toast.makeText(getBaseContext(),"获取金额内容---"+mony,Toast.LENGTH_SHORT).show();
    }
}
