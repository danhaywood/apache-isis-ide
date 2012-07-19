package com.halware.eclipseutil.util;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.swt.widgets.Table;

public final class JFaceUtil {
	
	private JFaceUtil() {}
	
	@SuppressWarnings("deprecation")
	public static void selectTopIfNotSelected(final TableTreeViewer viewer) {
		if (viewer.getTableTree().getItemCount() == 0) {
			return;
		}
		if (!viewer.getSelection().isEmpty()) {
			return;
		}
		Object topElement = viewer.getElementAt(0);
		viewer.setSelection(new StructuredSelection(topElement), true);
	}

	@SuppressWarnings("deprecation")
	public static Table getTable(final TableTreeViewer viewer) {
		return viewer.getTableTree().getTable();
	}



}
