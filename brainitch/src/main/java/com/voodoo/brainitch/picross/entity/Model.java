package com.voodoo.brainitch.picross.entity;

import java.util.LinkedList;
import java.util.List;

public class Model {
	int rowSize;
	public Row[] rows;
	public int[][] tipNums;
	public int[][] state;
	
	
	
	public Model(int[][] tipNums, int rowSize) {
		this.tipNums = tipNums;
		this.rowSize = rowSize;
		rows = getRows();
		state = getState();
	}

	public Row[] getRows() {
		Row[] rows = new Row[tipNums.length];
		for (int i = 0; i < tipNums.length; i++) {
			rows[i] = new Row(tipNums[i], rowSize);
		}
		return rows;
	}
	
	public int[][] getState() {
		int[][] state = new int[tipNums.length][];
		for (int i = 0; i < state.length; i++) {
			state[i] = new int[rowSize];
		}
		return state;
		
	}
	
	public List<int[]> getIncompleteState() {
		List<int[]> incomplete = new LinkedList<>();
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[i].length; j++) {
				if (state[i][j] == 0) {
					incomplete.add(new int[] {i,j});
				};
			}
		}
		return incomplete;
		
	}
	
	/**
	 * 将modelState读进每个rows的state
	 */
	public void readState() {
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[i].length; j++) {
				rows[i].state[j] = state[i][j];
			}
		}
	}
	
	/**
	 * 把rows的state写入model的state
	 * @return 改变的row的数量，返回-1为数据冲突
	 */
	public int writeState() {
		int result = 0;
		for (int i = 0; i < rows.length; i++) {
			int rowResult = 0;
			for (int j = 0; j < rows[i].state.length; j++) {
				if(rows[i].state[j] != 0) {
					if(state[i][j] == 0) {
						state[i][j] = rows[i].state[j];
						rowResult += 1;
					}else {
						if(rows[i].state[j] != state[i][j]) {
							return -1;
						}
					}
				}
			}
			if(rowResult > 0) {
				result += 1;
			}
		}
		return result;
	}
	/**
	 * 一次处理流程
	 * 根据可能性，确认state里肯定的答案
	 * 再根据state排除不可能的可能性
	 * 循环这两个流程直至没有变动为止
	 * @return 最终有改变过的行数
	 */
	public int singleProcess(char sign) {
		int result = 0;
		int isDone = 0;
		readState();
		do{
			for (int i = 0; i < rows.length; i++) {
				int isRemoved = rows[i].removeImpossibleStates(sign);
				//只会出现在pencilMode
				if (rows[i].possibleState.size() == 0) {
					continue;
				}
				int isSetTrue = rows[i].setStateTrue(sign);
				int isSetFalse = rows[i].setStateFalse(sign);
				isDone = isRemoved + isSetTrue + isSetFalse;
			}
		}while(isDone != 0);
		result = writeState();
		return result;
	}
	
	public List<int[]> getEmptyState() {
		List<int[]> result = new LinkedList<>();
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[i].length; j++) {
				if (state[i][j] == 0) {
					result.add(new int[] {i,j});
				}
			}
		}
		return result;
	}
	
	
	public List<int[]> getPencilState() {
		List<int[]> result = new LinkedList<>();
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[i].length; j++) {
				if (state[i][j] == 3 || state[i][j] == 4) {
					result.add(new int[] {i,j});
				}
			}
		}
		return result;
	}
	
	/**
	 * 擦除铅笔模式state
	 * @return 擦除数
	 */
	public int erasePencilState(List<int[]> changedState) {
		int result = 0;
		for (int[] is : changedState) {
			state[is[0]][is[1]] = 0;
			result += 1;
		}
		readState();
		return result;
	}
	
	/**
	 * 擦除铅笔模式state
	 * @return 擦除数
	 */
	public int ensurePencilState() {
		int result = 0;
		for (int j = 0; j < state.length; j++) {
			for (int k = 0; k < state[j].length; k++) {
				if (state[j][k] == 3) {
					state[j][k] = 1;
					result += 1;
				}else if(state[j][k] == 4) {
					state[j][k] = 2;
					result += 1;
				}
			}
		}
		readState();
		return result;
	}
	
	/**
	 * 是否有possibleState为0的情况，有的话证明铅笔模式所填的数为假
	 * @return
	 */
	public boolean hasWrongPossibleState() {
		for (int i = 0; i < rows.length; i++) {
			if (rows[i].possibleState.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 重新根据state和tipNum生成possibleArray
	 */
	public void recalculatePossibleStates(char sign) {
		for (Row row : rows) {
			row.possibleState = row.getPossibleBooleanArrays();
			row.removeImpossibleStates(sign);
		}
	}
}
