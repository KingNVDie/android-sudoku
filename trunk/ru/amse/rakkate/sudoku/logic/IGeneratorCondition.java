package ru.amse.rakkate.sudoku.logic;

public interface IGeneratorCondition {
    public int[][] createCondition(int quantity);
    public int[][] getSolution(int[][] matrix);
}
