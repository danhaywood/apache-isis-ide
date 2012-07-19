package com.halware.eclipseutil.util;

import java.util.Date;

public final class ThreadUtils {

    private ThreadUtils() {
    }

    /**
     * Sleep (uninterrupted) for specified number of milliseconds.
     * 
     * @param milliseconds
     */
    public static void sleepFor(
            int milliseconds) {
        long t0 = new Date().getTime();
        long t1 = t0 + milliseconds;
        long stillToGo = t1 - t0;
        while (stillToGo > 0) {
            try {
                Thread.sleep(stillToGo);
            } catch (InterruptedException e) {
                // ignore - we will loop around.
            }
            stillToGo = t1 - new Date().getTime();
        }
    }

}
