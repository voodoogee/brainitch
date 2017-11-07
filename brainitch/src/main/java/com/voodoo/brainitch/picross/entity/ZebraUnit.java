package com.voodoo.brainitch.picross.entity;

public class ZebraUnit {
	private int index;
	private char attr;
	private int size;
	

	public ZebraUnit(int index, char attr, int size) {
		this.index = index;
		this.attr = attr;
		this.size = size;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public char getAttr() {
		return attr;
	}
	public void setAttr(char attr) {
		this.attr = attr;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
}
