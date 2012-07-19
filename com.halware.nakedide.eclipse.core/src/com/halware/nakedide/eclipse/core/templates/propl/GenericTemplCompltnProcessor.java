/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.halware.nakedide.eclipse.core.templates.propl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

import com.halware.eclipseutil.util.Generics;
import com.halware.eclipseutil.util.RegionUtil;
import com.halware.nakedide.eclipse.core.Activator;
import com.halware.nakedide.eclipse.core.templates.GenericTemplateRegistry;

public class GenericTemplCompltnProcessor 
    extends TemplateCompletionProcessor {
    
    private static final String DEFAULT_IMAGE = "icons/hal_logo.gif"; //$NON-NLS-1$

    public GenericTemplCompltnProcessor(String contextTypeId) {
        this.contextTypeId = contextTypeId;
    }

    private final String contextTypeId;
    public String getContextTypeId() {
        return this.contextTypeId;
    }

    /**
     * Creates a concrete template context for the given region in the document. This involves finding out which
     * context type is valid at the given location, and then creating a context of this type. The default implementation
     * returns a <code>DocumentTemplateContext</code> for the context type at the given location.
     *
     * @param viewer the viewer for which the context is created
     * @param region the region into <code>document</code> for which the context is created
     * @return a template context that can handle template insertion at the given location, or <code>null</code>
     */
    protected TemplateContext createContext(ITextViewer viewer, IRegion region) {
        IDocument document = viewer.getDocument();

        // somewhat heavy handed.  Oh well.
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(document.get().toCharArray());
        CompilationUnit parsedCompilationUnit = (CompilationUnit) parser.createAST(null);

        List<AbstractTypeDeclaration> typeDeclarations = Generics.asT(parsedCompilationUnit.types());
        TypeDeclaration containingTypeDeclaration = null;
        for(AbstractTypeDeclaration candidateTypeDeclaration: typeDeclarations) {
            if (!regionOverlaps(candidateTypeDeclaration, region)) {
                continue;
            }

            // ignore enums and annotations
            if (!(candidateTypeDeclaration instanceof TypeDeclaration)) {
                continue;
            }
            
            // ignore nested (not top-level) types.
            if (!candidateTypeDeclaration.isPackageMemberTypeDeclaration()) {
                continue;
            }
            
            containingTypeDeclaration = (TypeDeclaration)candidateTypeDeclaration;
            break;
        }

        if (containingTypeDeclaration != null) {
            MethodDeclaration[] methodDeclarations = containingTypeDeclaration.getMethods();
            for(MethodDeclaration methodDeclaration: methodDeclarations) {
                if (regionOverlaps(methodDeclaration, region)) {
                    // no templates within methods.
                    return null;
                }
            }
        }
       
        TemplateContextType contextType = getContextType(viewer, region);
        if (contextType != null) {
            return new DocTemplContext(
                    contextType, document, region.getOffset(), region.getLength(), 
                    containingTypeDeclaration != null);
        }
        return null;

    }

    
    private boolean regionOverlaps(
            AbstractTypeDeclaration atd,
            IRegion region) {
        return RegionUtil.overlaps(
                new Region(atd.getStartPosition(), atd.getLength()), region);
    }

    private boolean regionOverlaps(
            MethodDeclaration md,
            IRegion region) {
        return RegionUtil.overlaps(
                new Region(md.getStartPosition(), md.getLength()), region);
    }

    private static final class ProposalComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            return ((TemplateProposal) o2).getRelevance() - ((TemplateProposal) o1).getRelevance();
        }
    }

    private static final Comparator fgProposalComparator= new ProposalComparator();

    /**
     * @see http://dev.eclipse.org/newslists/news.eclipse.platform/msg26165.html
     */
    @SuppressWarnings("unchecked")
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {

        ITextSelection selection= (ITextSelection) viewer.getSelectionProvider().getSelection();

        // adjust offset to end of normalized selection
        if (selection.getOffset() == offset)
            offset= selection.getOffset() + selection.getLength();

        String prefix= extractPrefix(viewer, offset);
        Region region= new Region(offset - prefix.length(), prefix.length());
        DocTemplContext context= 
            (DocTemplContext) createContext(viewer, region);
        if (context == null)
            return new ICompletionProposal[0];

        context.setVariable("selection", selection.getText()); // name of the selection variables {line, word}_selection //$NON-NLS-1$

        Template[] templates= getTemplates(context.getContextType().getId());

        List matches= new ArrayList();
        String prefixLowerCase = prefix.toLowerCase();
        for (int i= 0; i < templates.length; i++) {
            Template template= templates[i];
            if (!context.canEvaluate(template, prefix)) {
                continue;
            }
            try {
                context.getContextType().validate(template.getPattern());
            } catch (TemplateException e) {
                continue;
            }

            if (prefixLowerCase.length() == 0) {
                // original code: doesn't use prefix
                if (template.matches(prefixLowerCase, context.getContextType().getId())) {
                    matches.add(createProposal(template, context, (IRegion) region, getRelevance(template, prefix)));
                }
            } else {
                // as per http://dev.eclipse.org/newslists/news.eclipse.platform/msg26165.html
                if (template.matches(prefix, context.getContextType().getId()) &&
                    template.getName().toLowerCase().startsWith(prefixLowerCase)) {
                    matches.add(createProposal(template, context, (IRegion)region,getRelevance(template, prefixLowerCase)));
                }            
            }
        }

        Collections.sort(matches, fgProposalComparator);

        return (ICompletionProposal[]) matches.toArray(new ICompletionProposal[matches.size()]);
    }

    
    /**
     * Creates a new proposal.
     * <p>
     * The default implementation returns an instance of
     * {@link TemplateProposal}. Subclasses may replace this method to provide
     * their own implementations.
     * </p>
     *
     * @param template the template to be applied by the proposal
     * @param context the context for the proposal
     * @param region the region the proposal applies to
     * @param relevance the relevance of the proposal
     * @return a new <code>ICompletionProposal</code> for
     *         <code>template</code>
     */
    protected ICompletionProposal createProposal(Template template, TemplateContext context, IRegion region, int relevance) {
        return new TemplPropl(template, context, region, getImage(template), relevance);
    }

    
    /**
     * We watch for angular brackets since those are often part of XML
     * templates.
     * 
     * @param viewer
     *            the viewer
     * @param offset
     *            the offset left of which the prefix is detected
     * @return the detected prefix
     */
    protected String extractPrefix(ITextViewer viewer, int offset) {
        IDocument document = viewer.getDocument();
        int i = offset;
        if (i > document.getLength())
            return ""; //$NON-NLS-1$

        try {
            while (i > 0) {
                char ch = document.getChar(i - 1);
                if (ch != '<' && !Character.isJavaIdentifierPart(ch))
                    break;
                i--;
            }
            return document.get(i, offset - i);
        } catch (BadLocationException e) {
            return ""; //$NON-NLS-1$
        }
    }

    /**
     * Cut out angular brackets for relevance sorting, since the template name
     * does not contain the brackets.
     * 
     * @param template
     *            the template
     * @param prefix
     *            the prefix
     * @return the relevance of the <code>template</code> for the given
     *         <code>prefix</code>
     */
    protected int getRelevance(Template template, String prefix) {
        if (template.getName().startsWith(prefix))
            return 90;
        return 0;
    }

    /**
     * Simply return all templates.
     * 
     * @param contextTypeId
     *            the context type, ignored in this implementation
     * @return all templates
     */
    protected Template[] getTemplates(String contextTypeId) {
        return GenericTemplateRegistry.getDefault().getTemplateStore().getTemplates(contextTypeId);
    }

    /**
     * Return the context type that is supported by this plug-in.
     * 
     * @param viewer
     *            the viewer, ignored in this implementation
     * @param region
     *            the region, ignored in this implementation
     * @return the supported XML context type
     */
    protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
        return getContextTypeRegistry().getContextType(getContextTypeId());
    }

    /**
     * Always return the default image.
     * 
     * @param template
     *            the template, ignored in this implementation
     * @return the default template image
     */
    protected Image getImage(Template template) {
        ImageRegistry registry = getImageRegistry();
        Image image = registry.get(DEFAULT_IMAGE);
        if (image == null) {
            ImageDescriptor desc = GenericTemplateRegistry.imageDescriptorFromPlugin(
                    Activator.PLUGIN_ID, DEFAULT_IMAGE); //$NON-NLS-1$
            registry.put(DEFAULT_IMAGE, desc);
            image = registry.get(DEFAULT_IMAGE);
        }
        return image;
    }

    private ContextTypeRegistry getContextTypeRegistry() {
        return GenericTemplateRegistry.getDefault().getContextTypeRegistry();
    }

    private ImageRegistry getImageRegistry() {
        return GenericTemplateRegistry.getDefault().getImageRegistry();
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
