package com.tetris;

import java.util.Random;

public class Shape {
	enum Tetrominoes{
		NoShape, L, S, RefL, RefS, Line, Square, T
	}
	Tetrominoes tetrominoes;
	int[][] coords;
	static int[][][] reference = new int[][][]{
		{{0,0}, {0,0}, {0,0}, {0,0}}, //NoShape
		{{0,0}, {1,0}, {0,1}, {0,2}}, //L
		{{0,0}, {-1,0},{0,1}, {1,1}}, //S
		{{0,0}, {0,-1},{0,1}, {0,2}}, //RefL
		{{0,0}, {1,0}, {0,1}, {-1,1}}, //RefS
		{{0,-2},{0,-1},{0,0}, {0,1}}, //Line
		{{0,0}, {0,1}, {1,0}, {1,1}}, //Square
		{{0,0}, {1,0}, {0,1}, {-1,0}}  //T
	};
	public Shape(){
		coords = new int[4][2];
		coords = reference[0];
		tetrominoes = Tetrominoes.NoShape;
	}
	void setShape(int index){
		coords = reference[index];
		tetrominoes = Tetrominoes.values()[index];
	}
	public void setRandomShape(){
		Random r = new Random();
		int x = Math.abs(r.nextInt()%7)+1;
		setShape(x);
	}
	public int[][] getCoords(){
		return coords;
	}
	public int getMaxY(){
		int maxY = 0;
		for(int[] coord : coords){
			if(coord[1] > maxY)
				maxY = coord[1];
		}
		return maxY;
	}
	public String toString() {
		return this.tetrominoes.name() + ": " + coordsToString();
	}
	
	String coordsToString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("{");
		for(int i=0; i<4; i++) {
			sb.append("{");
			sb.append(coords[i][0]);
			sb.append(",");
			sb.append(coords[i][1]);
			sb.append("} ");
		}
		sb.append("}");
		return sb.toString();
	}
//	public void setX(int index, int newX){
//		coords[index][0] = newX;
//	}
//	public void setY(int index, int newY){
//		coords[index][1] = newY;
//	}
}