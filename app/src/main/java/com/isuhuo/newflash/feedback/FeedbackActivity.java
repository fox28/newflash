package com.isuhuo.newflash.feedback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.isuhuo.newflash.R;
import com.isuhuo.newflash.base.MyAppLocation;
import com.isuhuo.newflash.network.NormalPostRequest;
import com.isuhuo.newflash.network.SingleVolleyRequestQueue;
import com.isuhuo.newflash.network.URLMannager;
import com.isuhuo.newflash.util.MFGT;
import com.isuhuo.newflash.util.SpUtils;
import com.isuhuo.newflash.util.UserBeen;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by 鱼 on 2017/8/27.
 */
public class FeedbackActivity extends AppCompatActivity {
    private static final String TAG = "FeedbackActivity";
    @BindView(R.id.fankui_fanhui)
    LinearLayout mBackArea;
    @BindView(R.id.fankui_edit)
    EditText mEtContent;

    Unbinder bind;
    private String feedbackContent;

    // 反馈Api
    private UserBeen user;
    private Map<String, String> params;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        bind = ButterKnife.bind(this);
        initListener();
        initData();
    }

    private void initData() {
        user = MyAppLocation.app.getUser();
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
            params = new HashMap<>();
            params.put("uid", SpUtils.getUser(FeedbackActivity.this, "id", null));
            params.put("content", feedbackContent);
            mQueue = SingleVolleyRequestQueue.getInstance(this).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(URLMannager.Base_URL + URLMannager.URL_FEEDBACK,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Toast.makeText(FeedbackActivity.this, "谢谢您的抱歉意见，我们着重考虑", Toast.LENGTH_SHORT).show();
                            MFGT.finish(FeedbackActivity.this);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(FeedbackActivity.this, "抱歉，网络问题、反馈失败。", Toast.LENGTH_SHORT).show();
                }
            }, params);
            mQueue.add(request);
        }
    }
}
