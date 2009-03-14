package ru.amse.rakkate.sudoku.logic.impl;

import java.io.*;

import ru.amse.rakkate.sudoku.logic.IModel;
import ru.amse.rakkate.sudoku.logic.IWriterModel;

public class WriterTXT implements IWriterModel {
    
    private final IModel myModel;
    private final int myWidth  = Model.myWidth;
    private final int myHeight = Model.myHeight;
    
    public WriterTXT(IModel model) {
        myModel = model;
    }
    
    private void writeMatrix(int[][] matrix, Writer w) throws IOException, SecurityException{
        for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    String  s = String.valueOf(matrix[i][j]);
                    w.write(s);
                }
                w.write("\n");
            }        
    }
    
    public void write(Writer w) throws IOException, SecurityException{
        writeMatrix(myModel.getSudokuCondition(), w);
        writeMatrix(myModel.getSudokuSolution(), w);
        writeMatrix(myModel.getSudoku(), w);
        writeMatrix(myModel.getMatrixPrompting(), w);
    }
    
    public void writeStream(FileOutputStream w, IModel model) throws IOException {
    	String s = null;
    	byte buf[];
    	 for (int i = 0; i < Model.myHeight; i++) {
    		 for (int j = 0; j <Model.myWidth; j++) {
    			 w.write(model.getCell(i, j));
    		 }
    		 s = "\n";
    		 buf = s.getBytes();
    		 for (int k = 0; k <buf.length; k++) {
              	 w.write(buf[k]);
             }
    	 }
    	 
    	 for (int i = 0; i < Model.myHeight; i++) {
    		 for (int j = 0; j <Model.myWidth; j++) {
    			 w.write(model.getCellSudokuCondition(i, j));
    		 }
    		 s = "\n";
    		 buf = s.getBytes();
    		 for (int k = 0; k <buf.length; k++) {
              	 w.write(buf[k]);
             }
    	 }
    	 
    	 for (int i = 0; i < Model.myHeight; i++) {
    		 for (int j = 0; j <Model.myWidth; j++) {
    			 w.write(model.getSudokuSolution()[i][j]);
    		 }
    		 s = "\n";
    		 buf = s.getBytes();
    		 for (int k = 0; k <buf.length; k++) {
              	 w.write(buf[k]);
             }
    	 }
     }
}
    
