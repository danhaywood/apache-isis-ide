package com.halware.eclipseutil.extpt;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

/**
 * Used to sort config elements - see {@link #compare(IConfigurationElement, IConfigurationElement)}.
 * @author Mike
 *
 */
public final class ConfigElementSorter implements Comparator<IConfigurationElement> {
	
    private static final Map<Bundle, Bundle[]> BUNDLE_REQUISITES
    	= new HashMap<Bundle,Bundle[]>();
    private static final Bundle[] EMPTY_BUNDLE_ARRAY = new Bundle[0];
	

	/**
	 * Sorts config elements so more specialised ones come before general
	 * ones.
	 * 
	 * <p>
	 * This is based on the declaring plugins' dependencies so that a plugin
	 * 'A' is deemed more specialist than plugin 'B' if its is dependent on 
	 * plugin 'B'.
	 * 
	 * @see java.util.Comparator#compare(T, T)
	 */
	public int compare(
			final IConfigurationElement configEl1, 
			final IConfigurationElement configEl2) {
        
        String bundleId1 = configEl1.getDeclaringExtension().getContributor().getName();
        String bundleId2 = configEl2.getDeclaringExtension().getContributor().getName();
        
        // shortcut
        if ( bundleId1.equals( bundleId2 ) ) {
			return 0;
		}
    	// else must do bundle checks
        try {
            Bundle bundle1 = Platform.getBundle( bundleId1 );
        	Bundle bundle2 = Platform.getBundle( bundleId2 );
            if ( isDependent( bundle1, bundle2 ) ) {
                // plugin1 depends on plugin2 so must be higher in list
                return -1;
            }
            else if ( isDependent( bundle2, bundle1 ) ) {
                // plugin2 depends on plugin1 so must be higher in list
                return 1;
            }
        }
        catch ( BundleException be ) {
        	assert false;
        }
        return 0;
	}
       	
    
    /**
     * Returns all bundles that the passed bundle depends on.  This 
     * traverses the full dependency tree.
     * Note an implementation detail: results are cached for performance
     * as it is not expected that the plugin dependencies can change during
     * the life of the VM.
     * TODO - this is fo course incorrect for Eclipse 3
     * @param bundle
     * @return
     */
    private Bundle[] getRequisiteBundles( final Bundle bundle )
            throws BundleException {
        if (bundle == null) {
			throw new IllegalArgumentException();
		}
        Bundle[] requisites = ConfigElementSorter.BUNDLE_REQUISITES.get( bundle );
        if ( requisites == null ) {
			// question : how do I parameterise this?
            String requires = (String)bundle.getHeaders().get( 
					Constants.REQUIRE_BUNDLE );
            if ( requires == null ) {
            	requisites = ConfigElementSorter.EMPTY_BUNDLE_ARRAY;
            }
            else {
                ManifestElement[] elements = ManifestElement.parseHeader(
                        Constants.REQUIRE_BUNDLE, requires);
                // use a set so do not get duplicates
                Set<Bundle> requiredBundles = new HashSet<Bundle>();
                for ( int i=0 ; i < elements.length; i++ ) {
                    Bundle required = Platform.getBundle( elements[i].getValue() );
                    requiredBundles.add( required );
                    // recursive call
                    Bundle[] requiredsRequired = getRequisiteBundles( required ) ;
                    for ( int j=0 ; j < requiredsRequired.length; j++ ) {
                    	requiredBundles.add( requiredsRequired[j] );
                    }
                }
                // convert to array and cache
				requisites = requiredBundles.toArray( ConfigElementSorter.EMPTY_BUNDLE_ARRAY );
            }
            ConfigElementSorter.BUNDLE_REQUISITES.put( bundle, requisites );
        }
        assert requisites != null;
        return requisites;
    }
	
    // is plugin 1 dependent on plugin 2?
    private boolean isDependent( final Bundle bundle1, 
                                 final Bundle bundle2 ) throws BundleException  {
    	
    	List required = Arrays.asList( getRequisiteBundles( bundle1 ) );
        return required.contains( bundle2 );
    }
        
}