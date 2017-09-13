package com.isuhuo.newflash.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.isuhuo.newflash.base.BaseActivity;

/**
 * Created by Administrator on 2017-08-30.
 */
public abstract class RootAdapter<T> extends BaseAdapter {
    private final Context context;

    private final LayoutInflater inflater;

    private int resourcesId;

    private BaseActivity activity;

    private List<Integer> arrayViewId;

    private List<T> data;

    private int position;

    public RootAdapter(Context context, List<T> list, int layoutId) {
        this.context = context;
        if (context instanceof BaseActivity) {
            activity = (BaseActivity) context;
        }
        resourcesId = layoutId;
        data = list;
        inflater = LayoutInflater.from(context);
    }

    public void addViewClickId(int viewId) {
        if (arrayViewId == null) {
            arrayViewId = new ArrayList<Integer>();
        }
        arrayViewId.add(viewId);
    }

    @Override
    public int getCount() {
        return (data == null || data.size() == 0) ? 0 : data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getList() {
        return data;
    }

    public void setList(List<T> list) {
        this.data = list;
    }

    public Context getContext() {
        return context;
    }

    public int getPosition() {
        return position;
    }

    public BaseActivity getActivity() {
        return activity;
    }

    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterViewClickListener listener = null;
        if (convertView == null) {
            convertView = inflater.inflate(resourcesId, parent, false);
            if (arrayViewId != null && arrayViewId.size() > 0) {
                listener = new AdapterViewClickListener();
                convertView.setTag(resourcesId, listener);
                for (Integer id : arrayViewId) {
                    convertView.findViewById(id).setOnClickListener(listener);
                }
            }
        }
        bindView(convertView, position, data.get(position));
        listener = (AdapterViewClickListener) convertView.getTag(resourcesId);
        if (listener != null) {
            listener.parentV = convertView;
            listener.pos = position;
        }
        return convertView;
    }

    public void bindView(View convertView, int position, T t) {
    }

   /* public abstract void bindView(View itemV, int position, T obj);*/

    public void bindViewClick(View parentV, View v, T obT, int position) {
    }

    private class AdapterViewClickListener implements View.OnClickListener {

        int pos;
        View parentV;

        @Override
        public void onClick(View v) {
            bindViewClick(parentV, v, data.get(pos), pos);
            position = pos;
        }
    }

    /*
     * final public static class ViewHolder{ private final SparseArray<View>
     * views ; private View itemV;
     * 
     * private ViewHolder() { views = new SparseArray<View>(); }
     * 
     * public static ViewHolder getHolder(View itemV){ ViewHolder holder =
     * (ViewHolder) itemV.getTag(); if(holder == null){ holder = new
     * ViewHolder(); itemV.setTag(holder); holder.itemV = itemV; } return
     * holder; }
     * 
     * @SuppressWarnings("unchecked") public <T extends View> T getItemV(int
     * id){ View v = views.get(id); if(v == null){ v = itemV.findViewById(id);
     * views.put(id, v); } return (T) v; } }
     */
    final public static class ViewHolder{
        private final SparseArray<View> views ;
        private View itemV;

        private ViewHolder() {
            views = new SparseArray<View>();
        }

        public static ViewHolder getHolder(View itemV){
            ViewHolder holder = (ViewHolder) itemV.getTag();
            if(holder == null){
                holder = new ViewHolder();
                itemV.setTag(holder);
                holder.itemV = itemV;
            }
            return holder;
        }
        @SuppressWarnings("unchecked")
        public <T extends View> T getItemV(int id){
            View v =  views.get(id);
            if(v == null){
                v = itemV.findViewById(id);
                views.put(id, v);
            }
            return (T) v;
        }
    }
    public void notifyDataSingleItem(ListView listView) {
        if (listView != null) {
            int start = listView.getFirstVisiblePosition();
            int last = listView.getLastVisiblePosition();
            for (int i = start, j = last; i <= j; i++)
                if (position == listView.getItemIdAtPosition(i)) {
                    View view = listView.getChildAt(i - start);
                    getView(i, view, listView);
                    Log.d("notifyDataSingleItem", "position=" + position);
                    break;
                }
        }
    }
}
