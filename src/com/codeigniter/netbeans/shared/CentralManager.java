/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.shared;

import static com.codeigniter.netbeans.shared.FileExtractor.getFilesFromDirectory;
import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author dwoods
 * 
 *  This class is a singleton that stores important plugin-wide information, including
 *  life cycle events, location of the Code Igniter system folder, etc.
 */
public class CentralManager {
    
    private static CentralManager instance = null;
    private Dictionary<CiClass, List<CiFunction>> ciDoc = null;
    
    private CentralManager() {
        
    }
    
    public static CentralManager Instance() {
        if (instance == null) {
            instance = new CentralManager();
        }
        
        return instance;
    }
    
    /**
     * 
     * @return Dictionary of the functions with the class as the key and a list of functions
     */
    public Dictionary<CiClass, List<CiFunction>> getCIDocFunctions() {
        if (this.ciDoc == null) {    
            File ciLibDir = new File("/Applications/XAMPP/htdocs/system3/libraries/");
            File ciHelperDir = new File("/Applications/XAMPP/htdocs/system3/helpers/");
            List<String> fileExts = new LinkedList<String>();
            fileExts.add("php");
            Hashtable<CiClass, List<CiFunction>> docs = new Hashtable<CiClass, List<CiFunction>>();

            try {
                List<File> libFiles = getFilesFromDirectory(ciLibDir, fileExts, false);
                List<File> helperFiles = getFilesFromDirectory(ciHelperDir, fileExts, false);

                for (File f : libFiles) {
                    CiClass ciClass = PHPDocumentParser.extractCiClass(f);
                    ArrayList<CiFunction> funcs = PHPDocumentParser.extractFunctions(f);
                    docs.put(ciClass, funcs);
                }

                for (File f : helperFiles) {
                    CiClass ciClass = PHPDocumentParser.extractCiClass(f);
                    ArrayList<CiFunction> funcs = PHPDocumentParser.extractFunctions(f);
                    docs.put(ciClass, funcs);
                }

                this.ciDoc = docs;

            }
            catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
        
        return this.ciDoc;
    }
}
