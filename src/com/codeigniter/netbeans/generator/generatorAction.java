/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.generator;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.WizardDescriptor;
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
        List<WizardDescriptor.Panel<WizardDescriptor>> panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
        panels.add(new GenwizardWizardPanel1());
        panels.add(new GenwizardWizardPanel2());
        String[] steps = new String[panels.size()];
        for (int i = 0; i < panels.size(); i++) {
            Component c = panels.get(i).getComponent();
            // Default step name to component name of panel.
            steps[i] = c.getName();
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
            }
        }
        WizardDescriptor wiz = new WizardDescriptor(new WizardDescriptor.ArrayIterator<WizardDescriptor>(panels));
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wiz.setTitleFormat(new MessageFormat("{0}"));
        wiz.setTitle("Generator");
        if (DialogDisplayer.getDefault().notify(wiz) == WizardDescriptor.FINISH_OPTION) {
            // do something
            String name = (String) wiz.getProperty("name");
            int selection = (Integer) wiz.getProperty("selection");
            String path;
            switch(selection){
                case 0:
                    path = "PROJECT_ROOT/application/models/";
                    try{
                        File file= new File (path+name+".php");
                        FileWriter fw;
                        if (file.exists()){
                            fw = new FileWriter(file,true);//if file exists append to file. Works fine.
                        }
                        else{
                            fw = new FileWriter(file);// If file does not exist. Create it. This throws a FileNotFoundException. Why? 
                        }
                        fw.close();
                    }
                    catch(Exception ex){
                        System.out.println("Exception: Not working!!");
                    }
                    break;
                case 1:
                    path = "PROJECT_ROOT/application/views/";
                                        try{
                        File file= new File (path+name+".php");
                        FileWriter fw;
                        if (file.exists()){
                            fw = new FileWriter(file,true);//if file exists append to file. Works fine.
                        }
                        else{
                            fw = new FileWriter(file);// If file does not exist. Create it. This throws a FileNotFoundException. Why? 
                        }
                        fw.close();
                    }
                    catch(Exception ex){
                        System.out.println("Exception: Not working!!");
                    }
                    break;
                case 2:
                    path = "PROJECT_ROOT/application/controllers/";
                                        try{
                        File file= new File (path+name+".php");
                        FileWriter fw;
                        if (file.exists()){
                            fw = new FileWriter(file,true);//if file exists append to file. Works fine.
                        }
                        else{
                            fw = new FileWriter(file);// If file does not exist. Create it. This throws a FileNotFoundException. Why? 
                        }
                        fw.close();
                    }
                    catch(Exception ex){
                        System.out.println("Exception: Not working!!");
                    }
                    break;
            }        
            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(name + " " + selection));
        }
    }
}
