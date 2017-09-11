package com.isuhuo.newsflash.setting;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.isuhuo.newsflash.R;
import com.isuhuo.newsflash.base.MyAppLocation;
import com.isuhuo.newsflash.util.MFGT;
import com.isuhuo.newsflash.util.UserBeen;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UpdateNickActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private String newNick;
    private UserBeen user;

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
                    // TODO 显示dialog: showDialog(); 后面分情况考虑dialog关闭；
                    // TODO 实现修改用户昵称的接口
                }
                break;
        }
    }

    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.update_user_nick));
        dialog.show();
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
