package ru.amse.rakkate.sudoku.logic;

import java.io.*;
import java.util.Scanner;

public interface IReaderModel {
    public IModel readTXT(Reader f, IModel map) throws IOException, SecurityException, FileNotFoundException;
    public String readHelp(Scanner sc);
}