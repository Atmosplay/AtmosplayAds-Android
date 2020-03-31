package com.atmosplayads.demo.sample;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.atmosplayads.AtmosplayFloatAd;
import com.atmosplayads.demo.R;
import com.atmosplayads.demo.ToolBarActivity;
import com.atmosplayads.demo.util.UserConfig;
import com.atmosplayads.listener.AtmosplayAdLoadListener;
import com.atmosplayads.listener.FloatAdListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FloatAdSample extends ToolBarActivity {
    private static final String TAG = "FloatAdSample";

    @BindView(R.id.text)
    TextView info;
    @BindView(R.id.unit_id)
    EditText mUnitIdEdit;
    @BindView(R.id.app_id)
    EditText mAppIdEdit;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.clear)
    View mClear;
    @BindView(R.id.clear2)
    View mClear2;
    @BindView(R.id.pointX)
    EditText mPointX;
    @BindView(R.id.pointY)
    EditText mPointY;
    @BindView(R.id.folatAd_width)
    EditText mFloatAdWidth;

    UserConfig mConfig;

    AtmosplayFloatAd mFloatAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_ad);
        ButterKnife.bind(this);

        showUpAction();
        showSettingsButton();

        mConfig = UserConfig.getInstance(this);
        mFloatAd = AtmosplayFloatAd.init(this, mConfig.getFloatAdAppId());
        setTitle("FloatAd");


        mAppIdEdit.setText(mConfig.getFloatAdAppId());
        mUnitIdEdit.setText(mConfig.getFloatAdUnitId());
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFloatAd.setChannelId(mConfig.getChannelId());
        mFloatAd.setAutoLoadAd(mConfig.isAutoload());
    }

    @OnClick(R.id.request)
    public void request() {
        final String unitId = mUnitIdEdit.getText().toString().trim();

        setInfo(unitId + " " + getString(R.string.start_request));

        mFloatAd.loadAd(unitId, new AtmosplayAdLoadListener() {

            @Override
            public void onLoadFinished() {
                setInfo(unitId + " " + getString(R.string.pre_cache_finished));
            }

            @Override
            public void onLoadFailed(int errorCode, String msg) {
                setInfo(unitId + " " + msg);
            }
        });
    }

    private void setInfo(final String msg) {
        Log.d(TAG, "setInfo: " + msg);
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                info.append(msg + "\n\n");
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
    
    @OnClick(R.id.hidden)
    public void hidden() {
        mFloatAd.hiddenFloatAd();
    }

    @OnClick(R.id.showAgainAfterHiding)
    public void showAgainAfterHiding() {
        mFloatAd.showAgainAfterHiding();
    }

    @OnClick(R.id.isReady)
    public void isReady() {
        final String unitId = mUnitIdEdit.getText().toString().trim();
        setInfo("isReady: " + mFloatAd.isReady(unitId));
    }

    @OnClick(R.id.updatePointAndWidth)
    public void updatePointAndWidth() {
        String pointX = mPointX.getText().toString().trim();
        String pointY = mPointY.getText().toString().trim();
        String width = mFloatAdWidth.getText().toString().trim();
        mFloatAd.updatePointAndWidth(this, dpToPx(pointX), dpToPx(pointY), dpToPx(width));
    }

    @OnClick(R.id.present)
    public void present() {
        String pointX = mPointX.getText().toString().trim();
        String pointY = mPointY.getText().toString().trim();
        String width = mFloatAdWidth.getText().toString().trim();
        mFloatAd.setPointAndWidth(dpToPx(pointX), dpToPx(pointY), dpToPx(width));


        final String unitId = mUnitIdEdit.getText().toString().trim();
        mFloatAd.show(this, unitId, new FloatAdListener() {
            @Override
            public void onFloatAdStartPlaying() {
                setInfo(unitId + " " + getString(R.string.ads_video_start));
            }

            @Override
            public void onFloatAdEndPlaying() {
                setInfo(unitId + " " + getString(R.string.ads_video_finished));
            }

            @Override
            public void onUserEarnedReward() {
                setInfo(unitId + " " + getString(R.string.ads_incentive));
            }

            @Override
            public void onFloatAdClicked() {
                setInfo(unitId + " " + getString(R.string.landing_page_install_btn_clicked));
            }

            @Override
            public void onFloatAdClosed() {
                setInfo(unitId + " " + getString(R.string.ad_present_closed));
            }

            @Override
            public void onFloatAdError(int code, String msg) {
                setInfo(unitId + " " + msg);
            }
        });
    }

    public static void launch(Context context) {
        Intent i = new Intent(context, FloatAdSample.class);
        context.startActivity(i);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFloatAd != null) {
            mFloatAd.destroy();
        }
        mConfig.setFloatAdAppId(mAppIdEdit.getText().toString().trim());
        mConfig.setFloatAdUnitId(mUnitIdEdit.getText().toString().trim());
    }


    public int dpToPx(String data) {
        try {
            int dp = Integer.valueOf(data);
            return (int) dp2px(dp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static float dp2px(int dp) {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * dm.density);
    }
}
