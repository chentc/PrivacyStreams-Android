package com.github.privacystreams.environment;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.providers.MStreamProvider;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;

/**
 * A Light item represents the data read from light sensor.
 */
@PSItem
public class Light extends Item {

    /**
     * The light intensity, in lumens.
     */
    @PSItemField(type = Float.class)
    public static final String INTENSITY = "intensity";

    /**
     * The timestamp of when the light sensor value is read.
     */
    @PSItemField(type = Long.class)
    public static final String TIMESTAMP = "timestamp";

    Light(float intensity, long timestamp) {
        this.setFieldValue(INTENSITY, intensity);
        this.setFieldValue(TIMESTAMP,timestamp);
    }

    /**
     * Provide a live stream of Light items that are read from the light sensor.
     * @return the provider function.
     */
    public static MStreamProvider asUpdates(){
        return new LightUpdatesProvider();
    }
}