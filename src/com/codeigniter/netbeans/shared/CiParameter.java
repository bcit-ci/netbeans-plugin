/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.shared;

import java.io.Serializable;

/**
 *
 * @author dwoods
 */
class CiParameter implements Serializable {
    
    public String name;
    public String defaultValue; // String representation of the default value or null if no default value
    
    public CiParameter(String name) {
        this(name, null);
    }
    
    public CiParameter(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @return - The string representation of the default value, or null if there is no default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }
    
    @Override
    public String toString() {
        String retval = this.name;
        if (this.defaultValue != null) {
            retval = retval.concat(String.format(" = %s", this.defaultValue));
        }
        return retval;
    }
}
