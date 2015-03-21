/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.shared;

import static com.codeigniter.netbeans.shared.FileExtractor.getFilesFromDirectory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;

/**
 *
 * @author dwoods
 * 
 *  This class is a singleton that stores important plugin-wide information, including
 *  location of the Code Igniter system folder, list of CI classes, list of all CI functions, etc.
 */
public class CentralManager {
    
    public static final ImageIcon ICON = 
            new ImageIcon(ImageUtilities.loadImage("com/codeigniter/netbeans/generator/cilogo16.png"));
    
    private static CentralManager instance = null;
    private List<CiClass> ciClasses = null; 
    private Hashtable<String, List<CiClass>> ciFuncs = null;
    private static final String CI_CLASSES_FILENAME = "ciDoc.ser";
    private static final String CI_CLASSES_BASEPATH = "src/com/codeigniter/netbeans/shared/";
    private static final String CI_SYSTEM_FOLDER = "/Applications/XAMPP/htdocs/system3/";   
            
    private CentralManager() {
        
    }
    
    /**
     * 
     * @return The CentralManager singleton
     */
    public static CentralManager Instance() {
        if (instance == null) {
            instance = new CentralManager();
        }
        
        return instance;
    }
    
    /**
     * 
     * @return A Hashtable with the method name as the key, and list of CIClasses that has that method
     */
    public Hashtable<String, List<CiClass>> getCiFunctions() {
        if (this.ciFuncs == null) {
            if (this.ciClasses == null) {
                getCiClasses();
            }
            
            this.ciFuncs = new Hashtable<String, List<CiClass>>();
                        
            for (CiClass ciClass : this.ciClasses) {                                                
                for (CiFunction func : ciClass.getFunctions()) {
                    List<CiClass> classList = new ArrayList<CiClass>();
                    if (this.ciFuncs.containsKey(func.getMethodName())) {
                        classList = this.ciFuncs.get(func.getMethodName());
                    }
                    classList.add(ciClass);
                    this.ciFuncs.put(func.getMethodName(), classList);
                }
            }            
        }        
        
        return this.ciFuncs;        
    }
    
    /**
     * 
     * @return List of all the CI classes
     */
    public List<CiClass> getCiClasses() {
        if (this.ciClasses == null) {              
            if (!this.loadCiClasses()) {      
                updateCiClasses(CI_SYSTEM_FOLDER);
            }
        }
        
        return this.ciClasses;
    }
    
    /**
     *  Saves this.ciClasses to disk at the location specified by ciClassesFilename
     */
    private void saveCiClasses() {
        try {
            FileOutputStream fileOut = new FileOutputStream(CI_CLASSES_BASEPATH + CI_CLASSES_FILENAME);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.ciClasses);
            out.close();
            fileOut.close();            
        } 
        catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
    }
    
    /**
     *  Loads the ciDoc.ser file from the location specified by ciClassesFilename
     * @return True if successful. False otherwise
     */
    private boolean loadCiClasses() {
        boolean retval = false;
        try {
            // Look in this package for the serialized file containing a list of all the CI methods
            InputStream is = this.getClass().getResourceAsStream(CI_CLASSES_FILENAME);            
            // Check if file was found
            if (is != null) {
                ObjectInputStream in = new ObjectInputStream(is);
                this.ciClasses = (ArrayList<CiClass>)in.readObject();
                in.close();
                is.close();
                retval = true;
            }
            else {
                System.err.println("Unable to find ciDoc.ser");
            }            
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }        
        
        return retval;
    }
    
    /**
     *     This method updates the file containing the list of CI methods & classes.
     * There are several steps required to updating this file:
     * 1) Delete the current file. Use CI_CLASSES_BASEPATH & CI_CLASSES_FILENAME to find it
     * 2) Change the CI_SYSTEM_FOLDER variable to point to the directory containing the Code Igniter framework
     * that is on YOUR system. This directory will be recursively searched for all libraries, helpers, & core CI files.
     * It will then extract all classes and public method from these files.
     * 3) Call this method and verify that the file has been created.
     * @param path The base path for the CI system folder
     */
    private void updateCiClasses(String path) {
        if (!path.endsWith("/")) {
            path = path.concat("/");
        }
                
        File ciLibDir = new File(path + "libraries/");
        File ciHelperDir = new File(path + "helpers/");
        File ciCoreDir = new File(path + "core/");
        List<String> fileExts = new LinkedList<String>();
        fileExts.add("php");
        this.ciClasses = new ArrayList<CiClass>();

        try {
            List<File> files = getFilesFromDirectory(ciLibDir, fileExts, false);
            files.addAll(getFilesFromDirectory(ciHelperDir, fileExts, false));
            files.addAll(getFilesFromDirectory(ciCoreDir, fileExts, false));

            for (File f : files) {
                CiClass ciClass = PHPDocumentParser.extractCiClass(f);
                ciClass.setFunctions(PHPDocumentParser.extractFunctions(f));
                this.ciClasses.add(ciClass);
            }                   

            saveCiClasses();

            // Print
//                    Enumeration<CiClass> ciEnum = this.ciClasses.keys();
//                    while (ciEnum.hasMoreElements()) {
//                        CiClass c = ciEnum.nextElement();
//                        List<CiFunction> funcs = this.ciClasses.get(c);
//                        System.out.println(c.toString());
//                        for (CiFunction func : funcs) {
//                            System.out.println(func.toString());
//                        }
//                    }
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }    
}
