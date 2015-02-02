/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.shared;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author dwoods
 */
public class CiFunction implements Serializable {
    
    private String method;
    private Vector<CiParameter> parameters;
    
    public CiFunction(String method) {
        this(method, new Vector<CiParameter>());
    }
    
    public CiFunction(String method, Vector<CiParameter> parameters) {
        assert(parameters != null);
        
        this.method = method;
        this.parameters = parameters;
    }
    
    public String getMethodName() {
        return method;
    }
    
    public Vector<CiParameter> getParameters() {
        // Defensive copy to prevent external modification
        return new Vector<CiParameter>(this.parameters);
    }
    
    public void addParameter(CiParameter param) {
        assert(this.parameters != null);
        this.parameters.add(param);
    }
    
    @Override
    public String toString() {
        String retval = String.format("%s(", this.method);
        boolean first = true;
        
        for (CiParameter param : this.parameters) {
            if (!first) {
                retval = retval.concat(", ");                
            }
            retval = retval.concat(param.toString());            
            first = false;
        }
        
        return retval.concat(")");        
    }
}
