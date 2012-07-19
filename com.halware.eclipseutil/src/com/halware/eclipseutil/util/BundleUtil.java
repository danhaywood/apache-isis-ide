package com.halware.eclipseutil.util;

import org.osgi.framework.Bundle;

import com.halware.eclipseutil.Activator;

public final class BundleUtil {

    private static final String CLIENT_BUNDLE_SUFFIX = ".client";

    private BundleUtil() {}
	
	public static Bundle getBundleThatLoaded(final Class javaClass) {
		Bundle parentBundle = Activator.getDefault().getPackageAdmin().getBundle(javaClass);
		return parentBundle;
	}

    
    /**
     * Returns the bundle (if any) that represents the &quot;client&quot; bundle
     * for the provided application bundle.
     * 
     * <p>
     * The IDE provides wizards to create an application project <tt>com.mycompany.myapp</tt>
     * (where the domain objects live), a client project that has a symbolic name that is the
     * same as the application project but with a <i>.client</i> suffix (ie <tt>com.mycompany.myapp.client</tt>)
     * and also a fixture project (not relevant to this discussion).  This method looks up
     * the client bundle for an (assumed) application bundle.
     * 
     * <p>
     * Used in order to guess where the icons for the domain projects reside.
     * 
     * @param applicationBundle
     * @return
     */
    public static Bundle getClientBundleFor(final Bundle applicationBundle) {
        if (applicationBundle == null) {
            return null;
        }
        String symbolicName = applicationBundle.getSymbolicName();
        if (symbolicName.endsWith(CLIENT_BUNDLE_SUFFIX)) {
            return applicationBundle;
        }
        String clientBundleSymbolicName = symbolicName + CLIENT_BUNDLE_SUFFIX;
        Bundle[] clientBundles = Activator.getDefault().getPackageAdmin().getBundles(clientBundleSymbolicName, null);
        return clientBundles != null && clientBundles.length > 0? clientBundles[0]: null;
    }
    
	@SuppressWarnings("unchecked")
	public static <T> Class<T> loadClass(
			final Bundle bundle, final String adapterFactoryName) throws ClassNotFoundException {
		return (Class<T>) bundle.loadClass(adapterFactoryName);
	}


}
