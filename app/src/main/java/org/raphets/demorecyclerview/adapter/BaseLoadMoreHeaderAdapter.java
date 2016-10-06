package org.raphets.demorecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.raphets.demorecyclerview.R;

import java.util.List;

/**
 * Created by RaphetS on 2016/10/1.
 *  支持上拉加载
 *  底部有进度条
 */

public abstract class BaseLoadMoreHeaderAdapter<T> extends RecyclerView.Adapter {
    private Context mContext;
    private boolean isLoading=false;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnItemClickListener mItemClickListener;
    private onLongItemClickListener mLongItemClickListener;
    private List<T> mDatas;
    private int mLayoutId;
    private View mHeadView;
    private final static int TYPE_HEADVIEW=100;
    private final static int TYPE_ITEM=101;
    private final static int TYPE_PROGRESS=102;

    public BaseLoadMoreHeaderAdapter(Context mContext, RecyclerView recyclerView, List<T> mDatas, int mLayoutId) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.mLayoutId = mLayoutId;
        init(recyclerView);
    }
    private void init(RecyclerView recyclerView) {
        //mRecyclerView添加滑动事件监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int    totalItemCount = linearLayoutManager.getItemCount();
                int    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading &&dy>0&&lastVisibleItemPosition>=totalItemCount-1) {
                    //此时是刷新状态
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }
    public void updateData(List<T> data) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        mDatas.addAll(data);
        notifyDataSetChanged();
    }
    public void addHeadView(View headView){
        mHeadView=headView;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_ITEM){
            View itemView= LayoutInflater.from(mContext).inflate(mLayoutId,parent,false);
            BaseViewHolder baseViewHolder=new BaseViewHolder(itemView);
            return baseViewHolder;
        }else if (viewType==TYPE_HEADVIEW){
            HeadViewHolder headViewHolder=new HeadViewHolder(mHeadView);
            return headViewHolder;
        } else{
            View progressView=LayoutInflater.from(mContext).inflate(R.layout.progress_item,parent,false);
            ProgressViewHolder progressViewHolder= new ProgressViewHolder(progressView);
            return progressViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
      if (holder instanceof BaseViewHolder){
          convert(mContext, holder, mDatas.get(position));
          ((BaseViewHolder) holder).mItemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  mItemClickListener.onItemClick(v,position-1);
              }
          });
          ((BaseViewHolder) holder).mItemView.setOnLongClickListener(new View.OnLongClickListener() {
              @Override
              public boolean onLongClick(View v) {
                  mLongItemClickListener.onLongItemClick(v,position-1);
                  return true;
              }
          });
      }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeadView!=null){
         if (position==getItemCount()-1){
            return TYPE_PROGRESS;
          }else if (position==0){
            return TYPE_HEADVIEW;
           }else {
            return TYPE_ITEM;
           }
        }else {
            if (position==getItemCount()-1){
                return TYPE_PROGRESS;
            }else {
                return TYPE_ITEM;
            }
        }
    }


    public abstract void convert(Context mContext, RecyclerView.ViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mDatas.size()+1;
    }
    public void setLoading(boolean b){
        isLoading=b;
    }

    public    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public    interface onLongItemClickListener {
        void onLongItemClick(View view, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setonLongItemClickListener(onLongItemClickListener listener) {
        this.mLongItemClickListener = listener;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        this.mOnLoadMoreListener=listener;
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
    public class ProgressViewHolder extends RecyclerView.ViewHolder{
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder{
        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }
}
