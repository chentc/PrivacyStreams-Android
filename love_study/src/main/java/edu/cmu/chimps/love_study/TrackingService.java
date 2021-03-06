package edu.cmu.chimps.love_study;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.github.privacystreams.accessibility.BrowserSearch;
import com.github.privacystreams.accessibility.BrowserVisit;
import com.github.privacystreams.accessibility.SerializedAccessibilityNodeInfo;
import com.github.privacystreams.accessibility.TextEntry;
import com.github.privacystreams.accessibility.UIAction;
import com.github.privacystreams.calendar.CalendarEvent;
import com.github.privacystreams.commons.arithmetic.ArithmeticOperators;
import com.github.privacystreams.commons.comparison.Comparators;
import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.communication.Contact;
import com.github.privacystreams.communication.Message;
import com.github.privacystreams.communication.Phonecall;
import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.actions.collect.Collectors;
import com.github.privacystreams.core.items.EmptyItem;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.device.BatteryInfo;
import com.github.privacystreams.device.BluetoothDevice;
import com.github.privacystreams.device.DeviceEvent;
import com.github.privacystreams.device.WifiAp;
import com.github.privacystreams.environment.Light;
import com.github.privacystreams.image.Image;
import com.github.privacystreams.location.GeoLocation;
import com.github.privacystreams.storage.DropboxOperators;
import com.github.privacystreams.utils.GlobalConfig;
import com.github.privacystreams.utils.Logging;
import com.github.privacystreams.utils.time.Duration;
import com.google.android.gms.location.LocationRequest;

import edu.cmu.chimps.love_study.pam.PAMActivity;
import edu.cmu.chimps.love_study.reminders.ReminderManager;

