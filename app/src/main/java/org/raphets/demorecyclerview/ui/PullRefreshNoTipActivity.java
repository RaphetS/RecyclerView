package org.raphets.demorecyclerview.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.raphets.demorecyclerview.R;
import org.raphets.demorecyclerview.adapter.BaseAdapter;
import org.raphets.demorecyclerview.adapter.BaseLoadMoreAdapter;
import org.raphets.demorecyclerview.adapter.BaseLoadMoreAdapter2;
import org.raphets.demorecyclerview.adapter.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PullRefreshNoTipActivity extends AppCompatActivity {
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    private List<String> mDatas=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh_no_tip);
        mRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerView);

        init();
        addListener();
    }
    private void init() {
        for (int i=0;i<10;i++){
            mDatas.add("item>>>"+i);
        }

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mAdapter=new MyAdapter(this,mRecyclerView,mDatas,R.layout.item);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void addListener() {
        mRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.BLACK);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseLoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });

        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void refreshData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final List<String> data=new ArrayList<String>();
                for (int i=0;i<10;i++){
                    data.add("refresh--"+new Random().nextInt(10));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.updateData(data);
                        mRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }.start();
    }

    private void loadMore() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final List<String> data = new ArrayList<String>();
                for (int i = 0; i < 5; i++) {
                    data.add("load" + new Random().nextInt(100));
                }
                //addData()是在自定义的Adapter中自己添加的方法，用来给list添加数据
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addAll(data);
                        mAdapter.setLoading(false);
                    }
                });
            }
        }.start();

    }




    class MyAdapter extends BaseLoadMoreAdapter<String> {
        public MyAdapter(Context mContext, RecyclerView recyclerView, List mDatas, int mLayoutId) {
            super(mContext, recyclerView, mDatas, mLayoutId);
        }

        @Override
        protected void convert(Context mContext, BaseViewHolder holder, String s) {
            holder.setText(R.id.tv,s);
        }
    }

}
