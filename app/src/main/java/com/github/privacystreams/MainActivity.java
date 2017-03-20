package com.github.privacystreams;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.providers.mock.MockItem;
import com.github.privacystreams.core.purposes.Purpose;

public class MainActivity extends AppCompatActivity {
    public Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.button);

        UQI uqi = new UQI(MainActivity.this);
        uqi.getData(MockItem.asRandomUpdates(10, 10.0, 100), Purpose.TEST("Testing first data query."))
           .limit(10)
           .debug();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MyAsyncTask().execute();
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Object, Object, Object> {
        @Override
        protected Object doInBackground(Object[] objects) {

            UseCases useCases = new UseCases(MainActivity.this);

//            useCases.testContacts();
            useCases.testMockData();
            useCases.testTextEntry();
//
//            useCases.testBrowserSearchUpdates();
//            useCases.testBrowserHistoryUpdates();
//            useCases.testWifiUpdates(30);
//
//            useCases.testLightUpdatesProvider();
//            useCases.testBlueToothUpatesProvider();
//            useCases.testPhysicalMotionUpdatesProvider();
//            useCases.testLightUpdatesProvider();
//
//            useCases.testUIAction();
//            useCases.testIMUpdates();
            return null;
        }
    }
}