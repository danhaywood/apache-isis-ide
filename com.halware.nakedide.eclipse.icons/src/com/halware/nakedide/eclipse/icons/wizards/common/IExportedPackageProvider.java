package com.halware.nakedide.eclipse.icons.wizards.common;


public interface IExportedPackageProvider {

    String[] getExportedPackages();
    
    public static IExportedPackageProvider NULL = new NullExportedPackageProvider();

    static class NullExportedPackageProvider implements IExportedPackageProvider {

        private NullExportedPackageProvider() {}
        public String[] getExportedPackages() {
            return new String[]{};
        }

    }

}
