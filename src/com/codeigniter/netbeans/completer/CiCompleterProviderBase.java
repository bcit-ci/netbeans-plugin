/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.completer;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.spi.editor.completion.CompletionProvider;

/**
 *
 * @author Tamaki_Sakura
 */
public abstract class CiCompleterProviderBase implements CompletionProvider {
    
    /**
     * Get Document from JTextComponent
     * 
     * @param jtc JTextComponent
     * @return doc
     */
    protected Document getDocFromComponent(JTextComponent jtc) {
        return jtc.getDocument();
    }
}
