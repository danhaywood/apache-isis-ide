package com.halware.eclipseutil.util;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * Miscellaneous UI utilities.
 */
public class SWTTableTreeUtil {

	/**
	 * Resizes all the columns in a table to fit the column's contents,
	 * including the column header.
	 * 
	 * Callers should use the Table.setRedraw method before calling this method
	 * in order to avoid flicker.
	 * 
	 * WARNING: Do not call this method from within a table resize event. This
	 * method may cause the table to resize, therefore calling this method from
	 * within a table resize event may cause a stack overflow.
	 * 
	 */
	public static void packTableColumns(final Table table) {
		TableColumn[] columns = table.getColumns();
		int columnWidth = 0;
		for (int i = 0; i < columns.length; i++) {
			TableColumn column = columns[i];
			int minWidth = column.getWidth();

			column.pack();
			if ((column.getWidth() < minWidth) && (i < columns.length - 1)) {
				column.setWidth(minWidth);
			}
			if (i < columns.length - 1) {
				columnWidth += column.getWidth();
			}
		}
	}

	static public void packTableColumns(final Table table, final int row) {
		TableColumn[] columns = table.getColumns();
		int columnWidth = 0;
		for (int i = 0; i < columns.length; i++) {
			TableColumn column = columns[i];
			int minWidth = column.getWidth();
			column.pack();
			if (column.getWidth() < minWidth) {
				column.setWidth(minWidth);
			}
			if (i < columns.length - 1) {
				columnWidth += column.getWidth();
			}
		}
	}

	/**
	 * Evenly expands column widths to suck up any leftover space in the table.
	 * Does not expand a column past it's packed width.
	 * 
	 * This method should only be called on hidden tables since it will cause
	 * crazy insane flicker otherwise.
	 * 
	 * @param table
	 */
	public static void evenlyWidenColumns(final Table table) {
		if (table == null) {
			throw new IllegalArgumentException();
		}
		TableColumn[] columns = table.getColumns();
		if (0 < columns.length) {
			int clientWidth = table.getClientArea().width;
			clientWidth -= table.getBorderWidth() * 2;

			int totStartWidth = 0;
			int[] startWidths = new int[columns.length];
			for (int i = 0; i < columns.length; i++) {
				TableColumn column = columns[i];
				totStartWidth += startWidths[i] = column.getWidth();
				column.pack();
			}

			if (clientWidth <= totStartWidth) {
				return;
			}
			int[] packedWidths = new int[columns.length];
			int totPackedWidth = 0;
			for (int i = 0; i < columns.length; i++) {
				TableColumn column = columns[i];
				int width = column.getWidth();
				if (width < startWidths[i]) {
					column.setWidth(width = startWidths[i]);
				}
				totPackedWidth += packedWidths[i] = width;
			}

			if (totPackedWidth < clientWidth) {
				autosizeLastColumn(table);
				return;
			}

			int totExcess = totPackedWidth - clientWidth;
			int totDiff = totPackedWidth - totStartWidth;
			int totFinalWidth = 0;
			for (int i = 0; i < columns.length; i++) {
				TableColumn column = columns[i];
				int packedWidth = packedWidths[i];
				int diff = packedWidth - startWidths[i];
				int excess = (totExcess * diff) / totDiff;
				if (0 < excess) {
					int w = packedWidth - excess;
					totFinalWidth += w;
					column.setWidth(w);
				}
			}
		}
	}

