package ru.amse.rakkate.sudoku.logic.impl;

import java.lang.IndexOutOfBoundsException;
import ru.amse.rakkate.sudoku.logic.IModel;
import ru.amse.rakkate.sudoku.logic.IModelListener;
import ru.amse.rakkate.sudoku.logic.IGeneratorCondition;
import java.util.ArrayList;

public class Model implements IModel {
    
    private final int mySudoku[][];
    public static final int myHeight = 9;
    public static final int myWidth = 9;
    private final int mySudokuCondition[][];
    private int mySudokuSolution[][];
    private ArrayList<IModelListener> listeners;
    private int myPrompting[][];
    
    public Model() {
        listeners = new ArrayList<IModelListener>();
        mySudoku = new int[myHeight][myWidth];
        mySudokuCondition = new int[myHeight][myWidth];
        mySudokuSolution = new int[myHeight][myWidth];
        for (int i = 0; i < myWidth; i++) {
            for (int j = 0; j < myHeight; j++) {
                mySudoku[i][j] = 0;
                mySudokuCondition[i][j] = 0;
                mySudokuSolution[i][j] = 0;
            }
        }
        myPrompting = new int[myHeight * myWidth][4];
        for (int i = 0; i < (myHeight * myWidth); i++) {
            for (int j =0; j < 4; j++) {
                myPrompting[i][j] = 0;
            }
        }
    }
    
    public void setPrompting(int x, int y, int num) {
        myPrompting[x][y] = num;
        updateAll();
    }
    
    public int getPrompting(int x, int y) {
        return myPrompting[x][y];
    }
    
    public void addListener(IModelListener l) {
        listeners.add(l);     
    }
    
    public void removeListener(IModelListener l) {
        int i = listeners.indexOf(l);
        if (i != -1) {
            listeners.remove(i);
        }   
    }
    
    public int getWidth() {
        return myWidth;
    }
    
    public int[][] getSudokuCondition() {
        return mySudokuCondition;
    }
    
    public int[][] getSudokuSolution() {
        IGeneratorCondition generator = new GeneratorCondition();
        mySudokuSolution = generator.getSolution(mySudokuCondition);
        return mySudokuSolution;
    }
    
    public void updateAll() {
        for (IModelListener l:listeners) {
            l.update(this);   
        }
        
    }
    
    public void setSudokuSolution(int[][] matrix) {
        for (int i = 0; i < myWidth; i++) {
            for (int j = 0; j < myHeight; j++) {
                mySudokuSolution[i][j] = matrix[i][j];
            }
        } 
        updateAll();
    }
    
    public int getHeight() {
        return myHeight;
    }
    
    public int getCell(int x, int y) {
        if ((myWidth <= x) || (myHeight <= y ) || (x < 0) || (y < 0)) {
            throw new IndexOutOfBoundsException("IMap: getCell");
        } else {
            return mySudoku[x][y];
        }
    }
   
    public void setCell(int x, int y, int num) {
        if ((myWidth <= x) || (myHeight <= y ) || (x < 0) || (y < 0)) {
            throw new IndexOutOfBoundsException("IMap: setCell");
        } else if (mySudokuCondition[x][y] == 0) {
            mySudoku[x][y] = num;   
            updateAll();
        }
    }
    
    public int getCellSudokuCondition(int x, int y) {
        return mySudokuCondition[x][y];
    }
    
    public int[][] getSudoku() {
        return mySudoku;
    }
    
    private boolean getCheck(int[] array, int sum) {
        for (int j = 0; j < myWidth; j++) {
                sum = sum + array[j];
                array[j] = 0;
            }
            if (sum != myHeight) {
                return false;
            } else {
                return true;
            }      
    }
    
    public boolean isFull() {
    	int num = 0;
    	for (int i = 0; i < myHeight; i++) {
    		for (int j = 0; j < myWidth; j++) {
    			if (mySudoku[i][j] != 0) {
    				num++;
    			}
    			
    		}
    	}
    	if (num == 81) {
    		return true;
    	}
    	return false;
    	
    }
    
    public boolean isAccuracy() {
        for (int i = 0; i < myHeight; i++) {
            for (int j = 0; j < myWidth; j++) {
                if (mySudoku[i][j] == 0) {
                    return false;
                }
            }
        }
        boolean result  = true;
        int sum = 0;
        int[] arrayRoll = new int[myHeight];
        int[] arrayCoulumn = new int[myWidth];
        int[] arraySquare = new int[myHeight];
        for (int i = 0; i < myHeight; i++) {
            arrayRoll[i] = 0;
            arrayCoulumn[i] = 0;
            arraySquare[i] = 0;
        }
        for(int i = 0; i < myHeight; i++) {
            for (int j = 0; j < myWidth; j++) {
                arrayRoll[mySudoku[i][j] - 1] = 1;
                arrayCoulumn[mySudoku[j][i] - 1] = 1;
                arraySquare[mySudoku[(j / 3) + (i / 3) * 3][j % 3 + (i % 3) * 3] - 1] = 1;
            }
            if (getCheck(arrayRoll, sum)) {
                sum = 0;
            } else {
                return false;
            }
            if (getCheck(arrayCoulumn, sum)) {
                sum = 0;
            } else {
                return false;
            }
            if (getCheck(arraySquare, sum)) {
                sum = 0;
            } else {
                return false;
            }
        }
        return result;
    }
    
    public void setSudoku(int[][] map) {
        for (int i = 0; i < myWidth; i++) {
            for (int j = 0; j < myHeight; j++) {
                mySudoku[i][j] = map[i][j];
                mySudokuCondition[i][j] = map[i][j];
            }
        }
        for (int i = 0; i < (myHeight * myWidth); i++) {
            for (int j =0; j < 4; j++) {
                myPrompting[i][j] = 0;
            }
        }
        updateAll();
    }  
    
    public int[][] getMatrixPrompting() {
        return myPrompting;
    }
    
    public void setMatrixPrompting(int[][] matrix) {
        myPrompting = matrix;
    }
}
