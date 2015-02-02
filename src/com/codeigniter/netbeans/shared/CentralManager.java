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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author dwoods
 * 
 *  This class is a singleton that stores important plugin-wide information, including
 *  location of the Code Igniter system folder, etc.
 */
public class CentralManager {
    
    private static CentralManager instance = null;
    private Hashtable<CiClass, List<CiFunction>> ciClasses = null;
    private Hashtable<String, List<CiClass>> ciFuncs = null;
    private static final String ciClassesFilename = "./src/com/codeigniter/netbeans/documentation/ciDoc.ser";
    
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
    
    public Hashtable<String, List<CiClass>> getCiFunctions() {
        if (this.ciFuncs == null) {
            if (this.ciClasses == null) {
                getCiClasses();
            }
            
            this.ciFuncs = new Hashtable<String, List<CiClass>>();
            Enumeration<CiClass> keys = this.ciClasses.keys();
            
            while (keys.hasMoreElements()) {
                CiClass ciClass = keys.nextElement();
                List<CiFunction> functions = this.ciClasses.get(ciClass);
                
                for (CiFunction func : functions) {
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
     * @return Hashtable of all the CI classes with a list of their functions
     */
    public Hashtable<CiClass, List<CiFunction>> getCiClasses() {
        if (this.ciClasses == null) {              
            if (!this.loadCiClasses()) {      
                // In order to update the list of CI classes/functions delete the ciDoc.ser file
                // and change the below file paths to point to the libraries and helpers directories
                // in the CI System folder
                File ciLibDir = new File("/Applications/XAMPP/htdocs/system3/libraries/");
                File ciHelperDir = new File("/Applications/XAMPP/htdocs/system3/helpers/");
                File ciCoreDir = new File("/Applications/XAMPP/htdocs/system3/core/");
                List<String> fileExts = new LinkedList<String>();
                fileExts.add("php");
                this.ciClasses = new Hashtable<CiClass, List<CiFunction>>();

                try {
                    List<File> files = getFilesFromDirectory(ciLibDir, fileExts, false);
                    files.addAll(getFilesFromDirectory(ciHelperDir, fileExts, false));
                    files.addAll(getFilesFromDirectory(ciCoreDir, fileExts, false));

                    for (File f : files) {
                        CiClass ciClass = PHPDocumentParser.extractCiClass(f);
                        ArrayList<CiFunction> funcs = PHPDocumentParser.extractFunctions(f);
                        this.ciClasses.put(ciClass, funcs);
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
        
        return this.ciClasses;
    }
    
    private void saveCiClasses() {
        try {
            FileOutputStream fileOut = new FileOutputStream(ciClassesFilename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.ciClasses);
            out.close();
            fileOut.close();            
        } 
        catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
    }
    
    private boolean loadCiClasses() {
        boolean retval = false;
        try {
            FileInputStream fileIn = new FileInputStream(ciClassesFilename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.ciClasses = (Hashtable<CiClass, List<CiFunction>>)in.readObject();
            in.close();
            fileIn.close();
            retval = true;
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }        
        
        return retval;
    }
}
