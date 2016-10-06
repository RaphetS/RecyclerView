package org.raphets.demorecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;


/**
 * Created by RaphetS on 2016/10/1.
 *  支持上拉加载
 * 底部没有进度条
 */

public abstract class BaseLoadMoreAdapter<T> extends BaseAdapter<T> {
    private RecyclerView mRecyclerView;
    private boolean isLoading=false;
    private OnLoadMoreListener mOnLoadMoreListener;
    public BaseLoadMoreAdapter(Context mContext, RecyclerView recyclerView,List mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
        this.mRecyclerView=recyclerView;
        init();

    }

    private void init() {
            //mRecyclerView添加滑动事件监听
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int    totalItemCount = linearLayoutManager.getItemCount();
                    int    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading &&dy>0&&lastVisibleItemPosition>=totalItemCount-5) {
                        //此时是刷新状态
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });
        }


    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        this.mOnLoadMoreListener=listener;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }
    public void setLoading(boolean b){
        isLoading=b;
    }
}