	/**
	 * Evenly expands column widths to suck up any leftover space in the table.
	 * Does not expand a column past it's packed width.
	 * 
	 * This method should only be called on hidden tables since it will cause
	 * crazy insane flicker otherwise.
	 * 
	 * @param tree
	 */
	public static void evenlyWidenColumns(final Tree tree) {
		if (tree == null) {
			throw new IllegalArgumentException();
		}
		TreeColumn[] columns = tree.getColumns();
		if (0 < columns.length) {
			int clientWidth = tree.getClientArea().width;
			clientWidth -= tree.getBorderWidth() * 2;

			int totStartWidth = 0;
			int[] startWidths = new int[columns.length];
			for (int i = 0; i < columns.length; i++) {
				TreeColumn column = columns[i];
				totStartWidth += startWidths[i] = column.getWidth();
				column.pack();
			}

			if (clientWidth <= totStartWidth) {
				return;
			}
			int[] packedWidths = new int[columns.length];
			int totPackedWidth = 0;
			for (int i = 0; i < columns.length; i++) {
				TreeColumn column = columns[i];
				int width = column.getWidth();
				if (width < startWidths[i]) {
					column.setWidth(width = startWidths[i]);
				}
				totPackedWidth += packedWidths[i] = width;
			}

			if (totPackedWidth < clientWidth) {
				autosizeLastColumn(tree);
				return;
			}

			int totExcess = totPackedWidth - clientWidth;
			int totDiff = totPackedWidth - totStartWidth;
			int totFinalWidth = 0;
			for (int i = 0; i < columns.length; i++) {
				TreeColumn column = columns[i];
				int packedWidth = packedWidths[i];
				int diff = packedWidth - startWidths[i];
				int excess = (totExcess * diff) / totDiff;
				if (0 < excess) {
					int w = packedWidth - excess;
					totFinalWidth += w;
					column.setWidth(w);
				}
			}
		}
	}

	/**
	 * Expands the last column in a table to suck up any left over space in it's
	 * table.
	 * 
	 * @param table
	 */
	public static void autosizeLastColumn(final Table table) {
		if (table == null) {
			throw new IllegalArgumentException();
		}
		autosizeColumn(table, table.getColumns().length - 1);
	}

	/**
	 * Expands the last column in a table to suck up any left over space in it's
	 * table.
	 * 
	 * @param tree
	 */
	public static void autosizeLastColumn(final Tree tree) {
		if (tree == null) {
			throw new IllegalArgumentException();
		}
		autosizeColumn(tree, tree.getColumns().length - 1);
	}

	/**
	 * Expands the denoted column in a table to suck up any left over space in
	 * it's table.
	 * 
	 * @param table
	 * @param column
	 *            index
	 */
	public static void autosizeColumn(final Table table, final int columnIndex) {
		if (table == null) {
			throw new IllegalArgumentException();
		}
		if (columnIndex < 0) {
			throw new IllegalArgumentException();
		}
		if (columnIndex >= table.getColumnCount()) {
			throw new IllegalArgumentException();
		}
		TableColumn[] columns = table.getColumns();
		if (0 < columns.length) {
			int totColumnWidth = 0;
			for (int i = 0; i < columns.length; i++) {
				if (i != columnIndex) {
					totColumnWidth += columns[i].getWidth();
				}
			}

			int clientWidth = table.getClientArea().width;
			clientWidth -= table.getBorderWidth() * 2;
			int lastColWidth = clientWidth - totColumnWidth;
			TableColumn lastColumn = columns[columnIndex];
			if (lastColumn.getWidth() < lastColWidth) {
				lastColumn.setWidth(lastColWidth);
			}
		}
	}

	/**
	 * Expands the denoted column in a tree to suck up any left over space in
	 * it's tree.
	 * 
	 * @param tree
	 * @param column
	 *            index
	 */
	public static void autosizeColumn(final Tree tree, final int columnIndex) {
		if (tree == null) {
			throw new IllegalArgumentException();
		}
		if (columnIndex < 0) {
			throw new IllegalArgumentException();
		}
		if (columnIndex >= tree.getColumnCount()) {
			throw new IllegalArgumentException();
		}
		TreeColumn[] columns = tree.getColumns();
		if (0 < columns.length) {
			int totColumnWidth = 0;
			for (int i = 0; i < columns.length; i++) {
				if (i != columnIndex) {
					totColumnWidth += columns[i].getWidth();
				}
			}
			int clientWidth = tree.getClientArea().width;
			clientWidth -= tree.getBorderWidth() * 2;
			int lastColWidth = clientWidth - totColumnWidth;
			TreeColumn lastColumn = columns[columnIndex];
			if (lastColumn.getWidth() < lastColWidth) {
				lastColumn.setWidth(lastColWidth);
			}
		}
	}

	/**
	 * Better to use PropertyDescriptors.
	 * 
	 * @param table
	 * @param style
	 * @param label
	 * @param width
	 * @return
	 */
	public static TableColumn setUpTableColumn(final Table table,
			final int style, final String label, final int width) {
		TableColumn tc = new TableColumn(table, style);
		tc.setText(label);
		tc.setWidth(width);
		return tc;
	}

}
