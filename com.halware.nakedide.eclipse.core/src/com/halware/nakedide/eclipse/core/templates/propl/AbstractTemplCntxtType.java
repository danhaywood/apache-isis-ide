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
import java.util.List;

import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateVariableResolver;

public abstract class AbstractTemplCntxtType extends TemplateContextType {

    /**
     * Creates a new XML context type.
     */
    public AbstractTemplCntxtType(final String id) {
        super();
        this.id = id;
        addGlobalResolvers();
        addJavaResolvers();
    }

    private final String id;
    public String getId() {
        return id;
    }
    
    protected void addGlobalResolvers() {
        addResolver(new GlobalTemplateVariables.Cursor());
        addResolver(new GlobalTemplateVariables.WordSelection());
        addResolver(new GlobalTemplateVariables.LineSelection());
        addResolver(new GlobalTemplateVariables.Dollar());
        addResolver(new GlobalTemplateVariables.Date());
        addResolver(new GlobalTemplateVariables.Year());
        addResolver(new GlobalTemplateVariables.Time());
        addResolver(new GlobalTemplateVariables.User());
    }

    protected void addJavaResolvers() {
        for (TemplateVariableResolver resolver : new MyJavaContextType().javaResolvers()) {
            addResolver(resolver);
        }
    }


    @SuppressWarnings("restriction")
    class MyJavaContextType extends
            org.eclipse.jdt.internal.corext.template.java.JavaContextType {
        public List<TemplateVariableResolver> javaResolvers() {
            List<TemplateVariableResolver> resolvers = new ArrayList<TemplateVariableResolver>();
            
            // compilation unit
            resolvers.add(new File() {});
            resolvers.add(new PrimaryTypeName());
            resolvers.add(new ReturnType());
            resolvers.add(new Method());
            resolvers.add(new Type());
            resolvers.add(new Package());
            resolvers.add(new Project());
            resolvers.add(new Arguments());

            // java
            resolvers.add(new Array());
            resolvers.add(new ArrayType());
            resolvers.add(new ArrayElement());
            resolvers.add(new Index());
            resolvers.add(new Iterator());
            resolvers.add(new Collection());
            resolvers.add(new Iterable());
            resolvers.add(new IterableType());
            resolvers.add(new IterableElement());
            resolvers.add(new Todo());

            return resolvers;
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
