package com.halware.nakedide.eclipse.ext.annot.mdd;


import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.halware.eclipseutil.util.Generics;
import com.halware.nakedide.eclipse.ext.annot.action.NakedObjectAction;
import com.halware.nakedide.eclipse.ext.annot.action.params.NakedObjectActionParameter;
import com.halware.nakedide.eclipse.ext.annot.ast.MultipleValueAnnotationSingleMemberEvaluatorAndModifier;
import com.halware.nakedide.eclipse.ext.annot.coll.NakedObjectCollection;
import com.halware.nakedide.eclipse.ext.annot.prop.NakedObjectProperty;
import com.halware.nakedide.eclipse.ext.outline.NakedObjectMember;

/**
 * Represents some metadata (annotation or other information) about
 * a class, a property/collection/action of a class, or a parameter 
 * of an action.
 */
public class MetadataDescriptor {


    public MetadataDescriptor(
			String memberPropertyName, 
			IEvaluatorAndModifier evaluatorAndModifier) {
		this(
			memberPropertyName,  
            (String)null,
			evaluatorAndModifier);
	}
	
    public MetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            IEvaluatorAndModifier evaluatorAndModifier) {
        this(
            memberPropertyName,  
            memberPropertyDescription,  
            evaluatorAndModifier, evaluatorAndModifier, null);
    }
    
	public MetadataDescriptor(
			String memberPropertyName, 
			IEvaluator evaluator, 
			IModifier modifier) {
		this(
			memberPropertyName,
            (String)null,
			evaluator, modifier);
	}

    public MetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            IEvaluator evaluator, 
            IModifier modifier) {
        this(
            memberPropertyName,
            memberPropertyDescription,
            evaluator, modifier, null);
    }

	public MetadataDescriptor(
			String memberPropertyName, 
			IEvaluatorAndModifier evaluatorAndModifier,
			IEvaluatedLabelProvider evaluatedLabelProvider) {
		this(
			memberPropertyName,
            (String)null,
			evaluatorAndModifier, evaluatedLabelProvider);
	}
	

    public MetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            IEvaluatorAndModifier evaluatorAndModifier,
            IEvaluatedLabelProvider evaluatedLabelProvider) {
        this(
            memberPropertyName, memberPropertyDescription, 
            evaluatorAndModifier, evaluatorAndModifier, 
            evaluatedLabelProvider);
    }

    public MetadataDescriptor(
            String memberPropertyName, 
            MultipleValueAnnotationSingleMemberEvaluatorAndModifier mvasmeam) {
		this(memberPropertyName, (String)null, mvasmeam);
    }

    public MetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            MultipleValueAnnotationSingleMemberEvaluatorAndModifier mvasmeam) {
        this(memberPropertyName, mvasmeam, mvasmeam, null);
    }

	public MetadataDescriptor(
			String memberPropertyName, 
			IEvaluator evaluator, 
			IModifier modifier, 
			IEvaluatedLabelProvider labelProvider) {
        this(memberPropertyName, null, evaluator, modifier, labelProvider);
	}


    public MetadataDescriptor(
            String memberPropertyName, 
            String memberPropertyDescription, 
            IEvaluator evaluator, 
            IModifier modifier, 
            IEvaluatedLabelProvider labelProvider) {
        this.name = memberPropertyName;
        this.description = memberPropertyDescription;
        this.metadataDescriptorKind = evaluator.getKind();
        this.evaluator = evaluator;
        this.modifier = modifier;
        this.labelProvider = labelProvider;
    }

    /**
	 * Called by {@link MetadataDescriptorSet} when the descriptor has been added to it.
	 * 
	 * @param table
	 * @return
	 */
	void init(int position, Table table, IEvaluatedLabelProvider defaultLabelProvider) {
		
		this.position = position;
		this.table = table;
		this.defaultLabelProvider = defaultLabelProvider;

		this.cellEditor = metadataDescriptorKind.createCellEditor(this.table);
		setUpTableColumn();
		
		if (getCellEditor() instanceof StringCellEditor) {
			StringCellEditor stringCellEditor = getCellEditor();
            if (regex != null) {
                stringCellEditor.setVerifyRegex(regex);
            }
            if (validator != null) {
                stringCellEditor.setValidator(validator);
            }
		}
	}


	private int position;
	/**
	 * Column position in the table; so that label provider can switch.
	 *
	 * <p>
	 * Populated only after the object has been added to a {@link MetadataDescriptorSet}).
	 * 
	 * @return
	 */
	public int getPosition() {
		return position;
	}


	private final String name;
	public String getName() {
		return name;
	}
	
    private final String description;
    private String getDescription() {
        return description;
    }


	private final MetadataDescriptorKind metadataDescriptorKind;
	public MetadataDescriptorKind getKind() {
		return metadataDescriptorKind;
	}

	private final IEvaluator evaluator;
	public IEvaluator getEvaluator() {
		return evaluator;
	}
	
	private final IModifier modifier;
	public boolean isModifiable() {
		return modifier != null;
	}

	private final IEvaluatedLabelProvider labelProvider;
	/**
	 * Returns the labelProvider supplied in the constructor,o or if <tt>null</tt>
	 * then the default label provider (as per {@link #getDefaultLabelProvider()})
	 * provided by {@link #init(int, Table, IEvaluatedLabelProvider)}.
	 * 
	 * <p>
	 * For this reason, should only be called after the object has been added to a 
	 * {@link MetadataDescriptorSet}).
	 * 
	 * @param <T>
	 * @return
	 */
	public IEvaluatedLabelProvider getLabelProvider() {
		return labelProvider != null? labelProvider: getDefaultLabelProvider();
	}

	private Table table;
	/**
	 * Populated only after the object has been added to a {@link MetadataDescriptorSet}).
	 * 
	 * @param <T>
	 * @return
	 */
	public Table getTable() {
		return table;
	}
	private CellEditor cellEditor;
	/**
	 * Populated only after the object has been added to a {@link MetadataDescriptorSet}).
	 * 
	 * @param <T>
	 * @return
	 */
	public <T extends CellEditor> T getCellEditor() {
		return Generics.asT(cellEditor);
	}


	private IEvaluatedLabelProvider defaultLabelProvider;
	/**
	 * Populated only after the object has been added to a {@link MetadataDescriptorSet}).
	 * 
	 * @param <T>
	 * @return
	 */
	public IEvaluatedLabelProvider getDefaultLabelProvider() {
		return defaultLabelProvider;
	}

	private TableColumn setUpTableColumn() {
		TableColumn tc = new TableColumn(getTable(), getKind().getStyle());
		tc.setText(getName());
        tc.setToolTipText(getDescription());
        if (columnLength != 0) {
            tc.setWidth(columnLength);
        } else {
            tc.setWidth(getKind().getColumnLength());
        }
		return tc;
	}

    private String regex;
    /**
     * A regex to be verified during verification (ie, as each character
     * is typed).
     * 
     * <p>
     * Only applies to {@link MetadataDescriptorKind} which set up a
     * {@link StringCellEditor} (or subclass).
     * 
     * @param regex
     */
	public void setVerifyRegex(String regex) {
		this.regex = regex;
	}

    private ICellEditorValidator validator;
    /**
     * Install an cell validator (possibly in addition to a
     * verifyListener.
     *
     * @see #setVerifyRegex(String)
     * 
     * @param validator
     */
    protected void setValidator(ICellEditorValidator validator) {
        this.validator = validator;
    }

    private int columnLength;
    /**
     * Override the default column length associated with the {@link MetadataDescriptorKind}.
     * @param length
     */
    protected void setLength(int length) {
        this.columnLength = length;
    }



	public Object evaluate(Object object) {
		if (evaluator == null) {
			return null;
		}
		return evaluator.evaluate(object);
	}

	public void modify(Object object, Object value) {
		if (modifier == null) {
			return;
		}
		modifier.modify(object, value);
	}


	/**
	 * Returns the value as a string, except for booleans which just return an empty
	 * string (since we have an icon for them).
	 * 
	 * @param object
	 * @return
	 */
	public String getTextFor(Object object) {
		return getLabelProvider().getTextFor(evaluator, object);
	}

	public Image getImageFor(Object object) {
		return getLabelProvider().getImageFor(evaluator, object);
	}

    public boolean isApplicable(NakedObjectMember nom) {
        return true;
    }

    public boolean isApplicable(NakedObjectProperty nop) {
        return true;
    }

    public boolean isApplicable(NakedObjectCollection noc) {
        return true;
    }

    public boolean isApplicable(NakedObjectAction noa) {
        return true;
    }

    public boolean isApplicable(NakedObjectActionParameter noap) {
        return true;
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
