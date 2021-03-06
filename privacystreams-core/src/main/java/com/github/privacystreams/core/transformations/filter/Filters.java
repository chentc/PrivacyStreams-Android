package com.github.privacystreams.core.transformations.filter;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.transformations.M2MTransformation;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access filter functions
 */
@PSOperatorWrapper
public class Filters {

    /**
     * Keep all items that satisfies a predicate, and remove the items that don't satisfy.
     *
     * @param predicate the predicate to check for each item
     * @return the filter function
     */
    public static M2MTransformation keep(Function<Item, Boolean> predicate) {
        return new PredicateFilter(predicate);
    }
}
