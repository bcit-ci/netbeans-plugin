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
 *  location of the Code Igniter system folder, list of CI classes, list of all CI functions, etc.
 */
public class CentralManager {
    
    private static CentralManager instance = null;
    private List<CiClass> ciClasses = null; 
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
                updateCiClasses("/Applications/XAMPP/htdocs/system3/");
            }
        }
        
        return this.ciClasses;
    }
    
    /**
     *  Saves this.ciClasses to disk at the location specified by ciClassesFilename
     */
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
    
    /**
     *  Loads the ciDoc.ser file from the location specified by ciClassesFilename
     * @return True if successful. False otherwise
     */
    private boolean loadCiClasses() {
        boolean retval = false;
        try {
            FileInputStream fileIn = new FileInputStream(ciClassesFilename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.ciClasses = (ArrayList<CiClass>)in.readObject();
            in.close();
            fileIn.close();
            retval = true;
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }        
        
        return retval;
    }
    
    /**
     *     This method updates the ciDoc.ser file
     * @param path The base path for the CI system folder
     */
    private void updateCiClasses(String path) {
        if (path.endsWith("/")) {
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
