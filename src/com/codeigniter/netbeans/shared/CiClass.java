/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.shared;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *  CiClass contains information about a Code Igniter class that's useful for navigating to its documentation page
 * @author dwoods
 */
public class CiClass implements Serializable {
    
    private URL documentationLink;
    private String name;
    private ArrayList<CiFunction> functions;
    
    public CiClass(String name) {
        this(name, null);
    }
    
    public CiClass(String name, URL documentationLink) {
        assert(name != null);
        this.name = name;
        this.documentationLink = documentationLink;
        this.functions = new ArrayList<CiFunction>();
    }
    
    public void addFunction(CiFunction function) {
        this.functions.add(function);
    }
    
    public void setFunctions(ArrayList<CiFunction> functions) {
        this.functions = functions;
    }
    
    public URL getDocumentationLink() {
        return this.documentationLink;
    }
    
    public ArrayList<CiFunction> getFunctions() {
        return this.functions;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        String retval = this.name;
        if (this.documentationLink != null) {
            retval = retval.concat(String.format(" : %s", this.documentationLink.toString()));
        }
        return retval;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