/**
 * Created by fanglinchen on 3/16/17.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class TrackingService extends Service {
    private  static final int NOTIFICATION_ID = 1234;

    private static String participantId;
    UQI uqi;
    ReminderManager reminderManager;


    private void setupDropbox(){
        GlobalConfig.DropboxConfig.accessToken = uqi.getContext()
                .getResources().getString(R.string.dropbox_access_token);
        GlobalConfig.DropboxConfig.leastSyncInterval = Duration.minutes(1);
        GlobalConfig.DropboxConfig.onlyOverWifi = false;
        participantId = Utils.getParticipantID(this);
        if(participantId==null){
            Toast.makeText(this,"Please fill in your participant id then start tracking. ", Toast.LENGTH_LONG).show();
        }
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, PAMActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.heart);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Love study is running")
                .setSmallIcon(R.drawable.heart)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        startForeground(NOTIFICATION_ID, notification);

    }

    public void collectData(){
        Logging.debug("Collecting Data..");

        collectLogs();
        collectDeviceStates();
        collectTextEntry();
        collectLocation();
        collectIM();
        collectNotifications();
        collectBrowserVisits();
        collectBrowserSearch();
        collectLightIntensity();
        collectUIAction();
        collectDeviceEvent();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        uqi = new UQI(this);
        reminderManager = new ReminderManager(this);

        if(intent!=null
                && intent.getAction()!=null
                && intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)){
            Logging.debug("start collecting..");
            showNotification();
            setupDropbox();
            collectData();

            reminderManager.initialize();
        }
        return START_STICKY;
    }

    public void collectLocation(){
        uqi.getData(GeoLocation.asUpdates(Duration.minutes(2), Duration.minutes(1),
                LocationRequest.PRIORITY_HIGH_ACCURACY),
                Purpose.FEATURE("Collect GPS Coordinate Every 2 minutes"))
                .forEach(DropboxOperators.<Item>uploadTo(participantId+"/Location.txt",true));

    }

    public void collectNotifications(){
        uqi.getData(com.github.privacystreams.notification.Notification.asUpdates(), Purpose.FEATURE("Love Study Notification Collection"))
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(com.github.privacystreams.notification.Notification.TIME_CREATED,
                        Duration.minutes(20))))
                .localGroupBy("time_round")
                .forEach(DropboxOperators.<Item>uploadTo(participantId+"/Notification.txt",true));
    }

    public void collectDeviceStates(){
        uqi
                .getData(EmptyItem.asUpdates(Duration.minutes(20)), Purpose.FEATURE("Love Study Device State Collection"))
                .setIndependentField("wifi_ap_list", WifiAp.asScanList().compound(Collectors.toItemList()))
                .setIndependentField("bluetooth_list", BluetoothDevice.asScanList().compound(Collectors.toItemList()))
                .setIndependentField("battery", BatteryInfo.asSnapshot().compound(Collectors.toItem()))
                .forEach(DropboxOperators.<Item>uploadTo(participantId+"/DeviceState.txt",true));
    }

    public void collectLogs(){
        uqi
                .getData(EmptyItem.asUpdates(Duration.hours(4)), Purpose.FEATURE("Love Study Logs Collection"))
                .setIndependentField("images", Image.readFromStorage().compound(Collectors.toItemList()))
                .setIndependentField("call_logs", Phonecall.asLogs().compound(Collectors.toItemList()))
                .setIndependentField("calendar_events",CalendarEvent.asList().compound(Collectors.toItemList()))
                .setIndependentField("contact_list", Contact.asList().compound(Collectors.toItemList()))
                .forEach(DropboxOperators.<Item>uploadTo(participantId+"/Logs.txt",true));
    }

    public void collectBrowserVisits(){
        uqi.getData(BrowserVisit.asUpdates(), Purpose.FEATURE("Love Study Browser Visit Collection"))
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(BrowserVisit.TIME_CREATED, Duration.minutes(10))))
                .localGroupBy("time_round")
                .forEach(DropboxOperators.<Item>uploadTo(participantId+"/BrowserVisits.txt",true));
    }

    public void collectBrowserSearch(){
        uqi.getData(BrowserSearch.asUpdates(), Purpose.FEATURE("Love Study Browser Search Collection"))
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(BrowserSearch.TIME_CREATED, Duration.minutes(10))))
                .localGroupBy("time_round")
                .forEach(DropboxOperators.<Item>uploadTo(participantId+"/BrowserSearches.txt",true));

    }
    public void collectLightIntensity(){
        uqi.getData(Light.asUpdates(),Purpose.FEATURE("Love Study Light Collection"))
                .filter(Comparators.lt(Light.INTENSITY, 50))
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(Light.TIMESTAMP, Duration.minutes(10))))
                .localGroupBy("time_round")
                .forEach(DropboxOperators.<Item>uploadTo(participantId+"/DarkLight.txt",true));
    }

    public void collectUIAction(){
        uqi.getData(UIAction.asUpdates(), Purpose.FEATURE("Love Study UIAction Collection"))
                .setField(UIAction.ROOT_VIEW, new Function<Item, SerializedAccessibilityNodeInfo>() {
                    @Override
                    public SerializedAccessibilityNodeInfo apply(UQI uqi, Item input) {
                        AccessibilityNodeInfo node = input.getValueByField(UIAction.ROOT_VIEW);
                        return SerializedAccessibilityNodeInfo.serialize(node);
                    }
                })
                .setField(UIAction.SOURCE_NODE, new Function<Item, SerializedAccessibilityNodeInfo>() {
                    @Override
                    public SerializedAccessibilityNodeInfo apply(UQI uqi, Item input) {
                        AccessibilityNodeInfo node = input.getValueByField(UIAction.SOURCE_NODE);
                        return SerializedAccessibilityNodeInfo.serialize(node);
                    }
                })
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(UIAction.TIME_CREATED, Duration.minutes(20))))
                .localGroupBy("time_round")
                .forEach(DropboxOperators.<Item>uploadTo(participantId+"/UIAction.txt",true));
    }

    public void collectDeviceEvent(){
        uqi.getData(DeviceEvent.asUpdates(),Purpose.FEATURE("Love Study Device Event Collection"))
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(DeviceEvent.TIME_CREATED, Duration.minutes(1))))
                .localGroupBy("time_round")
                .forEach(DropboxOperators.<Item>uploadTo(participantId+"/DeviceEvent.txt",true));
    }


    public void collectIM(){
        uqi.getData(Message.asIMUpdates(), Purpose.FEATURE("LoveStudy Message Collection"))
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(Message.TIME_CREATED, Duration.minutes(1))))
                .localGroupBy("time_round")
                .forEach(DropboxOperators.<Item>uploadTo(participantId+"/IM.txt",true));
    }

    public void collectTextEntry(){
        uqi.getData(TextEntry.asUpdates(), Purpose.FEATURE("Love Study Text Entry Collection"))
                .setField(UIAction.ROOT_VIEW, new Function<Item, SerializedAccessibilityNodeInfo>() {
                    @Override
                    public SerializedAccessibilityNodeInfo apply(UQI uqi, Item input) {
                        AccessibilityNodeInfo node = input.getValueByField(UIAction.ROOT_VIEW);
                        return SerializedAccessibilityNodeInfo.serialize(node);
                    }
                })
                .setField(UIAction.SOURCE_NODE, new Function<Item, SerializedAccessibilityNodeInfo>() {
                    @Override
                    public SerializedAccessibilityNodeInfo apply(UQI uqi, Item input) {
                        AccessibilityNodeInfo node = input.getValueByField(UIAction.SOURCE_NODE);
                        return SerializedAccessibilityNodeInfo.serialize(node);
                    }
                })
                .map(ItemOperators.setField("time_round", ArithmeticOperators.roundUp(TextEntry.TIME_CREATED, Duration.minutes(10))))
                .localGroupBy("time_round")
                .forEach(DropboxOperators.<Item>uploadTo(participantId+"/TextEntry.txt",true));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
