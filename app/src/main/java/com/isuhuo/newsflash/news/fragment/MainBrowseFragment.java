package com.isuhuo.newsflash.news.fragment;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isuhuo.newsflash.R;
import com.isuhuo.newsflash.network.NormalPostRequest;
import com.isuhuo.newsflash.network.URLMannager;
import com.isuhuo.newsflash.news.activity.MainActivity;
import com.isuhuo.newsflash.news.activity.SearchDetailsWebActivity;
import com.isuhuo.newsflash.news.adapter.DongdongSuBaoAdapter;
import com.isuhuo.newsflash.util.Kuaibao;
import com.isuhuo.newsflash.view.LoadingDialog;


public class MainBrowseFragment extends Fragment {
    private List<Kuaibao> list;
    private PullToRefreshListView listView;
    private DongdongSuBaoAdapter adapter;
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
    private MainActivity activity;
    private Map<String,String> params;
    private int shoucCode = 0;
    private int index = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_boutique_main, container, false);
        activity = (MainActivity)getActivity();
        listView = (PullToRefreshListView) v.findViewById(R.id.jiankang_list);
        list = new ArrayList<>();
        loadingDialog=new LoadingDialog(getActivity());
        loadingDialog.show();
        date = new Date();
        initView();
        initData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            MainActivity activity = (MainActivity) getActivity();
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
        params.put("keyword", "");
        params.put("sign", "3");
        params.put("public_ids", "");
        params.put("ids", "");
        params.put("uid", "");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        Request<JSONObject> request = new NormalPostRequest(URLMannager.Base_URL + URLMannager.Get_News,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        myJson(response);
                        if(getActivity()!=null) {
                            adapter = new DongdongSuBaoAdapter(getContext(),list,R.layout.kuaibao_item);
                            listView.setAdapter(adapter);
                            if (isup) {
                                listView.getRefreshableView().setSelectionFromTop(count * 20 - 20+1, listView.getHeight());
//                                listView.getRefreshableView().setSelection(count * 10 - 13);
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
}
