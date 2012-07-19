package com.halware.nakedide.eclipse.icons.wizards.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the project dependencies in the manifest.
 * <tt>Require-Bundle</tt>.
 */
public abstract class AbstractProjectRequiredBundles implements IRequiredBundles {

    protected AbstractProjectRequiredBundles() {
    }

    /**
     * Use {@link List#add(Object)} to add the bundle Ids of any additional
     * project-specific bundles.
     * 
     * @param dependencies
     */
    public void doAddBundleIds(List<String> dependencies) {
    }

    public final List<String> getBundleIds() {
        List<String> dependencies = new ArrayList<String>();
        doAddBundleIds(dependencies);
        return dependencies;
    }
}

