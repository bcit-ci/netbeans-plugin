/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.completer;

import com.codeigniter.netbeans.shared.FileExtractor;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Tamaki_Sakura
 */
public abstract class CiCompleterProviderBase implements CompletionProvider {

    
    public void initCodeComplete(JTextComponent jtc) {
        Document doc = jtc.getDocument();
        FileObject docObject = NbEditorUtilities.getFileObject(doc);
        
        FileExtractor.addCompleteToIncludePathFromDoc(docObject);
    }
}
