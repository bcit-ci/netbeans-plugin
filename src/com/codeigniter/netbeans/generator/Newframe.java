/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeigniter.netbeans.generator;


import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.JFrame;


/**
 *
 * @author Maxence
 */
public class Newframe extends JFrame {
    private final GenPanel p1;
    private final Container cp;
    public Newframe(String s){
        super(s);
        setSize(400, 200);
        p1 = new GenPanel();
        cp = getContentPane();
        cp.setLayout(new FlowLayout());
        cp.add(p1);
        setVisible(true);
    }
    
}
