package io.slugstack.oss.slugstackpublishingtest;

import lombok.RequiredArgsConstructor;

/**
 * Use the usage service placeholder
 */
@RequiredArgsConstructor
public class OkHttpClientUsagePlaceholder {
    private final OkHttpClientUsageServicePlaceholder service;
    private final SomeUrlDataClass someUrlDataClass;

    /**
     * Does work; again, just using this to generate some javadoc and such
     */
    public void doWork() {
        this.doWork(someUrlDataClass);
    }

    /**
     * Does work; again, just using this to generate some javadoc and such
     *
     * @param someUrlDataClass data to pass in
     */
    public void doWork(SomeUrlDataClass someUrlDataClass) {
        service.getTarget(someUrlDataClass.getSomeTargetUrl());
    }
}
