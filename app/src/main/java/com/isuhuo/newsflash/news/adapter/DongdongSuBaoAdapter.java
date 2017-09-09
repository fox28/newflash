package com.isuhuo.newsflash.news.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import com.isuhuo.newsflash.R;
import com.isuhuo.newsflash.util.Kuaibao;
import com.isuhuo.newsflash.util.TimeUtil;

/**
 * Created by Administrator on 2017-08-28.
 */
public class DongdongSuBaoAdapter extends RootAdapter<Kuaibao>{
    public DongdongSuBaoAdapter(Context context, List<Kuaibao> list, int layoutId){
        super(context, list,layoutId);
    }

    @Override
    public void bindView(View convertView, int position, Kuaibao kuaibao) {
        ViewHolder view = ViewHolder.getHolder(convertView);
        ImageView imageView=(ImageView) view.getItemV(R.id.kuaibao_item_imageView);
        TextView title=(TextView)view.getItemV(R.id.kuaibao_item_title);
        TextView from=(TextView)view.getItemV(R.id.kuaibao_item_from);
//        TextView num=(TextView)view.findViewById(R.id.kuaibao_item_num);
        TextView time=(TextView)view.getItemV(R.id.kuaibao_item_time);
        Glide.with(getContext())
                .load(kuaibao.getImg_url_1())
                .into(imageView);
        title.setText(kuaibao.getName());
//        num.setText(/*kuaibao_info.get(position).getContent()*/"ssss");
        from.setText("by "+ kuaibao.getFaburen());


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = formatter.parse(kuaibao.getRelease_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        date = formatter.parse(kuaibao_info.get(position).getRelease_time());
        time.setText(TimeUtil.getTimeFormatText(date));

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext, SearchDetailsWebActivity.class);
                intent.putExtra("id", kuaibao_info.get(position).getId());
                intent.putExtra("url", kuaibao_info.get(position).getUrl());
                intent.putExtra("name", kuaibao_info.get(position).getName());
               *//* if(BaseApplication.app.getUser()!=null){
                    intent.putExtra("collect_status", kuaibao_info.get(position).getCollect_status());
                }*//*

                myContext.startActivity(intent);
            }
        });*/
    }

   /* @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(myContext).inflate(R.layout.kuaibao_item,null);
        ImageView imageView=(ImageView) view.findViewById(R.id.kuaibao_item_imageView);
        TextView title=(TextView)view.findViewById(R.id.kuaibao_item_title);
        TextView from=(TextView)view.findViewById(R.id.kuaibao_item_from);
//        TextView num=(TextView)view.findViewById(R.id.kuaibao_item_num);
        TextView time=(TextView)view.findViewById(R.id.kuaibao_item_time);
        Glide.with(myContext)
                .load(kuaibao_info.get(position).img_url_1)
                .into(imageView);
        title.setText(kuaibao_info.get(position).getName());
//        num.setText(*//*kuaibao_info.get(position).getContent()*//*"ssss");
        from.setText("by "+ kuaibao_info.get(position).getFaburen());


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = formatter.parse(kuaibao_info.get(position).getRelease_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        date = formatter.parse(kuaibao_info.get(position).getRelease_time());
        time.setText(TimeUtil.getTimeFormatText(date));

        *//*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext, SearchDetailsWebActivity.class);
                intent.putExtra("id", kuaibao_info.get(position).getId());
                intent.putExtra("url", kuaibao_info.get(position).getUrl());
                intent.putExtra("name", kuaibao_info.get(position).getName());
               *//**//* if(BaseApplication.app.getUser()!=null){
                    intent.putExtra("collect_status", kuaibao_info.get(position).getCollect_status());
                }*//**//*

                myContext.startActivity(intent);
            }
        });*//*
        return view;
    }*/
}
