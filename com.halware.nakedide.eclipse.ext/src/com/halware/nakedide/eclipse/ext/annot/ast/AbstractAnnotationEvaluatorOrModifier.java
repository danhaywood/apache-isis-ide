package com.halware.nakedide.eclipse.ext.annot.ast;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import com.halware.eclipseutil.util.Generics;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractNode;
import com.halware.nakedide.eclipse.ext.annot.mdd.AbstractModifier;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorKind;
import com.halware.nakedide.eclipse.ext.annot.utils.AstUtils;
import com.halware.nakedide.eclipse.ext.annot.utils.AstUtils.QualifiedNameValue;
import com.halware.nakedide.eclipse.ext.annot.utils.dali.ASTTools;

public abstract class AbstractAnnotationEvaluatorOrModifier extends AbstractModifier {

    public abstract Logger getLOGGER();

	public AbstractAnnotationEvaluatorOrModifier(Class annotationClass, MetadataDescriptorKind<? extends CellEditor> kind) {
		this(annotationClass.getCanonicalName(), kind);
	}

	public AbstractAnnotationEvaluatorOrModifier(String annotationFullyQualifiedName, MetadataDescriptorKind<? extends CellEditor> kind) {
		this.annotationFullyQualifiedName = annotationFullyQualifiedName;
        this.kind = kind;
	}

	private final String annotationFullyQualifiedName;
	public String getAnnotationFullyQualifiedName() {
		return annotationFullyQualifiedName;
	}
	public String getAnnotationUnqualifiedName() {
		return AstUtils.unqualified(getAnnotationFullyQualifiedName());
	}

    private MetadataDescriptorKind<? extends CellEditor> kind;
    public MetadataDescriptorKind<? extends CellEditor> getKind() {
        return kind;
    }
    
    /**
     * Alters behaviour for evaluating boolean members.
     */
    public boolean isBooleanMember() {
        return kind == MetadataDescriptorKind.BOOLEAN;
    }
    
    protected Object nullForThisKind() {
        if (isBooleanMember()) return false;
        return null;
    }

    protected Object valueForThisKind(Object value) {
        if (isBooleanMember()) {
            return value!=null?value:false;
        }
        return value;
    }

    /**
     * @return the object, possibly with a value of <tt>null</tt> if there is no 
     *         such member), or
     *         a simple <tt>null</tt> if there is no annotation.
     */
    protected Object evaluateMemberAsExpression(Object object, String memberName) {
        AbstractNode node = (AbstractNode)object;
        Annotation annotation = annotation(node);
        if (annotation == null) {
            return nullForThisKind();
        }
        if (annotation instanceof MarkerAnnotation && "value".equals(memberName)) {
            return QualifiedNameValue.DEFAULT_VALUE;
        }
        Expression expression = ASTTools.member(annotation, memberName);
        if (expression == null) {
            return nullForThisKind();
        }
        Object value = AstUtils.getValue(expression);
        return valueForThisKind(value);
    }
	
	///////////// MarkerAnnotation ////////////////

	protected void addMarkerAnnotation(AbstractNode node) throws MalformedTreeException, BadLocationException, CoreException {
		addMarkerAnnotation(node.getCompilationUnit(), node.getDeclaration());
	}

	private void addMarkerAnnotation(ICompilationUnit compilationUnit, ASTNode declaration) throws MalformedTreeException, BadLocationException, CoreException {
		if (declaration instanceof BodyDeclaration) {
			addMarkerAnnotation(compilationUnit, (BodyDeclaration)declaration);
		}
		else if (declaration instanceof SingleVariableDeclaration) {
			addMarkerAnnotation(compilationUnit, (SingleVariableDeclaration)declaration);
		}
	}

	private void addMarkerAnnotation(ICompilationUnit compilationUnit, BodyDeclaration declaration) throws MalformedTreeException, BadLocationException, CoreException {
		Annotation annotation = 
			ASTTools.annotation(declaration, getAnnotationUnqualifiedName());
		if (annotation != null) {
			return;
		}
		
		AST ast = declaration.getAST();
		
		annotation = AstUtils.createMarkerAnnotation(
			ast, getAnnotationFullyQualifiedName());
		annotation.setSourceRange(
				AstUtils.calculateOffset(declaration, true).offset, 
				annotation.getLength());
		
		ImportDeclaration importDeclaration = 
			AstUtils.createImportStatement(ast, getAnnotationFullyQualifiedName());
		
		AstUtils.rewriteAddFirst(compilationUnit, declaration, annotation, importDeclaration);
	}
	
