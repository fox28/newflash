package com.isuhuo.newflash.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.isuhuo.newflash.R;
import com.isuhuo.newflash.util.Cache;

/**
 * Created by Administrator on 2017-08-30.
 */
public class SmoothListView extends ListView implements AbsListView.OnScrollListener {
    //	public static final int LOAD_DATA_FINSH = 1;

    private static final String TAG = "SwepLoadListview";

    private View footerLayout = null;

    private OnLoadActionListener onLoadListener;

    private OnRefreshListener onRefreshListener;

    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;
    private int mTouchSlop;

    //是否已经请求完毕，避免用户重复发送请求
    private boolean isLoading = false;

    public SmoothListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public SmoothListView(Context context) {
        this(context, null);
    }

    public SmoothListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initFooterView(context);
    }

    private void initFooterView(Context context) {
        LinearLayout footerView = (LinearLayout)inflate(context, R.layout.listview_load_more_layout, null);
        footerLayout = footerView.findViewById(R.id.loadFooterLiner);
        addFooterView(footerView);
        setOnScrollListener(this);
    }

    public void setOnLoadActionListener(OnLoadActionListener listener) {
        this.onLoadListener = listener;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.onRefreshListener = listener;
    }


    private int lastItemIndex = 0;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if(lastItemIndex == getCount() && scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && !isLoading && onLoadListener != null){
            setLoading(true);
            onLoadListener.onLoad();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        //Log.d(TAG, "firstVisibleItem=" + firstVisibleItem + " visibleItemCount=" + visibleItemCount + " totalItemCount=" + totalItemCount);
        lastItemIndex = firstVisibleItem + visibleItemCount;
    }

    /**
     * 不要动这里的代码有待完善
     */
    int y = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                // 抬起
                if((mYDown - mLastY) < mTouchSlop && (mYDown != mLastY) && Cache.isTop){
                    Cache.isTop = false;
                    onRefreshListener.onRefresh();
                }
                break;
            default:break;
        }

        return super.onTouchEvent(event);
		/*y = (int) ev.getRawY();
		return super.onTouchEvent(ev);*/
    }


    public void updateFooterView(boolean isLoading) {
        if(isLoading){
            footerLayout.setVisibility(View.VISIBLE);
            setSelection(getBottom());
        }else{
            footerLayout.setVisibility(View.GONE);
        }
    }

    public void setLoading(boolean isLoad) {
        this.isLoading = isLoad;
        updateFooterView(isLoad);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public static interface OnLoadActionListener{
        void onLoad();
    }

    public static interface OnRefreshListener{
        void onRefresh();
    }

}
