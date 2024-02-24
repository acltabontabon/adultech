package com.acltabontabon.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DeviceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Device getDeviceSample1() {
        return new Device().id(1L).name("name1");
    }

    public static Device getDeviceSample2() {
        return new Device().id(2L).name("name2");
    }

    public static Device getDeviceRandomSampleGenerator() {
        return new Device().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