	private void addMarkerAnnotation(ICompilationUnit compilationUnit, SingleVariableDeclaration declaration) throws MalformedTreeException, BadLocationException, CoreException {
		Annotation annotation = 
			ASTTools.annotation(declaration, getAnnotationUnqualifiedName());
		if (annotation != null) {
			return;
		}
		
		AST ast = declaration.getAST();
		
		annotation = AstUtils.createMarkerAnnotation(
			ast, getAnnotationFullyQualifiedName());
		annotation.setSourceRange(
				AstUtils.calculateOffset(declaration).offset, 
				annotation.getLength());
		
		ImportDeclaration importDeclaration = 
			AstUtils.createImportStatement(ast, getAnnotationFullyQualifiedName());
		
		AstUtils.rewriteAddFirst(compilationUnit, declaration, annotation, importDeclaration);
	}

	///////////// SingleMemberAnnotation ////////////////

	protected void addSingleMemberAnnotation(AbstractNode node, Object value) throws MalformedTreeException, BadLocationException, CoreException {
		addSingleMemberAnnotation(node.getCompilationUnit(), node.getDeclaration(), value);
	}

	private void addSingleMemberAnnotation(
			ICompilationUnit compilationUnit, 
			ASTNode declaration, Object value) throws MalformedTreeException, BadLocationException, CoreException {
		if (declaration instanceof BodyDeclaration) {
			addSingleMemberAnnotation(compilationUnit, (BodyDeclaration)declaration, value);
		}
		else if (declaration instanceof SingleVariableDeclaration) {
			addSingleMemberAnnotation(compilationUnit, (SingleVariableDeclaration)declaration, value);
		}
	}

	private void addSingleMemberAnnotation(
			ICompilationUnit compilationUnit, 
			BodyDeclaration declaration, Object value) throws MalformedTreeException, BadLocationException, CoreException {

		if (annotationExists(declaration)) {
			return;
		}
		AST ast = declaration.getAST();
		Annotation annotation = AstUtils.createSingleMemberAnnotation(
						ast, getAnnotationFullyQualifiedName(), value);
		annotation.setSourceRange(
				AstUtils.calculateOffset(declaration, true).offset, 
				annotation.getLength());
		
		ImportDeclaration importDeclaration = 
			AstUtils.createImportStatement(ast, getAnnotationFullyQualifiedName());
		
		AstUtils.rewriteAddFirst(compilationUnit, declaration, annotation, importDeclaration);
	}
	protected void addSingleMemberAnnotation(
			ICompilationUnit compilationUnit, 
			SingleVariableDeclaration declaration, Object value) throws MalformedTreeException, BadLocationException, CoreException {

		if (annotationExists(declaration)) {
			return;
		}
		AST ast = declaration.getAST();
		Annotation annotation = AstUtils.createSingleMemberAnnotation(
						ast, getAnnotationFullyQualifiedName(), value);
		annotation.setSourceRange(
				AstUtils.calculateOffset(declaration).offset, 
				annotation.getLength());
		
		ImportDeclaration importDeclaration = 
			AstUtils.createImportStatement(ast, getAnnotationFullyQualifiedName());
		
		AstUtils.rewriteAddFirst(compilationUnit, declaration, annotation, importDeclaration);
	}

	protected void updateSingleMemberAnnotation(
			AbstractNode node, Object value) throws JavaModelException, MalformedTreeException, BadLocationException {
		updateSingleMemberAnnotation(
			node.getCompilationUnit(), node.getDeclaration(), value);
	}

	private void updateSingleMemberAnnotation(
			ICompilationUnit compilationUnit, 
			ASTNode declaration, Object value) throws JavaModelException, MalformedTreeException, BadLocationException {
		if (declaration instanceof BodyDeclaration) {
			updateSingleMemberAnnotation(
				compilationUnit, (BodyDeclaration)declaration, value);
		}
		else if (declaration instanceof SingleVariableDeclaration) {
			updateSingleMemberAnnotation(
				compilationUnit, (SingleVariableDeclaration)declaration, value);
		}
	}

