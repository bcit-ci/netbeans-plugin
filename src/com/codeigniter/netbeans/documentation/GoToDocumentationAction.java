/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.documentation;

import com.codeigniter.netbeans.shared.CentralManager;
import com.codeigniter.netbeans.shared.CiClass;
import com.codeigniter.netbeans.shared.CiFunction;
import static com.codeigniter.netbeans.shared.PHPDocumentParser.*;
import java.awt.event.ActionEvent;
import java.util.Dictionary;
import java.util.List;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.lexer.Token;
import org.netbeans.editor.BaseDocument;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.netbeans.editor.ext.ExtKit;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;

@ActionID(
        category = "Help",
        id = "com.codeigniter.netbeans.documentation.GoToDocumentationAction"
)
@ActionRegistration(
        displayName = "Go to CI documentation"
)
@ActionReferences({
    @ActionReference(path = "Editors/text/x-php5/Popup", position = 550)
})

@Messages("CTL_GoToDocumentationAction=Go to Documentation")
public final class GoToDocumentationAction extends ExtKit.GotoAction{

    private final static String CI_DOC_BASE_URL = "http://www.codeigniter.com/userguide3/libraries/security.html?#CI_Security.sanitize_filename";
    
    @Override
    public void actionPerformed(ActionEvent evt, JTextComponent target) {       
        int offset = target.getCaretPosition();
        Document doc = target.getDocument();
        
        if (doc == null) {
            System.err.println("Unable to get active document. Returning...");
            return;
        }                  
        
        Token<PHPTokenId> token = getToken(doc, offset);
        System.out.printf("Carat is pointing to %s\n", token.text().toString());
        System.out.printf("Token type is %s\n", token.id().toString());
        
        Dictionary<CiClass,List<CiFunction>> d = CentralManager.Instance().getCIDocFunctions();
    }
    
    @Override
    protected int getOffsetFromLine(BaseDocument doc, int lineOffset)
    {
        int retval = super.getOffsetFromLine(doc, lineOffset);
        System.out.printf("Returning %d from GoToDocumentationAction.getOffsetFromLine()", retval);
        return retval;
    } 

}
