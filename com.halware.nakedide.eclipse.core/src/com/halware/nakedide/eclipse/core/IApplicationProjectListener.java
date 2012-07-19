package com.halware.nakedide.eclipse.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;

public interface IApplicationProjectListener {
    boolean configure(IProject project, IConfigurationElement config);
}
