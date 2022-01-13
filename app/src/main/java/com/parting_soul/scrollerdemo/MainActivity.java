package com.parting_soul.scrollerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View layout = findViewById(R.id.layout);
        Button bt = findViewById(R.id.bt1);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.scrollTo(-100, -100);
            }
        });

        findViewById(R.id.bt2)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layout.scrollBy(100, 100);
                    }
                });
    }

    public void onClick(View view) {
        Class clazz = null;
        switch (view.getId()) {
            case R.id.bt_pulldown:
                clazz = PullDownActivity.class;
                break;
            case R.id.bt_login:
                clazz = LoginActivity.class;
                break;
            case R.id.bt_qq_message_item:
                clazz = QQMessageItemActivity.class;
                break;
            case R.id.bt_viewpager:
                clazz = ViewPagerActivity.class;
                break;
        }
        if (clazz != null) {
            startActivity(new Intent(this, clazz));
        }
    }
}
