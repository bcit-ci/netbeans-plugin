/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.documentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Help",
        id = "com.codeigniter.netbeans.documentation.GoToDocumentationAction"
)
@ActionRegistration(
        displayName = "Go to Documentation"
)
@ActionReferences({
    @ActionReference(path = "Editors/text/x-php5/Popup", position = 550)
})

@Messages("CTL_GoToDocumentationAction=Go to Documentation")
public final class GoToDocumentationAction implements ActionListener {

    //http://www.codeigniter.com/userguide3/libraries/security.html?#CI_Security.sanitize_filename
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
