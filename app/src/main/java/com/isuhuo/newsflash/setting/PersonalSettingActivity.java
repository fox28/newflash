package com.isuhuo.newsflash.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.isuhuo.newsflash.R;
import com.isuhuo.newsflash.base.MyAppLocation;
import com.isuhuo.newsflash.ui.activity.MainActivity;
import com.isuhuo.newsflash.util.MFGT;
import com.isuhuo.newsflash.util.SpUtils;
import com.isuhuo.newsflash.util.UserBeen;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PersonalSettingActivity extends AppCompatActivity {
    private static final String TAG = "PersonalSettingActivity";
    private UserBeen user = new UserBeen();

    @BindView(R.id.iv_user_profile_avatar)
    ImageView mIvUserProfileAvatar;
    @BindView(R.id.tv_user_profile_nick)
    TextView mTvUserProfileNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        ButterKnife.bind(this);

        user = MyAppLocation.app.getUser();
        updatePersonalProfile();
    }

    @OnClick({R.id.backArea, R.id.layout_user_profile_avatar, R.id.layout_user_profile_nickname})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArea:
                MFGT.finish(PersonalSettingActivity.this);
                break;
            case R.id.layout_user_profile_avatar:
                break;
            case R.id.layout_user_profile_nickname:
                MFGT.gotoUpdateNickActivity(PersonalSettingActivity.this);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePersonalProfile();
    }

    private void updatePersonalProfile() {
        if (user != null) {
            Glide.with(PersonalSettingActivity.this)
                    .load(user.getUser_head_img())
                    .into(mIvUserProfileAvatar);
            // user.getName() == SpUtils.getUser(this, "name", null)
            mTvUserProfileNick.setText(user.getName());
        }
    }
}
