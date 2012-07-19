package com.halware.nakedide.eclipse.ext.builders.markres;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.internal.corext.dom.NodeFinder;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.texteditor.ITextEditor;

import com.halware.eclipseutil.util.EditorUtil;

@SuppressWarnings("restriction")
public abstract class AbstractMarkedMethodResolution implements IMarkerResolution {
    
    public abstract Logger getLOGGER();
    
    public AbstractMarkedMethodResolution(
            final IMethod method) {
        this(method, true);
    }

    public AbstractMarkedMethodResolution(
            final IMethod method, final boolean createAst) {
        this.method = method;
        if (createAst) {
            createAst(method);
        }
    }

    private void createAst(
            final IMethod method) {
        ICompilationUnit compilationUnit = getCompilationUnit();
        
        this.parser = ASTParser.newParser(AST.JLS3);
        parser.setResolveBindings(true);
        parser.setSource(compilationUnit);

        ISourceRange sourceRange;
        try {
            sourceRange = method.getSourceRange();

            this.parsedCompilationUnit = (CompilationUnit) parser.createAST(null);
            
            ASTNode methodNode = NodeFinder.perform(parsedCompilationUnit, sourceRange);
            if (!(methodNode instanceof MethodDeclaration)) {
                return;
            }
            this.methodDeclaration = (MethodDeclaration) methodNode;
            
        } catch (JavaModelException e) {
            getLOGGER().error(e);
        } catch (MalformedTreeException e) {
            getLOGGER().error(e);
        }
    }

    private IMethod method;
    protected IMethod getMethod() {
        return method;
    }

    protected int getMethodOffset() {
        try {
            return getMethod().getSourceRange().getOffset();
        } catch (JavaModelException e) {
            return 0;
        }
    }

    protected int getMethodLength() {
        try {
            return getMethod().getSourceRange().getLength();
        } catch (JavaModelException e) {
            return 0;
        }
    }

    /**
     * The {@link ICompilationUnit} corresponding to the
     * method ({@link #getMethod()}).
     * 
     * @return
     */
    protected ICompilationUnit getCompilationUnit() {
        return getMethod().getCompilationUnit();
    }


    /**
     * Attempts to (re-)select method in the active editor.
     *
     */
    protected void selectInEditor() {
        IEditorPart activeEditor = EditorUtil.getActiveEditor();
        if (!(activeEditor instanceof ITextEditor)) {
            return;
        }
        ITextEditor textEditor = (ITextEditor)activeEditor;
        IEditorInput editorInput = textEditor.getEditorInput();
        IJavaElement editorJavaEl = JavaUI.getEditorInputJavaElement(editorInput);
        if (editorJavaEl == getCompilationUnit()) {
            EditorUtil.selectInEditor(textEditor, getMethodOffset(), 0);
        }
    }

    private MethodDeclaration methodDeclaration;
    /**
     * Populated if AST is requested via constructor.
     * 
     * @return
     */
    public MethodDeclaration getMethodDeclaration() {
        return methodDeclaration;
    }
    
    private CompilationUnit parsedCompilationUnit;
    /**
     * Populated if AST is requested via constructor.
     * 
     * @return
     */
    public CompilationUnit getParsedCompilationUnit() {
        return parsedCompilationUnit;
    }

    private ASTParser parser;
    /**
     * Populated if AST is requested via constructor.
     *
     * <p>
     * Will be set up with a source of the compilation unit.
     * 
     * @return
     */
    public ASTParser getParser() {
        return parser;
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
