package com.isuhuo.newflash.feedback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.isuhuo.newflash.R;
import com.isuhuo.newflash.util.MFGT;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by 鱼 on 2017/8/27.
 */
public class FeedbackActivity extends AppCompatActivity {
    @BindView(R.id.fankui_fanhui)
    LinearLayout mBackArea;
    @BindView(R.id.fankui_edit)
    EditText mEtContent;

    Unbinder bind;
    private String feedbackContent;

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

    @OnClick(R.id.fankui_bt)
    public void onCommitClicked() {
        feedbackContent = mEtContent.getText().toString().trim();
        if (TextUtils.isEmpty(feedbackContent)) {
            Toast.makeText(this, "提交信息不能为空", Toast.LENGTH_SHORT).show();
        } else {
            // TODO 请求服务器，feedback接口
        }
    }
}
