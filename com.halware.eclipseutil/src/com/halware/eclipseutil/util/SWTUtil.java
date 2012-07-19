package com.halware.eclipseutil.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;


/**
 * Miscellaneous UI utilities.
 */
public class SWTUtil {


    /**
     * Generates what the value in the passed <code>Text</code> would be if the
     * passed <code>VerifyEvent</code> is applied
     * @param event
     * @return
     */
    public static String buildResultantText( final Text text, final VerifyEvent event ) {
        if ( text == null ) {
            throw new IllegalArgumentException() ;
        }
        if ( event == null ) {
            throw new IllegalArgumentException() ;
        }
        
        String orig = text.getText();
        if ( orig.length() == 0 ) {
            return event.text;
        }
        else {
            StringBuffer sb = new StringBuffer();
            sb.append( orig.substring( 0, event.start ) );
            sb.append( event.text );
            sb.append( orig.substring( event.end, orig.length() ) );
            return sb.toString();
        }
    }
    
    /**
     * Generates what the value in the passed <code>Text</code> would be if the
     * passed <code>VerifyEvent</code> is applied
     * 
     * @param event
     * @return
     */
    public static String buildResultantText( final Combo text, final VerifyEvent event ) {
        if ( text == null ) {
            throw new IllegalArgumentException() ;
        }
        if ( event == null ) {
            throw new IllegalArgumentException() ;
        }
        
        String orig = text.getText();
        if ( orig.length() == 0 ) {
            return event.text;
        }
        else {
            StringBuffer sb = new StringBuffer();
            sb.append( orig.substring( 0, event.start ) );
            sb.append( event.text );
            sb.append( orig.substring( event.end, orig.length() ) );
            return sb.toString();
        }
    }
    

    /**
     * Sets enablement of all controls on the passed composite.  Recurses as
     * necessary.
     * 
     * @param parent
     * @param enabled
     */
    public static void setEnabled( final Composite parent, final boolean enabled ) {
    	if ( parent == null ) {
			throw new IllegalArgumentException() ;
		}
    	for ( Control child : parent.getChildren() ) {
    		if ( child instanceof Composite ) {
    			setEnabled( (Composite)child, enabled );
    		}
    		else {
    			child.setEnabled( enabled );
    		}
    	}
    }
    
    /**
     * Finds first parent of the passed class or <code>null</code>.
     * 
     * @param control
     * @param requiredClass
     * @return
     */
    public static <T extends Composite> T getParent(
    		final Control control, final Class<T> requiredClass ) {
    	if ( control == null ) {
			throw new IllegalArgumentException() ;
		}
    	if ( requiredClass == null ) {
			throw new IllegalArgumentException() ;
		}
    	Composite parent = control.getParent();
    	while ( parent != null ) {
    		if (parent.getClass().equals(requiredClass) ) {
				return Generics.asT(parent);
    		}
    		parent = parent.getParent();
    	}
    	return null;
    }

	/**
     * Returns true of the given control or any of it's children has the focus.
     */
    public static boolean isFocusAncestor(final Control control) {
        Display display= control.getDisplay();
        Control focusControl= display.getFocusControl();
        while (focusControl != null) {
            if (focusControl == control) {
				return true;
			}
            focusControl= focusControl.getParent();
        }
        return false; 
    }

    public static final boolean isDescendant(final Composite parent, final Control target) {
        if (parent == target) {
			return true;
		}
        Control[] children= parent.getChildren();
        for (int i= 0; i < children.length; i++) {
            Control child= children[i];
            if (target == child) {
				return true;
			}
            if (child instanceof Composite) {
                if (isDescendant((Composite)child, target)) {
					return true;
				}
            }
        }
        return false;
    }

    /**
     * Positions the pulldown control underneath the bottom lefthand corner of the 
     * chiclet control.  However, the pulldown control position will be adjusted 
     * so that no part of it is off the display.
     * 
     * @param newShell
     * @param control
     */
    public static void positionPulldown(
    		final Control pulldown, final Control chiclet) {
        Point p= chiclet.getLocation();
        p.y+= chiclet.getSize().y;
        pulldown.setLocation(p);
    }
    

    /**
     * Looks for all input elements (text fields, checkboxes, etc) and disables them.
     * Recurses through all children.
     */
    public static void setReadOnlyOnAllInputElements(final Widget parent, final boolean value) {
        if (isInputElement(parent)) {
			setEnabled(parent, value);
		}
        if (parent instanceof Composite) {
            Control[] children= ((Composite)parent).getChildren();
            for (int i= 0; i < children.length; i++) {
                setReadOnlyOnAllInputElements(children[i], value);
            }
        }
    }
    
    private static boolean isInputElement(final Widget widget) {
        if (widget instanceof Text) {
			return true;
		}
        if (widget instanceof Combo) {
			return true;
		}
        if (widget instanceof Button) {
			return true;
		}
        if (widget instanceof Menu) {
			return true;
		}
        return false;
    }
    
    private static void setEnabled(
    		final Widget widget, final boolean value) {
        if (widget instanceof Control) {
			((Control)widget).setEnabled(value);
		}
        if (widget instanceof Menu) {
			((Menu)widget).setEnabled(value);
		}
    }

    /**
     * Returns the string that would result if the text change represented by
     * the verify event were to be applied to the text object.
     * 
     * <p>
     * The verify event is supposed to originate from this text, eg:
     * <pre>
     * text.addVerifyListener(
     *     new VerifyListener() {
     *         public void verifyText(VerifyEvent e) {
     *             String resultantText = 
     *                 SWTUtil.ifAcceptedVerifyEvent(text, e);
     *             e.doit = isOk(resultantText);
     *         }
     *
     *         private boolean isOk(String text) {
     *             // validate text here
     *         }
     *     });
     * </pre>
     * @param text
     * @param e
     * @return
     */
	public static String ifAcceptedVerifyEvent(Text text, VerifyEvent e) {
		return text.getText(0, e.start-1) + e.text + text.getText(e.end, Integer.MAX_VALUE);
	}


    public static void paintGradiated(
                final ExpandBar bar, final int color1, final int color2) {
            final Display display = bar.getShell().getDisplay();
            
            bar.addListener (SWT.Resize, new Listener () {
                public void handleEvent (Event event) {
                    Rectangle rect = bar.getBounds();
                    Image newImage = new Image (display , Math.max (1, rect.width), 1);  
                    GC gc = new GC (newImage);
                    gc.setForeground (display.getSystemColor (color1));
                    gc.setBackground (display.getSystemColor (color2));
                    gc.fillGradientRectangle (rect.x, rect.y, rect.width, 1, false);
                    gc.dispose ();
                    final Image oldImage = bar.getBackgroundImage();
                    bar.setBackgroundImage (newImage);
                    if (oldImage != null) {
                        oldImage.dispose();
                    }
                }
            });
        }


    public static void matchSize(
            final Control parent,
            final Control child) {
        matchSize(parent, child, false);
    }

    public static void matchSize(
            final Control parent,
            final Control child,
            final boolean listen) {
        child.setSize(parent.getSize());
        if (listen) { 
            parent.addControlListener(new ControlAdapter() {
                @Override
                public void controlResized(
                        ControlEvent e) {
                    child.setSize(parent.getSize());
                }
            });
        }
    }

    public static void matchBackground(
            Control parent,
            Control child) {
        child.setBackground(parent.getBackground());
    }

}
  
