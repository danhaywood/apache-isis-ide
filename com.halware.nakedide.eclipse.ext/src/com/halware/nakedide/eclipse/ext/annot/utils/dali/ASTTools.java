package com.halware.nakedide.eclipse.ext.annot.utils.dali;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;

import com.halware.eclipseutil.util.Generics;
import com.halware.nakedide.eclipse.ext.annot.utils.AstUtils;

/**
 * The methods in this class that modify annotations must be used within a
 * Member.editAnnotation() call. No modifications will appear in the java code
 * if these methods are called outside of that context.
 *
 * <p>
 * From package org.eclipse.jst.jpa.internal.utility;
 */
public class ASTTools {

	/**
	 * Convenience method on AST. Creates a StringLiteral and calls setLiteralValue(STRING)
	 */
	public static StringLiteral newStringLiteral(AST ast, String literalValue) {
		StringLiteral stringLiteral = ast.newStringLiteral();
		stringLiteral.setLiteralValue(literalValue);
		return stringLiteral;
	}
	
	public static BooleanLiteral newBooleanLiteral(AST ast, boolean booleanValue) {
		BooleanLiteral booleanLiteral = ast.newBooleanLiteral(booleanValue);
		return booleanLiteral;
	}
	
	/**
	 * Convenience method on AST to create a new NormalAnnotation with the given annotation name.
	 * If annotationName is "Table", this will result in  "@Table()" in the java source when it is added 
	 * to a BodyDeclaration. See addAnnotation(BodyDeclaration, Annotation)
	 */
	public static NormalAnnotation newNormalAnnotation(AST ast, String annotationName) {
		NormalAnnotation annotation = ast.newNormalAnnotation();
		annotation.setTypeName(ast.newSimpleName(annotationName));
		return annotation;
	}
	
	/**
	 * Convenience method on AST to create a new MarkerAnnotation with the given annotation name.
	 * If annotationName is "Table" this will result in "@Table" in the java source when it is added 
	 * to a BodyDeclaration.  See addAnnotation(BodyDeclaration, Annotation)
	 */
	public static MarkerAnnotation newMarkerAnnotation(AST ast, String annotationName) {
		MarkerAnnotation annotation = ast.newMarkerAnnotation();
		annotation.setTypeName(ast.newSimpleName(annotationName));
		return annotation;
	}
	
	/**
	 * Convenience method on AST to create a new MarkerAnnotation with the given annotation name.
	 * If annotationName is "Table" this will result in "@Table()" in the java source when it is added 
	 * to a BodyDeclaration.  See addAnnotation(BodyDeclaration, Annotation)
	 */
	public static SingleMemberAnnotation newSingleMemberAnnotation(AST ast, String annotationName) {
		SingleMemberAnnotation annotation = ast.newSingleMemberAnnotation();
		annotation.setTypeName(ast.newSimpleName(annotationName));
		return annotation;
	}
	
