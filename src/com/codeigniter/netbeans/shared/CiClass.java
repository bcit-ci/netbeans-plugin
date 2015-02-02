/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.shared;

import java.net.URL;

/**
 *
 * @author dwoods
 */
public class CiClass {
    
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
}
