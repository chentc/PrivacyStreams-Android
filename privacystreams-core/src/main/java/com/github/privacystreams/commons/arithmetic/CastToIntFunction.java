package com.github.privacystreams.commons.arithmetic;

/**
 * Created by yuanchun on 05/02/2017.
 * Round a number field.
 */
class CastToIntFunction extends ArithmeticFunction<Integer> {

    CastToIntFunction(String numField) {
        super(numField);
    }

    @Override
    protected Integer processNum(Number number) {
        return number.intValue();
    }

}
