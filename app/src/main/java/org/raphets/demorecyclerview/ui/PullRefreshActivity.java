package org.raphets.demorecyclerview.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.raphets.demorecyclerview.R;
import org.raphets.demorecyclerview.adapter.BaseLoadMoreAdapter2;
import org.raphets.demorecyclerview.adapter.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PullRefreshActivity extends AppCompatActivity {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<String> mDatas=new ArrayList<>();
    private int page=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh);
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

        mAdapter.setOnLoadMoreListener(new BaseLoadMoreAdapter2.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
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
        page++;
        mDatas.add(null);
        mAdapter.notifyDataSetChanged();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final List<String> data = new ArrayList<String>();
                for (int i = 0; i < 6; i++) {
                    data.add("page" + page+"item"+i);
                }
                //addData()是在自定义的Adapter中自己添加的方法，用来给list添加数据
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDatas.remove(mDatas.size()-1);
                        mAdapter.addAll(data);
                        mAdapter.setLoading(false);
                    }
                });
            }
        }.start();

    }

    class MyAdapter  extends BaseLoadMoreAdapter2<String>{
        public MyAdapter(Context mContext, RecyclerView recyclerView, List<String> mDatas, int mLayoutId) {
            super(mContext, recyclerView, mDatas, mLayoutId);
        }

        @Override
        public void convert(Context mContext, RecyclerView.ViewHolder holder, String s) {
            if (holder instanceof BaseViewHolder){
                ((BaseViewHolder) holder).setText(R.id.tv,s);
            }
        }
    }
}

