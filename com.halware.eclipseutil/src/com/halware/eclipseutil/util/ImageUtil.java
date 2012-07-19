package com.halware.eclipseutil.util;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

import com.halware.eclipseutil.Activator;

/**
 * Static methods for handling image operations.
 * @author Mike
 */
public class ImageUtil {

	
	// prevent instantiation
	private ImageUtil() {
		super();
	}

	/**
	 * Fetches / creates image from passed plugin-relative path from passed
	 * plugin.
	 * 
	 * <p>
	 * Caches and disposes of the image using the plugin's image
	 * registry.
	 * 
	 * @param plugin
	 * @param imagePath
	 * @return
	 */
	public static final Image getImage( 
			final AbstractUIPlugin plugin, final String imagePath ) {
		String key = plugin.getBundle().getSymbolicName() + "." + imagePath; //$NON-NLS-1$
		Image image = plugin.getImageRegistry().get( key );
		if ( image == null ) {
			image = createImage(plugin, imagePath, key);
		}
		return image;
	}

	private static Image createImage(final AbstractUIPlugin plugin, final String imagePath, final String key) {
		Image image;
		ImageDescriptor desc = AbstractUIPlugin.imageDescriptorFromPlugin(
				plugin.getBundle().getSymbolicName(),
				imagePath );
		if ( desc == null ) {
			desc = ImageDescriptor.getMissingImageDescriptor();
		}
		image = desc.createImage();
		plugin.getImageRegistry().put( key, image );
		assert image != null;
		return image;
	}
	

	/**
	 * Fetches / creates image descriptor from passed plugin-relative path 
	 * from passed plugin.  
	 */
	public static final ImageDescriptor getImageDescriptor( 
			final AbstractUIPlugin plugin, 
            final String imagePath ) {
        @SuppressWarnings("unused")
        String key;
        key = plugin.getBundle().getSymbolicName() + "." + imagePath; //$NON-NLS-1$
        ImageDescriptor desc = AbstractUIPlugin.imageDescriptorFromPlugin(
                plugin.getBundle().getSymbolicName(),
                imagePath );
        if ( desc == null) {
            desc = ImageDescriptor.getMissingImageDescriptor();
        }
        return desc;
    }


    public static ImageDescriptor getImageDescriptor(
            Class pojoClass,
            String[] formats) {
        for(String format: formats) {
            ImageDescriptor imageDescriptor = getImageDescriptor(pojoClass, null, format);
            if (imageDescriptor != null) {
                return imageDescriptor;
            }
        }
        return ImageDescriptor.getMissingImageDescriptor();
    }

    public static ImageDescriptor getImageDescriptor(
            Class pojoClass,
            String iconName, 
            String[] formats) {
        for(String format: formats) {
            ImageDescriptor imageDescriptor = getImageDescriptor(pojoClass, iconName, format);
            if (imageDescriptor != null) {
                return imageDescriptor;
            }
        }
        return ImageDescriptor.getMissingImageDescriptor();
    }


    
	public static ImageDescriptor getImageDescriptor(
            final Class pojoClass, 
            final String format) {
		return ImageUtil.getImageDescriptor(pojoClass, 0, format);
	}
	
    public static ImageDescriptor getImageDescriptor(
            final Class pojoClass, 
            final String iconName, 
            final String format) {
        return ImageUtil.getImageDescriptor(pojoClass, iconName, 0, format);
    }
    
    
	public static ImageDescriptor getImageDescriptor(
            final Class pojoClass, 
            final int size, 
            final String format) {
        return getImageDescriptor(pojoClass, null, size, format);
	}

