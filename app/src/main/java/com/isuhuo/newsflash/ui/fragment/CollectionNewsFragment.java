package com.isuhuo.newsflash.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isuhuo.newsflash.R;
import com.isuhuo.newsflash.network.NormalPostRequest;
import com.isuhuo.newsflash.network.URLMannager;
import com.isuhuo.newsflash.ui.activity.HistoryAndCollectionActivity;
import com.isuhuo.newsflash.ui.activity.SearchDetailsWebActivity;
import com.isuhuo.newsflash.ui.adapter.RootAdapter;
import com.isuhuo.newsflash.util.Kuaibao;
import com.isuhuo.newsflash.util.TimeUtil;
import com.isuhuo.newsflash.widget.LoadingDialog;

/**
 * 我的收藏
 * 抽屉布局界面的"我的收藏"跳转界面
 */
public class CollectionNewsFragment extends Fragment {
    private List<Kuaibao> list;
    private PullToRefreshListView listView;
    private ShouCangAdapter adapter;
    int count=1;
    int total=0;
    boolean isup;
    private PopupWindow popWindow;
    private LoadingDialog loadingDialog;
    private TextView foot;
    private String label="";
    private String type="";
    private String keyword="";
    private Date date;
    private HistoryAndCollectionActivity activity;
    private Map<String,String> params;
    private int shoucCode = 0;
    private int index = 0;
    private LinearLayout bainjiLayout;
    private boolean isShoucan = false;
    private List<Integer> list_delete = new ArrayList<Integer>();
    private List<String> list_string = new ArrayList<String>();
    private TextView delTv,yjqkTv;
    private static final String SEP1 = ",";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_collection_news, container, false);
        activity = (HistoryAndCollectionActivity)getActivity();
        listView = (PullToRefreshListView) v.findViewById(R.id.jiankang_list);
        list = new ArrayList<>();
        loadingDialog=new LoadingDialog(getActivity());
        loadingDialog.show();
        date = new Date();

        bainjiLayout = (LinearLayout) v.findViewById(R.id.bainjiLayout) ;
        delTv = (TextView)v.findViewById(R.id.delTv);
        yjqkTv = (TextView)v.findViewById(R.id.yjqkTv);
        initView();
        initData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activity.bianjiTv.setVisibility(View.VISIBLE);
                activity.qxTv.setVisibility(View.GONE);
                bainjiLayout.setVisibility(View.GONE);
                adapter.setBo(false);
                list_delete.clear();
                list_string.clear();
                Intent intent = new Intent(getActivity(), SearchDetailsWebActivity.class);

                Log.e("tag", ""+adapter.getItem(position-1).getCollect_status());
                intent.putExtra("id", adapter.getItem(position-1).getId());
                intent.putExtra("url", adapter.getItem(position-1).getUrl());
                intent.putExtra("name", adapter.getItem(position-1).getName());
               /* if(BaseApplication.app.getUser()!=null){
                    intent.putExtra("collect_status", kuaibao_info.get(position).getCollect_status());
                }*/
                startActivityForResult(intent,shoucCode);
                index = position;
//                startActivity(intent);
            }
        });
        delTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ListToString(list_string);
                s.substring(0,s.length());
                activity.bianjiTv.setVisibility(View.VISIBLE);
                activity.qxTv.setVisibility(View.GONE);
                bainjiLayout.setVisibility(View.GONE);
                adapter.setBo(false);
                for(int i = 0; i < list.size(); i++){
                    for(int j = 0; j < list_string.size(); j++){
                        if(list.get(i).getId().equals(list_string.get(j))){
                            list.remove(i);
                        }
                    }
                }
