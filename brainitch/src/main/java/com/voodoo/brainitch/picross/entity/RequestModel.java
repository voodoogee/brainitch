package com.voodoo.brainitch.picross.entity;

import org.springframework.stereotype.Component;

@Component
public class RequestModel {
	private int[][] htips;
	private int[][] vtips;
	
	public int[][] getHtips() {
		return htips;
	}
	public void setHtips(int[][] htips) {
		this.htips = htips;
	}
	public int[][] getVtips() {
		return vtips;
	}
	public void setVtips(int[][] vtips) {
		this.vtips = vtips;
	}
	
	
}
