package com.voodoo.brainitch.picross.entity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.voodoo.brainitch.picross.solver.ZebraTools;

public class Row {
	public int[] tipNum;
	int size;
	public int[] state;
	List<boolean[]> possibleState;
	
	public Row(int[] tipNum, int size) {
		this.tipNum = tipNum;
		this.size = size;
		state = new int[size];
		possibleState = getPossibleBooleanArrays();
	}
	
	public int tipSum() {
		int sum = 0;
		for(int i = 0; i<tipNum.length; i++) {
			sum += tipNum[i];
		}
		return sum;
	}
	
	public int gapSum() {
		return size- tipSum();
	}
	
	/**
	 * 
	 * @param size 白列的总和
	 * @param blackLength 黑列数组的长度
	 * @return
	 */
	private List<List<Integer>> getWhiteArrays() {
		int gapSum = gapSum();
		int blackLength = tipNum.length;
		List<List<Integer>> whiteArrays = new LinkedList<>();
		int minWhiteLength = blackLength - 1;
		int maxWhiteLength = blackLength + 1;
		for (int i = minWhiteLength ; i <= maxWhiteLength; i++) {
			List<Integer> temp = new LinkedList<>();
			whiteArrays.addAll(ZebraTools.split(temp, gapSum, i));
		}
		return whiteArrays;
	}
	/**
	 * 得到所有可能的斑马列，返回斑马列
	 * @return List<ZebraArray>
	 */
	private List<ZebraArray> getPossibleZebraArrays() {
		int[] blackArray = tipNum;
		int[][] whiteArrays = ZebraTools.intListToArray(getWhiteArrays());
		List<ZebraArray> zebraArrays = new LinkedList<>();
		for (int[] whiteArray : whiteArrays) {
			if(blackArray.length > whiteArray.length) {
				ZebraArray zebraArray = new ZebraArray(blackArray, whiteArray, 'b');
				zebraArrays.add(zebraArray);
			}else if (blackArray.length < whiteArray.length) {
				ZebraArray zebraArray = new ZebraArray(blackArray, whiteArray, 'w');
				zebraArrays.add(zebraArray);
			}else {
				ZebraArray zebraArray1 = new ZebraArray(blackArray, whiteArray, 'b');
				ZebraArray zebraArray2 = new ZebraArray(blackArray, whiteArray, 'w');
				zebraArrays.add(zebraArray1);
				zebraArrays.add(zebraArray2);
			}
		}
		return zebraArrays;
	}
	/**
	 * 根据斑马列得到可能的布尔列
	 * @return
	 */
	public List<boolean[]> getPossibleBooleanArrays() {
		List<ZebraArray> zebraArrays = getPossibleZebraArrays();
		List<boolean[]> result = new LinkedList<>();
		for (int i = 0; i < zebraArrays.size(); i++) {
			result.add(zebraArrays.get(i).getBooleanArray());
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param sign pencilMode标志
	 * @return state是否被修改，有则返回true无则返回false
	 */
	public int setStateTrue(char sign) {
		int isChanged = 0;
		boolean[][] indexState = ZebraTools.verticalToHorizonal(ZebraTools.booleanListToArray(possibleState));
		for (int i = 0; i < indexState.length; i++) {
			boolean result = true;
			for (int j = 0; j < indexState[i].length; j++) {
				result &= indexState[i][j];
			}
			if(result == true && state[i] == 0) {
				if(sign == 'p') {
					state[i] = 3;
				}else {
					state[i] = 1;
				}
				isChanged += 1;
			}
		}
		return isChanged;
	}
	
	/**
	 * 
	 * @param sign pencilMode标志
	 * @return state是否被修改，有则返回true无则返回false
	 */
	public int setStateFalse(char sign) {
		int isChanged = 0;
		boolean[][] indexState = ZebraTools.verticalToHorizonal(ZebraTools.booleanListToArray(possibleState));
		for (int i = 0; i < indexState.length; i++) {
			boolean result = false;
			for (int j = 0; j < indexState[i].length; j++) {
				result |= indexState[i][j];
			}
			if(result == false && state[i] == 0) {
				if(sign == 'p') {
					state[i] = 4;
				}else {
					state[i] = 2;
				}
				isChanged += 1;
			}
		}
		return isChanged;
	}

	/**
	 * 根据state状况，从pissibleState里移除不可能的states
	 */
	public int removeImpossibleStates(char sign) {
		int result = 0;
		for (int i = 0; i < state.length; i++) {
			if(state[i] == 1 || state[i] == 3) {
				Iterator<boolean[]> it = possibleState.iterator();
				while(it.hasNext()){
					boolean[] bs = it.next();
				    if(bs[i] == false){
				        it.remove();
				        result += 1;
				    }
				}
			}else if (state[i] == 2 || state[i] == 4) {
				Iterator<boolean[]> it = possibleState.iterator();
				while(it.hasNext()){
					boolean[] bs = it.next();
				    if(bs[i] == true){
				        it.remove();
				        result += 1;
				    }
				}
			}
		}
		//如果possibleState剩下一种，将其赋值给state
		if (possibleState.size() == 1) {
			for (int i = 0; i < possibleState.get(0).length; i++) {
				if(sign == 'p') {
					if (state[i] == 0) {
						state[i] = possibleState.get(0)[i] == true ? 3 : 4;
					}
				}else {
					state[i] = possibleState.get(0)[i] == true ? 1 : 2;
				}
			}
		}
		return result;
	}
	
	/**
	 * 重新根据state和tipNum生成possibleArray
	 */
	public void recalculatePossibleStates(char sign) {
		possibleState = getPossibleBooleanArrays();
		removeImpossibleStates(sign);
	}
	
	@Override
	public String toString() {
		String result = "possibleStates:\n";
		for (boolean[] bs : possibleState) {
			result += Arrays.toString(bs)+"\n";
		}
		result += "state:"+Arrays.toString(state);
		return result;
	}
}
