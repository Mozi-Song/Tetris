package com.tetris;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
//http://zetcode.com/tutorials/javagamestutorial/tetris/
public class Tetris extends JFrame{
	private static final long serialVersionUID = 1L;
	JLabel statusBar;
	
	public Tetris(){
		statusBar = new JLabel("0");
		statusBar.setSize(200, 5);
		statusBar.setVerticalTextPosition(JLabel.BOTTOM);
		statusBar.setHorizontalTextPosition(JLabel.LEFT);
		add(statusBar, BorderLayout.SOUTH);
		Board board = new Board(this);
		board.setPreferredSize(new Dimension(200,400));
//		Border blackline = BorderFactory.createLineBorder(Color.black);
//		board.setBorder(blackline);
		getContentPane().add(board);
		pack();
		board.start();
		
//		setSize(220, 400);
		setVisible(true);
//		getContentPane().setSize(200, 400);
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
