/**
 * Copyright 2016 Netflix, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.hystrix.metric;

import org.HdrHistogram.Histogram;

public class CachedValuesHistogram {

    private final static int NUMBER_SIGNIFICANT_DIGITS = 3;

    private final int mean;
    private final int p0;
    private final int p5;
    private final int p10;
    private final int p15;
    private final int p20;
    private final int p25;
    private final int p30;
    private final int p35;
    private final int p40;
    private final int p45;
    private final int p50;
    private final int p55;
    private final int p60;
    private final int p65;
    private final int p70;
    private final int p75;
    private final int p80;
    private final int p85;
    private final int p90;
    private final int p95;
    private final int p99;
    private final int p99_5;
    private final int p99_9;
    private final int p99_95;
    private final int p99_99;
    private final int p100;

    private final long totalCount;

    public static CachedValuesHistogram backedBy(Histogram underlying) {
        return new CachedValuesHistogram(underlying);
    }

    private CachedValuesHistogram(Histogram underlying) {
        /**
         * Single thread calculates a variety of commonly-accessed quantities.
         * This way, all threads can access the cached values without synchronization
         * Synchronization is only required for values that are not cached
         */

        mean = (int) underlying.getMean();
        p0 = (int) underlying.getValueAtPercentile(0);
        p5 = (int) underlying.getValueAtPercentile(5);
        p10 = (int) underlying.getValueAtPercentile(10);
        p15 = (int) underlying.getValueAtPercentile(15);
        p20 = (int) underlying.getValueAtPercentile(20);
        p25 = (int) underlying.getValueAtPercentile(25);
        p30 = (int) underlying.getValueAtPercentile(30);
        p35 = (int) underlying.getValueAtPercentile(35);
        p40 = (int) underlying.getValueAtPercentile(40);
        p45 = (int) underlying.getValueAtPercentile(45);
        p50 = (int) underlying.getValueAtPercentile(50);
        p55 = (int) underlying.getValueAtPercentile(55);
        p60 = (int) underlying.getValueAtPercentile(60);
        p65 = (int) underlying.getValueAtPercentile(65);
        p70 = (int) underlying.getValueAtPercentile(70);
        p75 = (int) underlying.getValueAtPercentile(75);
        p80 = (int) underlying.getValueAtPercentile(80);
        p85 = (int) underlying.getValueAtPercentile(85);
        p90 = (int) underlying.getValueAtPercentile(90);
        p95 = (int) underlying.getValueAtPercentile(95);
        p99 = (int) underlying.getValueAtPercentile(99);
        p99_5 = (int) underlying.getValueAtPercentile(99.5);
        p99_9 = (int) underlying.getValueAtPercentile(99.9);
        p99_95 = (int) underlying.getValueAtPercentile(99.95);
        p99_99 = (int) underlying.getValueAtPercentile(99.99);
        p100 = (int) underlying.getValueAtPercentile(100);

        totalCount = underlying.getTotalCount();
    }

    /**
     * Return the cached value only
     * @return cached distribution mean
     */
    public int getMean() {
        return mean;
    }

    /**
     * Return the cached value if available.
     * Otherwise, we need to synchronize access to the underlying {@link Histogram}
     * @param percentile percentile of distribution
     * @return value at percentile (from cache if possible)
     */
    public int getValueAtPercentile(double percentile) {
        int permyriad = (int) (percentile * 100);
        return switch (permyriad) {
            case 0 -> p0;
            case 500 -> p5;
            case 1000 -> p10;
            case 1500 -> p15;
            case 2000 -> p20;
            case 2500 -> p25;
            case 3000 -> p30;
            case 3500 -> p35;
            case 4000 -> p40;
            case 4500 -> p45;
            case 5000 -> p50;
            case 5500 -> p55;
            case 6000 -> p60;
            case 6500 -> p65;
            case 7000 -> p70;
            case 7500 -> p75;
            case 8000 -> p80;
            case 8500 -> p85;
            case 9000 -> p90;
            case 9500 -> p95;
            case 9900 -> p99;
            case 9950 -> p99_5;
            case 9990 -> p99_9;
            case 9995 -> p99_95;
            case 9999 -> p99_99;
            case 10000 -> p100;
            default -> throw new IllegalArgumentException("Percentile (" + percentile + ") is not currently cached");
        };
    }

    public long getTotalCount() {
        return totalCount;
    }

    public static Histogram getNewHistogram() {
        return new Histogram(NUMBER_SIGNIFICANT_DIGITS);
    }
}
