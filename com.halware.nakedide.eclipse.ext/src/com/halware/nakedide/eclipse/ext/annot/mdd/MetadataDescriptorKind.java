package com.halware.nakedide.eclipse.ext.annot.mdd;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class MetadataDescriptorKind<T extends CellEditor> {
	
	public final static MetadataDescriptorKind<StringCellEditor> STRING = 
		new MetadataDescriptorKind<StringCellEditor>(100, SWT.LEFT, StringCellEditor.class);
    public final static MetadataDescriptorKind<StringCellEditor> COMPACT_STRING = 
        new MetadataDescriptorKind<StringCellEditor>(60, SWT.LEFT, StringCellEditor.class);
	public final static MetadataDescriptorKind<CheckboxCellEditor> BOOLEAN = 
		new MetadataDescriptorKind<CheckboxCellEditor>(80, SWT.CENTER, CheckboxCellEditor.class);
    public final static MetadataDescriptorKind<CheckboxCellEditor> COMPACT_BOOLEAN = 
        new MetadataDescriptorKind<CheckboxCellEditor>(30, SWT.CENTER, CheckboxCellEditor.class);
	public final static MetadataDescriptorKind<IntegerCellEditor> INTEGER = 
		new MetadataDescriptorKind<IntegerCellEditor>(80, SWT.RIGHT, IntegerCellEditor.class);
	public final static MetadataDescriptorKind<StringCellEditor> LONG_STRING = 
		new MetadataDescriptorKind<StringCellEditor>(400, SWT.LEFT, StringCellEditor.class);
    public static final MetadataDescriptorKind<ClassNameCellEditor> CLASS_NAME =
        new MetadataDescriptorKind<ClassNameCellEditor>(200, SWT.LEFT, ClassNameCellEditor.class);
	/**
	 * Defaults to the same column length as that of a string.
	 * 
	 * @param style
	 * @param cellEditorClass
	 * 
	 * @see MetadataDescriptorKind#STRING
	 */
	public MetadataDescriptorKind(int style, Class<T> cellEditorClass) {
		this(MetadataDescriptorKind.STRING.getColumnLength(), style, cellEditorClass);
	}

	public MetadataDescriptorKind(int columnLength, int style, Class<T> cellEditorClass) {
		this.columnLength = columnLength;
		this.style = style;
		this.cellEditorClass = cellEditorClass;
	}
	
	private int columnLength;
	public int getColumnLength() {
		return columnLength;
	}

	private int style;
	public int getStyle() {
		return style;
	}
	
	private final Class<T> cellEditorClass;
	public T createCellEditor(Composite table) {
		try {
			Constructor<T> constructor = cellEditorClass.getConstructor(new Class[]{Composite.class});
			return constructor.newInstance(new Object[]{table});
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}

/******************************************************************************
 * (c) 2007 Haywood Associates Ltd.
 * 
 * Distributed under Eclipse Public License 1.0, see
 * http://www.eclipse.org/legal/epl-v10.html for full details.
 *
 * In particular:
 * THE PROGRAM IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED INCLUDING, WITHOUT 
 * LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE, NON-INFRINGEMENT, 
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
 *
 * If you require this software under any other type of license, then contact 
 * Dan Haywood through http://www.haywood-associates.co.uk.
 *
 *****************************************************************************/
