package com.halware.eclipseutil.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

public final class SelectionUtil {
	private SelectionUtil() {}
	
	public static Object singleSelection(final DoubleClickEvent event) {
		return singleSelection(event.getSelection());
	}

	public static Object singleSelection(final ISelection selection) {
		if (!(selection instanceof IStructuredSelection)) {
			return null;
		}
		return singleSelection((IStructuredSelection)selection);
	}

	public static Object singleSelection(final IStructuredSelection selection) {
		if (selection.size() != 1) {
			return null;
		}
		return selection.getFirstElement();
	}

	public static Object[] multipleSelection(final ISelection selection) {
		if (!(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection structuredSelection = (IStructuredSelection)selection;
		Object[] toArray = structuredSelection.toArray();
		if (toArray.length == 0) {
			return null; // simplifies the client; anything non-null means we have something to process
		}
		return toArray;
	}

	public static <V> List<V> multipleSelection(final ISelection selection, final Class<V> requiredType) {
		return multipleSelection(selection, requiredType, false);
	}


	public static <V> List<V> multipleSelection(final ISelection selection, final Class<V> requiredType, final boolean strict) {
		Object[] multipleSelection = multipleSelection(selection);
		if (multipleSelection == null) {
			return null;
		}
		List<V> selected = new ArrayList<V>();
		for(int i=0; i<multipleSelection.length; i++) {
			Object object = multipleSelection[i];
			if (requiredType.isAssignableFrom(object.getClass())) {
				selected.add(asV(object, requiredType));
			} else {
				if (strict) {
					return Collections.emptyList();
				}
			}
		}
		return selected;
	}

	@SuppressWarnings("unchecked")
	private static <V> V asV(final Object object, final Class<V> type) {
		return (V)object;
	}

}
