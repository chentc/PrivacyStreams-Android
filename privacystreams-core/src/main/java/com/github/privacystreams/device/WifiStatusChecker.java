package com.github.privacystreams.device;

import android.Manifest;
import android.content.Context;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.ConnectionUtils;

/**
 * Get device id
 */
class WifiStatusChecker extends Function<Void, Boolean> {

    WifiStatusChecker() {
        this.addRequiredPermissions(Manifest.permission.ACCESS_WIFI_STATE);
    }

    @Override
    public Boolean apply(UQI uqi, Void input) {
        return ConnectionUtils.isWifiConnected(uqi);
    }
}
