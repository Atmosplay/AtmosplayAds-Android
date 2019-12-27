package com.atmosplayads.demo;

import android.app.Application;

import com.atmosplayads.AtmosplayAdsSettings;
import com.atmosplayads.entity.GDPRStatus;

/**
 * Creator: lgd
 * Date: 17-9-13
 * Description:
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AtmosplayAdsSettings.enableAutoRequestPermissions(true);
        AtmosplayAdsSettings.setGDPRConsent(GDPRStatus.PERSONALIZED);
    }
}
