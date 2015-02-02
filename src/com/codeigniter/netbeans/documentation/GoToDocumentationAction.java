/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.documentation;

import com.codeigniter.netbeans.shared.CentralManager;
import com.codeigniter.netbeans.shared.CiClass;
import static com.codeigniter.netbeans.shared.PHPDocumentParser.*;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
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
import org.openide.awt.HtmlBrowser.URLDisplayer;

@ActionID(
        category = "Help",
        id = "com.codeigniter.netbeans.documentation.GoToDocumentationAction"
)
@ActionRegistration(
        displayName = "Go to CI documentation"
)
@ActionReferences({
    @ActionReference(path = "Shortcuts", name = "CA-D"),
    @ActionReference(path = "Editors/text/x-php5/Popup", position = 550)    
})

@Messages("CTL_GoToDocumentationAction=Go to Documentation")
public final class GoToDocumentationAction extends ExtKit.GotoAction {

    private final static String CI_DOC_BASE_URL = "http://www.codeigniter.com/userguide3/libraries/security.html?#CI_Security.sanitize_filename";
    private final static String CI_DOC_SEARCH_BASE = "http://www.codeigniter.com/userguide3/search.html?q=";
    private final static String CI_DOC_SEARCH_SUFFIX = "&check_keywords=yes&area=default";

    @Override
    public void actionPerformed(ActionEvent evt, JTextComponent target) {
        int offset = target.getCaretPosition();
        Document doc = target.getDocument();

        if (doc == null) {
            System.err.println("Unable to get active document. Returning...");
            return;
        }

        Token<PHPTokenId> token = getToken(doc, offset);
        String tokenString = token.text().toString();

        Hashtable<String, List<CiClass>> ciFunctions = CentralManager.Instance().getCiFunctions();
        if (ciFunctions.containsKey(tokenString)) {
            List<CiClass> applicableClasses = ciFunctions.get(tokenString);
            if (applicableClasses.size() > 1) {
                // More than 1 possible function. Send user to the search
                goToSearchURL(tokenString);
            } 
            else if (applicableClasses.size() == 1) {
                // Send the user directly to the function in the documentation                                
                CiClass ciClass = applicableClasses.get(0);
                if (ciClass.getDocumentationLink() == null) {
                    // Don't have a link for this class. Go to search
                    goToSearchURL(tokenString);
                }
                else {
                    goToDocumentation(ciClass, tokenString);
                }                                              
            } 
            else {
                assert (false); // List of classes should never be empty 
            }
        } 
        else {
            System.out.println("\007");
        }

    }

    @Override
    protected int getOffsetFromLine(BaseDocument doc, int lineOffset) {
        int retval = super.getOffsetFromLine(doc, lineOffset);
        System.out.printf("Returning %d from GoToDocumentationAction.getOffsetFromLine()", retval);
        return retval;
    }

    private void goToDocumentation(CiClass ciClass, String methodName) {
        try {
            URL url = new URL(String.format("%s?#%s.%s", ciClass.getDocumentationLink().toString(), 
                    ciClass.getName(), methodName));
            System.out.printf("Going to URL: %s\n", url.toString());
            URLDisplayer.getDefault().showURL(url);
        } 
        catch (MalformedURLException mue) {
            System.out.println("\007"); // Play the error sound
            mue.printStackTrace(System.err);
        }
    }            
    
    private void goToSearchURL(String methodName) {
        try {
            URL url = new URL(CI_DOC_SEARCH_BASE + methodName + CI_DOC_SEARCH_SUFFIX);
            System.out.printf("Going to URL: %s\n", url.toString());
            URLDisplayer.getDefault().showURL(url);
        } 
        catch (MalformedURLException mue) {
            System.out.println("\007"); // Play the error sound
            mue.printStackTrace(System.err);
        }
    }
}
