package com.github.privacystreams.device;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.List;

/**
 * Created by fanglinchen on 3/17/17.
 */
/**
 * A DeviceEvent item represents an event about the device.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
@PSItem
public class DeviceState extends Item {
    /**
     * The timestamp of when the state is requested.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * The list of currently scanned bluetooth device.
     */
    @PSItemField(type = List.class)
    public static final String BLUETOOTH_DEVICE_LIST = "bluetooth_device_list";

    /**
     * The list of currently scanned Wifi APs.
     */
    @PSItemField(type = List.class)
    public static final String WIFI_AP_LIST = "wifi_ap_list";

    /**
     * The current battery level.
     */
    @PSItemField(type = Float.class)
    public static final String BATTERY_LEVEL = "battery_level";

    public static class Masks {
        public static final int BLUETOOTH_DEVICE_LIST = 0x1;
        public static final int WIFI_AP_LIST = 0x2;
        public static final int BATTERY_LEVEL = 0x4;
        public static final int FOREGROUND_APPS = 0x8;
    }

    /**
     * Provide a live stream of device states, including bluetooth, wifi, etc.
     *
     * @param frequency
     * @param mask
     * @return
     */

    public static MStreamProvider asUpdates(long frequency, int mask) {
        return new DeviceStateUpdatesProvider(frequency, mask);
    }
}
