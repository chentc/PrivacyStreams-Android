package com.github.privacystreams.location;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.utils.Assertions;

import java.util.List;

/**
 * Created by yuanchun on 28/12/2016.
 * Process the location field in an item.
 */
abstract class LocationProcessor<Tout> extends ItemFunction<Tout> {

    private final String coordinatesField;

    LocationProcessor(String coordinatesField) {
        this.coordinatesField = Assertions.notNull("coordinatesField", coordinatesField);
        this.addParameters(this.coordinatesField);
    }

    @Override
    public final Tout apply(UQI uqi, Item input) {
        List<Double> coordinates = input.getValueByField(this.coordinatesField);
        double latitude = coordinates.get(0);
        double longitude = coordinates.get(1);
        return this.processLocation(latitude, longitude);
    }

    protected abstract Tout processLocation(double latitude, double longitude);
}
