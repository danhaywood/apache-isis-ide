package com.halware.nakedide.eclipse.ext.annot.ast;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.text.edits.MalformedTreeException;

import com.halware.nakedide.eclipse.ext.annot.common.AbstractNode;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluatorAndModifier;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorKind;


/**
 * Maintains annotations in the form <tt>Named("Customer")</tt> and
 * also <tt>Named(value="Customer")</tt>.
 * 
 * <p>
 * In the AST, the first form is represented as a {@link SingleMemberAnnotation} and
 * the second as a {@link NormalAnnotation}.  This evaluator/modifier deals with
 * both forms transparently.
 * 
 * <p>
 * When modifying, if the supplied value is null or is an empty STRING, then
 * the annotation is removed.  Equally, if a non-null value/non-empty STRING is
 * provided and there is no annotation, then it will be automatically added
 * (as a {@link SingleMemberAnnotation}). 
 * 
 * <p>
 * Annotations with multiple elements can be maintained using either
 * {@link MultipleValueAnnotationEvaluatorAndModifier} or 
 * {@link MultipleValueAnnotationSingleMemberEvaluatorAndModifier}.
 */
public class SingleValueAnnotationEvaluatorAndModifier 
	extends AbstractAnnotationEvaluatorOrModifier
	implements IEvaluatorAndModifier {
	
    private static final Logger LOGGER = Logger.getLogger(SingleValueAnnotationEvaluatorAndModifier.class);
    public Logger getLOGGER() {
        return LOGGER;
    }

	public SingleValueAnnotationEvaluatorAndModifier(Class annotationClass, MetadataDescriptorKind<? extends CellEditor> kind) {
		super(annotationClass, kind);
	}

	public SingleValueAnnotationEvaluatorAndModifier(String annotationFullyQualifiedName, MetadataDescriptorKind<? extends CellEditor> kind) {
        super(annotationFullyQualifiedName, kind);
    }

    public Object evaluate(Object object) {
        return evaluateMemberAsExpression(object, "value");
    }
    
    
	public void modify(Object object, Object value) {
		if (!(object instanceof AbstractNode)) {
			return;
		}
		AbstractNode node = (AbstractNode) object;
		Annotation annotation = annotation(node);
		if (  annotation != null && 
            !(annotation.isSingleMemberAnnotation() ||
              annotation.isMarkerAnnotation()          )) {
			return;
		}
		
		try {
			if (value == null || isEmptyString(value)) {
				if (annotation != null) {
					removeAnnotation(node);
				}
			} else {
				if (annotation == null) {
					addSingleMemberAnnotation(node, value);
				} else {
					updateSingleMemberAnnotation(node, value);
				}
			}
		} catch (JavaModelException e) {
            getLOGGER().error(e);
		} catch (MalformedTreeException e) {
            getLOGGER().error(e);
		} catch (BadLocationException e) {
            getLOGGER().error(e);
		} catch (CoreException e) {
            getLOGGER().error(e);
		}
	}

	private boolean isEmptyString(Object value) {
		return value != null &&
		       value instanceof String && ((String)value).length() == 0;
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
