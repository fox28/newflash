package com.isuhuo.newsflash.news.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.isuhuo.newsflash.R;
import com.isuhuo.newsflash.base.BaseActivity;
import com.isuhuo.newsflash.base.MyAppLocation;
import com.isuhuo.newsflash.feedback.FeedbackActivity;
import com.isuhuo.newsflash.login.LoginActivity;
import com.isuhuo.newsflash.news.adapter.FragmentAdapter;
import com.isuhuo.newsflash.news.fragment.MainBoutiqueFragment;
import com.isuhuo.newsflash.news.fragment.MainHotFragment;
import com.isuhuo.newsflash.news.fragment.MainBrowseFragment;
import com.isuhuo.newsflash.setting.SettingActivity;
import com.isuhuo.newsflash.util.UserBeen;
import com.isuhuo.newsflash.view.MyImageView;
import com.isuhuo.newsflash.view.TabStripIndicator;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    public ViewPager viewPager;
    public TabStripIndicator tabIndicator;
    public static final Long[] STATUS = {1L, 2L, 3L};
    private static final CharSequence[] INDICATOR_TITLES = {"最新素报", "热门", "随便看看"};
    private NavigationView left;
    private boolean isDrawer = false;
    private DrawerLayout drawer;
    private LinearLayout syLinearlayout;
    private EditText home_et;
    private TextView home_tv;
    private LinearLayout searchline;
    private UserBeen user = new UserBeen();
    private int login = 2;
    private LinearLayout dengluLinear;
    private LinearLayout weidenglLinear;
    private TextView nameTv;
    private MyImageView touxiangIv;
    private MyImageView home_touxiang;
    private FrameLayout fankui;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabIndicator = (TabStripIndicator) findViewById(R.id.tabIndicator);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        syLinearlayout = (LinearLayout) findViewById(R.id.syLinearlayout);
        left = (NavigationView) findViewById(R.id.nav_view);
        tabIndicator.setIndicatorColor(Color.RED);
        home_touxiang = (MyImageView) findViewById(R.id.home_touxiang);
        fankui = (FrameLayout)findViewById(R.id.home_add) ;
        viewPager = (ViewPager) findViewById(R.id.framntVp);
        home_et = (EditText) findViewById(R.id.home_et);
        searchline = (LinearLayout) findViewById(R.id.home_searchline);
        home_tv = (TextView) findViewById(R.id.home_tv);
        /**
         * 点击搜索栏，跳转SearchActivity
         */
        searchline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        user = MyAppLocation.app.getUser();

        List<Fragment> list = new ArrayList<>();
        list.add(new MainBoutiqueFragment());
        list.add(new MainHotFragment());
        list.add(new MainBrowseFragment());

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), this, list, INDICATOR_TITLES));
        tabIndicator.setViewPager(viewPager);
        // Glide使用
        if(user!= null){
            Glide.with(MainActivity.this)
                    .load(user.getUser_head_img())
                    .into(home_touxiang);
        }
        fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);

            }
        });
        home_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDrawer) {
                    drawer.openDrawer(left);
                }
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                isDrawer = true;
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                syLinearlayout.layout(left.getRight(), 0, left.getRight() + display.getWidth(), display.getHeight());
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawer = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        View drawheadview = navigationView.inflateHeaderView(R.layout.nav_header_main);
        dengluLinear = (LinearLayout) drawheadview.findViewById(R.id.dengluLinear);
        weidenglLinear = (LinearLayout) drawheadview.findViewById(R.id.weidenglLinear);
        if (user != null) {
            dengluLinear.setVisibility(View.VISIBLE);
            weidenglLinear.setVisibility(View.GONE);
        } else {
            dengluLinear.setVisibility(View.GONE);
            weidenglLinear.setVisibility(View.VISIBLE);
        }
        nameTv = (TextView) drawheadview.findViewById(R.id.nameTv);
        touxiangIv = (MyImageView) drawheadview.findViewById(R.id.touxiangIv);
        TextView historyTv = (TextView) drawheadview.findViewById(R.id.historyTv);
        TextView myshcTv = (TextView) drawheadview.findViewById(R.id.myshcTv);
        TextView xtszTv = (TextView) drawheadview.findViewById(R.id.xtszTv);
        LinearLayout wxIv = (LinearLayout) drawheadview.findViewById(R.id.wxIv);
        LinearLayout QQIv = (LinearLayout) drawheadview.findViewById(R.id.QQIv);
        LinearLayout sjIv = (LinearLayout) drawheadview.findViewById(R.id.sjIv);
        historyTv.setOnClickListener(headerListener);
        myshcTv.setOnClickListener(headerListener);
        xtszTv.setOnClickListener(headerListener);
        wxIv.setOnClickListener(headerListener);
        QQIv.setOnClickListener(headerListener);
        sjIv.setOnClickListener(headerListener);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private View.OnClickListener headerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.historyTv: // 历史记录
                    Intent intent1 = new Intent(MainActivity.this, HistoryAndCollectionActivity.class);
                    intent1.putExtra("hors", "his");
                    MainActivity.this.startActivity(intent1);
                    break;
                case R.id.myshcTv: // 我的收藏
                    Intent intent2 = new Intent(MainActivity.this, HistoryAndCollectionActivity.class);
                    intent2.putExtra("hors", "shoucang");
                    MainActivity.this.startActivity(intent2);
                    break;
                case R.id.xtszTv: // 系统设置按钮
                    Intent intent4 = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.wxIv: // 微博登录

                    break;
                case R.id.QQIv: // qq登录

                    break;
                case R.id.sjIv:
                    Intent intent3 = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivityForResult(intent3, login);
//                    MainActivity.this.startActivity(intent3);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 2:
                String tou = data.getStringExtra("denglu");
                String dengluname = data.getStringExtra("dengluname");
                Log.d("TAG","tou="+tou);
                Log.d("dengluname","dengluname="+dengluname);
                if (tou != null && !tou.equals("")) {
                    dengluLinear.setVisibility(View.VISIBLE);
                    weidenglLinear.setVisibility(View.GONE);
                    Glide.with(MainActivity.this)
                            .load(tou)
                            .into(touxiangIv);
                    nameTv.setText(dengluname);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
