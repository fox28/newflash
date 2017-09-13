package com.isuhuo.newflash.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.isuhuo.newflash.R;
import com.isuhuo.newflash.ui.adapter.FragmentAdapter;
import com.isuhuo.newflash.ui.fragment.HistoryNewsFragment;
import com.isuhuo.newflash.ui.fragment.CollectionNewsFragment;
import com.isuhuo.newflash.widget.TabStripIndicator;


public class HistoryAndCollectionActivity extends AppCompatActivity {

    private LinearLayout back;
    private TextView titleTv;
    private String hors;
    private Map<String,String> params;
    public ViewPager viewPager;
    public TextView bianjiTv, qxTv;
    public TabStripIndicator historytabIndicator;
    public static final Long[] STATUS = {1L, 2L};
    private HistoryNewsFragment fragment1;
    private CollectionNewsFragment fragment2;
    private static final CharSequence[] INDICATOR_TITLES = {"我的收藏", "阅读历史"};
    private boolean delequxiao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_and_collection);
        back=(LinearLayout)findViewById(R.id.my_set_fanhui);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bianjiTv = (TextView) findViewById(R.id.rightTv) ;
        bianjiTv.setVisibility(View.VISIBLE);
        qxTv = (TextView)findViewById(R.id.right2Tv) ;
        Intent intent = getIntent();
        hors = intent.getStringExtra("hors"); // hors
        titleTv = (TextView) findViewById(R.id.head_title);
        titleTv.setText("收藏/历史");
        historytabIndicator = (TabStripIndicator) findViewById(R.id.historytabIndicator);
        viewPager = (ViewPager) findViewById(R.id.historyvp);
        historytabIndicator.setIndicatorColor(Color.RED);
        List<Fragment> list = new ArrayList<>();
        fragment1 = new HistoryNewsFragment();
        fragment2 = new CollectionNewsFragment();
        list.add(fragment2);
        list.add(fragment1);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), this, list, INDICATOR_TITLES));
        historytabIndicator.setViewPager(viewPager);
        if(hors.equals("his")){
            viewPager.setCurrentItem(1);
        }
        bianjiTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delequxiao = true;
                if(viewPager.getCurrentItem() == 1){
                    bianjiTv.setVisibility(View.GONE);
                    qxTv.setVisibility(View.VISIBLE);
                    fragment1.edit(delequxiao);
                }else{
                    bianjiTv.setVisibility(View.GONE);
                    qxTv.setVisibility(View.VISIBLE);
                    fragment2.edit(delequxiao);
                }
            }
        });
        qxTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delequxiao = false;
                if(viewPager.getCurrentItem() == 1){
                    bianjiTv.setVisibility(View.VISIBLE);
                    qxTv.setVisibility(View.GONE);
                    fragment1.edit(delequxiao);
                }else{
                    bianjiTv.setVisibility(View.VISIBLE);
                    qxTv.setVisibility(View.GONE);
                    fragment2.edit(delequxiao);
                }
            }
        });
    }

}
