package com.halware.nakedide.eclipse.ext.annot.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.WildcardType;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.compiler.ast.Literal;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditGroup;

import com.halware.eclipseutil.util.ClosureUtil;
import com.halware.eclipseutil.util.CollectionUtil;
import com.halware.eclipseutil.util.Generics;
import com.halware.eclipseutil.util.StringUtil;
import com.halware.nakedide.eclipse.core.util.MethodUtils;

public final class AstUtils {
	
	private AstUtils() {}

    
    /**
     * Represents a {@link QualifiedName}, as a value object.
     * 
     * <p>
     * Effectively just wraps a pair of strings.
     * 
     * <p>
     * The special {@link #DEFAULT_VALUE} static is a placeholder to represent that
     * the default value for an annotation member should be used.
     */
    public static class QualifiedNameValue {

        public final static QualifiedNameValue DEFAULT_VALUE = new QualifiedNameValue(null,null);
        
        public QualifiedNameValue(String name, String identifier) {
            this.name = name;
            this.identifier = identifier;
        }

        private String name;
        /**
         * Can be fully qualified or simple.
         * 
         * @return
         */
        public String getName() {
            return name;
        }

        private String identifier;
        public String getIdentifier() {
            return identifier;
        }
        
        @Override
        public boolean equals(
                Object obj) {
            if (obj == null || obj.getClass() != getClass()) { 
                return false;
            }
            return equals((QualifiedNameValue)obj);
        }

        public boolean equals(QualifiedNameValue obj) {
            return 
                StringUtil.equals(getName(), obj.getName()) &&
                StringUtil.equals(getIdentifier(), obj.getIdentifier());
        }

        @Override
        public String toString() {
            return getName() + "." + getIdentifier();
        }

    }

    /**
     * Represents the name of a class, without verifying if the
     * class actually exists.
     * 
     * <p>
     * Effectively just wraps a <tt>java.lang.String</tt>, but 
     * this allows us to create an annotation with a string literal
     * of <tt>@SomeAnnotation(SomeClass.class)</tt> rather than
     * <tt>@SomeAnnotation("SomeClass")</tt>.
     * 
     */
    public static class TypeLiteralValue {
        private String className;
        public String getClassName() {
            return className;
        }
    
        public TypeLiteralValue(String className) {
            this.className = className;
        }
        
        /**
         * Called by one of the label providers...
         * 
         * <p>
         * ... so don't append a '.class' suffix, or it will
         * mess things up.
         */
        @Override
        public String toString() {
            return getClassName();
        }
    }

    /**
	 * Finds the declaration node within the supplied {@link CompilationUnit}, 
	 * using a stale declaration (from an earlier AST) as the search.
	 * 
	 * @param compilationUnit
	 * @param searchDeclaration - the declaration from an earlier AST.
	 * @return
	 */
	public static <T extends ASTNode> T findDeclaration(
			CompilationUnit compilationUnit, 
			T searchDeclaration) {
		if (searchDeclaration == null) {
			return null;
		}
		
		// no need to do a search if this is the same AST.
		if (compilationUnit.getAST() == searchDeclaration.getAST()) {
			return searchDeclaration;
		}

		IBinding binding = null;
		if (searchDeclaration instanceof MethodDeclaration) {
			binding = ((MethodDeclaration)searchDeclaration).resolveBinding();
		}
		if (searchDeclaration instanceof TypeDeclaration) {
			binding = ((TypeDeclaration)searchDeclaration).resolveBinding();
		}
		if (searchDeclaration instanceof SingleVariableDeclaration) {
			binding = ((SingleVariableDeclaration)searchDeclaration).resolveBinding();
		}
		
		return Generics.asT(
				binding != null?
					findDeclaration(compilationUnit, binding.getKey()):
					null);
	}


