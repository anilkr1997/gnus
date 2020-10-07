package com.bspl.gnus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Splacescreen extends Activity {
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        setContentView(R.layout.activity_splacescreen);


        Timer timer=new Timer();

        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(Splacescreen.this,MainActivity.class));
                finish();
            }
        };
        timer.schedule(timerTask,4000);
    }
}