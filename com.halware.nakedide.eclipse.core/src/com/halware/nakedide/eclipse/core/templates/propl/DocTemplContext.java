package com.halware.nakedide.eclipse.core.templates.propl;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContextType;

public class DocTemplContext extends DocumentTemplateContext {



    public DocTemplContext(
            TemplateContextType type,
            IDocument document,
            int offset,
            int length, 
            final boolean withinType) {
        super(type, document, offset, length);
        this.withinType = withinType;
    }

    private boolean withinType;

    
    @Override
    public boolean canEvaluate(
            Template template) {
        if (!canEvaluate(template, null)) {
            return false;
        }
        return super.canEvaluate(template);
    }

    /**
     * Same idea as the {@link #canEvaluate(Template)} hook, except
     * also taking the prefix into account.
     * 
     * <p>
     * For templates that are valid outside the type definition 
     * (as per {@link #isOutsideTypeTemplate(Template)}), we also
     * check that the prefix - if supplied - is empty.
     * 
     * @param template
     * @param prefix
     * @return
     */
    boolean canEvaluate(
            Template template, String prefix) {
        if ( withinType &&  isOutsideTypeTemplate(template) ||
            !withinType && !isOutsideTypeTemplate(template)) {
            return false;
        }
        if (prefix == null) {
            return true;
        }
        return !(prefix.length() > 0 && isOutsideTypeTemplate(template));
    }

    private boolean isOutsideTypeTemplate(
            Template template) {
        String templateName = template.getName();
        return templateName.startsWith("applib") ||
               templateName.startsWith("entity") || 
               templateName.startsWith("value")  ;
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
