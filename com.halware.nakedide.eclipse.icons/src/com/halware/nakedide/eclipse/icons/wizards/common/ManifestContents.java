package com.halware.nakedide.eclipse.icons.wizards.common;

import java.util.Map;

import org.eclipse.core.runtime.internal.adaptor.PluginConverterImpl;
import org.eclipse.osgi.util.ManifestElement;

/**
 * Ripped off from {@link PluginConverterImpl}.
 */
@SuppressWarnings("restriction")
public class ManifestContents {

    private static int MAXLINE = 511;
    private static final String LIST_SEPARATOR = ",\n "; //$NON-NLS-1$
    private static final String LINE_SEPARATOR = "\n "; //$NON-NLS-1$


    private StringBuilder buf = new StringBuilder();
    public ManifestContents() {
    }

    public void writeEntries(Map<String, String> manifestProperties) {
        for(String key: manifestProperties.keySet()) {
            String val = manifestProperties.get(key);
            writeEntry(key, val);
        }
    }
    public void writeEntry(String key, String value) {
        if (value != null && value.length() > 0) {
            buf.append(splitOnComma(key + ": " + value)); //$NON-NLS-1$
            buf.append('\n');
        }
    }

    /**
     * Ripped off from {@link PluginConverterImpl}.
     * 
     * @param value
     * @return
     */
    private String splitOnComma(String value) {
        if (value.length() < MAXLINE || value.indexOf(LINE_SEPARATOR) >= 0)
            return value; // assume the line is already split
        String[] values = ManifestElement.getArrayFromList(value);
        if (values == null || values.length == 0)
            return value;
        StringBuffer sb = new StringBuffer(value.length() + ((values.length - 1) * LIST_SEPARATOR.length()));
        for (int i = 0; i < values.length - 1; i++)
            sb.append(values[i]).append(LIST_SEPARATOR);
        sb.append(values[values.length -1]);
        return sb.toString();
    }
    
    public String getContents() {
        return buf.toString();
    }
}
