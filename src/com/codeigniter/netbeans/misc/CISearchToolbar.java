/*
 * This class handled the codeIgniter "Help Search" action.
 */
package com.codeigniter.netbeans.misc;

import com.codeigniter.netbeans.misc.HelpSearchPanel;
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
        iconBase = "com/codeigniter/netbeans/misc/logo.png",
        displayName = "#CTL_CISearchToolbar"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 0),
    @ActionReference(path = "Toolbars/File", position = 0)
})
@Messages("CTL_CISearchToolbar=CodeIgniter")
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