	/**
	 * Finds the {@link MethodDeclaration} within the supplied {@link CompilationUnit}, 
	 * using a method declaration key as the search.
	 * 
	 * @param compilationUnit
	 * @param searchMethodDeclarationKey
	 * @return
	 */
	public static <T extends ASTNode> T findDeclaration(
			CompilationUnit compilationUnit, 
			String searchDeclarationKey) {
		if (compilationUnit == null || searchDeclarationKey == null) {
			return null;
		}
		ASTNode astNode = compilationUnit.findDeclaringNode(searchDeclarationKey);
		if ( astNode == null) {
			return null;
		}
		if (!(astNode instanceof BodyDeclaration) &&
			!(astNode instanceof SingleVariableDeclaration) ) {
			return null;
		}
		return Generics.asT(astNode);
	}


	/**
	 * Returns the (first) declared type ({@link TypeDeclaration}) within the
	 * supplied (AST) {@link CompilationUnit}, if any.
	 * 
	 * @param methodDeclaration
	 * @param annotationClass
	 * @return
	 */
	public static TypeDeclaration determineTypeDeclaration(CompilationUnit compilationUnit) {
		List<TypeDeclaration> typeDeclarations = getChildProperties(compilationUnit, TypeDeclaration.class);
		return CollectionUtil.firstOrNull(typeDeclarations);
	}

	/**
	 * Returns (as a list) all those child properties of the supplied type.
	 * 
	 * @param <T>
	 * @param node
	 * @param requiredType
	 * @return
	 */
	public static <T> List<T> getChildProperties(ASTNode node, Class<T> requiredType) {
		List<T> children = new ArrayList<T>();
		for (ChildListPropertyDescriptor clpd: getChildListPropertyDescriptors(node)) {
			List<ASTNode> childProperties = Generics.asT(node.getStructuralProperty(clpd));
			for(ASTNode childNode: childProperties) {
				if (requiredType.isAssignableFrom(childNode.getClass())) {
					T t = Generics.asT(childNode);
					children.add(t);
				}
			}
		}
		return children;
	}

	/**
	 * Returns all the {@link ChildListPropertyDescriptor}s of the supplied
	 * {@link ASTNode} (as per {@link ASTNode#structuralPropertiesForType()}.
	 * 
	 * @param node
	 * @return
	 */
	public static List<ChildListPropertyDescriptor> getChildListPropertyDescriptors(ASTNode node) {
		List<ChildListPropertyDescriptor> childListPropertyDescriptors = new ArrayList<ChildListPropertyDescriptor>();
		List list= node.structuralPropertiesForType();
		for (int i= 0; i < list.size(); i++) {
			StructuralPropertyDescriptor curr= (StructuralPropertyDescriptor) list.get(i);
			if (!(curr instanceof ChildListPropertyDescriptor)) {
				continue;
			}
			childListPropertyDescriptors.add((ChildListPropertyDescriptor)curr);
		}
		return childListPropertyDescriptors;
	}


	/**
	 * Returned by {@link AstUtils#calculateOffset(MethodDeclaration).
	 * 
	 * @author dkhaywood
	 */
	public static class OffsetAndLength {
		OffsetAndLength(int offset, int length) { this.offset = offset; this.length = length; }
		public int offset;
		public int length;
	}

	/**
	 * Returns the offset and length for the {@link SimpleName} corresponding to the
	 * supplied {@link MethodDeclaration}.
	 */
	public final static OffsetAndLength calculateOffset(BodyDeclaration declaration) {
		return calculateOffset(declaration, false);
	}

