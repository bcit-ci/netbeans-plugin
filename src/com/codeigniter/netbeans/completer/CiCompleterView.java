/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.completer;

import com.codeigniter.netbeans.shared.PHPDocumentParser;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;
import org.netbeans.spi.editor.completion.CompletionTask;

/**
 *
 * @author Tamaki_Sakura
 */
//@MimeRegistration
public class CiCompleterView extends CiCompleterProviderBase {

    @Override
    public CompletionTask createTask(int i, JTextComponent jtc) {
        Document doc = getDocFromComponent(jtc);
        TokenSequence<PHPTokenId> tokens = PHPDocumentParser.getTokenSequence(doc);
        if (tokens == null) {
            return null;
        }
        //TBD
        
        return null;
    }

    @Override
    public int getAutoQueryTypes(JTextComponent jtc, String string) {
        //TBD
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
