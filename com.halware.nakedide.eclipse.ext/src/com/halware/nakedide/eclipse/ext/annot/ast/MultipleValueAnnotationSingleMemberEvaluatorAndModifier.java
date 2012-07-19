package com.halware.nakedide.eclipse.ext.annot.ast;

import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.text.edits.MalformedTreeException;

import com.halware.eclipseutil.util.Generics;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractNode;
import com.halware.nakedide.eclipse.ext.annot.mdd.IEvaluatorAndModifier;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorKind;
import com.halware.nakedide.eclipse.ext.annot.utils.dali.ASTTools;


/**
 * Maintains a single member of annotations that take the form <tt>MultiLine(numberOfLines=5, wrapped=true)</tt>, or
 * <tt>FooBar(widget="Xyz")</tt> (corresponding to {@link NormalAnnotation}).
 * 
 * <p>
 * Note that {@link MultipleValueAnnotationEvaluatorAndModifier} is capable of maintaining 
 * <i>all</i> the elements of such an annotation, but requires some mechanism to distinguish
 * the member values.
 *
 * <p>
 * This class is also suitable for annotations that have a single member that 
 * <i>isn't</i> called <tt>value</tt>.
 * 
 * <p>
 * Annotations that take only a single value with the member named <tt>value</tt>
 * are maintained using {@link SingleValueAnnotationEvaluatorAndModifier}.  For example,
 * both <tt>Named(value="Customer")</tt> and just <tt>Named("Customer")</tt> would be
 * maintained using {@link SingleValueAnnotationEvaluatorAndModifier}.  (Internally
 * in the AST the former is actually a {@link NormalAnnotation}), which is why this 
 * class names isn't called NormalAnnotationEvaluatorAndModifier).
 * 
 * <p>
 * When modifying, if all supplied valued are null or an empty String, then
 * the annotation is removed.  Equally, if any of the supplied values are 
 * non-null/non-empty String and there is no annotation, then it will be 
 * automatically added (as a {@link NormalAnnotation}). 
 *  
 */
public class MultipleValueAnnotationSingleMemberEvaluatorAndModifier 
	extends AbstractAnnotationEvaluatorOrModifier
	implements IEvaluatorAndModifier {
	
    private static final Logger LOGGER = Logger.getLogger(MultipleValueAnnotationSingleMemberEvaluatorAndModifier.class);
    public Logger getLOGGER() {
        return LOGGER;
    }

    public MultipleValueAnnotationSingleMemberEvaluatorAndModifier(Class annotationClass, String memberName, MetadataDescriptorKind<? extends CellEditor> kind) {
        this(annotationClass.getCanonicalName(), memberName, kind);
    }

    public MultipleValueAnnotationSingleMemberEvaluatorAndModifier(String annotationFullyQualifiedName, String memberName, MetadataDescriptorKind<? extends CellEditor> kind) {
        super(annotationFullyQualifiedName, kind);
        this.memberName = memberName;
    }

    private final String memberName;
    public String getMemberName() {
        return memberName;
    }

	/**
	 * @return the object, possibly with a value of <tt>null</tt> if there is no 
     *         such member), or
	 *         a simple <tt>null</tt> if there is no annotation.
	 */
	public Object evaluate(Object object) {
        return evaluateMemberAsExpression(object, memberName);
	}

    /**
	 * @param value - an Object
	 */
	public void modify(Object object, Object value) {
		AbstractNode node = (AbstractNode)object;
		Annotation annotation = annotation(node);
		if (annotation != null && !annotation.isNormalAnnotation()) {
			return;
		}
        
        // get existing members as map (if any), and update
        NormalAnnotation normalAnnotation = (NormalAnnotation)annotation;
        LinkedHashMap<String, Object> memberValues = ASTTools.memberValues(normalAnnotation);
        if (value != null) {
            memberValues.put(memberName, Generics.asT(value));
        } else {
            memberValues.remove(memberName);
        }
        
		try {
			if (memberValues.size() == 0) {
				if (annotation != null) {
					removeAnnotation(node);
				}
			} else {
				if (annotation == null) {
					addNormalAnnotation(node, memberValues);
				} else {
					updateNormalAnnotation(node, memberValues);
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
