package com.tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.tetris.Shape.Tetrominoes;

public class Board extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	final int BoardWidth = 10;
	final int BoardHeight = 20;
	
	int curX, curY;
	Shape curPiece;
	boolean hasDropped = false;
	boolean isPaused = false;
	Timer timer;
	Tetrominoes[][] dropped;
	int numLinesRemoved = 0;
	JLabel statusBar;
	static Color[] colors = {new Color(0,0,0), new Color(204,102,102),
			new Color(102,204,102), new Color(102,102,204),
			new Color(204,204,102), new Color(204,102,204),
			new Color(102,204,204), new Color(218,170,0)};
//	static Color[] colors = {
//			Color.WHITE, Color.blue, Color.MAGENTA, Color.GREEN,
//			Color.PINK, Color.ORANGE, Color.YELLOW, Color.gray
//	};
	public Board(Tetris parent){
		setFocusable(true);
		timer = new Timer(400, this);
		dropped = new Tetrominoes[BoardWidth][BoardHeight];
		for(int i=0; i<BoardWidth; i++)
			Arrays.fill(dropped[i], Tetrominoes.NoShape);
		statusBar = parent.getStatusBar();
		addKeyListener(new TAdapter());
	}
	public void start(){
		curPiece = new Shape();
		newPiece();
		timer.start();
	}
	void pause() {
		if(!isPaused) {
			isPaused = true;
			statusBar.setText("paused");
			timer.stop();
		}
		else {
			isPaused = false;
			statusBar.setText(Integer.toString(numLinesRemoved));
			timer.start();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(hasDropped){
			newPiece();
		}else{
			oneLineDown();
		}
	}
	void newPiece(){
		curPiece.setRandomShape();
		curX = BoardWidth / 2;
		curY = BoardHeight - 1 - curPiece.getMaxY();
		hasDropped = false;
		if(!tryMove(curPiece, curX, curY)){
			timer.stop();
			statusBar.setText("Game over! "+Integer.toString(numLinesRemoved));
		}
	}
	void oneLineDown(){
		if(isPaused) return;
		boolean tryMove = tryMove(curPiece, curX, curY-1);
		if(!tryMove)
			pieceDropped();
		repaint();
	}
	void pieceDropped(){
		for(int[] coord : curPiece.getCoords()){
			int x = coord[0]+curX, y = coord[1]+curY;
			dropped[x][y] = curPiece.tetrominoes;
		}
		removeFullLines();
		hasDropped = true;
	}
	void dropDown(){
		while(!hasDropped){
			oneLineDown();
		}
	}
	void removeFullLines(){
		int count = 0;
		for(int j=BoardHeight-1; j>=0; j--){
			boolean lineIsFull = true;
			for(int i=0; i<BoardWidth; i++){
				if(dropped[i][j] == Tetrominoes.NoShape){
					lineIsFull = false;
				}
			}
			if(lineIsFull){
				count++;
				//move upper lines down by one line
				for(int k=j+1; k<BoardHeight; k++){
					for(int n=0; n<BoardWidth; n++)
						dropped[n][k-1] = dropped[n][k];
				}
				repaint();
			}
		}
		numLinesRemoved += count;
		statusBar.setText(Integer.toString(numLinesRemoved));
	}
	boolean tryMove(Shape s, int newX, int newY){
		if(isPaused) return false;
		for(int[] coord : curPiece.getCoords()){
			int x = coord[0]+newX, y = coord[1]+newY;
			if(x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight){
				System.out.println("cannot move more!"+ s.toString() + 
							 "->newX: " + newX + ", newY: " + newY);
				return false;
			}
			if(dropped[x][y] != Tetrominoes.NoShape){
				return false;
			}
		}
		curX = newX;
		curY = newY;
		repaint();
		return true;
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		//paint shapes already dropped
		for(int i=0; i<BoardWidth; i++){
			for(int j=0; j<BoardHeight; j++){
				Tetrominoes t = dropped[i][j];
				if(t != Tetrominoes.NoShape)
					drawSquare(g, t, i*squareWidth(), (BoardHeight-j-1)*squareHeight());
			}
		}
		for(int[] coord : curPiece.getCoords()){
			int x = coord[0], y = coord[1];
			drawSquare(g, curPiece.tetrominoes, (curX+x)*squareWidth(), (BoardHeight-1-curY-y)*squareHeight());
		}
    }
	void drawSquare(Graphics g, Tetrominoes t, int x, int y){
		Color c = colors[t.ordinal()];
		g.setColor(c);
		g.drawRect(x+1, y+1, squareWidth()-2, squareHeight()-2);
		g.fillRect(x+1, y+1, squareWidth()-2, squareHeight()-2);
		
		g.setColor(c.brighter());
		g.drawLine(x, y, x, y+squareHeight());
		g.drawLine(x, y, x+squareWidth(), y);
		
		g.setColor(c.darker());
		g.drawLine(x, y, x+squareWidth(), y);
		g.drawLine(x+squareWidth(), y, x+squareWidth(), y+squareHeight());
	}
	int squareWidth(){
		return (int) getSize().getWidth()/BoardWidth;
	}
	int squareHeight(){
		return (int) getSize().getHeight()/BoardHeight;
	}
	Shape rotate(){ //counterclockwise
		for(int[] coord : curPiece.coords){
			int x = coord[0], y = coord[1];
			int newX = curX - y, newY = curY + x;
			if(newX<0 || newX>=BoardWidth || newY<0 || newY>=BoardHeight) 
				return curPiece;
		}
		for(int[] coord : curPiece.coords){
			int x = coord[0], y = coord[1];
			coord[0] = -y;
			coord[1] = x;
		}
		return curPiece;
	}
	class TAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			int keyCode = e.getKeyCode();
			switch(keyCode){
			case KeyEvent.VK_LEFT:
				tryMove(curPiece, curX-1, curY);
				break;
			case KeyEvent.VK_RIGHT:
				tryMove(curPiece, curX+1, curY);
				break;
			case KeyEvent.VK_UP:
				tryMove(rotate(), curX, curY);
				break;
			case KeyEvent.VK_DOWN:
				dropDown();
				break;
			case KeyEvent.VK_P:
				pause();
				break;
			}
		}
	}
}
