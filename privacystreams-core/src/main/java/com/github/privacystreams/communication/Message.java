package com.github.privacystreams.communication;

import android.os.Build;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.Logging;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

import java.util.ArrayList;

/**
 * A text message. It could be from SMS, WhatsApp, Facebook, etc.
 */
@PSItem
public class Message extends Item {

    /**
     * The message type, could be "received" or "sent".
     */
    @PSItemField(type = String.class)
    public static final String TYPE = "type";

    /**
     * The message content.
     */
    @PSItemField(type = String.class)
    public static final String CONTENT = "content";

    /**
     * The package name of the app where message is captured.
     */
    @PSItemField(type = String.class)
    public static final String PACKAGE_NAME = "package_name";

    /**
     * The contact (phone number or name) of the message.
     */
    @PSItemField(type = String.class)
    public static final String CONTACT = "contact";

    /**
     * The timestamp of when the message is sent/received.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    /**
     * The current count of item of the message list. It can be
     * used as a message id (sort of).
     */
    @PSItemField(type = Long.class)
    public static final String ITEMCOUNT = "itemcount";

    public static class Types {
        public static final String RECEIVED = "received";
        public static final String SENT = "sent";
    };

    Message(int itemCount, ArrayList<String> type, ArrayList<String> content, String packageName, String contact, long timestamp){
        this.setFieldValue(TYPE, type);
        this.setFieldValue(CONTENT, content);
        this.setFieldValue(ITEMCOUNT,itemCount);
        this.setFieldValue(PACKAGE_NAME, packageName);
        this.setFieldValue(CONTACT, contact);
        this.setFieldValue(TIMESTAMP, timestamp);
    }

    /**
     * Provide a live stream of Message items from IM apps, including WhatsApp and Facebook.
     * @return the provider function
     */

     public static MStreamProvider asIMUpdates(){
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
             return new IMUpdatesProvider();
         else {
             Logging.warn("Illegal SDK version.");
             return null;
         }
    }
    /**
     * Provide a live stream of Message items from the Android SMS app.
     * @return the provider
     */
    public static MStreamProvider asSMSUpdates() {
        return new SMSMessageUpdatesProvider();
    }

    /**
     * Provide a list of historic Message items from the Android SMS app.
     * @return the provider
     */
    public static MStreamProvider asSMSHistory() {
        return new SMSMessageHistoryProvider();
    }
}