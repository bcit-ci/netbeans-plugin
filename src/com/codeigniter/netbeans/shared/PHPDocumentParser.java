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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;

/**
 *
 * @author dwoods
 */
public abstract class PHPDocumentParser {
    
    private static final String ciDocBase = "http://codeigniter.com/user_guide";
    
    /**
     *  Attempts to extract the class name and CI documentation URL from the file.
     *  Note: Assumes there is at most 1 class per file. Will take the first class name encountered in the event of multiple classes
     * @param file
     * @return - CiClass for the file. The filename will be used as the className if no class is 
     *           declared in the file (E.g. a helper file)
     * @throws FileNotFoundException, IllegalArgumentException 
     */
    public static CiClass extractCiClass(File file) 
            throws FileNotFoundException, IllegalArgumentException {
        if (!FileExtractor.isPHPFile(file)) {
            throw new IllegalArgumentException(String.format("%s is not a .php file\n", file.getAbsolutePath()));
        }
        
        CiClass retval = null;
        URL docLink = null;
        String className = null;
        TokenSequence<PHPTokenId> tokenSeq = getTokensFromFile(file);
        
        if (tokenSeq != null) {
            boolean done = false;
            
            while (!done && tokenSeq.moveNext()) {
                // Search for URL in the starting comment blocks
                Token token = tokenSeq.token();
                
                if (token.id().equals(PHPTokenId.PHPDOC_COMMENT)) {
                    String docComment = token.text().toString();
                    String[] arr = docComment.split("\\s+");
                    
                    for (String s : arr) {
                        if (s.startsWith(ciDocBase)) {
                            try {
                                // Update all links to point to the user_guide 3
                                s = s.replaceFirst("/user_guide/", "/userguide3/");
                                /* If http:// is used instead of http://www. then the code igniter website
                                   won't open at the specified function.
                                   For example: http://codeigniter.com/userguide3/libraries/table.html?#CI_Table.make_columns
                                   won't open at the location where make_columns is documented. It works if http://www. is used instead though                                
                                */
                                s = s.replaceFirst("http://", "http://www.");
                                docLink = new URL(s);
                            }
                            catch (MalformedURLException mue) {
                                mue.printStackTrace(System.err);
                            }
                            
                            // Done if the className has already been found, regardless of the success/failure of the URL
                            done = (className != null);
                            break;
                        }
                    }
                }
                else if (token.id().equals(PHPTokenId.PHP_CLASS)) {
                    // Find the first string after the word "class"
                    while (tokenSeq.moveNext()) {
                        if (tokenSeq.token().id().equals(PHPTokenId.PHP_STRING)) {
                            className = tokenSeq.token().text().toString();
                            done = (docLink != null);
                            break;
                        }
                    }
                }
            }
            
            // If a class name couldn't be found, use the file name without the extension
            if (className == null) {
                int index = file.getName().lastIndexOf(".");
                className = file.getName().substring(0, index);
            }
            retval = new CiClass(className, docLink);
        }

        return retval;
    }
    
    /**
     *  Extracts all public functions from the PHP file 
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IllegalArgumentException 
     */
    public static ArrayList<CiFunction> extractFunctions(File file) 
            throws FileNotFoundException, IllegalArgumentException {
        
        if (!FileExtractor.isPHPFile(file)) {
            throw new IllegalArgumentException(String.format("%s is not a .php file\n", file.getAbsolutePath()));
        }
        
        ArrayList<CiFunction> retval = new ArrayList<CiFunction>();
        TokenSequence<PHPTokenId> tokenSeq = getTokensFromFile(file);
        
        if (tokenSeq != null) {            
            while (tokenSeq.moveNext()) {
                Token token = tokenSeq.token();
                
                if (token.id().equals(PHPTokenId.PHP_FUNCTION)) {
                    // Found a new function
                    int backCount = 1; // Number of movePrevious() calls
                    boolean isPublic = true;
                    
                    // Walk backwards to see if the function is private or protected
                    while (tokenSeq.movePrevious()) {
                        PHPTokenId id = tokenSeq.token().id();
                                                
                        if (!id.equals(PHPTokenId.WHITESPACE)) {
                            if (id.equals(PHPTokenId.PHP_PROTECTED) || id.equals(PHPTokenId.PHP_PRIVATE)) {
                                isPublic = false;
                            }
                            break;
                        }                                                                        
                                       
                        backCount++;    
                    }
                    
                    // Move the token sequence back to pointing to the "function" keyword
                    for (int i = 0; i < backCount; i++) {
                        tokenSeq.moveNext();
                    }
                    
                    if (isPublic) {
                        String funcName = null;
                        // Get the function name
                        while (tokenSeq.moveNext()) {
                            if (tokenSeq.token().id().equals(PHPTokenId.PHP_STRING)) {
                                funcName = tokenSeq.token().text().toString();
                                break;
                            }
                        }
                        
                        if (funcName != null) {
                            // Create a string starting after the first "(" and ending before the closing ")"
                            StringBuilder stringBuilder = new StringBuilder();
                            // Move tokenSeq to after the first "("
                            while (tokenSeq.moveNext()) {
                                if (tokenSeq.token().text().equals("(")) {
                                    break;
                                }
                            }
                            
                            int count = 1; // Keep a count of unpaired brackets. When at 0 we've found the closing ")"
                            while (tokenSeq.moveNext()) {
                                if (tokenSeq.token().text().equals(")")) {
                                    count--;
                                }
                                else if (tokenSeq.token().text().equals("(")) {
                                    count++;
                                }
                                
                                if (count == 0) {
                                    break;
                                }
                                else {
                                    stringBuilder = stringBuilder.append(tokenSeq.token().text());
                                }
                            }
                            
                            Vector<CiParameter> params = parseParameterString(stringBuilder.toString());
                            CiFunction func = new CiFunction(funcName, params);
                            
                            retval.add(func);
                        }
                    }                   
                }
            }
        }
        
        return retval;
    } 
    
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

        if (!FileExtractor.isPHPFile(file)) {
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
    
    /**
     * 
     * @param str
     * @return 
     */
    private static Vector<CiParameter> parseParameterString(String str) {
        Vector<CiParameter> retval = new Vector<CiParameter>();
        String[] params = str.split(",");
        
        for (String param : params) {
            String[] nameAndDefault = param.split("=");            
            if (nameAndDefault.length == 1) {
                retval.add(new CiParameter(nameAndDefault[0].trim()));
            }
            else if (nameAndDefault.length == 2) {
                retval.add(new CiParameter(nameAndDefault[0].trim(), nameAndDefault[1].trim()));
            }
            else {
                assert(false); // Shouldn't ever have more than 2 items
            }
        }
        
        return retval;
    }
}