    /**
     * Attempts to locate an {@link ImageDescriptor} for the supplied class, relative to the bundle that
     * supplied the class or the client bundle otherwise.
     * 
     * <p>
     * Looks for an image named
     * <tt>zzz/IconName-xx.yyy</tt> where -xx is the file size, yyy is the format and zzz is the path.
     * The paths tried are <tt>images/</tt>, <tt>icons/</tt> and <tt>src/main/resources/images/</tt>.
     * If the supplied iconName is null, then uses the {@link Class#getSimpleName() simple) class name.
     * 
     * @param pojoClass
     * @param iconName if null, derived from pojoClass.
     * @param size if 0, then "-xx" is omitted.
     * @param format
     * @return
     */
    public static ImageDescriptor getImageDescriptor(
            final Class pojoClass, 
            final String iconName, 
            final int size, 
            final String format) {
        String[] pathCandidates = {"images/", "icons/", "src/main/resources/images/"};
        Bundle applicationBundle = BundleUtil.getBundleThatLoaded(pojoClass);
        Bundle clientBundle = BundleUtil.getClientBundleFor(applicationBundle);
        Bundle[] bundleCandidates = new Bundle[]{applicationBundle, clientBundle};
        for(int i=0; i<pathCandidates.length; i++) {
            String imagePath = iconName != null? 
                    imagePathFor(iconName, pathCandidates[i], size): 
                    imagePathFor(pojoClass, pathCandidates[i], size);
            ImageDescriptor imageDescriptorOf = getImageDescriptor(bundleCandidates, imagePath, format);
            if (imageDescriptorOf != null) {
                return imageDescriptorOf;
            }
        }
        return null;
    }


    
    public static String imagePathFor(final String iconName, String path, final int size) {
        StringBuilder buf = new StringBuilder();
        buf.append(path)
           .append(iconName);
        if (size != 0) {
            buf.append("-")   //$NON-NLS-1$
               .append(size);
        }
        return buf.toString();
    }

    public static String imagePathFor(final Class pojoClass, String path, final int size) {
        return imagePathFor(pojoClass.getSimpleName(), path, size);
    }

    public static String imagePathFor(final Class javaClass, final int size) {
        return imagePathFor(javaClass, "images/", size);
    }

	private static ImageDescriptor getImageDescriptor(final Bundle[] bundleCandidates, String imageFileName, final String format) {
        for(Bundle bundle: bundleCandidates) {
            ImageDescriptor imageDescriptor = getImageDescriptor(bundle, imageFileName, format);
            if (imageDescriptor != null) {
                return imageDescriptor;
            }
        }
        return null;
	}
	
    private static ImageDescriptor getImageDescriptor(final Bundle bundle, String imageFileName, final String format) {
        if (bundle == null) {
            return null;
        }
        imageFileName = imageFileName + "." + format; //$NON-NLS-1$

        return AbstractUIPlugin.imageDescriptorFromPlugin(
                    bundle.getSymbolicName(),
                    imageFileName );
    }
    
	/**
	 * Creates a copy of the image scaled to the passed size.
	 * <br>This image is added to the <code>GUIPlugin</code>'s repository
	 * for caching and resource handling.
	 * <br>If the passed image is the correct size simply returns it.
	 * <br>This method must be called in the UI thread else it will throw
	 * an <code>IllegalThreadStateException</code>
	 * @param image not null
	 * @param size not null
	 * @return
	 */
	public static final Image resize( final Image image, final Point size ) {
		if ( image == null ) {
			throw new IllegalArgumentException();
		}
		if ( size == null ) {
			throw new IllegalArgumentException();
		}
		if ( Display.getCurrent() == null ) {
			throw new IllegalThreadStateException();
		}
		
		// check if anything to do
		ImageData orig = image.getImageData();
		if ( orig.height == size.y && orig.width == size.x ) {
			return image;
		}
		
		// check if already resized and cached
		String key = image.toString() + size.toString();
		Image resized = Activator.getDefault().getImageRegistry().get( key );
		if ( resized == null ) {
			final ImageData scaledTo = orig.scaledTo( size.x, size.y );
            scaledTo.x = scaledTo.x+2;
            scaledTo.y = scaledTo.y+2;
            resized = new Image( 
					Display.getCurrent(), 
					scaledTo ) ;
			Activator.getDefault().getImageRegistry().put( key, resized );
		}
		return resized;
	}



}
