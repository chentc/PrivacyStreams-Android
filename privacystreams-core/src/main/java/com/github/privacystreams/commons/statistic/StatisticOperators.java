package com.github.privacystreams.commons.statistic;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

import java.util.List;

/**
 * A helper class to access statistic-related functions
 */
@PSOperatorWrapper
public class StatisticOperators {
    /**
     * Count the number of items in the stream and output the number.
     *
     * @return the function.
     */
    public static Function<List<Item>, Integer> count() {
        return new StreamCounter();
    }

    /**
     * Calculate the range of the number values of a field in the stream.
     * The field values must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no item in the stream), the "range" result will be null.
     *
     * @param numField the name of the field to calculate range, the field value must be a number.
     * @return the function
     */
    public static Function<List<Item>, Double> range(String numField) {
        return new FieldRangeStatistic(numField);
    }

    /**
     * Calculate the sum of the number values of a field in the stream.
     * The field values must be in Number type, such as Integer, Double, Long, ...
     * If there is no valid field value in the stream, the "sum" result will be 0.0
     *
     * @param numField the name of the field to calculate sum, the field value must be a number.
     * @return the function
     */
    public static Function<List<Item>, Double> sum(String numField) {
        return new FieldSumStatistic(numField);
    }

    /**
     * Calculate the average of the number values of a field in the stream.
     * The field values must be in Number type, such as Integer, Double, Long, ...
     * If calculation fails (e.g. there is no item in the stream), the "average" result will be null.
     *
     * @param field the name of the field to calculate average, the field value must be a number.
     * @return the function
     */
    public static Function<List<Item>, Double> average(String field) {
        return new FieldAverageStatistic(field);
    }
}
