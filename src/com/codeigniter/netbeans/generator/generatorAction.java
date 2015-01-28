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
        category = "File",
        id = "com.codeigniter.netbeans.generator.generatorAction"
)
@ActionRegistration(
        iconBase = "com/codeigniter/netbeans/generator/logo16.png",
        displayName = "#CTL_generatorAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = -100),
    @ActionReference(path = "Toolbars/File", position = -100),
    @ActionReference(path = "Shortcuts", name = "DS-A")
})
@Messages("CTL_generatorAction=New Files")
public final class generatorAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
