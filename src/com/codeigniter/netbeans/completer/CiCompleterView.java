/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.completer;

import com.codeigniter.netbeans.shared.PHPDocumentParser;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 *
 * @author Tamaki_Sakura
 */
//@MimeRegistration(mimeType = "text/x-php5", service = CompletionProvider.class)
public class CiCompleterView extends CiCompleterProviderBase {

    @Override
    public CompletionTask createTask(int i, JTextComponent jtc) {
        //TBD
        AsyncCompletionQuery mViewCompletionQuery
                = new CiViewAsyncCompletionQuery();
        return new AsyncCompletionTask(mViewCompletionQuery, jtc);
    }

    @Override
    public int getAutoQueryTypes(JTextComponent jtc, String string) {
        //Have not finished yet, probably should change
        return 0;
    }
    
    public static class CiViewAsyncCompletionQuery extends AsyncCompletionQuery {
        @Override
        protected void query(CompletionResultSet set, Document doc, int offset) {
            TokenSequence<PHPTokenId> tokens = PHPDocumentParser.getTokenSequence(doc);
            if (tokens == null) {
                return;
            }
            
            tokens.move(offset);
            tokens.moveNext();
            Token<PHPTokenId> token = tokens.token();
            
            if (token.id() != PHPTokenId.PHP_CONSTANT_ENCAPSED_STRING) {
                return;
            }
            
            //Add Element
            set.finish();
        }
        
    }
}
