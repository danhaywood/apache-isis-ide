package com.halware.eclipseutil.extpt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * A utility class for locating a list of {@link org.eclipse.core.runtime.IConfigurationElement}s and
 * owning {@link Bundle} that contributes to the specified configuration element
 * with a specified <tt>id</tt>.
 * 
 * <p>
 * If the id is not specified, then not used as a filter.
 * 
 * <p>
 * The <tt>Bundle</tt> is obtained from the <tt>IConfigurationElement</tt>'s
 * namespace. 
 * 
 * @author Dan Haywood
 */
public final class ExtensionPointContributionLocator {

	public ExtensionPointContributionLocator(final String configurationElementId) {
		this(configurationElementId, null);
	}

	public ExtensionPointContributionLocator(final String configurationElementId, final String id) {
		_configurationElementId = configurationElementId;
		_id = id;
		deriveBundleAndConfigurationElementsIfPossible();
	}

	private final String _configurationElementId;
	public String getConfigurationElementId() {
		return _configurationElementId;
	}

	
	private final String _id;
	public String getId() {
		return _id;
	}
	

	private List<IConfigurationElement> _configurationElements = new ArrayList<IConfigurationElement>();
	/**
	 * All configuration elements, sorted by "order" attribute if present.
	 * @return
	 */
	public List<IConfigurationElement> getConfigurationElements() {
		Collections.sort(_configurationElements, new Comparator<IConfigurationElement>() {
			public int compare(final IConfigurationElement o1, final IConfigurationElement o2) {
				int order1 = order(o1);
				int order2 = order(o2);
				if (order1 < order2) {
					return -1;
				}
				if (order1 > order1) {
					return +1;
				}
				return 0;
			}
			private int order(final IConfigurationElement o1) {
				String orderStr = o1.getAttribute("order");
				if (orderStr == null) {
					return 0;
				}
				try {
					return Integer.parseInt(orderStr);
				} catch(Exception ex) {
					return 0;
				}
			}
		});
		return _configurationElements;
	}
	
	
	private Bundle _bundle;
	public Bundle getBundle() {
		return _bundle;
	}
	
	/**
	 * Looks for a plugin that extends the configuration element (as identified
	 * by {@link #setConfigurationElementId(String)} where the contribution's
	 * <tt>id</tt> matches that injected into {@link #setId(String)}.
	 * 
	 * <p>
	 * If found then obtains the {@link Bundle} from configuration element and
	 * sets (otherwise does nothing).
	 */
	private void deriveBundleAndConfigurationElementsIfPossible() {
		if (_configurationElementId == null) {
			return;
		}
		
		IExtensionPoint extensionPoint = 
			Platform.getExtensionRegistry().getExtensionPoint(_configurationElementId);
		for(IConfigurationElement element: extensionPoint.getConfigurationElements()) {
			if (_id == null || _id.equals(element.getAttribute("id"))) {
				_configurationElements.add(element);
				if (_bundle == null) {
					_bundle = Platform.getBundle(element.getContributor().getName());	
				}
			}
		}
	}

}
