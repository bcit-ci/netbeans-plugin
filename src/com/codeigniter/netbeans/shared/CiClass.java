/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.shared;

import java.io.Serializable;
import java.net.URL;

/**
 *
 * @author dwoods
 */
public class CiClass implements Serializable {
    
    private URL documentationLink;
    private String name;
    
    public CiClass(String name) {
        this(name, null);
    }
    
    public CiClass(String name, URL documentationLink) {
        assert(name != null);
        this.name = name;
        this.documentationLink = documentationLink;
    }
    
    public URL getDocumentationLink() {
        return this.documentationLink;
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
}
