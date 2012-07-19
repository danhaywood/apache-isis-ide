package com.halware.nakedide.eclipse.icons.wizards.common;

import org.eclipse.osgi.util.NLS;

public final class ProjectWizardsMessages extends NLS {

	private static final String RESOURCE_BUNDLE_NAME = 
		ProjectWizardsMessages.class.getCanonicalName();
    
	private ProjectWizardsMessages() {
	}
	
	
    public static String NewIconProjectWizard_MainPage_title;
    public static String NewIconProjectWizard_MainPage_desc;
    public static String NewIconProject_windowTitle;

	public static String ManifestPage_title;
	public static String ManifestPage_desc;
	public static String ManifestPage_pprovider;
	public static String ManifestPage_pid;
	public static String ManifestPage_pversion;
	public static String ManifestPage_pname;

	public static String ManifestPage_classpath;

	static {
		NLS.initializeMessages(RESOURCE_BUNDLE_NAME, ProjectWizardsMessages.class);
	}
}
