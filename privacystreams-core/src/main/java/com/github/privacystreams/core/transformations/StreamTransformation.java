package com.github.privacystreams.core.transformations;

import com.github.privacystreams.core.EventDrivenFunction;
import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.Stream;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yuanchun on 28/11/2016.
 * Transform a stream to a stream
 */

public abstract class StreamTransformation<InStream extends Stream, OutStream extends Stream> extends EventDrivenFunction<InStream, OutStream> {

    protected abstract void onInput(Item item);

    protected final void output(Item item) {
        this.output.write(item);
    }

    @Subscribe
    protected void onEvent(Item item) {
        this.onInput(item);
    }

    @Override
    protected void init() {
        this.input.register(this);
    }

    protected void finish() {
        this.input.unregister(this);
    }
}