	/**
	 * Returns the offset and length for the {@link MethodDeclaration}, optionally
	 * including any modifiers (including annotations and visibility).
	 * 
	 * @param declaration
	 * @param includingModifiers - whether to include modifiers in the offset and length.
	 */
	public final static OffsetAndLength calculateOffset(BodyDeclaration declaration, boolean includingModifiers) {
		if (!(declaration instanceof MethodDeclaration ) &&
		    !(declaration instanceof TypeDeclaration   )    ) {
			throw new IllegalArgumentException("declaration must be TypeDeclaration or MethodDeclaration");
		}
		SimpleName methodName = nameFor(declaration);
		if (!includingModifiers) {
			return new OffsetAndLength(methodName.getStartPosition(), methodName.getLength());
		} else {
			// including modifiers
			int endOfMethodName = methodName.getStartPosition() + methodName.getLength();
			ChildListPropertyDescriptor modifiersProperty = declaration.getModifiersProperty();
			List<ASTNode> methodModifiers = 
				Generics.asT(declaration.getStructuralProperty(modifiersProperty));
			if (methodModifiers.size() > 0) {
				int startOfModifiers = methodModifiers.get(0).getStartPosition();
				return new OffsetAndLength(startOfModifiers, endOfMethodName - startOfModifiers);
			}
			if (declaration instanceof MethodDeclaration) {
				Type returnType2 = ((MethodDeclaration)declaration).getReturnType2();
				if (returnType2 != null) {
					int startOfReturnType = returnType2.getStartPosition();
					return new OffsetAndLength(startOfReturnType, endOfMethodName - startOfReturnType);
				}
			}
			int startOfMethodName = declaration.getStartPosition();
			return new OffsetAndLength(startOfMethodName, endOfMethodName - startOfMethodName);
		}
	}
	/**
	 * Returns the offset and length for the {@link TypeDeclaration}, optionally
	 * including any modifiers (including annotations and visibility).
	 * 
	 * @param declaration
	 * @param includingModifiers - whether to include modifiers in the offset and length.
	 */
	public final static OffsetAndLength calculateOffset(TypeDeclaration declaration, boolean includingModifiers) {
		SimpleName typeName = declaration.getName();
		if (!includingModifiers) {
			return new OffsetAndLength(typeName.getStartPosition(), typeName.getLength());
		} else {
			// including modifiers
			int endOfMethodName = typeName.getStartPosition() + typeName.getLength();
			ChildListPropertyDescriptor modifiersProperty = declaration.getModifiersProperty();
			List<ASTNode> methodModifiers = 
				Generics.asT(declaration.getStructuralProperty(modifiersProperty));
			if (methodModifiers.size() > 0) {
				int startOfModifiers = methodModifiers.get(0).getStartPosition();
				return new OffsetAndLength(startOfModifiers, endOfMethodName - startOfModifiers);
			}
			int startOfMethodName = declaration.getStartPosition();
			return new OffsetAndLength(startOfMethodName, endOfMethodName - startOfMethodName);
		}
	}
	/**
	 * Returns the offset and length for the {@link MethodDeclaration}, optionally
	 * including any modifiers (including annotations and visibility).
	 * 
	 * @param declaration
	 * @param includingModifiers - whether to include modifiers in the offset and length.
	 */
	public final static OffsetAndLength calculateOffset(SingleVariableDeclaration declaration) {
		SimpleName methodName = declaration.getName();
		return new OffsetAndLength(methodName.getStartPosition(), methodName.getLength());
	}

	/**
	 * Can also use to remove the original node, by specifying a <tt>null</tt>
	 * for the replacement.
	 *  
	 * @param compilationUnit
	 * @param original
	 * @param replacement - or <tt>null</tt> to remove the node.
	 * 
	 * @throws JavaModelException
	 * @throws MalformedTreeException
	 * @throws BadLocationException
	 */
	public static void rewriteReplace(
			ICompilationUnit compilationUnit, 
			ASTNode original, 
			ASTNode replacement) throws JavaModelException, MalformedTreeException, BadLocationException {
        rewriteReplace(compilationUnit, Collections.singletonList(original), replacement);
	}

    public static void rewriteReplace(
            ICompilationUnit compilationUnit,
            List<ASTNode> originals,
            ASTNode replacement) throws JavaModelException, MalformedTreeException, BadLocationException {
        
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(compilationUnit);

        rewriteReplace(parser, compilationUnit, originals, replacement);
    }

