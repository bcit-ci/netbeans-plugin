/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.documentation;

import com.codeigniter.netbeans.navigator.CiHyperlinkProviderBase;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.editor.BaseDocument;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.netbeans.editor.Utilities;
import org.netbeans.editor.ext.ExtKit;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;

@ActionID(
        category = "Help",
        id = "com.codeigniter.netbeans.documentation.GoToDocumentationAction"
)
@ActionRegistration(
        displayName = "Go to Documentation"
)
@ActionReferences({
    @ActionReference(path = "Editors/text/x-php5/Popup", position = 550)
})

@Messages("CTL_GoToDocumentationAction=Go to Documentation")
public final class GoToDocumentationAction extends ExtKit.GotoAction{

    private final static String CI_DOC_BASE_URL = "http://www.codeigniter.com/userguide3/libraries/security.html?#CI_Security.sanitize_filename";
    
    @Override
    public void actionPerformed(ActionEvent evt, JTextComponent target) {
        System.out.println("Entered actionPerformed()");
        
        int offset = target.getCaretPosition();
        
        Document doc = target.getDocument();
        if (doc == null) {
            System.err.println("Unable to get active document. Returning...");
            return;
        }                  
        
        String token = getTokenString(doc, offset);
        System.out.printf("Carat is pointing to %s\n", token);
    }
    
    @Override
    protected int getOffsetFromLine(BaseDocument doc, int lineOffset)
    {
        int retval = super.getOffsetFromLine(doc, lineOffset);
        System.out.printf("Returning %d from GoToDocumentationAction.getOffsetFromLine()", retval);
        return retval;
    }
    
    /**
     * Get the TokenSequence for the full documentation
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

        Token<PHPTokenId> token = ts.token();
        
        String target = token.text().toString();
        
        return target;
    }
}
