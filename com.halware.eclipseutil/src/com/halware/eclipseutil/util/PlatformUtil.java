package com.halware.eclipseutil.util;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public final class PlatformUtil {
    private PlatformUtil() {}

    /**
     * Returns true if Eclipse's runtime bundle has the same or a newer than
     * given version.
     */
    public static boolean isEclipseSameOrNewer(int majorVersion,
            int minorVersion) {
        Bundle bundle = Platform.getBundle(Platform.PI_RUNTIME);
        if (bundle != null) {
            String version = (String) bundle.getHeaders().get(
                    org.osgi.framework.Constants.BUNDLE_VERSION);
            StringTokenizer st = new StringTokenizer(version, ".");
            try {
                int major = Integer.parseInt(st.nextToken());
                if (major > majorVersion) {
                    return true;
                }
                if (major == majorVersion) {
                    int minor = Integer.parseInt(st.nextToken());
                    if (minor >= minorVersion) {
                        return true;
                    }
                }
            } catch (NoSuchElementException e) {
                // ignore
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return false;
    }

}
