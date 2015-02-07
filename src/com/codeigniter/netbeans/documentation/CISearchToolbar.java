/*
 * This class handled the codeIgniter "Help Search" action.
 */
package com.codeigniter.netbeans.documentation;

import com.codeigniter.netbeans.documentation.HelpSearchPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

@ActionID(
        category = "File",
        id = "com.codeigniter.netbeans.CISearchToolbar"
)
@ActionRegistration(
        displayName = "#CTL_CISearchToolbar",
        lazy = false
)
@ActionReferences({
    @ActionReference(path = "Toolbars/File", position = 0)
})
@Messages("CTL_CISearchToolbar=Search CI Docs")
public final class CISearchToolbar extends AbstractAction implements Presenter.Toolbar {

    @Override
    public Component getToolbarPresenter() {
        return new HelpSearchPanel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //delegated to toolbar
    }

}
