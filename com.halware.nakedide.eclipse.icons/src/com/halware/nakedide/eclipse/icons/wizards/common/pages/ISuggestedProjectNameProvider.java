package com.halware.nakedide.eclipse.icons.wizards.common.pages;

public interface ISuggestedProjectNameProvider {

    void addSuggestedProjectNameListener(ISuggestedProjectNameListener listener);
    void removeSuggestedProjectNameListener(ISuggestedProjectNameListener listener);
    
    void suggestProjectName();

}
