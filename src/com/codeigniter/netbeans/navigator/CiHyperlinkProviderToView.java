/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.navigator;

import com.codeigniter.netbeans.shared.FileExtractor;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProviderExt;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkType;
import org.netbeans.modules.csl.api.UiUtils;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Tamaki_Sakura
 */
@MimeRegistration(mimeType = "text/x-php5", service = HyperlinkProviderExt.class)
public class CiHyperlinkProviderToView extends CiHyperlinkProviderBase {
    
    private FileObject view;
    
    @Override
    public boolean isHyperlinkPoint(Document doc, int offset, HyperlinkType ht) {
        view = null;
        FileObject docObject = NbEditorUtilities.getFileObject(doc);
        
        if (docObject == null) {
            return false;
        }
        
        String extendedPath = getStringTokenString(doc, offset);
        if (extendedPath == null) {
            return false;
        }
        extendedPath = FileExtractor.VIEW_PATH + extendedPath + ".php";
        
        FileObject parent = FileExtractor.getCiRoot(docObject);
        if (parent == null) {
            return false;
        }
        view = parent.getFileObject(extendedPath);
        
        return true;
    }

    @Override
    public String getTooltipText(Document doc, int offset, HyperlinkType ht) {
        //TODO: Implement a Tooltip based on the path
        return null;
    }
    
    @Override
    public void performClickAction(Document doc, int offset, HyperlinkType ht) {
        if (view != null) {
            UiUtils.open(view, 0);
        }
    }

}
