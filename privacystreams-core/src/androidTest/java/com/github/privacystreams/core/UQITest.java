package com.github.privacystreams.core;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.github.privacystreams.core.providers.mock.MockObject;
import com.github.privacystreams.core.providers.mock.MockItem;
import com.github.privacystreams.core.purposes.Purpose;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by yuanchun on 18/02/2017.
 * Test UQI
 */
@RunWith(AndroidJUnit4.class)
public class UQITest {
    private UQI uqi;
    private Purpose testPurpose;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        this.uqi = new UQI(appContext);
        this.testPurpose = Purpose.test("unit test.");

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getContext() throws Exception {
        Context context = this.uqi.getContext();
        assertNotNull(context);
    }

    @Test
    public void getUUID() throws Exception {
        assertNotNull(this.uqi.getUUID());
    }

    @Test
    public void getGson() throws Exception {

    }

    @Test
    public void getDataItems() throws Exception {
        List<MockObject> mockObjectList = MockObject.getRandomList(10);
        int itemCount = this.uqi
                .getDataItems(MockItem.asHistory(mockObjectList), this.testPurpose)
                .count();
        assertEquals(10, itemCount);
    }

    @Test
    public void getDataItem() throws Exception {
        MockObject mockObject = MockObject.getRandomInstance();
        int mockItemX = this.uqi
                .getDataItem(MockItem.asItem(mockObject), this.testPurpose)
                .getField(MockItem.X);
//        System.out.println("mockItemX: " + mockItemX);
        assertEquals(mockObject.getX(), mockItemX);
    }

}