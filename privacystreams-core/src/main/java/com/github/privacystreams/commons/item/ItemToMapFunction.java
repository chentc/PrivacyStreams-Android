package com.github.privacystreams.commons.item;

import com.github.privacystreams.commons.ItemFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;

import java.util.Map;

/**
 * Created by yuanchun on 27/12/2016.
 * A function that gets the item map of an item
 */

class ItemToMapFunction extends ItemFunction<Map<String, Object>> {

    @Override
    public Map<String, Object> apply(UQI uqi, Item input) {
        return input.toMap();
    }
}
