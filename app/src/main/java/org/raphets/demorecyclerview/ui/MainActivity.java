package org.raphets.demorecyclerview.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.raphets.demorecyclerview.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnDragSwipe;
    private Button btnPullRefresh;
    private Button btnPullRefreshNoTip;
    private Button btnSupportHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDragSwipe= (Button) findViewById(R.id.btn_first);
        btnPullRefresh= (Button) findViewById(R.id.btn_second);
        btnPullRefreshNoTip= (Button) findViewById(R.id.btn_third);
        btnSupportHeader= (Button) findViewById(R.id.btn_fourth);
        btnDragSwipe.setOnClickListener(this);
        btnPullRefresh.setOnClickListener(this);
        btnPullRefreshNoTip.setOnClickListener(this);
        btnSupportHeader.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_first:
                startActivity(new Intent(MainActivity.this,DragSwipeActivity.class));
                break;
            case R.id.btn_second:
                startActivity(new Intent(MainActivity.this,PullRefreshActivity.class));
                break;
            case R.id.btn_third:
                startActivity(new Intent(MainActivity.this,PullRefreshNoTipActivity.class));
                break;
            case R.id.btn_fourth:
                startActivity(new Intent(MainActivity.this,SupportHeadViewActivity.class));
                break;
            default:
                break;
        }
    }
}
