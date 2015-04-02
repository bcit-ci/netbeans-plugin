/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.navigator;

import com.codeigniter.netbeans.shared.FileExtractor;
import java.io.File;
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
public class CiHyperlinkProviderToFile extends CiHyperlinkProviderBase {
    
    private FileObject target;
    
    @Override
    public boolean isHyperlinkPoint(Document doc, int offset, HyperlinkType ht) {
        target = null;
        FileObject docObject = NbEditorUtilities.getFileObject(doc);
        
        if (docObject == null) {
            return false;
        }
        
        String extendedPath = getStringTokenString(doc, offset);
        if (extendedPath == null) {
            return false;
        }
        
        String targetBases[] = {
            FileExtractor.VIEW_PATH, 
            FileExtractor.MODEL_PATH};
        
        return getFileFromBase(targetBases, extendedPath, docObject);
    }

    @Override
    public String getTooltipText(Document doc, int offset, HyperlinkType ht) {
        //TODO: Implement a Tooltip based on the path
        return null;
    }
    
    @Override
    public void performClickAction(Document doc, int offset, HyperlinkType ht) {
        if (target != null) {
            UiUtils.open(target, 0);
        }
    }
    
    /**
     * 
     * @param targetBases a selection of possible bases
     * @param extendedPath the extended path from base
     * @param docObject the Fileobject of current document
     * @return if there is a file from the selected bases
     */
    private boolean getFileFromBase(
            String[] targetBases, String extendedPath, FileObject docObject) {
        
        for (String base: targetBases) {
            String filePath = base + extendedPath + ".php";

            FileObject parent = FileExtractor.getCiRoot(docObject);
            if (parent == null) {
                continue;
            }
            FileObject targetFileObject = parent.getFileObject(filePath);
            if (targetFileObject == null) {
                continue;
            }

            File targetFile = new File(targetFileObject.getPath());

            if (targetFile.exists()) {
                target = targetFileObject;
                return true;
            }
        }
        
        return false;
    }

}
