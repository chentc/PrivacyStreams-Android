package com.github.privacystreams.audio;

import android.Manifest;

import com.github.privacystreams.core.providers.MStreamProvider;

/**
 * Created by yuanchun on 28/12/2016.
 * record audio periodically with microphone
 */

class AudioPeriodicRecorder extends MStreamProvider {
    private final Long duration_per_record;
    private final Long interval;

    AudioPeriodicRecorder(long duration_per_record, long interval) {
        this.duration_per_record = duration_per_record;
        this.interval = interval;
        this.addParameters(duration_per_record, interval);
        this.addRequiredPermissions(Manifest.permission.RECORD_AUDIO);
    }

    @Override
    protected void provide() {
        // TODO implement this
    }

}
