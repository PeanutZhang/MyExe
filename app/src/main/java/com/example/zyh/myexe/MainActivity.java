package com.example.zyh.myexe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chaychan.viewlib.PhoneEditText;
import com.chaychan.viewlib.PowerfulEditText;
import com.example.zyh.myexe.singlepickers.OptionPicker;

public class MainActivity extends Activity {

    protected PowerfulEditText pet1;
    protected PowerfulEditText pet2;
    protected PowerfulEditText pet3;
    protected PowerfulEditText pet4;
    protected TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        pet1 = (PowerfulEditText) findViewById(R.id.pet_1);
        pet2 = (PowerfulEditText) findViewById(R.id.pet_2);
        pet3 = (PowerfulEditText) findViewById(R.id.pet_3);
        pet4 = (PowerfulEditText) findViewById(R.id.pet_4);
        tvContent = (TextView) findViewById(R.id.tv_content);

    }


    public void getContent(View view) {

        String s = pet1.getText().toString();
        tvContent.setText(s);



    }

    public void gotoRunningnumber(View view) {
        Intent it = new Intent(MainActivity.this,RunningNumberActivity.class);
        startActivity(it);

    }

    public void goPhoneBook(View view) {
        Intent it = new Intent(MainActivity.this, PhoneBookActivity.class);
        startActivity(it);
    }

    public void opntionPicker(View view) {

        OptionPicker picker = new OptionPicker(this, new String[]{
                "第一项", "第二项", "很长很长的","12个月","16个月","18个月","24个月","3年"
        });
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(1);
        picker.setShadowColor(Color.WHITE, 40);
        picker.setSelectedIndex(2);
        picker.setCycleDisable(false);
        picker.setTextSize(14);
        picker.setTextColor(Color.BLACK);

        final int ScreenHeigt = picker.getScreenHeightPixels();
        picker.setHeight(2*ScreenHeigt/5);
        picker.setTopLineColor(Color.GRAY);
        picker.setLineColor(Color.GRAY);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                showToast("index=" + index + ", item=" + item+"screenHeight----"+ScreenHeigt);
            }
        });
        picker.show();

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void openwebview(View view) {
            Intent it = new Intent(this,webActivity.class);
            startActivity(it);

    }
}
