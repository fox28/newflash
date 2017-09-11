package com.isuhuo.newsflash.feedback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.isuhuo.newsflash.R;
import com.isuhuo.newsflash.util.MFGT;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by 鱼 on 2017/8/27.
 */
public class FeedbackActivity extends AppCompatActivity {
    @BindView(R.id.fankui_fanhui)
    LinearLayout mBackArea;

    Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        bind = ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        mBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFGT.finish(FeedbackActivity.this);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        this.finish();

    }
}