	private void updateSingleMemberAnnotation(
			ICompilationUnit compilationUnit, 
			BodyDeclaration declaration, Object value) throws JavaModelException, MalformedTreeException, BadLocationException {
		
		SingleMemberAnnotation singleMemberAnnotation = 
			annotation(declaration, SingleMemberAnnotation.class);
		if (singleMemberAnnotation != null) {
            AST ast = singleMemberAnnotation.getAST();
            if (value != QualifiedNameValue.DEFAULT_VALUE) {
                Expression replacement =  
                    value != null? AstUtils.createExpression(
                            ast, value): null;
        
                AstUtils.rewriteReplace(
                    compilationUnit, singleMemberAnnotation.getValue(), replacement);
                return;
            } else {
                MarkerAnnotation replacementAnnotation = ast.newMarkerAnnotation();
                replacementAnnotation.setTypeName(ast.newName(singleMemberAnnotation.getTypeName().getFullyQualifiedName()));
                AstUtils.rewriteReplace(
                    compilationUnit, singleMemberAnnotation, replacementAnnotation);
                return;
            }
        }
        MarkerAnnotation markerAnnotation = 
            annotation(declaration, MarkerAnnotation.class);
        if (markerAnnotation != null) {
            AST ast = markerAnnotation.getAST();
            if (value != QualifiedNameValue.DEFAULT_VALUE) {
                SingleMemberAnnotation replacementAnnotation = ast.newSingleMemberAnnotation();
                replacementAnnotation.setTypeName(ast.newName(markerAnnotation.getTypeName().getFullyQualifiedName()));
                Expression memberValueExpression =  
                    value != null? AstUtils.createExpression(
                            ast, value): null;
                replacementAnnotation.setValue(memberValueExpression);
                AstUtils.rewriteReplace(
                    compilationUnit, markerAnnotation, replacementAnnotation);
            } else {
                // nothing to do
            }
        }

	}
	private void updateSingleMemberAnnotation(
			ICompilationUnit compilationUnit, 
			SingleVariableDeclaration declaration, Object value) throws JavaModelException, MalformedTreeException, BadLocationException {
		
		SingleMemberAnnotation singleMemberAnnotation = 
			annotation(declaration, SingleMemberAnnotation.class);
		if (singleMemberAnnotation == null) {
			return;
		}

		Expression replacement =  
			value != null? AstUtils.createExpression(
					singleMemberAnnotation.getAST(), value): null;

		AstUtils.rewriteReplace(
			compilationUnit, singleMemberAnnotation.getValue(), replacement);
	}

	

	///////////// NormalAnnotation ////////////////
	
	protected void addNormalAnnotation(
			AbstractNode node, 
			Map<String, Object> memberValues) throws MalformedTreeException, BadLocationException, CoreException {
		addNormalAnnotation(
			node.getCompilationUnit(), node.getDeclaration(), memberValues);
	}

	private void addNormalAnnotation(
			ICompilationUnit compilationUnit, ASTNode declaration,
			Map<String, Object> memberValues) throws MalformedTreeException, BadLocationException, CoreException {
		if (declaration instanceof BodyDeclaration) {
			addNormalAnnotation(
				compilationUnit, (BodyDeclaration)declaration, memberValues);
		}
		else if (declaration instanceof SingleVariableDeclaration) {
			addNormalAnnotation(
				compilationUnit, (SingleVariableDeclaration)declaration, memberValues);
		}
	}