//                adapter = new ShouCangAdapter(getContext(),list,R.layout.kuaibao_item);
//                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                params = new HashMap<String, String>();
                params.put("news_ids",s.substring(0,s.length()));
                params.put("uid", "MDAwMDAwMDAwMICdi3M");
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                Request<JSONObject> request = new NormalPostRequest(URLMannager.Base_URL + URLMannager.Del_Collect,

                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"删除失败",Toast.LENGTH_SHORT).show();
                    }
                }, params);

                requestQueue.add(request);
                list_delete.clear();
                list_string.clear();
            }
        });
        return v;
    }

    private String ListToString(List<String> list_string) {
        StringBuffer sb = new StringBuffer();
        if(list_string != null && list_string.size() > 0){
            for(int i = 0; i < list_string.size(); i++){
                if (list_string.get(i) == null || list_string.get(i) == "") {
                    continue;
                }
                if(i == list_string.size()){
                    sb.append(list_string.get(i));
                }else{
                    sb.append( list_string.get(i));
                    sb.append(SEP1);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            HistoryAndCollectionActivity activity = (HistoryAndCollectionActivity) getActivity();
            count = 1;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                int shoucang = data.getIntExtra("shoucang",0);
                if(shoucang == 2){
                    adapter.getItem(index-1).setCollect_status("2");
                    adapter.getList().remove(index -1);
                    adapter.notifyDataSetChanged();
                }else if(shoucang == 1){
                    adapter.getItem(index-1).setCollect_status("1");
                }
                break;
        }
    }

    private void initData() {
        if(listView.getRefreshableView().getFooterViewsCount()>0){
            listView.getRefreshableView().removeFooterView(foot);
        }
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        params = new HashMap<String, String>();
        params.put("pages", count+"");
        params.put("limit", "20");
        params.put("sign", 1+"");
        params.put("uid", "MDAwMDAwMDAwMICdi3M");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        Request<JSONObject> request = new NormalPostRequest(URLMannager.Base_URL + URLMannager.User_List,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        myJson(response);
                        if(getActivity()!=null) {
                            adapter = new ShouCangAdapter(getContext(),list,R.layout.kuaibao_item);
                            listView.setAdapter(adapter);
                            if (isup) {
                                listView.getRefreshableView().setSelectionFromTop(count * 20 - 20+1, listView.getHeight());
                                isup = false;
                            }
                            adapter.notifyDataSetChanged();
                            if(loadingDialog.isShowing()){
                                loadingDialog.dismiss();
                            }
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, params);

        requestQueue.add(request);
    }
    public void myJson(JSONObject jsonObj1){

        try {
            Log.d("==========ss",jsonObj1.toString());
            if(jsonObj1.getString("status").equals("success")) {
                JSONObject jsonObj2 = jsonObj1.getJSONObject("data");
                if (!jsonObj2.isNull("totalpage")) {
                    total = Integer.valueOf(jsonObj2.getString("totalpage"));
                    if (total == count) {
                        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        listView.getRefreshableView().addFooterView(foot);
                    }
                }
                JSONArray array1 = jsonObj2.getJSONArray("list");
                if(array1.length()>0) {
                    for (int i = 0; i < array1.length(); i++) {
                        Kuaibao kuaibao = new Kuaibao();
                        JSONObject array1_2 = array1.getJSONObject(i);
                        kuaibao.id = array1_2.getString("id");
                        kuaibao.name = array1_2.getString("name");
                        kuaibao.release_time = array1_2.getString("release_time");
                        kuaibao.img_url_1 = array1_2.getString("img_url_1");
                        kuaibao.faburen = array1_2.getString("public");
                        kuaibao.url = array1_2.getString("url");
                        list.add(kuaibao);
                    }
                }else {
                    JSONArray ja2=jsonObj2.getJSONArray("like_dish");
                    for (int i = 0; i < ja2.length(); i++) {
                        Kuaibao kuaibao = new Kuaibao();
                        JSONObject array1_2 = array1.getJSONObject(i);
                        kuaibao.id = array1_2.getString("id");
                        kuaibao.name = array1_2.getString("name");
                        kuaibao.release_time = array1_2.getString("release_time");
                        kuaibao.img_url_1 = array1_2.getString("img_url_1");
                        kuaibao.faburen = array1_2.getString("public");
                        kuaibao.url = array1_2.getString("url");
                        list.add(kuaibao);
                    }
                }
            }else {
                foot.setText(jsonObj1.getString("Message"));
                listView.getRefreshableView().addFooterView(foot);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initView() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDate = sdf.format(date); // 当期日期
        listView.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新");
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate);
        listView.getLoadingLayoutProxy(false, true).setPullLabel("上拉刷新...");
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开刷新...");
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate);

        listView.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                String currentDate2 = sdf2.format(date); // 当期日期
                listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新数据\t今天"+currentDate2);
                listView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate2);
                date=new Date();
                list=new ArrayList<Kuaibao>();
                count=0;
                listView.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
                new DataRefresh().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
                String currentDate3 = sdf3.format(date); // 当期日期
                listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新数据\t今天"+currentDate3);
                listView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate3);
                date=new Date();
                isup=true;
                new DataRefresh().execute();
            }
        });
        foot = new TextView(getContext());
        foot.setTextSize(12);
        foot.setTextColor(0xffa0a0a0);
        AbsListView.LayoutParams params2 = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
        foot.setLayoutParams(params2);
        foot.setGravity(Gravity.CENTER);
        foot.setText("已经全部加载完毕");
    }

    public class DataRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //给系统2秒时间用来做出反应
            SystemClock.sleep(2000);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listView.onRefreshComplete();
            count++;
            if(count<=total){
                initData();
            }
            else{
                Toast.makeText(getContext(),"己加载全部",Toast.LENGTH_SHORT).show();
                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
    }
    private class ShouCangAdapter extends RootAdapter<Kuaibao> {
        private Context context;
        public ShouCangAdapter(Context context, List<Kuaibao> lists, int layoutId) {
            super(context, lists, layoutId);
            this.context = context;
        }

        private boolean bo;

        public boolean isBo() {
            return bo;
        }

        public void setBo(boolean bo) {
            this.bo = bo;
        }


        @Override
        public void bindView(View convertView, final int position, final Kuaibao kuaibao) {
            ViewHolder view = ViewHolder.getHolder(convertView);

            ImageView imageView = (ImageView) view.getItemV(R.id.kuaibao_item_imageView);
            TextView title = (TextView) view.getItemV(R.id.kuaibao_item_title);
            TextView from = (TextView) view.getItemV(R.id.kuaibao_item_from);
            final TextView time = (TextView) view.getItemV(R.id.kuaibao_item_time);
            final CheckBox cb = (CheckBox) view.getItemV(R.id.cb_select);
            Glide.with(getContext())
                    .load(kuaibao.getImg_url_1())
                    .into(imageView);
            title.setText(kuaibao.getName());
            from.setText("by " + kuaibao.getFaburen());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = null;
            try {
                date = formatter.parse(kuaibao.getRelease_time());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            time.setText(TimeUtil.getTimeFormatText(date));
            cb.setChecked(kuaibao.isDelet());
            if(adapter.isBo()){
                cb.setVisibility(View.VISIBLE);
            }else{
                cb.setVisibility(View.GONE);
            }
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cb.isChecked()){
                        kuaibao.setDelet(true);
                        list_delete.add(position);
                        list_string.add(kuaibao.getId());
                    }else{
                        kuaibao.setDelet(false);
                        for (int i = 0; i < list_delete.size(); i++){
                            if(position == list_delete.get(i)){
                                list_delete.remove(i);
                            }
                        }
                        list_string.remove(kuaibao.getId());
                    }
                    if(list_delete.size() > 0){
                        delTv.setText("删除( "+list_delete.size()+"）");
                        delTv.setTextColor(Color.parseColor("#ff5e5e"));
                    }else{
                        delTv.setText("删除");
                        delTv.setTextColor(Color.parseColor("#999"));
                    }

                }
            });
            /*cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        kuaibao.setDelet(true);
                    }else{
                        kuaibao.setDelet(false);
                    }

                }
            });*/
        }
    }
    public void edit(boolean b){
        if(b){
            bainjiLayout.setVisibility(View.VISIBLE);
        }else{
            bainjiLayout.setVisibility(View.GONE);
        }
        adapter.setBo(b);
        adapter.notifyDataSetChanged();
    }
}