    /**
     * Provided as a slight performance optimization when an ASTParser is already available
     * to do the refactoring.
     *  
     * @param parser - must have the compilationUnit as its source, per {@link ASTParser#setSource(ICompilationUnit)}.
     * @param compilationUnit
     * @param originals - to be replaced with the (single) replacement node.
     * @param replacement - the (single) replacement node; may be null meaning the originals are simply removed completely.
     * 
     * @throws JavaModelException
     * @throws MalformedTreeException
     * @throws BadLocationException
     */
    public static void rewriteReplace(
            ASTParser parser,
            ICompilationUnit compilationUnit,
            ASTNode original,
            ASTNode replacement)  throws JavaModelException, MalformedTreeException, BadLocationException {
        rewriteReplace(parser, compilationUnit, Collections.singletonList(original), replacement);
    }
    
    /**
     * Provided as a slight performance optimization when an ASTParser is already available
     * to do the refactoring.
     *  
     * @param parser - must have the compilationUnit as its source, per {@link ASTParser#setSource(ICompilationUnit)}.
     * @param compilationUnit
     * @param originals - to be replaced with the (single) replacement node.
     * @param replacement - the (single) replacement node; may be null meaning the originals are simply removed completely.
     * 
     * @throws JavaModelException
     * @throws MalformedTreeException
     * @throws BadLocationException
     */
    public static void rewriteReplace(
            ASTParser parser,
            ICompilationUnit compilationUnit,
            List<ASTNode> originals,
            ASTNode replacement)  throws JavaModelException, MalformedTreeException, BadLocationException {
        
        String source = compilationUnit.getBuffer().getContents();
        Document document= new Document(source);
        
        ASTNode firstOriginal = originals.get(0);
        ASTRewrite rewrite = ASTRewrite.create(firstOriginal.getAST());
        
        TextEditGroup editGroup = new TextEditGroup("Replacing nodes");
        for(ASTNode original: originals) {
            if (original == firstOriginal) {
                rewrite.replace(original, replacement, editGroup);
            } else {
                rewrite.replace(original, null, editGroup);
            }
        }
        
        TextEdit edits = rewrite.rewriteAST(document, compilationUnit.getJavaProject().getOptions(true));
        
        // computation of the new source code
        edits.apply(document);
        String newSource = document.get();
        
        // update of the compilation unit
        compilationUnit.getBuffer().setContents(newSource);
    }


