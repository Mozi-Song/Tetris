package com.tetris;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
//http://zetcode.com/tutorials/javagamestutorial/tetris/
public class Tetris extends JFrame{

	JLabel statusBar;
	
	public Tetris(){
		statusBar = new JLabel("0");
		add(statusBar, BorderLayout.SOUTH);
		Board board = new Board(this);
		add(board);
		board.start();
		
		setSize(200, 400);
	}
	public JLabel getStatusBar() {
		return statusBar;
	}
	
	public static void main(String[] args){
		Tetris t = new Tetris();
		t.setLocationRelativeTo(null);
		t.setVisible(true);
	}
	
}
