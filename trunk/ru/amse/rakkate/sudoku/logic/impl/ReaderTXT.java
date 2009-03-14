package ru.amse.rakkate.sudoku.logic.impl;

import java.io.*;

import ru.amse.rakkate.sudoku.logic.IModel;
import ru.amse.rakkate.sudoku.logic.IReaderModel;
import java.util.Scanner;

public class ReaderTXT implements IReaderModel {
    private final int myWidth  = Model.myWidth;
    private final int myHeight = Model.myWidth;
    private int[][] myMatrix;
    
    public ReaderTXT() {
        myMatrix = new int[myWidth][myHeight];
    }
    
    private void getMatrix(Reader f) throws IOException, SecurityException, FileNotFoundException {
        for(int i = 0; i < myMatrix.length; i++) {
            for (int j = 0; j < myMatrix[0].length; j++) {
               myMatrix[i][j] = 0;
            }
        }
        int s;
        for (int i = 0; i < myMatrix.length; i++) {
            for (int j = 0; j < myMatrix[0].length; j++) {
                s = f.read();
                if ((s >= '0') && (s <= '9' )) {
                    myMatrix[i][j] = (s - 48);
                } else if((s == 10) || (s == 13)) {
                    j--;      
                } else {
                    myMatrix[i][j] = 0;
                }
           }
        }   
    }
    
    public String readHelp(Scanner sc) {
    	
        String str ="  ";
        while (sc.hasNextLine()) { 
           String s = sc.nextLine();
           str = str + s;
           str = str +"\n";
        }
        return str;
    }
    
    public IModel readStream(InputStream f, IModel model) throws IOException {
    	int s = f.read();
    	int i = 0;
    	int[][] matrix = new int[Model.myHeight][Model.myWidth];
    	while(s != -1) {
    		if (s == 10) {
    			s = f.read();
    		} else {
    			matrix[i / 9][i %9] = s;
    			i++;
    			if (i == 80) {
    				for (int k = 0; k < Model.myHeight; k++) {
    					for (int j = 0; j <Model.myWidth; j++) {
    						model.setCell(k, j, matrix[k][j]);
    					}
    				}
    			}
    			if (i == 160) {
    				model.setSudoku(matrix);
    			}
    			if (i == 240) {
    				model.setSudokuSolution(matrix);
    			}
    			s = f.read();
    		}
    	}
        return model;
    }
    
    public IModel readTXT(Reader f, IModel model) throws IOException, SecurityException, FileNotFoundException {
        try{
            getMatrix(f);
            model.setSudoku(myMatrix);
            getMatrix(f);
            int counter = 0;
            for (int i = 0; i < myHeight; i++) {
                for (int j = 0; j < myWidth; j++) {
                    if (myMatrix[i][j] != 0) {
                        counter++;
                    }
                }
            }
            if (counter == (Model.myHeight * Model.myWidth)) {
                model.setSudokuSolution(myMatrix);
            }
            getMatrix(f);
            for (int i = 0; i < myHeight; i++) {
               for (int j = 0; j < myWidth; j++) {
                   if (myMatrix[i][j] != 0) {
                       model.setCell(i, j, myMatrix[i][j]);   
                   }
                }
            }
            myMatrix = new int[myHeight * myWidth][4];
            getMatrix(f);
            model.setMatrixPrompting(myMatrix);
        } finally {
            if (f != null) {
                f.close();
            }
        }
        return model;
    }
    
}
