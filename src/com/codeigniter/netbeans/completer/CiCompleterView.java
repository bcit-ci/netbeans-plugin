/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.completer;

import com.codeigniter.netbeans.shared.FileExtractor;
import com.codeigniter.netbeans.shared.PHPDocumentParser;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 *
 * @author Tamaki_Sakura
 */
//@MimeRegistration(mimeType = "text/x-php5", service = CompletionProvider.class)
public class CiCompleterView extends CiCompleterProviderBase {

    @Override
    public CompletionTask createTask(int i, JTextComponent jtc) {
        //TODO: We may have to do Something
        AsyncCompletionQuery mViewCompletionQuery
                = new CiViewAsyncCompletionQuery();
        return new AsyncCompletionTask(mViewCompletionQuery, jtc);
    }

    @Override
    public int getAutoQueryTypes(JTextComponent jtc, String string) {
        //TODO: We may have to do Something
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
            
            FileObject docObject = NbEditorUtilities.getFileObject(doc);
            if (docObject == null) {
                return;
            }
            FileObject parent = FileExtractor.getCiRoot(docObject);
            if (parent == null) {
                return;
            }
            
            int startOffset = tokens.offset() + 1;
            int removeLength = token.length() - 2;
                 
            String path = FileExtractor.VIEW_PATH + parent.getPath();
            List<String> phpExtention = new ArrayList<String>();
            phpExtention.add("php");
            try {
                //TODO: Due to the problem of gFFD, its recursive functionality is
                //not good for my application. Think about changing it...
                List<File> viewFile = FileExtractor.getFilesFromDirectory(
                        new File(path), phpExtention, false);
                
                for (File vf: viewFile) {
                    CiViewCompletionItem vfci = 
                            new CiViewCompletionItem(vf.getName(), 
                                    startOffset, removeLength);
                    //TODO: We may have to do Something
                    set.addItem(vfci);
                }
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
                   
            set.finish();
        }
        
    }
    
    public static class CiViewCompletionItem implements CompletionItem {

        private String text;
        private int startIndex;
        private int removeLength;
        
        public CiViewCompletionItem(String view, int start, int remove) {
            text = view;
            startIndex = start;
            removeLength = remove;
        }
        
        @Override
        public void defaultAction(JTextComponent jtc) {
            try {
                final StyledDocument doc = (StyledDocument) jtc.getDocument();
                // Replace existing text with complete frase.
                doc.remove(startIndex, removeLength);
                doc.insertString(startIndex, text, null);
                jtc.setCaretPosition(startIndex + text.length());
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
            
            Completion.get().hideAll();
        }

        @Override
        public void processKeyEvent(KeyEvent ke) {
        }

        @Override
        public int getPreferredWidth(Graphics grphcs, Font font) {
            return CompletionUtilities.getPreferredWidth(text, null, grphcs, font);
        }

        @Override
        public void render(Graphics grphcs, Font font, Color color, Color color1, int i, int i1, boolean bln) {
            //TODO: Add Icon here
            CompletionUtilities.renderHtml(null, 
                    text, null, grphcs, font, color, i, i1, bln);
        }

        @Override
        public CompletionTask createDocumentationTask() {
            return null;
        }

        @Override
        public CompletionTask createToolTipTask() {
            return null;
        }

        @Override
        public boolean instantSubstitution(JTextComponent jtc) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getSortPriority() {
            return 0;
        }

        @Override
        public CharSequence getSortText() {
            return text;
        }

        @Override
        public CharSequence getInsertPrefix() {
            return text;
        }
        
    }
}
