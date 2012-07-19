package com.halware.nakedide.eclipse.ext.annot.ast;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

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
 * Maintains annotations in the form <tt>MultiLine(numberOfLines=5, wrapped=true)</tt>, or
 * <tt>FooBar(widget="Xyz")</tt> (corresponding to {@link NormalAnnotation}).
 * 
 * <p>
 * Note that {@link MultipleValueAnnotationSingleMemberEvaluatorAndModifier} is similar,
 * but maintains only a single member of such annotations.
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
public class MultipleValueAnnotationEvaluatorAndModifier 
	extends AbstractAnnotationEvaluatorOrModifier
	implements IEvaluatorAndModifier {
    
    private static final Logger LOGGER = Logger.getLogger(MultipleValueAnnotationEvaluatorAndModifier.class);
    public Logger getLOGGER() {
        return LOGGER;
    }
    
	
	public MultipleValueAnnotationEvaluatorAndModifier(Class annotationClass, String[] memberNames, MetadataDescriptorKind<? extends CellEditor> kind) {
		this(annotationClass, Arrays.asList(memberNames), kind);
	}

	public MultipleValueAnnotationEvaluatorAndModifier(Class annotationClass, List<String> memberNames, MetadataDescriptorKind<? extends CellEditor> kind) {
		this(annotationClass.getCanonicalName(), memberNames, kind);
	}

	public MultipleValueAnnotationEvaluatorAndModifier(String annotationFullyQualifiedName, String[] memberNames, MetadataDescriptorKind<? extends CellEditor> kind) {
		this(annotationFullyQualifiedName, Arrays.asList(memberNames), kind);
	}

	public MultipleValueAnnotationEvaluatorAndModifier(String annotationFullyQualifiedName, List<String> memberNames, MetadataDescriptorKind<? extends CellEditor> kind) {
		super(annotationFullyQualifiedName, kind);
		this.memberNames = Collections.unmodifiableList(memberNames);
	}

    /**
     * TODO: should be able to remove, and instead use {@link ASTTools#memberValues(NormalAnnotation)} ??
     */
    private final List<String> memberNames;
    public List<String> getMemberNames() {
        return memberNames;
    }

	/**
	 * @return a <tt>LinkedHashMap&lt;String, Object></tt> keyed by member names, 
	 *         (possibly with a value of <tt>null</tt> if there is no such member), or
	 *         a simple <tt>null</tt> if there is no annotation.
	 */
	public Object evaluate(Object object) {
		AbstractNode node = (AbstractNode)object;
		Annotation annotation = annotation(node);
		if (annotation == null) {
			return null;
		}
		LinkedHashMap<String, Object> elementValues = memberValuesOf(object, annotation);
		return elementValues;
	}


    private LinkedHashMap<String, Object> memberValuesOf(Object object, Annotation annotation) {
        LinkedHashMap<String, Object> elementValues = new LinkedHashMap<String, Object>();
		for(String memberName: memberNames) {
            Object value = evaluateMemberAsExpression(object, memberName);
			elementValues.put(memberName, value);
		}
        return elementValues;
    }

    
	
	/**
	 * @param value - a LinkedHashMap<String,Object>.
	 */
	public void modify(Object object, Object value) {
		AbstractNode node = (AbstractNode)object;
		Annotation annotation = annotation(node);
		if (annotation != null && !annotation.isNormalAnnotation()) {
			return;
		}
		LinkedHashMap<String, Object> elementValues = Generics.asT(value);
		
		try {
			if (value == null) {
				if (annotation != null) {
					removeAnnotation(node);
				}
			} else {
				if (annotation == null) {
					addNormalAnnotation(node, elementValues);
				} else {
					updateNormalAnnotation(node, elementValues);
				}
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