	private void addNormalAnnotation(
			ICompilationUnit compilationUnit, BodyDeclaration declaration,
			Map<String, Object> memberValues) throws MalformedTreeException, BadLocationException, CoreException {

		if (annotationExists(declaration)) {
			return;
		}

		AST ast = declaration.getAST();
		
		NormalAnnotation normalAnnotation = AstUtils.createNormalAnnotation(
						ast, getAnnotationFullyQualifiedName(), memberValues);
		normalAnnotation.setSourceRange(
				AstUtils.calculateOffset(declaration, true).offset, 
				normalAnnotation.getLength());

		ImportDeclaration importDeclaration = 
			AstUtils.createImportStatement(ast, getAnnotationFullyQualifiedName());

		
		String source = compilationUnit.getBuffer().getContents();
		Document document= new Document(source);
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(compilationUnit);
		
		ASTRewrite rewrite = ASTRewrite.create(declaration.getAST());
		
		ChildListPropertyDescriptor modifiersProperty = declaration.getModifiersProperty();
		
		ListRewrite listRewrite = rewrite.getListRewrite(declaration, modifiersProperty);
		listRewrite.insertFirst(normalAnnotation, null);

		maintainValuesProperty(normalAnnotation, memberValues, ast, rewrite);

		TextEdit importEdits = null;
		ImportRewrite importRewrite = ImportRewrite.create(compilationUnit, true);
		importRewrite.addImport(importDeclaration.getName().getFullyQualifiedName());
		importEdits = importRewrite.rewriteImports(null);
		
		Map options = compilationUnit.getJavaProject().getOptions(true);
		TextEdit edits = rewrite.rewriteAST(document, options);
		
		// computation of the new source code
		edits.apply(document);
		if (importEdits != null) {
			importEdits.apply(document);
		}
		String newSource = document.get();
		
		// update of the compilation unit
		compilationUnit.getBuffer().setContents(newSource);
	}
	private void addNormalAnnotation(
			ICompilationUnit compilationUnit, SingleVariableDeclaration declaration,
			Map<String, Object> memberValues) throws MalformedTreeException, BadLocationException, CoreException {

		if (annotationExists(declaration)) {
			return;
		}

		AST ast = declaration.getAST();
		
		NormalAnnotation normalAnnotation = AstUtils.createNormalAnnotation(
						ast, getAnnotationFullyQualifiedName(), memberValues);
		normalAnnotation.setSourceRange(
				AstUtils.calculateOffset(declaration).offset, 
				normalAnnotation.getLength());

		ImportDeclaration importDeclaration = 
			AstUtils.createImportStatement(ast, getAnnotationFullyQualifiedName());

		
		String source = compilationUnit.getBuffer().getContents();
		Document document= new Document(source);
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(compilationUnit);
		
		ASTRewrite rewrite = ASTRewrite.create(declaration.getAST());
		
		ListRewrite listRewrite = rewrite.getListRewrite(declaration, SingleVariableDeclaration.MODIFIERS2_PROPERTY);
		listRewrite.insertFirst(normalAnnotation, null);

		maintainValuesProperty(normalAnnotation, memberValues, ast, rewrite);

		TextEdit importEdits = null;
		ImportRewrite importRewrite = ImportRewrite.create(compilationUnit, true);
		importRewrite.addImport(importDeclaration.getName().getFullyQualifiedName());
		importEdits = importRewrite.rewriteImports(null);
		
		Map options = compilationUnit.getJavaProject().getOptions(true);
		TextEdit edits = rewrite.rewriteAST(document, options);
		
		// computation of the new source code
		edits.apply(document);
		if (importEdits != null) {
			importEdits.apply(document);
		}
		String newSource = document.get();
		
		// update of the compilation unit
		compilationUnit.getBuffer().setContents(newSource);
	}

	protected void updateNormalAnnotation(
			AbstractNode node, 
			Map<String, Object> memberValues) throws JavaModelException, MalformedTreeException, BadLocationException {
		updateNormalAnnotation(
			node.getCompilationUnit(), node.getDeclaration(), memberValues);
	}

	private void updateNormalAnnotation(
			ICompilationUnit compilationUnit, ASTNode declaration, 
			Map<String, Object> memberValues) throws JavaModelException, MalformedTreeException, BadLocationException {
		if (declaration instanceof BodyDeclaration) {
			updateNormalAnnotation(
				compilationUnit, (BodyDeclaration)declaration, memberValues);
		}
		else if (declaration instanceof SingleVariableDeclaration) {
			updateNormalAnnotation(
				compilationUnit, (SingleVariableDeclaration)declaration, memberValues);
		}
	}

