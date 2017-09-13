package com.isuhuo.newflash.setting;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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


public class UpdateNickActivity extends AppCompatActivity {
    private static final String TAG = "UpdateNickActivity";

    private ProgressDialog dialog;
    private String newNick;
    private UserBeen user;

    // 修改昵称
    private Map<String, String> params;

    @BindView(R.id.et_update_user_nick)
    EditText mEtUpdateUserNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);

        user = MyAppLocation.app.getUser();
    }

    @OnClick({R.id.backArea, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArea:
                MFGT.finish(UpdateNickActivity.this);
                break;
            case R.id.btn_save:
                if (checkInput()) {
                    updateNick();

                }
                break;
        }
    }

    private void updateNick() {
        params = new HashMap<>();
        params.put("uid", user.getId());
        params.put("name", newNick);
        // 获得请求队列
        RequestQueue mQueue = SingleVolleyRequestQueue.getInstance(this).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(URLMannager.Base_URL + URLMannager.URL_UPDATE_NICK, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                // 修改SharePreference
                SpUtils.putUser(UpdateNickActivity.this, "name",newNick);
                // 修改Application中user中内存
                user.setName(newNick);
                MFGT.finish(UpdateNickActivity.this);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UpdateNickActivity.this, "修改失败："+volleyError, Toast.LENGTH_SHORT).show();
            }
        }, params);
        mQueue.add(request);
     }


    private boolean checkInput() {
        newNick = mEtUpdateUserNick.getText().toString().trim();
        if (TextUtils.isEmpty(newNick)) {
            mEtUpdateUserNick.requestFocus();
            mEtUpdateUserNick.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }
        if (newNick.equals(user.getName())) {
            mEtUpdateUserNick.requestFocus();
            mEtUpdateUserNick.setError(getString(R.string.update_nick_fail_unmodify));
            return false;
        }
        return true;
    }
}
