package com.voodoo.brainitch.sudoku.entity;

import java.util.HashSet;
import java.util.Set;

public class Cell {
	public int possibleLength;
	public Set<Integer> possibleDigit = new HashSet<>();
	public int finalResult;
	
	public Cell() {
		possibleLength = 9;
		for (int i = 1; i <= 9; i++) {
			possibleDigit.add(i);
		}
	}
	
	public Cell(int finalResult) {
		possibleLength = 1;
		this.finalResult = finalResult;
	}
	
	public boolean remove(int wrongDigit) {
		boolean result = possibleDigit.remove(wrongDigit);
		possibleLength -= 1;
		return result;
	}
	
	public boolean ensure() {
		boolean result = false;
		if(possibleLength == 1) {
			finalResult = possibleDigit.iterator().next();
			result = true;
		}
		return result;
	}
}
