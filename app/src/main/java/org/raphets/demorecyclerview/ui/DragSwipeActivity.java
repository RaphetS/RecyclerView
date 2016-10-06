package org.raphets.demorecyclerview.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import org.raphets.demorecyclerview.DefaultItemTouchHelpCallback;
import org.raphets.demorecyclerview.R;
import org.raphets.demorecyclerview.utils.SnackbarUtil;
import org.raphets.demorecyclerview.adapter.BaseAdapter;
import org.raphets.demorecyclerview.adapter.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragSwipeActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> mData = new ArrayList<>();
    private DragSwipeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_swipe);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        init();
        addListener();

    }


    private void init() {
        for (int i = 0; i < 20; i++) {
            mData.add("item" + i);
        }

        mAdapter = new DragSwipeAdapter(this, mData, R.layout.item);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        setItemTouchHelper();

        SnackbarUtil.show(getWindow().getDecorView(),"支持长按拖拽、左右滑动删除的哦(⊙o⊙)哦");

    }


    private void setItemTouchHelper() {
        DefaultItemTouchHelpCallback mCallback= new DefaultItemTouchHelpCallback(new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
                // 滑动删除的时候，从数据库、数据源移除，并刷新UI
                if (mData != null) {
                    mData.remove(adapterPosition);
                    mAdapter.notifyItemRemoved(adapterPosition);
                }
            }

            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
                if (mData != null) {

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


    private void addListener() {
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(DragSwipeActivity.this, "click" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.setonLongItemClickListener(new BaseAdapter.onLongItemClickListener() {
            @Override
            public void onLongItemClick(View view, int postion) {
                Toast.makeText(DragSwipeActivity.this, "long" + postion, Toast.LENGTH_SHORT).show();

            }
        });
    }


   class DragSwipeAdapter extends BaseAdapter<String> {
       public DragSwipeAdapter(Context mContext, List<String> mDatas, int mLayoutId) {
           super(mContext, mDatas, mLayoutId);
       }

       @Override
       protected void convert(Context mContext, BaseViewHolder holder, String s) {
            holder.setText(R.id.tv,s);
       }
   }
}