	public static void rewriteAddFirst(
			ICompilationUnit compilationUnit,
			BodyDeclaration declaration, 
			Annotation annotation, 
			ImportDeclaration importDeclaration) throws MalformedTreeException, BadLocationException, CoreException {
		
		String source = compilationUnit.getBuffer().getContents();
		Document document= new Document(source);
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(compilationUnit);
	
		ASTRewrite rewrite = ASTRewrite.create(declaration.getAST());

		ChildListPropertyDescriptor modifiersProperty = declaration.getModifiersProperty();

		ListRewrite listRewrite = rewrite.getListRewrite(declaration, modifiersProperty);
		listRewrite.insertFirst(annotation, null);
		
		TextEdit importEdits = null;
		if (importDeclaration != null) {
			ImportRewrite importRewrite = ImportRewrite.create(compilationUnit, true);
			importRewrite.addImport(importDeclaration.getName().getFullyQualifiedName());
			importEdits = importRewrite.rewriteImports(null);
		}
		
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

	public static void rewriteAddFirst(
			ICompilationUnit compilationUnit,
			SingleVariableDeclaration declaration, 
			Annotation annotation, 
			ImportDeclaration importDeclaration) throws MalformedTreeException, BadLocationException, CoreException {
		
		String source = compilationUnit.getBuffer().getContents();
		Document document= new Document(source);
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(compilationUnit);
	
		ASTRewrite rewrite = ASTRewrite.create(declaration.getAST());

		ListRewrite listRewrite = rewrite.getListRewrite(declaration, SingleVariableDeclaration.MODIFIERS2_PROPERTY);
		listRewrite.insertFirst(annotation, null);
		
		TextEdit importEdits = null;
		if (importDeclaration != null) {
			ImportRewrite importRewrite = ImportRewrite.create(compilationUnit, true);
			importRewrite.addImport(importDeclaration.getName().getFullyQualifiedName());
			importEdits = importRewrite.rewriteImports(null);
		}
		
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

	
	/**
	 * Creates an (AST) {@link Literal} expression.
	 * 
	 * @param ast
	 * @param value
	 * @return
	 */
	public static Expression createExpression(AST ast, Object value) {
		if (value instanceof String) {
			String stringValue = (String)value;
			StringLiteral newStringLiteral = ast.newStringLiteral();
			newStringLiteral.setLiteralValue(stringValue);
			return newStringLiteral;
		}
		if (value instanceof Boolean) {
			Boolean booleanValue = (Boolean)value;
			return ast.newBooleanLiteral(booleanValue);
		}
		if (value instanceof Number) {
			Number numberValue = (Number)value;
			return ast.newNumberLiteral(numberValue.toString());
		}
        if (value instanceof AstUtils.TypeLiteralValue) {
            AstUtils.TypeLiteralValue classHandle = (AstUtils.TypeLiteralValue)value;
            TypeLiteral newTypeLiteral = ast.newTypeLiteral();
            
            Name typeName = ast.newName(classHandle.getClassName());
            Type type = ast.newSimpleType(typeName);
            newTypeLiteral.setType(type);
            return newTypeLiteral;
        }
        if (value instanceof AstUtils.QualifiedNameValue) {
            AstUtils.QualifiedNameValue enumMember = (AstUtils.QualifiedNameValue)value;
            Name enumTypeName = ast.newName(enumMember.getName());
            SimpleName enumMemberName = ast.newSimpleName(enumMember.getIdentifier());
            return ast.newQualifiedName(enumTypeName, enumMemberName);
        }
		return ast.newNullLiteral();
	}


    /**
	 * 
	 * @param ast
	 * @param annotationName - can be fully qualified or not.
	 * @return
	 */
	public static MarkerAnnotation createMarkerAnnotation(AST ast, String annotationName) {
		return createMarkerAnnotation(ast, annotationName, false);
	}

	public static MarkerAnnotation createMarkerAnnotation(AST ast, String annotationName, boolean forceFullyQualified) {
		MarkerAnnotation newMarkerAnnotation = ast.newMarkerAnnotation();
		String name = forceFullyQualified?annotationName:unqualified(annotationName);
		newMarkerAnnotation.setTypeName(ast.newName(name));
		return newMarkerAnnotation;
	}

	/**
	 * 
	 * @param ast
	 * @param annotationName - can be fully qualified or not.
	 * @return
	 */
	public static Annotation createSingleMemberAnnotation(
			AST ast, String annotationName, Object value) {
		return createSingleMemberAnnotation(ast, annotationName, value, false);
	}

	public static Annotation createSingleMemberAnnotation(
			AST ast, String annotationName, Object value, boolean forceFullyQualified) {
	    String name = forceFullyQualified?annotationName:unqualified(annotationName);
        if (value != QualifiedNameValue.DEFAULT_VALUE) {
            SingleMemberAnnotation newSingleMemberAnnotation = ast.newSingleMemberAnnotation();
            newSingleMemberAnnotation.setTypeName(ast.newName(name));
            newSingleMemberAnnotation.setValue(AstUtils.createExpression(ast, value));
            return newSingleMemberAnnotation;
        } else {
            MarkerAnnotation newMarkerAnnotation = ast.newMarkerAnnotation();;
            newMarkerAnnotation.setTypeName(ast.newName(name));
            return newMarkerAnnotation;
        }
	}

	public static NormalAnnotation createNormalAnnotation(
			AST ast, String annotationName, Map<String, Object> memberValues) {
		return createNormalAnnotation(ast, annotationName, memberValues, false);
	}

	public static NormalAnnotation createNormalAnnotation(
			AST ast, String annotationName, Map<String, Object> memberValues, boolean forceFullyQualified) {
		NormalAnnotation newNormalAnnotation = ast.newNormalAnnotation();
		String name = forceFullyQualified?annotationName:unqualified(annotationName);
		newNormalAnnotation.setTypeName(ast.newName(name));
		//newNormalAnnotation.
		//TODO
		//newNormalAnnotation.setValue(AstUtils.createExpression(ast, memberValues));
		return newNormalAnnotation;
	}


	public static String unqualified(String annotationFullyQualifiedName) {
		int lastDot = annotationFullyQualifiedName.lastIndexOf('.');
		if (lastDot == -1) {
			return annotationFullyQualifiedName; 
		}
		if (lastDot == annotationFullyQualifiedName.length()-1) {
			return ""; // lastDot is at end of string
		}
		return annotationFullyQualifiedName.substring(lastDot + 1);
	}

	public static ImportDeclaration createImportStatement(AST ast, String annotationFullyQualifiedName) {
		ImportDeclaration newImportDeclaration = ast.newImportDeclaration();
		newImportDeclaration.setName(ast.newName(annotationFullyQualifiedName));
		return newImportDeclaration;
	}

	public static Object getValue(ASTNode node) {
		
		switch (node.getNodeType()) {
//			case ASTNode.ANONYMOUS_CLASS_DECLARATION:
//				return "Anonymous class declaration";
//			case ASTNode.ARRAY_ACCESS:
//				return "Array access";
//			case ASTNode.ARRAY_CREATION:
//				return "Array creation";
//			case ASTNode.ARRAY_INITIALIZER:
//				return "Array initializer";
//			case ASTNode.ARRAY_TYPE:
//				ArrayType arrayType = (ArrayType)node;
//				return "Array type: " + arrayType.getElementType().toString();
//			case ASTNode.ASSERT_STATEMENT:
//				return "Assert statement";
//			case ASTNode.ASSIGNMENT:
//				return "Assignment";
//			case ASTNode.BLOCK:
//				return "Block";
			case ASTNode.BOOLEAN_LITERAL:
				BooleanLiteral booleanLiteral = (BooleanLiteral) node;
				return booleanLiteral.booleanValue();
//			case ASTNode.BREAK_STATEMENT:
//				return "Break statement";
//			case ASTNode.CAST_EXPRESSION:
//				return "Cast expression";
//			case ASTNode.CATCH_CLAUSE:
//				return "Catch clause";
			case ASTNode.CHARACTER_LITERAL:
				CharacterLiteral characterLiteral = (CharacterLiteral) node;
				return characterLiteral.charValue();
//			case ASTNode.CLASS_INSTANCE_CREATION:
//				return "Class instance creation";
//			case ASTNode.COMPILATION_UNIT:
//				return "Compilation unit";
//			case ASTNode.CONDITIONAL_EXPRESSION:
//				return "Conditional Expression";
//			case ASTNode.CONSTRUCTOR_INVOCATION:
//				return "constructor invocation";
//			case ASTNode.CONTINUE_STATEMENT:
//				return "continue statement";
//			case ASTNode.DO_STATEMENT:
//				return "Do statement";
//			case ASTNode.EMPTY_STATEMENT:
//				return "Empty statement";
//			case ASTNode.EXPRESSION_STATEMENT:
//				return "Expression statement";
//			case ASTNode.FIELD_ACCESS:
//				return "field access";
//			case ASTNode.FIELD_DECLARATION:
//				return "Field declaration";
//			case ASTNode.FOR_STATEMENT:
//				return "For statement";
//			case ASTNode.IF_STATEMENT:
//				return "If statement";
//			case ASTNode.IMPORT_DECLARATION:
//				return "Import declaration";
//			case ASTNode.INFIX_EXPRESSION:
//				return "Infix expression";
//			case ASTNode.INITIALIZER:
//				return "Initializer";
//			case ASTNode.INSTANCEOF_EXPRESSION:
//				return "Instanceof expression";
//			case ASTNode.JAVADOC:
//				return "Javadoc";
//			case ASTNode.LABELED_STATEMENT:
//				return "Labeled statement";
//			case ASTNode.METHOD_DECLARATION:
//				return "Method declaration";
//			case ASTNode.METHOD_INVOCATION:
//				return "Method invocation";
//			case ASTNode.NULL_LITERAL:
//				return "Null literal";
			case ASTNode.NUMBER_LITERAL:
				NumberLiteral numberLiteral = (NumberLiteral) node;
				String token = numberLiteral.getToken();
				try {
					return Integer.parseInt(token);
				} catch(Exception ex) {}
				try {
					return Long.parseLong(token);
				} catch(Exception ex) {}
				try {
					return Double.parseDouble(token);
				} catch(Exception ex) {}
				try {
					return Float.parseFloat(token);
				} catch(Exception ex) {}
				return Double.NaN;
//			case ASTNode.PACKAGE_DECLARATION:
//				return "Package declaration";
//			case ASTNode.PARENTHESIZED_EXPRESSION:
//				return "Parenthesized expression";
//			case ASTNode.POSTFIX_EXPRESSION:
//				return "Postfix expression";
//			case ASTNode.PREFIX_EXPRESSION:
//				return "Prefix expression";
//			case ASTNode.PRIMITIVE_TYPE:
//				PrimitiveType primitiveType = (PrimitiveType) node;
//				return "Primitive type: " + primitiveType.getPrimitiveTypeCode().toString();
			case ASTNode.QUALIFIED_NAME:
                QualifiedName qualifiedName = (QualifiedName) node;
                return new AstUtils.QualifiedNameValue(
                        qualifiedName.getQualifier().getFullyQualifiedName(), 
                        qualifiedName.getName().getIdentifier());
//			case ASTNode.RETURN_STATEMENT:
//				return "Return statement";
//			case ASTNode.SIMPLE_NAME:
//				SimpleName simpleName = (SimpleName) node;
//				return "Simple name: " + simpleName.getIdentifier();
//			case ASTNode.SIMPLE_TYPE:
//				SimpleType simpleType = (SimpleType) node;
//				return "Simple type (" + simpleType.getName().toString() + ")";
//			case ASTNode.SINGLE_VARIABLE_DECLARATION:
//				return "Single variable declaration";
			case ASTNode.STRING_LITERAL:
				StringLiteral stringLiteral = (StringLiteral) node;
				return stringLiteral.getLiteralValue();
//			case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
//				return "Super constructor invocation";
//			case ASTNode.SUPER_FIELD_ACCESS:
//				return "Super field access";
//			case ASTNode.SUPER_METHOD_INVOCATION:
//				return "Super method invocation";
//			case ASTNode.SWITCH_CASE:
//				return "Switch case";
//			case ASTNode.SWITCH_STATEMENT:
//				return "Switch statement";
//			case ASTNode.SYNCHRONIZED_STATEMENT:
//				return "Synchronized statement";
//			case ASTNode.THIS_EXPRESSION:
//				return "This expression";
//			case ASTNode.THROW_STATEMENT:
//				return "Throw statement";
//			case ASTNode.TRY_STATEMENT:
//				return "Try statement";
//			case ASTNode.TYPE_DECLARATION:
//				return "Type declaration";
//			case ASTNode.TYPE_DECLARATION_STATEMENT:
//				return "Type declaration statement";
			case ASTNode.TYPE_LITERAL:
                TypeLiteral typeLiteral = (TypeLiteral) node;
                return new AstUtils.TypeLiteralValue(typeLiteral.toString());
//			case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
//				return "Varialbe declaration expression";
//			case ASTNode.VARIABLE_DECLARATION_FRAGMENT:
//				return "Variable declaration fragment";
//			case ASTNode.VARIABLE_DECLARATION_STATEMENT:
//				return "Variable declaration statement";
//			case ASTNode.WHILE_STATEMENT:
//				return "While statement";
		}
		return null;
	}

	public static String asString(Type type) {
		if (type.isArrayType()) {
			ArrayType arrayType = (ArrayType)type;
			return asString(arrayType.getComponentType()) + "[]";
		}
		if (type.isParameterizedType()) {
			ParameterizedType parameterizedType = (ParameterizedType)type;
			List<Type> typeArguments = Generics.asT(parameterizedType.typeArguments());
			class TypeToString implements ClosureUtil.IClosure<Type, String> {
				public String eval(Type type) {
					return asString(type);
				}
			}
			return asString(parameterizedType.getType()) + "<" + StringUtil.join(
				ClosureUtil.forEach(typeArguments, new TypeToString()), ", ") + ">";
		}
		if (type.isPrimitiveType()) {
			PrimitiveType primitiveType = (PrimitiveType)type;
			return primitiveType.getPrimitiveTypeCode().toString();
		}
		if (type.isQualifiedType()) {
			QualifiedType qualifiedType = (QualifiedType)type;
			return qualifiedType.getName().getFullyQualifiedName();
		}
		if (type.isSimpleType()) {
			SimpleType simpleType = (SimpleType)type;
			return simpleType.getName().getFullyQualifiedName();
		}
		if (type.isWildcardType()) {
			WildcardType wildcardType = (WildcardType)type;
			Type boundType = wildcardType.getBound();
			if (boundType != null) {
				if (wildcardType.isUpperBound()) {
					return "? extends " + asString(boundType);
				} else {
					return "? super " + asString(boundType);
				}
			} else {
				return "?";
			}
		}
		return "(unknown type)";
	}

	/**
	 * Returns the {@link SimpleName} of the supplied declaration, if
	 * supported.
	 * 
	 * <p>
	 * As of 3.2, all subtypes provide a name except for
	 * {@link FieldDeclaration} and {@link Initializer}.
	 * 
	 * @param declaration
	 * @return
	 */
	public final static SimpleName nameFor(BodyDeclaration declaration) {
		if (declaration instanceof MethodDeclaration) {
			return ((MethodDeclaration)declaration).getName();
		}
		if (declaration instanceof AbstractTypeDeclaration) {
			return ((AbstractTypeDeclaration)declaration).getName();
		}
		if (declaration instanceof AnnotationTypeMemberDeclaration) {
			return ((AnnotationTypeMemberDeclaration)declaration).getName();
		}
		if (declaration instanceof EnumConstantDeclaration) {
			return ((EnumConstantDeclaration)declaration).getName();
		}
		if (declaration instanceof FieldDeclaration) {
			return null;
		}
		if (declaration instanceof Initializer) {
			return null;
		}
		if (declaration instanceof MethodDeclaration) {
			return ((MethodDeclaration)declaration).getName();
		}
		return null;
	}

	/**
	 * Returns the index of the first modifier that is not an annotation
	 * This assumes that annotations belong before modifiers, though
	 * technically they can be interspersed with modifiers.
	 */
	public static int annotationLocation(BodyDeclaration declaration) {
		int count = 0;
		for (Iterator i = declaration.modifiers().listIterator(); i.hasNext();) {
			IExtendedModifier modifier = (IExtendedModifier) i.next();
			if (modifier.isModifier()) {
				break;
			}
			count++;
		}
		return count;
	}
	/**
	 * Returns the index of the first modifier that is not an annotation
	 * This assumes that annotations belong before modifiers, though
	 * technically they can be interspersed with modifiers.
	 */
	public static int annotationLocation(SingleVariableDeclaration declaration) {
		int count = 0;
		for (Iterator i = declaration.modifiers().listIterator(); i.hasNext();) {
			IExtendedModifier modifier = (IExtendedModifier) i.next();
			if (modifier.isModifier()) {
				break;
			}
			count++;
		}
		return count;
	}

    /**
     * Doesn't support {@link SimpleType}s (so isn't much use).
     * 
     * @param type
     * @return
     */
    public static boolean isValueType(Type type) {
        if (type.isArrayType()) {
            return false;
        }
        if (type.isParameterizedType()) {
            return false;
        }
        if (type.isPrimitiveType()) {
            return true;
        }
        if (type.isSimpleType()) {
            // because we can't tell which package the type is in :-(
            return false;
        }
        if (type.isQualifiedType()) {
            return MethodUtils.isBuiltInValueType(asString(type));
        }
        if (type.isWildcardType()) {
            return false;
        }
        return false;
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
