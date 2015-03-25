/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.shared;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.EditableProperties;
import org.netbeans.spi.project.support.ant.PropertyUtils;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Mutex;

/**
 *
 * @author dwoods
 * 
 *  This class is designed to contain functions for searching for files on the File System, and other file related functions
 *  
 */
public abstract class FileExtractor {
    
    public static final String INCLUDE_PATH = "include.path";
    
    public static final String VIEW_PATH = "application/views/";
    public static final String MODEL_PATH = "application/models/";
    public static final String CONTROLLER_PATH = "application/controllers/";
    
    private static final String[] APP_BASE
            = {"cache", "config", "controllers", "core", "helpers", "models"};
    
    /**
     * 
     * @param dir - The directory to be searched
     * @param fileExts - The list of valid file extensions for the files to be returned. The "." should not be included
     *                   Pass null to return all files in the directory
     * @param recursive - Whether to search the directory recursively (will search all directories within the given directory)
     * @return - A List of File's matching the given file extensions
     */
    public static List<File> getFilesFromDirectory(File dir, List<String> fileExts, boolean recursive) throws IllegalArgumentException, IOException
    {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(String.format("%s is not a directory\n", dir.getAbsolutePath()));
        }
        
        List<File> retval = new LinkedList<File>();
        
        // Use a DirectoryStream to iterate over files in the directory
        // since JavaDoc recommends it for its efficiency over other methods
        DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir.toPath());
        Iterator<Path> it = dirStream.iterator();

        while (it.hasNext()) {
            File nextFile = it.next().toFile();
            if (nextFile.isDirectory() && recursive) {
                try {
                    retval.addAll(getFilesFromDirectory(nextFile, fileExts, recursive));
                }
                catch (IllegalArgumentException iae) {
                    System.err.println("Illegal Argument: Something went terribly wrong in getFilesFromDirectory()!");
                }
                catch (IOException ioe) {
                    System.err.printf("getFilesFromDirectory(): Unable to get files from %s\n", nextFile.getAbsolutePath());
                }
            }
            else if (nextFile.isFile()) {
                String ext = getFileExtensionType(nextFile);
                if (fileExts == null || fileExts.contains(ext)) {
                    retval.add(nextFile);
                }
            }
        }
        
        return retval;
    }
    
    /**
     * 
     * @param file
     * @return - The file's extension (not including the ".") or empty string if file doesn't have an extension.
     */
    public static String getFileExtensionType(File file) throws IllegalArgumentException
    {
        if (file.isDirectory()) {
            throw new IllegalArgumentException(String.format("%s is not a file\n", file.getAbsoluteFile()));
        }
            
        String retval = "";
        int index = file.getName().lastIndexOf(".");
        
        if (index != -1) {
            retval = file.getName().substring(index + 1);
        }
        
        return retval;
    }
    
    /**
     * 
     * @param file
     * @return True if the file extension is ".php"
     */
    public static boolean isPHPFile(File file) {
        boolean retval = false;
        
        try {
            retval = getFileExtensionType(file).equals("php");
        }
        catch (IllegalArgumentException iae) {
            // Do nothing... will return false
        }
        
        return retval;
    }
    
    /**
     * Get the CodeIgniter Root for any file in application folder
     * 
     * @param doc FileObject document
     * @return root
     */
    public static FileObject getCiRoot(FileObject doc) {
        //TODO: Need Talk
        //What is the best way to know the "root" directory of CodeIgniter?
        
        while (doc != null) {
            doc = doc.getParent();
            FileObject[] children = doc.getChildren();
            int count = 0;
            
            for (FileObject child: children) {
                for (String folder: APP_BASE) {
                    if (child
                            .getName()
                            .equals(folder.toLowerCase(Locale.ENGLISH))) {
                        count++;
                        break;
                    }
                }
            }
            
            if (count == APP_BASE.length) {
                break;
            }
        }
        
        if (doc == null) {
            return null;
        }
        
        FileObject root = doc.getParent();
        return root;
    }
    
    /**
     * Use the current PHp document as a pointer to add 
     * auto completion file into the project path
     * @param doc any document of the current project
     */
    public static void addCompleteToIncludePathFromDoc(final FileObject doc) {
        FileObject ciRoot = getCiRoot(doc);
        if (ciRoot == null) {
             return;
        }
        Project project = FileOwnerQuery.getOwner(ciRoot);
        if (project == null) {
             return;
        }
        
        addCompleteToIncludePath(ciRoot, project);            
    }
    
    /**
     * Add auto completion file into the project path
     * @param ciRoot Root of CodeIgniter Project
     * @param project the Project object of the PHP Project
     */
    public static void addCompleteToIncludePath(
            final FileObject ciRoot, final Project project) {
        ProjectManager.mutex().writeAccess(new Mutex.Action<Void>() {

            @Override
            public Void run() {
                try {
                    AntProjectHelper helper = project.getLookup().lookup(AntProjectHelper.class);
                    if (helper == null) {
                        return null;
                    }   
                    FileObject target = ciRoot.getFileObject();
                    
                    EditableProperties properties 
                            = helper.getProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH);
                    String currentPaths[] 
                            = PropertyUtils.tokenizePath((properties.getProperty(INCLUDE_PATH)));
                    ArrayList<String> newPaths 
                            = new ArrayList<String>(Arrays.asList(currentPaths));
                    
                    
                    
                    properties.setProperty(INCLUDE_PATH, newPaths.toArray(new String[0]));
                    helper.putProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH, properties);
                    ProjectManager.getDefault().saveProject(project);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
                return null;
            }
            
        });
    }  

}
