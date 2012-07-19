package com.halware.eclipseutil.util;

import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

    private IOUtils() {}
    
    public static void closeSafely(
            InputStream inputStream) {
        if (inputStream == null) return;
        try {
            inputStream.close();
        } catch (IOException e) {
        }
    }

}
