package com.halware.nakedide.eclipse.core.templates.propl;

import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class TemplPropl extends TemplateProposal  {

    public TemplPropl(
            Template template,
            TemplateContext context,
            IRegion region,
            Image image,
            int relevance) {
        super(template, context, region, image, relevance);
        setInformationControlCreator(defaultCreatorWithHtmlTextPresenter());
    }

    public TemplPropl(
            Template template,
            TemplateContext context,
            IRegion region,
            Image image) {
        super(template, context, region, image);
    }

    @Override
    public String getDisplayString() {
        return getTemplate().getName();
    }
    
    @Override
    public String getAdditionalProposalInfo() {
        return getTemplate().getDescription();
    }
    
    private IInformationControlCreator defaultCreatorWithHtmlTextPresenter() {
        return 
            new IInformationControlCreator() {
                public IInformationControl createInformationControl(Shell parent) {
                    BrowserInformationControl browserInformationControl = new BrowserInformationControl(parent, JFaceResources.TEXT_FONT /*(SWT.TOOL | SWT.NO_TRIM), SWT.COLOR_GREEN*/, getTemplate().getName());
                    return browserInformationControl;
                }
            };
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