	private void updateNormalAnnotation(
			ICompilationUnit compilationUnit, BodyDeclaration declaration, 
			Map<String, Object> memberValues) throws JavaModelException, MalformedTreeException, BadLocationException {

		NormalAnnotation normalAnnotation = 
			annotation(declaration, NormalAnnotation.class);
		if (normalAnnotation == null) {
			return;
		}

		String source = compilationUnit.getBuffer().getContents();
		Document document= new Document(source);
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(compilationUnit);

		AST ast = normalAnnotation.getAST();

		ASTRewrite rewrite = ASTRewrite.create(ast);

		maintainValuesProperty(normalAnnotation, memberValues, ast, rewrite);
		
		Map options = compilationUnit.getJavaProject().getOptions(true);
		TextEdit edits = rewrite.rewriteAST(document, options);
		
		// computation of the new source code
		edits.apply(document);
		String newSource = document.get();
		
		// update of the compilation unit
		compilationUnit.getBuffer().setContents(newSource);
	}
	private void updateNormalAnnotation(
			ICompilationUnit compilationUnit, SingleVariableDeclaration declaration, 
			Map<String, Object> memberValues) throws JavaModelException, MalformedTreeException, BadLocationException {

		NormalAnnotation normalAnnotation = 
			annotation(declaration, NormalAnnotation.class);
		if (normalAnnotation == null) {
			return;
		}

		String source = compilationUnit.getBuffer().getContents();
		Document document= new Document(source);
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(compilationUnit);

		AST ast = normalAnnotation.getAST();

		ASTRewrite rewrite = ASTRewrite.create(ast);

		maintainValuesProperty(normalAnnotation, memberValues, ast, rewrite);
		
		Map options = compilationUnit.getJavaProject().getOptions(true);
		TextEdit edits = rewrite.rewriteAST(document, options);
		
		// computation of the new source code
		edits.apply(document);
		String newSource = document.get();
		
		// update of the compilation unit
		compilationUnit.getBuffer().setContents(newSource);
	}
	
	/**
	 * Adds, updates or removes element for normal annotation, as required.
	 * 
	 * @param normalAnnotation
	 * @param newMemberValues
	 * @param ast
	 * @param rewrite
	 */
	private void maintainValuesProperty(
			NormalAnnotation normalAnnotation, Map<String, Object> newMemberValues, 
			AST ast, ASTRewrite rewrite) {
		ListRewrite listRewrite = rewrite.getListRewrite(normalAnnotation, NormalAnnotation.VALUES_PROPERTY);
        LinkedHashMap<String, Object> existingMemberValues = ASTTools.memberValues(normalAnnotation);
        
        Set<String> existingKeySet = new HashSet<String>(existingMemberValues.keySet());
        Set<String> newKeySet = new HashSet<String>(newMemberValues.keySet());
        
        Set<String> commonMembers = new HashSet<String>(existingKeySet);
        commonMembers.retainAll(newKeySet);
        
        Set<String> newMembers = new HashSet<String>(newKeySet);
        newMembers.removeAll(existingKeySet);
        
        Set<String> removedMembers = new HashSet<String>(existingKeySet);
        removedMembers.removeAll(newKeySet);
        
		for(String memberName: commonMembers) {
			Object value = newMemberValues.get(memberName);
			MemberValuePair memberValuePair = 
				ASTTools.memberValuePair(normalAnnotation, memberName);
			if (value != null) {
				MemberValuePair newValuePair = createMemberValuePair(ast, memberName, value);
				listRewrite.replace(memberValuePair, newValuePair, null);
			} else {
				listRewrite.remove(memberValuePair, null);
			}
		}

        for(String memberName: newMembers) {
            Object value = newMemberValues.get(memberName);
            if (value != null) {
                MemberValuePair newValuePair = createMemberValuePair(ast, memberName, value);
                // TODO: should attempt to preserve order
                listRewrite.insertLast(newValuePair, null);
            } else {
                // nothing to do
            }
        }

        for(String memberName: removedMembers) {
            MemberValuePair memberValuePair = 
                ASTTools.memberValuePair(normalAnnotation, memberName);
            listRewrite.remove(memberValuePair, null);
        }

        // TODO: earlier implementation; to remove
//        for(String memberName: commonMembers) {
//            Object value = newMemberValues.get(memberName);
//            MemberValuePair memberValuePair = 
//                ASTTools.memberValuePair(normalAnnotation, memberName);
//            if (value != null) {
//                MemberValuePair newValuePair = createMemberValuePair(ast, memberName, value);
//                if (memberValuePair == null) {
//                    // TODO: should attempt to preserve the order here.
//                    listRewrite.insertLast(newValuePair, null);
//                } else {
//                    listRewrite.replace(memberValuePair, newValuePair, null);
//                }
//            } else {
//                if (memberValuePair == null) {
//                    // nothing to do
//                } else {
//                    listRewrite.remove(memberValuePair, null);
//                }
//            }
//        }
    }

