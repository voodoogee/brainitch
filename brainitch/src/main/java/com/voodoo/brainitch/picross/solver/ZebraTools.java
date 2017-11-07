package com.voodoo.brainitch.picross.solver;

import java.util.LinkedList;
import java.util.List;

public class ZebraTools {
	/**
	 * 去掉数组头，返回新数组
	 * @param array
	 * @return
	 */
	public static int[] headOff(int[] array) {
		int[] result = new int[array.length-1];
		for (int i = 0; i < result.length; i++) {
			result[i] = array[i+1];
		}
		return result;
	}
	
	/**
	 * 将整数sum拆分为piece个数的和，列举所有情形。
	 * @param list
	 * @param sum
	 * @param piece
	 * @return
	 */
	public static List<List<Integer>> split(List<Integer> list, int sum, int piece) {
		List<List<Integer>> result = new LinkedList<>();
		if(piece > 2) {
			for (int i = 1; i <= sum-piece+1; i++) {
				List<Integer> temp = new LinkedList<>();
				temp.addAll(list);
				temp.add(i);
				result.addAll(split(temp,sum-i, piece-1));
			}
		}else if(piece == 2){
			for (int i = 1; i < sum; i++) {
				List<Integer> temp = new LinkedList<>();
				temp.addAll(list);
				temp.add(i);
				temp.add(sum-i);
				result.add(temp);
			}
		}else {
			List<Integer> temp = new LinkedList<>();
			temp.add(sum);
			result.add(temp);
		}
		return result;
	}
	
	public static int[][] intListToArray(List<List<Integer>> list) {
		int[][] result = new int[list.size()][];
		for (int i = 0; i < result.length; i++) {
			int[] temp = new int[list.get(i).size()];
			for (int j = 0; j < temp.length; j++) {
				temp[j] = list.get(i).get(j);
			}
			result[i] = temp;
		}
		return result;
	}
	
	public static boolean[][] booleanListToArray(List<boolean[]> list) {
		boolean[][] result = new boolean[list.size()][];
		for (int i = 0; i < result.length; i++) {
			boolean[] temp = new boolean[list.get(i).length];
			for (int j = 0; j < temp.length; j++) {
				temp[j] = list.get(i)[j];
			}
			result[i] = temp;
		}
		return result;
	}
	
	/**
	 * 二维数组纵横坐标交换
	 * @param twoDimensionArray
	 * @return
	 */
	public static boolean[][] verticalToHorizonal(boolean[][] twoDimensionArray) {
		boolean[][] result = new boolean[twoDimensionArray[0].length][twoDimensionArray.length];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = twoDimensionArray[j][i];
			}
		}
		return result;
	}
}
