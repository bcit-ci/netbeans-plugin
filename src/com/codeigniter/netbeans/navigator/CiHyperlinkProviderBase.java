/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.navigator;

import java.util.EnumSet;
import java.util.Set;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProviderExt;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkType;

/**
 *
 * @author Tamaki_Sakura
 */
public abstract class CiHyperlinkProviderBase implements HyperlinkProviderExt {
   
    @Override
    public Set<HyperlinkType> getSupportedHyperlinkTypes() {
        return EnumSet.of(HyperlinkType.GO_TO_DECLARATION);
    }

    @Override
    public String getTooltipText(Document doc, int offset, HyperlinkType ht) {
        //TODO: A general Tooltip
        return null;
    }
    
    /**
     * Get the TokenSequence for the full documentation
     *
     * @param doc document
     * @return tokens
     */
    public static TokenSequence<PHPTokenId> getTokenSequence(Document doc) {
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
    public static String getTokenString(Document doc, int offset) {
        TokenSequence<PHPTokenId> ts = getTokenSequence(doc);
        if (ts == null) {
            return null;
        }
        ts.move(offset);
        ts.moveNext();
        int newOffset = ts.offset();

        Token<PHPTokenId> token = ts.token();
        
        String target = token.text().toString();
        PHPTokenId id = token.id();
        if (id == PHPTokenId.PHP_CONSTANT_ENCAPSED_STRING) {
            if (target.length() > 2) {
                target = target.substring(1, target.length() - 1);
            } else {
                target = null;
            }
        } else {
            target = null;
        }
        return target;
    }
}