	private MemberValuePair createMemberValuePair(AST ast, String elementName, Object value) {
		MemberValuePair newValuePair = ast.newMemberValuePair();
		newValuePair.setName(ast.newSimpleName(elementName));
		newValuePair.setValue(AstUtils.createExpression(ast, value));
		return newValuePair;
	}


	///////////////// Helpers ////////////////


	protected Annotation annotation(AbstractNode node) {
		return annotation(node.getDeclaration());
	}

	protected Annotation annotation(ASTNode node) {
		if (node instanceof BodyDeclaration) {
			return annotation((BodyDeclaration)node);
		} 
		if (node instanceof SingleVariableDeclaration) {
			return annotation((SingleVariableDeclaration)node);
		}
		throw new IllegalArgumentException("Unsupported ASTNode - only BodyDeclaration and SingleVariableDeclaration are supported.");
	}

	private Annotation annotation(BodyDeclaration declaration) {
		Annotation annotation = 
			ASTTools.annotation(declaration, getAnnotationUnqualifiedName());
		if (annotation == null) {
			annotation = 
				ASTTools.annotation(declaration, getAnnotationFullyQualifiedName());
		}
		if (annotation == null) {
			return null;
		}
		return Generics.asT(annotation);
	}
	private Annotation annotation(SingleVariableDeclaration declaration) {
		Annotation annotation = 
			ASTTools.annotation(declaration, getAnnotationUnqualifiedName());
		if (annotation == null) {
			annotation = 
				ASTTools.annotation(declaration, getAnnotationFullyQualifiedName());
		}
		if (annotation == null) {
			return null;
		}
		return Generics.asT(annotation);
	}

	protected <T extends Annotation> T annotation(BodyDeclaration declaration, Class<T> requiredType) {
		Annotation annotation = annotation(declaration);
		if (annotation == null) {
			return null;
		}
		if (!requiredType.isAssignableFrom(annotation.getClass())) {
			return null;
		}
		return Generics.asT(annotation);
	}
	protected <T extends Annotation> T annotation(SingleVariableDeclaration declaration, Class<T> requiredType) {
		Annotation annotation = annotation(declaration);
		if (annotation == null) {
			return null;
		}
		if (!requiredType.isAssignableFrom(annotation.getClass())) {
			return null;
		}
		return Generics.asT(annotation);
	}

	protected void removeAnnotation(AbstractNode node) throws JavaModelException, MalformedTreeException, BadLocationException {
		removeAnnotation(
			node.getCompilationUnit(), node.getDeclaration());
	}

	private void removeAnnotation(
			ICompilationUnit compilationUnit, ASTNode declaration) throws JavaModelException, MalformedTreeException, BadLocationException {
		if (declaration instanceof BodyDeclaration) {
			removeAnnotation(
				compilationUnit, (BodyDeclaration)declaration);
		}
		else if (declaration instanceof SingleVariableDeclaration) {
			removeAnnotation(
				compilationUnit, (SingleVariableDeclaration)declaration);
		}
	}

	private void removeAnnotation(
			ICompilationUnit compilationUnit, BodyDeclaration declaration) throws JavaModelException, MalformedTreeException, BadLocationException {
		Annotation annotation = 
			ASTTools.annotation(declaration, getAnnotationUnqualifiedName());
		if (annotation == null) {
			return;
		}
		AstUtils.rewriteReplace(compilationUnit, annotation, null);
	}

	private void removeAnnotation(
			ICompilationUnit compilationUnit, SingleVariableDeclaration declaration) throws JavaModelException, MalformedTreeException, BadLocationException {
		Annotation annotation = 
			ASTTools.annotation(declaration, getAnnotationUnqualifiedName());
		if (annotation == null) {
			return;
		}
		AstUtils.rewriteReplace(compilationUnit, annotation, null);
	}

	protected boolean annotationExists(ASTNode declaration) {
		return annotation(declaration) != null;
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
