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
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;
import org.netbeans.spi.lexer.LanguageHierarchy;

/**
 *
 * @author dwoods
 */
public abstract class PHPDocumentParser {

    /**
     *  Gets the TokenSequence from the specified file
     * 
     * @param file
     * @return TokenSequence for the file
     */
    public static TokenSequence<PHPTokenId> getTokensFromFile(File file) throws FileNotFoundException {
        TokenSequence<PHPTokenId> retval = null;

        if (file.getPath().endsWith(".php")) {
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
