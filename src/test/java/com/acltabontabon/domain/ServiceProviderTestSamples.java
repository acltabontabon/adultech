package com.acltabontabon.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceProviderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ServiceProvider getServiceProviderSample1() {
        return new ServiceProvider().id(1L).name("name1").unitMeasurement("unitMeasurement1").notes("notes1");
    }

    public static ServiceProvider getServiceProviderSample2() {
        return new ServiceProvider().id(2L).name("name2").unitMeasurement("unitMeasurement2").notes("notes2");
    }

    public static ServiceProvider getServiceProviderRandomSampleGenerator() {
        return new ServiceProvider()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .unitMeasurement(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
