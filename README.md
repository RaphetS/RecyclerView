### 对RecyclerViewAdapter进行了封装，使用起来将更简单。

-##### 封装了OnItemClickListener，onLongItemClickListener

-##### 封装了上拉加载OnLoadMoreListener

-##### 封装了HeadView

#######使用示例：

写一个Adapter继承BaseLoadMoreHeaderAdapter
```
    class MyAdapter  extends BaseLoadMoreHeaderAdapter<String> {
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
```

在Activity中为RecyclerView设置Adapter

```
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mAdapter=new MyAdapter(this,mRecyclerView,mDatas,R.layout.item);
        mRecyclerView.setLayoutManager(layoutManager);
        View headView= LayoutInflater.from(this).inflate(R.layout.headview,mRecyclerView,false);
        mAdapter.addHeadView(headView);
        mRecyclerView.setAdapter(mAdapter);
        
        mAdapter.setOnLoadMoreListener(new BaseLoadMoreHeaderAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });

        mAdapter.setOnItemClickListener(new BaseLoadMoreHeaderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_SHORT).show();

            }
        });
```
