/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.navigator;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProviderExt;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkType;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Tamaki_Sakura
 */
public abstract class CiHyperlinkProviderBase implements HyperlinkProviderExt {
   
    private static final String[] APP_BASE
            = {"cache", "config", "controllers", "core", "helpers", "models"};
    
    private int targetStart;
    private int targetEnd;
    
    @Override
    public Set<HyperlinkType> getSupportedHyperlinkTypes() {
        return EnumSet.of(HyperlinkType.GO_TO_DECLARATION);
    }
    
    @Override
    public int[] getHyperlinkSpan(Document doc, int offset, HyperlinkType ht) {
        if ((targetStart != 0) && (targetEnd != 0)) {
            return new int[]{targetStart, targetEnd};
        }
        return null;
    }

    /**
     * Get the TokenSequence for the full documentation
     *
     * @param doc document
     * @return tokens
     */
    protected TokenSequence<PHPTokenId> getTokenSequence(Document doc) {
        AbstractDocument absDoc = (AbstractDocument) doc;
        absDoc.readLock();
        TokenSequence<PHPTokenId> tokens;
        
        try {
            TokenHierarchy<Document> hierarchy = TokenHierarchy.get(doc);
            tokens = hierarchy.tokenSequence(PHPTokenId.language());
        } finally {
            absDoc.readUnlock();
        }
        
        return tokens;
    }
    
    /**
     * Get the String of the current Token
     * 
     * @param doc document
     * @param offset offset in document
     * @return target
     */
    protected String getStringTokenString(Document doc, int offset) {
        TokenSequence<PHPTokenId>tokens = getTokenSequence(doc);
        if (tokens == null) {
            return null;
        }
        
        resetLengthValue();
        
        tokens.move(offset);
        tokens.moveNext();
        int newOffset = tokens.offset();
        
        Token<PHPTokenId> token = tokens.token();
        
        String target = token.text().toString();
        PHPTokenId id = token.id();
        if (id == PHPTokenId.PHP_CONSTANT_ENCAPSED_STRING) {
            if (target.length() > 2) {
                target = target.substring(1, target.length() - 1);
                targetStart = newOffset + 1;
                targetEnd = targetStart + target.length();
            } else {
                target = null;
            }
        } else {
            target = null;
        }
        
        return target;
    }
    
    /**
     * Rest the targetStart/End value
     */
    private void resetLengthValue() {
        targetStart = 0;
        targetEnd = 0;
    }
    
    /**
     * Get the CodeIgniter Root for any file in application folder
     * 
     * @param doc FileObject document
     * @return root
     */
    protected FileObject getCiRoot(FileObject doc) {
        //TODO: Need Talk
        //What is the best way to know the "root" directory of CodeIgniter?
        
        while (doc != null) {
            doc = doc.getParent();
            FileObject[] children = doc.getChildren();
            int count = 0;
            
            for (FileObject child: children) {
                for (String folder: APP_BASE) {
                    if (child
                            .getName()
                            .equals(folder.toLowerCase(Locale.ENGLISH))) {
                        count++;
                        break;
                    }
                }
            }
            
            if (count == APP_BASE.length) {
                break;
            }
        }
        
        if (doc == null) {
            return null;
        }
        
        FileObject root = doc.getParent();
        return root;
    }
}
