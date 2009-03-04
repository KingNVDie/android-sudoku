package ru.amse.rakkate.sudoku.logic.impl;

import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import ru.amse.rakkate.sudoku.logic.IGeneratorCondition;


public class GeneratorCondition implements IGeneratorCondition {
    private final int[][] mySudoku;//судоку, который надо решить
    private int[][] mySudokuSolution;//сгенерированная сетка
    private final int[][] mySudokuCondition;//задача для решения
    private int[][] map1;
    private final int myHeight = Model.myHeight;
    private final int myWidth = Model.myWidth;
    private List<Integer>[] myMatrixDegree;
    private int[][] matrixColumn;
    private int[][] matrixSquare;
    private Random random;
    private boolean res = false;//fgifjvi
    

    public GeneratorCondition() {
        random = new Random();
        mySudokuSolution = new int[myHeight][myWidth];
        mySudoku = new int[myHeight][myWidth];
        mySudokuCondition = new int[myHeight][myWidth];
        myMatrixDegree = new List[myHeight * myHeight];
        map1 = new int[myHeight][myWidth];
        for (int i = 0; i < (myHeight * myHeight); i++) {
            myMatrixDegree[i] = new LinkedList<Integer>();
        }  
        for (int i = 0; i < myHeight; i++) {
            for (int j = 0; j < myWidth; j++) {
                mySudokuSolution[i][j] = 0;
                mySudoku[i][j] = 0;
                mySudokuCondition[i][j] = 0;
                map1[i][j] = 0;
            }
        }
    }
    
    public int[][] createSolution() {
        for (int i = 0; i < myHeight; i++) {
            for (int j = 0; j < myHeight; j++) {
               mySudokuSolution[i][j] = 0;   
            }
        }
        int randomInt = random.nextInt(myHeight);
        int randomNum = random.nextInt(myHeight * myWidth);
        mySudokuSolution[randomNum / myHeight][randomNum % myHeight] = randomInt;
        mySudokuSolution = getSolution(mySudokuSolution);        
        return mySudokuSolution;       
    }
    
     public int[][] createCondition(int quantity) {
        List<Integer> list = new LinkedList<Integer>();
        for (int i = 0; i < (myHeight * myWidth); i++) {
            list.add(i);
        }
        int counter = 0;
        random = new Random();
        int randomInt;
        int randomNum;
        mySudokuSolution = createSolution();
        for (int i = 0; i < myHeight; i++) {
            for (int j = 0; j < myWidth; j++) {
                mySudokuCondition[i][j] = mySudokuSolution[i][j];   
            }
        }
        while(counter < quantity) {
            randomNum = random.nextInt(list.size());
            randomInt = list.get(randomNum);
            list.remove(randomNum);
            if (mySudokuCondition[randomInt / myHeight][randomInt % myHeight] != 0) {
                mySudokuCondition[randomInt / myHeight][randomInt % myHeight] = 0;
                counter++;       
            }
            if (list.size() == 0) {
                break;
            }
        }
        return mySudokuCondition;
    }
   
    private boolean getMatrixDegree(int[][] matrixColumn, int[][] matrixSquare) {
        for (int i = 0; i < (myHeight * myWidth); i++) {
            myMatrixDegree[i] = new LinkedList<Integer>();
        }  
       for (int i = 0; i < myHeight; i++) {
           for (int j = 0; j < myWidth; j++) {            
               for (int k = 0; k < myHeight; k++) {
                   if((mySudoku[i][j] == 0) && (isAvailable(mySudoku[i], (k + 1)) == false) && ((isAvailable(matrixColumn[j], (k + 1))) == false) && ((isAvailable(matrixSquare[(i / 3) * 3 + j/3], (k + 1))) == false)) {
                       myMatrixDegree[i * myHeight + j].add(k + 1);
                   }                
               }
               if ((mySudoku[i][j] == 0) && (myMatrixDegree[i * myHeight + j].size() == 0)) {
                   return false;
               }
           }
       }
       return true;
    }
    
    public int[][] getSolution(int[][] matrix1) {
        for (int i = 0; i < myHeight; i++) {
            for (int j = 0; j < myWidth; j++) {
                mySudoku[i][j] = matrix1[i][j];
            }
        }     
        boolean res = true;
        matrixColumn = new int[myHeight][myWidth];
        matrixSquare = new int[myWidth][myHeight];
        for (int i = 0; i < myHeight; i++) {
            for (int j = 0; j < myWidth; j++) {
                matrixColumn[i][j] = mySudoku[j][i];
                matrixSquare[i][j] = mySudoku[(j / 3) + (i / 3) * 3][j % 3 + (i % 3) * 3];
            }
        }
         getMatrixDegree(matrixColumn, matrixSquare);
         int min = getMin();
         solve(min);
         return mySudokuSolution;
    }
    
    private void solve(int min) {
        for (int i = 0; i < myMatrixDegree[min].size(); i++) {
            if (res == false) {
                mySudoku[min / myHeight][min % myWidth] = myMatrixDegree[min].get(i);
                for (int x = 0; x < myHeight; x++) {
                    for (int j = 0; j < myWidth; j++) {
                        matrixColumn[x][j] = mySudoku[j][x];
                        matrixSquare[x][j] = mySudoku[(j / 3) + (x / 3) * 3][j % 3 + (x % 3) * 3];
                    }
                }
                if (isEnd() == false) {
                    if (getMatrixDegree(matrixColumn, matrixSquare)) {
                        int min1 = getMin();
                        solve(min1);
                        if (res == false) {
                            mySudoku[min / myHeight][min % myWidth] = 0; 
                        }
                        for (int x = 0; x < myHeight; x++) {
                            for (int j = 0; j < myWidth; j++) {
                                matrixColumn[x][j] = mySudoku[j][x];
                                matrixSquare[x][j] = mySudoku[(j / 3) + (x / 3) * 3][j % 3 + (x % 3) * 3];
                            }
                        }
                        getMatrixDegree(matrixColumn, matrixSquare);
                    
                    } else {
                        mySudoku[min / myHeight][min % myWidth] = 0;
                    }
                } else {  
                    mySudokuSolution = mySudoku;
                    res = true;
                }
            }
        }       
    }
    
    private int getMin() {
        int min, minEl, j = 0;
        while(true) {
            if (myMatrixDegree[j].size() != 0) {
                minEl = j;
                min = myMatrixDegree[j].size();
                break;
            } 
            j++;
        }        
        for (int i = j; i < myMatrixDegree.length; i++)
            if ((myMatrixDegree[i].size() != 0) && (myMatrixDegree[i].size() < min)) {
            min = myMatrixDegree[i].size();
            minEl = i;
            }
        return minEl;
    }
    private boolean isAvailable(int[] arr, int x) {
        for (int i = 0; i < myHeight; i++) {
           if (arr[i] == x) {
               return true;
           }    
        }
        return false;
    }
    
    private boolean isEnd() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (mySudoku[i][j] == 0)
                    return false;
            }
        }
        return true;
    }
    
}
