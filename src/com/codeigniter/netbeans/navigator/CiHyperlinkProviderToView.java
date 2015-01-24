/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.navigator;

import javax.swing.text.Document;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkType;
import org.netbeans.modules.csl.api.UiUtils;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Tamaki_Sakura
 */
public class CiHyperlinkProviderToView extends CiHyperlinkProviderBase {
    
    private FileObject view;
    
    @Override
    public boolean isHyperlinkPoint(Document doc, int offset, HyperlinkType ht) {
        String token = getTokenString(doc, offset);
        if (token == null) {
            return false;
        }
        
        FileObject parent;
        // Get Path
        
    }

    @Override
    public int[] getHyperlinkSpan(Document doc, int offset, HyperlinkType ht) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void performClickAction(Document doc, int offset, HyperlinkType ht) {
        if (view != null) {
            UiUtils.open(view, 0);
        }
    }

}
