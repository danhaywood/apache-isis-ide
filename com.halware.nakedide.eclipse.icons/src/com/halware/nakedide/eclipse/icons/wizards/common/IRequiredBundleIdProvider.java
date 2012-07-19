package com.halware.nakedide.eclipse.icons.wizards.common;

public interface IRequiredBundleIdProvider {
    String[] getBundleIds();

    public static IRequiredBundleIdProvider NULL = new NullRequiredBundleIdProvider();

    static class NullRequiredBundleIdProvider implements IRequiredBundleIdProvider {

        private NullRequiredBundleIdProvider() {}
        public String[] getBundleIds() {
            return new String[]{};
        }

    }

}
