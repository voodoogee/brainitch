package com.voodoo.brainitch.picross.entity;

import java.util.LinkedList;
import java.util.List;


public class ZebraArray {
	private int[] black;
	private int[] white;
	private char headColor;
	
	public ZebraArray(int[] black, int[] white, char headColor) {
		this.black = black;
		this.white = white;
		this.headColor = headColor;
	}
	
	public int[] getBlack() {
		return black;
	}

	public void setBlack(int[] black) {
		this.black = black;
	}

	public int[] getWhite() {
		return white;
	}

	public void setWhite(int[] white) {
		this.white = white;
	}
	
	public int tipSum() {
		int sum = 0;
		for(int i = 0; i<black.length; i++) {
			sum += black[i];
		}
		return sum;
	}
	
	public int gapSum() {
		int sum = 0;
		for(int i = 0; i<white.length; i++) {
			sum += white[i];
		}
		return sum;
	}
	
	public int totalSum() {
		return tipSum()+gapSum();
	}
	
	/**
	 * 根据初始化属性，返回和此array等价的用ZebraUnit组成的链表。
	 * @return
	 */
	public List<ZebraUnit> getUnitList() {
		List<ZebraUnit> unitList = new LinkedList<>();
		if(black.length == white.length + 1) {
			unitList.add(new ZebraUnit(0, 'b', black[0]));
			for (int i = 0; i < white.length; i++) {
				unitList.add(new ZebraUnit(2*i+1, 'w', white[i]));
				unitList.add(new ZebraUnit(2*i+2, 'b', black[i+1]));
			}
		}else if (black.length == white.length - 1) {
			unitList.add(new ZebraUnit(0, 'w', white[0]));
			for (int i = 0; i < black.length; i++) {
				unitList.add(new ZebraUnit(2*i+1, 'b', black[i]));
				unitList.add(new ZebraUnit(2*i+2, 'w', white[i+1]));
			}
		}else if(black.length == white.length) {
			if(headColor == 'w') {
				for (int i = 0; i < white.length; i++) {
					unitList.add(new ZebraUnit(2*i, 'w', white[i]));
					unitList.add(new ZebraUnit(2*i+1, 'b', black[i]));
				}
			}else if(headColor == 'b') {
				for (int i = 0; i < black.length; i++) {
					unitList.add(new ZebraUnit(2*i, 'b', black[i]));
					unitList.add(new ZebraUnit(2*i+1, 'w', white[i]));
				}
			}
		}
		return unitList;
	}
	
	/**
	 * 得到此zebraArray的布尔表达的数组。
	 * @return
	 */
	public boolean[] getBooleanArray() {
		List<ZebraUnit> units = getUnitList();
		boolean[] result = new boolean[totalSum()];
		int temp = 0;
		for (int i = 0; i < units.size(); i++) {
			if(units.get(i).getAttr() == 'b') {
				for (int j = 0; j < units.get(i).getSize(); j++) {
						result[temp] = true;
						temp++;
				}
			}else {
				for (int j = 0; j < units.get(i).getSize(); j++) {
					result[temp] = false;
					temp++;
				}
			}
		}
		return result;
	}
}
