package com.zetcode;

import java.awt.BorderLayout;

import javax.swing.*;


public class Mines extends JFrame {
	private static final long serialVersionUID = 4772165125287256837L;
	
	private static final int WIDTH = 250;
    private static final int HEIGHT = 290;

    public Mines() {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Minesweeper");

        JLabel statusBar = new JLabel("");
        add(statusBar, BorderLayout.SOUTH);

        add(new Board(statusBar));

        setResizable(false);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new Mines();
    }
}