/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.shared;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;

/**
 *
 * @author dwoods
 */
public abstract class PHPDocumentParser {
    
     /**
     * Get the token at offset in the given Document
     * 
     * @param doc document
     * @param offset offset in document
     * @return token
     */
    public static Token<PHPTokenId> getToken(Document doc, int offset) {
        TokenSequence<PHPTokenId> ts = getTokenSequence(doc);
        if (ts == null) {
            return null;
        }
        ts.move(offset);
        ts.moveNext();

        Token<PHPTokenId> token = ts.token();
        
        return token;                
    }
    
    /**
     * Get the TokenSequence for the full document
     *
     * @param doc document
     * @return tokens or null in the event of an error
     */
    public static TokenSequence<PHPTokenId> getTokenSequence(Document doc) {
        AbstractDocument absDoc = (AbstractDocument) doc;
        absDoc.readLock();
        TokenSequence<PHPTokenId> tokens = null;
        try {
            TokenHierarchy<Document> hierarchy = TokenHierarchy.get(doc);
            tokens = hierarchy.tokenSequence(PHPTokenId.language());
        } finally {
            absDoc.readUnlock();
        }
        return tokens;
    }
    
     /**
     *  Gets the TokenSequence from the specified file
     * 
     * @param file
     * @return TokenSequence for the file
     */
    public static TokenSequence<PHPTokenId> getTokensFromFile(File file) throws FileNotFoundException {
        TokenSequence<PHPTokenId> retval = null;

        if (file.getName().endsWith(".php")) {
            throw new IllegalArgumentException(
                    String.format("%s is not a PHP file\n", file.getPath()));
        }
        
        BufferedReader reader = new BufferedReader(new FileReader(file));
        TokenHierarchy th = TokenHierarchy.create(reader, PHPTokenId.language(), null, null);        
        retval = th.tokenSequence();
        
        try {
            reader.close();
        }
        catch (IOException ioe) {
           System.err.println("Unable to close reader in getTokensFromFile()\n"); 
        }
        
        return retval;
    }
}
