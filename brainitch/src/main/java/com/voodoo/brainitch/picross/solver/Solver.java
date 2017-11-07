package com.voodoo.brainitch.picross.solver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.voodoo.brainitch.picross.entity.Model;

public class Solver {
	public int[][] hTipNums;
	public int[][] vTipNums;
	public Model hModel;
	public Model vModel;
	
	public Solver(int[][] hTipNums, int[][] vTipNums) {
		this.hTipNums = hTipNums;
		this.vTipNums = vTipNums;
		hModel = new Model(vTipNums, hTipNums.length);
		vModel = new Model(hTipNums, vTipNums.length);
	}

	public void switchModel(char livingModel) {
		if(livingModel == 'h') {
			for (int i = 0; i < hModel.state.length; i++) {
				for (int j = 0; j < hModel.state[i].length; j++) {
					vModel.state[j][i] = hModel.state[i][j];
				}
			}
		}else if(livingModel == 'v') {
			for (int i = 0; i < vModel.state.length; i++) {
				for (int j = 0; j < vModel.state[i].length; j++) {
					hModel.state[j][i] = vModel.state[i][j];
				}
			}
		}
	}
	/**
	 * 简单排除模式
	 * 通过每一行的possibleState得出state，再横向竖向相互修改检查，排除掉possibleState，最终得出答案
	 */
	public void simpleSolve(char sign) {
		int modifiedRowNum = 0;
		do{
			modifiedRowNum = 0;
			modifiedRowNum += hModel.singleProcess(sign);
			switchModel('h');
			modifiedRowNum += vModel.singleProcess(sign);
			switchModel('v');
			//print(vModel);
		}while(modifiedRowNum != 0);
	}
	
	
	/**
	 * 可擦除的试错模式
	 * 1.走一次simpleSolve之后如果model里还有0，则进入pencil mode
	 * 2.找出possibleState大于1里，possibleState长度最小的row，假设第一个为0的state设为真，设为3
	 * 3.按simpleSolve往下走，直至走不动（里面全部state的设置均为pencil模式，3为真4为假）
	 * 4.查看有无possibleState为0的row，如果有，则把假设为3的state设为2，即是假，然后把原来设置为3和4的state全部归0，重新计算possibleState
	 * 5.再走一次simpleSolve，看能否得出答案，如果不行，则再进入pencilMode，也就是回到步骤1
	 * 6.如果步骤4没有，继续按照步骤2走，再假设符合条件的state为真
	 * @throws Exception 
	 * 
	 * */
	public void pencilSolve() throws Exception {
		List<int[]> incompleteState = hModel.getIncompleteState();
		List<int[]> hchangedState = new LinkedList<>();
		List<int[]> vchangedState = new LinkedList<>();
		int[][] beforeModified = new int[hModel.state.length][hModel.state[0].length];
		for (int i = 0; i < hModel.state.length; i++) {
			for (int j = 0; j < hModel.state[i].length; j++) {
				beforeModified[i][j] = hModel.state[i][j];
			}
		}
		for (int i = 0; i < incompleteState.size(); i++) {
			hModel.state[incompleteState.get(i)[0]][incompleteState.get(i)[1]] = 3;
			vModel.state[incompleteState.get(i)[1]][incompleteState.get(i)[0]] = 3;
			simpleSolve('p');
			//对比得到hModel里此次假设所改变的state
			for (int j = 0; j < hModel.state.length; j++) {
				for (int k = 0; k < hModel.state[j].length; k++) {
					if(beforeModified[j][k] != hModel.state[j][k]) {
						hchangedState.add(new int[] {j,k});
						vchangedState.add(new int[] {k,j});
					}
				}
			}
			if (hModel.hasWrongPossibleState() || vModel.hasWrongPossibleState()) {
				hModel.erasePencilState(hchangedState);
				vModel.erasePencilState(vchangedState);
				hModel.state[incompleteState.get(i)[0]][incompleteState.get(i)[1]] = 4;
				vModel.state[incompleteState.get(i)[1]][incompleteState.get(i)[0]] = 4;
				hModel.recalculatePossibleStates('p');
				vModel.recalculatePossibleStates('p');
				continue;
			}else {
				if (!hModel.getIncompleteState().isEmpty()) {
					pencilSolve();
				}else {
					hModel.ensurePencilState();
					throw new Exception();
				}
			}
		}
	}
	
	public void print(Model model) {
		for (int i = 0; i < model.state.length; i++) {
			for (int j = 0; j < model.state[i].length; j++) {
				switch (model.state[i][j]) {
				case 0:
					System.out.print(" ×");
					break;

				case 1:
					System.out.print(" ■");
					break;
				case 2:
					System.out.print(" □");
					break;
				case 3:
					System.out.print(" 3");
					break;
				case 4:
					System.out.print(" 4");
					break;
				default:
					break;
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void solve() {
		simpleSolve('r');
		if(hModel.getIncompleteState().size() != 0) {
			try {
				pencilSolve();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("it's fine");
			}
		}
		print(hModel);
	}
	
	public static void main(String[] args) {
		/*//此模式需pencilSolve
		int[][] hTipNums = new int[][] {
			{3,2},{1,1,1},{1,2},{1,2},{1,1},{3,2},{1,1,1},{4,1,2},{1,1,2},{2,1,1,1}
		};
		int[][] vTipNums = new int[][] {
			{2,3},{1,4,3},{1,3,1},{2},{1},{1,1},{1,1},{2,2},{1,2,1,3},{1,1,1,1}
		};*/
		
		int[][] hTipNums = new int[][] {
			{5,5},{4,5},{3,3,5},{4,2,2,3},{3,3,1,1,1},
			{6,6},{8,3,3,5},{2,11,1,5},{2,1,11,5},{5,13,2},
			{3,1,11,3,1},{2,2,10,5},{4,8,5},{3,1,5,1,5},{1,5,3,5},
			{1,4,6,1,4},{2,2,3,3,2},{3,10,5},{4,8,1,3},{5,5,2,2},
			{6,1,5},{7,3,5},{8,3,5},{9,2,5},{10,1,5}
		};
		int[][] vTipNums = new int[][] {
			{10},{3,9},{6,8},{1,1,5,7},{3,3,3,6},
			{5,1,3,5},{1,4,2,1,4},{1,5,2,3},{9,3,2},{1,9,4,1},
			{3,9,3},{2,9,4},{8,1,3,1},{8,5,2},{1,8,5,4},
			{2,6,6,2},{3,1,8,2},{4,2,3,1,3},{5,1,2,3},{1,1,1},
			{9,6,8},{4,4,6,1,6},{4,4,6,2,5},{3,5,14},{3,11,9}
		};
		Solver solver = new Solver(hTipNums, vTipNums);
		solver.solve();
	}
}



