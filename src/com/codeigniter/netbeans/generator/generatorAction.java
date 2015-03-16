/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.generator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "CodeIgniter",
        id = "com.codeigniter.netbeans.generator.generatorAction"
)
@ActionRegistration(
        iconBase = "com/codeigniter/netbeans/generator/cilogo16.png",
        displayName = "#CTL_generatorAction"
)
@ActionReferences({
    @ActionReference(path = "Toolbars/CodeIgniter", position = -100)
})
@Messages("CTL_generatorAction=CI Generator")
public final class generatorAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
        Newframe f = new Newframe("CI Generator");
    }
}