	/**
	 * Return whether the given BodyDeclaration contains any 
	 * annotation of the given annotationNames.  If it contains even 1 true will be returned.
	 * If it contains none false will be returned
	 */
	public static boolean containsAnyAnnotation(BodyDeclaration bodyDeclaration, String[] annotationNames) {
		for (Iterator stream = bodyDeclaration.modifiers().iterator(); stream.hasNext(); ) {
			IExtendedModifier modifier = (IExtendedModifier) stream.next();
			if (modifier.isAnnotation()) {
				if (CollectionTools.contains(annotationNames, ((Annotation) modifier).getTypeName().getFullyQualifiedName())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Return the annotation with the annotationName from the given BodyDeclaration.
	 * Return null if no annotation exists on the BodyDeclaration with the name.
	 */
	public static Annotation annotation(BodyDeclaration bodyDeclaration, String annotationName) {
		for (Iterator stream = bodyDeclaration.modifiers().iterator(); stream.hasNext(); ) {
			IExtendedModifier modifier = (IExtendedModifier) stream.next();
			if (modifier.isAnnotation()) {
				if (((Annotation) modifier).getTypeName().getFullyQualifiedName().equals(annotationName)) {
					return (Annotation) modifier;
				}
			}
		}
		return null;
	}
	
	/**
	 * Return the annotation with the annotationName from the given SingleVariableDeclaration.
	 * Return null if no annotation exists on the SingleVariableDeclaration with the name.
	 */
	public static Annotation annotation(SingleVariableDeclaration singleVariableDeclaration, String annotationName) {
		for (Iterator stream = singleVariableDeclaration.modifiers().iterator(); stream.hasNext(); ) {
			IExtendedModifier modifier = (IExtendedModifier) stream.next();
			if (modifier.isAnnotation()) {
				if (((Annotation) modifier).getTypeName().getFullyQualifiedName().equals(annotationName)) {
					return (Annotation) modifier;
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static void addValuePair(NormalAnnotation annotation, MemberValuePair valuePair) {
		List list = (List) annotation.getStructuralProperty(NormalAnnotation.VALUES_PROPERTY);
		list.add(valuePair);
	}
	
	@SuppressWarnings("unchecked")
	public static void addExpressionToArrayInitializer(ArrayInitializer arrayInitializer, Expression expression) {
		List list = (List) arrayInitializer.getStructuralProperty(ArrayInitializer.EXPRESSIONS_PROPERTY);
		list.add(expression);
	}

	@SuppressWarnings("unchecked")
	public static void addLiteralMemberValuePair(NormalAnnotation annotation, String name, String value) {
		AST ast = annotation.getAST();
		
		List list = (List) annotation.getStructuralProperty(NormalAnnotation.VALUES_PROPERTY);
		MemberValuePair newValuePair = ast.newMemberValuePair();
		newValuePair.setName(ast.newSimpleName(name));
		newValuePair.setValue(ASTTools.newStringLiteral(ast, value));
		list.add(newValuePair);
	}

	public static MarkerAnnotation addAnnotation(BodyDeclaration bodyDeclaration, String annotationName) {
		return addAnnotation(bodyDeclaration, newMarkerAnnotation(bodyDeclaration.getAST(), annotationName));
	}

	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T addAnnotation(BodyDeclaration bodyDeclaration, T annotation) {
		List list = (List) bodyDeclaration.getStructuralProperty(bodyDeclaration.getModifiersProperty());
		list.add(annotationLocation(bodyDeclaration), annotation);
		return annotation;
	}
	
	
	public static void removeAnnotation(BodyDeclaration bodyDeclaration, String annotationName) {
		removeAnnotation(bodyDeclaration, annotation(bodyDeclaration, annotationName));
	}

	
	public static void removeAnnotation(BodyDeclaration bodyDeclaration, Annotation annotation) {
		List list = (List) bodyDeclaration.getStructuralProperty(bodyDeclaration.getModifiersProperty());
		list.remove(annotation);
	}
	
	public static void removeAnnotationElement(BodyDeclaration bodyDeclaration, String annotationName, String elementName) {
		Annotation annotation = annotation(bodyDeclaration, annotationName);
		removeAnnotationElement(bodyDeclaration, annotation, elementName);
	}
	
	@SuppressWarnings("unchecked")
	public static void removeAnnotationElement(BodyDeclaration bodyDeclaration, Annotation annotation, String elementName) {
		MemberValuePair valuePairToRemove = memberValuePair(annotation, elementName);
		
		if (valuePairToRemove != null) {
			if (((NormalAnnotation) annotation).values().size() == 1) {
				MarkerAnnotation newAnnotation = newMarkerAnnotation(bodyDeclaration.getAST(), annotation.getTypeName().getFullyQualifiedName());
				
				List list = (List) bodyDeclaration.getStructuralProperty(bodyDeclaration.getModifiersProperty());
				int index = list.indexOf(annotation);
				list.set(index, newAnnotation);						
			}
			else {
				removeValuePair((NormalAnnotation) annotation, valuePairToRemove);
			}
		}
	}
	
	public static void removeValuePair(NormalAnnotation annotation, String valuePairName) {
		removeValuePair(annotation, memberValuePair(annotation, valuePairName));
	}

	public static void removeValuePair(NormalAnnotation annotation, MemberValuePair valuePair) {
		List list = (List) annotation.getStructuralProperty(NormalAnnotation.VALUES_PROPERTY);
		list.remove(valuePair);	
	}
	
	public static void removeFromArrayInitializer(ArrayInitializer arrayInitializer, int position) {
		List list = (List) arrayInitializer.getStructuralProperty(ArrayInitializer.EXPRESSIONS_PROPERTY);
		list.remove(position);
	}
	
	public static void removeFromArrayInitializer(ArrayInitializer arrayInitializer, Object object) {
		List list = (List) arrayInitializer.getStructuralProperty(ArrayInitializer.EXPRESSIONS_PROPERTY);
		list.remove(object);
	}

	public static int arrayInitializerSize(ArrayInitializer arrayInitializer) {
		return ((List) arrayInitializer.getStructuralProperty(ArrayInitializer.EXPRESSIONS_PROPERTY)).size();
	}
	
	@SuppressWarnings("unchecked")
	public static void replaceAnnotation(BodyDeclaration bodyDeclaration, Annotation oldAnnotation, Annotation newAnnotation) {
		List list = (List) bodyDeclaration.getStructuralProperty(bodyDeclaration.getModifiersProperty());
		int index = list.indexOf(oldAnnotation);
		list.set(index, newAnnotation);
	}
	
	public static MemberValuePair memberValuePair(BodyDeclaration bodyDeclaration, String annotationName, String name) {
		return memberValuePair(annotation(bodyDeclaration, annotationName), name);
	}	
	
	public static MemberValuePair memberValuePair(Annotation annotation, String elementName) {
		if ((annotation != null) && annotation.isNormalAnnotation()) {
			return memberValuePair((NormalAnnotation) annotation, elementName);
		}
		return null;
	}

	public static MemberValuePair memberValuePair(NormalAnnotation annotation, String elementName) {
		for (Iterator stream = annotation.values().iterator(); stream.hasNext(); ) {
			MemberValuePair valuePair = (MemberValuePair) stream.next();
			if (valuePair.getName().getFullyQualifiedName().equals(elementName)) {
				return valuePair;
			}
		}
		return null;
	}

	public static boolean containsAnnotationElement(BodyDeclaration bodyDeclaration, String annotationName, String elementName) {
		Annotation annotation = annotation(bodyDeclaration, annotationName);
		if (elementName.equals("value") && annotation.isSingleMemberAnnotation()) {
			return ((SingleMemberAnnotation) annotation).getValue() != null;
		}
		return memberValuePair(annotation, elementName) != null;
	}

	/**
	 * Given a bodyDeclaration, annotationName, and elementName, this will return the STRING value
	 * if the element value is a StringLiteral.  Given @Table(name="foo") annotationName is "Table",
	 * elementName is "name", "foo" is returned.
	 */
	public static String annotationElementStringValue(BodyDeclaration bodyDeclaration, String annotationName, String elementName) {
		return stringValue(annotationElement(bodyDeclaration, annotationName, elementName));
	}

	public static Expression annotationElement(BodyDeclaration bodyDeclaration, String annotationName, String elementName) {
		return member(annotation(bodyDeclaration, annotationName), elementName);
	}

	public static String memberStringValue(Annotation annotation, String memberName) {
		return stringValue(member(annotation, memberName));
	}

	public static Expression member(Annotation annotation, String memberName) {
		if (memberName.equals("value") && annotation.isSingleMemberAnnotation()) {
			return ((SingleMemberAnnotation) annotation).getValue();
		}
		MemberValuePair valuePair = memberValuePair(annotation, memberName);
		return (valuePair == null) ? null : valuePair.getValue();
	}

    public static LinkedHashMap<String, Object> memberValues(NormalAnnotation annotation) {
        LinkedHashMap<String, Object> memberValues = new LinkedHashMap<String, Object>();
        if (annotation == null) {
            return memberValues;
        }
        List<MemberValuePair> memberValuePairs = Generics.asT(annotation.values());
        for(MemberValuePair mvp: memberValuePairs) {
            memberValues.put(
                mvp.getName().getFullyQualifiedName(), AstUtils.getValue(mvp.getValue()));
        }
        return memberValues;
    }

	/**
	 * Given an annotation MemberValuePair name="foo", this method will return "foo"
	 * If the given MemberValuePair value is not a StringLiteral, it will return null.
	 */
	public static String stringValue(MemberValuePair valuePair) {
		return stringValue(valuePair.getValue());
	}

	public static String stringValue(Expression expression) {
		if (expression.getNodeType() == ASTNode.STRING_LITERAL) {
			return ((StringLiteral) expression).getLiteralValue();
		}
		return null;
	}
	
	public static Boolean booleanValue(MemberValuePair valuePair) {
		return booleanValue(valuePair.getValue());
	}

	public static Boolean booleanValue(Expression expression) {
		if (expression.getNodeType() == ASTNode.BOOLEAN_LITERAL) {
			return Boolean.valueOf(((BooleanLiteral) expression).booleanValue());
		}
		return null;
	}

	/**
	 * Return the ITextRange of an ASTNode
	 * @param astNode
	 * @return
	 */
	public static ITextRange textRange(final ASTNode astNode) {
		if (astNode == null) {
			throw new IllegalArgumentException("Cannot get the ITextRange of a null ASTNode");
		}
		return new ITextRange() {
			public int getLineNumber() {
				return ((CompilationUnit) astNode.getRoot()).getLineNumber(getOffset());
			}
		
			public int getOffset() {
				return astNode.getStartPosition();
			}
		
			public int getLength() {
				return astNode.getLength();
			}
		};
	}
	
	/**
	 * Returns the index of the first modifier that is not an annotation
	 * This assumes that annotations belong before modifiers, though
	 * technically they can be interspersed with modifiers.
	 */
	public static int annotationLocation(BodyDeclaration bodyDeclaration) {
		int count = 0;
		for (Iterator i = bodyDeclaration.modifiers().listIterator(); i.hasNext();) {
			IExtendedModifier modifier = (IExtendedModifier) i.next();
			if (modifier.isModifier()) {
				break;
			}
			count++;
		}
		return count;
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
