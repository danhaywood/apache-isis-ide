package com.halware.nakedide.eclipse.ext.annot.ast;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

import com.halware.nakedide.eclipse.ext.annot.common.AbstractNode;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluatorAndModifier;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorKind;


public class MarkerAnnotationEvaluatorAndModifier 
		extends AbstractAnnotationEvaluatorOrModifier 
		implements IEvaluatorAndModifier {
	
    private static final Logger LOGGER = Logger.getLogger(MarkerAnnotationEvaluatorAndModifier.class);
    public Logger getLOGGER() {
        return LOGGER;
    }

	public MarkerAnnotationEvaluatorAndModifier(Class annotationClass) {
		super(annotationClass, MetadataDescriptorKind.BOOLEAN);
	}

	public MarkerAnnotationEvaluatorAndModifier(String annotationFullyQualifiedName) {
		super(annotationFullyQualifiedName, MetadataDescriptorKind.BOOLEAN);
	}

	public Object evaluate(Object object) {
		AbstractNode node = (AbstractNode)object;
		return isPresent(node);
	}
	
	public boolean isPresent(Object object) {
		AbstractNode node = (AbstractNode)object;
		return annotation(node) != null;
	}

	public void modify(Object object, Object value) {
		AbstractNode node = (AbstractNode)object;
		
		if (!(value instanceof Boolean)) {
			return;
		}
		Boolean present = (Boolean)value;

		try {
	
			if (present.booleanValue()) {
				addMarkerAnnotation(node);
			} else {
				removeAnnotation(node);
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
