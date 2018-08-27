package com.example.demo.myapplication;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.jaeger.library.StatusBarUtil;

public class SplashActivity extends AppCompatActivity {

    Button btnJump;

    CountDownTimer mCountDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        //初始化控件
        initView();

        //设置状态栏透明，并偏移布局
        StatusBarUtil.setTranslucentForImageViewInFragment(this,btnJump);



        //倒计时
        mCountDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                btnJump.setText(String.format("跳过(%ss)",millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                btnJump.setText("正在跳转");
                enterMain();
            }
        };
        mCountDownTimer.start();



    }

    private void initView() {
        btnJump  = findViewById(R.id.btnJump);
        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mCountDownTimer != null){

                    mCountDownTimer.cancel();
                    mCountDownTimer.onFinish();
                    mCountDownTimer = null;
                }

            }
        });
    }


    private void enterMain(){
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCountDownTimer !=null){
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }
}
