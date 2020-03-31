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

import com.atmosplayads.AtmosplayWindowAd;
import com.atmosplayads.demo.R;
import com.atmosplayads.demo.ToolBarActivity;
import com.atmosplayads.demo.util.UserConfig;
import com.atmosplayads.listener.WindowAdListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WindowAdSample extends ToolBarActivity {
    private static final String TAG = "WindowAdSample";

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
    @BindView(R.id.windowAd_width)
    EditText mWindowAdWidth;

    UserConfig mConfig;

    AtmosplayWindowAd mWindowAd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_ad);
        ButterKnife.bind(this);

        showUpAction();
        showSettingsButton();

        mConfig = UserConfig.getInstance(this);
        setTitle("WindowAd");

        mAppIdEdit.setText(mConfig.getWindowAdAppId());
        mUnitIdEdit.setText(mConfig.getWindowAdUnitId());
    }

    @OnClick(R.id.request)
    public void request() {
        final String unitId = mUnitIdEdit.getText().toString().trim();
        setInfo(unitId + " " + getString(R.string.start_request));
        if (mWindowAd != null) {
            mWindowAd.destroy();
        }

        mWindowAd = new AtmosplayWindowAd(this, mAppIdEdit.getText().toString(), unitId);
        mWindowAd.setChannelId(mConfig.getChannelId());
        mWindowAd.setWindowAdListener(new WindowAdListener() {
            @Override
            public void onWindowAdPrepared() {
                setInfo(unitId + " " + getString(R.string.pre_cache_finished));
            }

            @Override
            public void onWindowAdPreparedFailed(int code, String error) {
                setInfo(unitId + " " + error);
            }

            @Override
            public void onWindowAdStart() {
                setInfo(unitId + " " + getString(R.string.ads_video_start));
            }

            @Override
            public void onWindowAdFinished() {
                setInfo(unitId + " " + getString(R.string.ads_video_finished));
            }

            @Override
            public void onWindowAdClose() {
                setInfo(unitId + " " + getString(R.string.ad_present_closed));
            }

            @Override
            public void onWindowAdClicked() {
                setInfo(unitId + " " + getString(R.string.ad_clicked));
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @OnClick(R.id.isReady)
    public void isReady() {
        setInfo("isReady: " + mWindowAd.isReady());
    }

    @OnClick(R.id.present)
    public void present() {
        if (mWindowAd != null) {
            String pointX = mPointX.getText().toString().trim();
            String pointY = mPointY.getText().toString().trim();
            String width = mWindowAdWidth.getText().toString().trim();
            mWindowAd.setPointAndWidth(dpToPx(pointX), dpToPx(pointY), dpToPx(width));

            mWindowAd.show(this);
        }
    }

    @OnClick(R.id.updatePointAndWidth)
    public void updatePointAndWidth() {
        if (mWindowAd != null) {
            String pointX = mPointX.getText().toString().trim();
            String pointY = mPointY.getText().toString().trim();
            String width = mWindowAdWidth.getText().toString().trim();

            mWindowAd.updatePointAndWidth(this, dpToPx(pointX), dpToPx(pointY), dpToPx(width));
        }
    }

    @OnClick(R.id.hidden)
    public void hidden() {
        if (mWindowAd != null) {
            mWindowAd.hiddenWindowAd();
        }
    }

    @OnClick(R.id.showAgainAfterHiding)
    public void showAgainAfterHiding() {
        if (mWindowAd != null) {
            mWindowAd.showAgainAfterHiding();
        }
    }

    @OnClick(R.id.destroy)
    public void destroy() {
        if (mWindowAd != null) {
            mWindowAd.destroy();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWindowAd != null) {
            mWindowAd.destroy();
        }
        mConfig.setWindowAdAppId(mAppIdEdit.getText().toString().trim());
        mConfig.setWindowAdUnitId(mUnitIdEdit.getText().toString().trim());
    }

    public static void launch(Context context) {
        Intent i = new Intent(context, WindowAdSample.class);
        context.startActivity(i);
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
