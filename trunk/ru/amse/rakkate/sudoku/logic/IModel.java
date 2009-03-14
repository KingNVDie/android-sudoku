package ru.amse.rakkate.sudoku.logic;

public interface IModel {
    public int getCell(int x, int y);
    public void setCell(int x, int y, int num);
    public void setSudoku(int[][] map);
    public int[][] getSudokuCondition(); 
    public int getCellSudokuCondition(int x, int y);
    public boolean isAccuracy();
    public int[][] getSudokuSolution();
    public void setSudokuSolution(int[][] matrix);
    public int[][] getSudoku();
    public void addListener(IModelListener l);
    public void removeListener(IModelListener l);
    public void setPrompting(int x, int y, int num);
    public int getPrompting(int x, int y);
    public int[][] getMatrixPrompting();
    public void setMatrixPrompting(int[][] matrix);
    public boolean isFull();
}
