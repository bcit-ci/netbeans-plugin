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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author dwoods
 * 
 *  This class is designed to contain functions for searching for files on the File System, and other file related functions
 *  
 */
public abstract class FileExtractor {
    
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
}
