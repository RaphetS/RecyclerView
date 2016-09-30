package org.raphets.demorecyclerview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> mData = new ArrayList<>();
    private MyRVAdapter mAdapter;
    private String TAG = "**************";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        init();
    }

    private void init() {
        for (int i = 0; i < 10; i++) {
            mData.add("item" + i);
        }

        mAdapter = new MyRVAdapter(this, mData, R.layout.item);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "click" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.setonLongItemClickListener(new BaseAdapter.onLongItemClickListener() {
            @Override
            public void onLongItemClick(View view, int postion) {
                Toast.makeText(MainActivity.this, "long" + postion, Toast.LENGTH_SHORT).show();

            }
        });



        DefaultItemTouchHelpCallback mCallback= new DefaultItemTouchHelpCallback(new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
                // 滑动删除的时候，从数据库、数据源移除，并刷新UI
                if (mData != null) {
//                    mPresenter.deleteLikeData(mList.get(adapterPosition).getId());
                    mData.remove(adapterPosition);
                    mAdapter.notifyItemRemoved(adapterPosition);
                }
            }

            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
                if (mData != null) {
                    // 更换数据库中的数据Item的位置
                    boolean isPlus = srcPosition < targetPosition;
//                    mPresenter.changeLikeTime(mList.get(srcPosition).getId(),mList.get(targetPosition).getTime(),isPlus);
                    // 更换数据源中的数据Item的位置
                    Collections.swap(mData, srcPosition, targetPosition);
                    // 更新UI中的Item的位置，主要是给用户看到交互效果
                    mAdapter.notifyItemMoved(srcPosition, targetPosition);
                    return true;
                }
                return false;
            }
        });
        mCallback.setDragEnable(true);
        mCallback.setSwipeEnable(true);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    class MyRVAdapter extends BaseAdapter<String> {

        public MyRVAdapter(Context mContext, List<String> mDatas, int mLayoutId) {
            super(mContext, mDatas, mLayoutId);
        }

        @Override
        protected void convert(Context mContext, BaseViewHolder holder, String s) {
            holder.setText(R.id.tv, s);
        }
    }

}





