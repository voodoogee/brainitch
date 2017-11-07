package com.voodoo.brainitch.sudoku.entity;

import java.util.HashSet;
import java.util.Set;

public class Cell {
	private int possibleSize;
	private Set<Integer> possibleDigit = new HashSet<>();
	private int finalResult = 0;
	
	public Cell() {
		possibleSize = 9;
		for (int i = 1; i <= 9; i++) {
			possibleDigit.add(i);
		}
	}
	
	public int getPossibleSize() {
		return possibleSize;
	}

	public int getFinalResult() {
		return finalResult;
	}

	public Cell(int finalResult) {
		possibleSize = 1;
		this.finalResult = finalResult;
	}
	
	public boolean remove(int wrongDigit) {
		boolean result = possibleDigit.remove(wrongDigit);
		possibleSize -= 1;
		return result;
	}
	
	public boolean ensure() {
		boolean result = false;
		if(possibleSize == 1) {
			finalResult = possibleDigit.iterator().next();
			result = true;
		}
		return result;
	}
}
