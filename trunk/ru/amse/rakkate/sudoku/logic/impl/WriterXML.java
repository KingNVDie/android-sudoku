package ru.amse.rakkate.sudoku.logic.impl;
import ru.amse.rakkate.sudoku.logic.*;

import java.io.*;

public class WriterXML {
    
    public void writeModel(FileWriter w, IModel model) throws IOException {
       // String gap = "        ";
        String gap ="";
        String currentGap = "";
        w.write("<?xml version = \"1.0\" encoding =\"UTF-8\" ?>\n");
        w.write("<model>\n");    
        w.write(currentGap + "<condition>\n");
        currentGap = currentGap + gap;
        for (int i = 0; i < Model.myHeight; i++ ) {
           w.write(currentGap + "<line>");
           for (int j = 0; j < Model.myWidth; j++) {  
                String s = String.valueOf(model.getSudokuCondition()[i][j]);
                w.write(s);
           }
           w.write("</line>\n");
        }
        currentGap = gap;        
        w.write(currentGap + "</condition>\n");
        w.write(currentGap + "<solution>\n");
        currentGap = currentGap + gap;
        for (int i = 0; i < Model.myHeight; i++ ) {
           w.write(currentGap + "<line>");
           for (int j = 0; j < Model.myWidth; j++) {     
                w.write(String.valueOf(model.getSudokuSolution()[i][j]));
            }
           w.write("</line>\n");
        }
        currentGap = gap;
        w.write(currentGap + "</solution>\n");
        w.write(currentGap + "<sudoku>\n");
        currentGap = currentGap + gap;
        for (int i = 0; i < Model.myHeight; i++ ) {
           w.write(currentGap + "<line>");
           for (int j = 0; j < Model.myWidth; j++) {        
                w.write(String.valueOf(model.getSudoku()[i][j]));
            }
            currentGap = gap + gap;
            w.write("</line>\n");
        }
        currentGap = gap;
        w.write(currentGap + "</sudoku>\n");
        w.write(currentGap + "<prompting>\n");
        currentGap = currentGap + gap;
        for (int i = 0; i < (Model.myHeight * Model.myWidth); i++ ) {
           w.write(currentGap + "<line>");
           for (int j = 0; j < 4; j++) {        
                w.write(String.valueOf(model.getPrompting(i, j)));
            }
            w.write("</line>\n");
        }
        currentGap = gap;
        w.write(currentGap + "</prompting>\n");
        w.write("</model>\n"); 
    }
}
    
