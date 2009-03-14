package ru.amse.rakkate.sudoku.logic.impl;

import java.io.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;
import ru.amse.rakkate.sudoku.logic.*;

public class ReaderXML {
    
    private IModel myModel;
    
    public IModel readModel(InputStream r) throws IOException, SAXException, ParserConfigurationException {
        myModel = new Model();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser p = factory.newSAXParser(); 
        p.parse(r, new MyDefaultHandlerMap());
        return myModel;
    }
    
    private class MyDefaultHandlerMap extends DefaultHandler {
        private boolean myCondition = false;
        private boolean mySolution = false;
        private boolean mySudoku = false;
        private boolean myPrompting = false;
        private int[][] myMatrixCondition = new int[Model.myHeight][Model.myWidth];
        private int[][] myMatrixSolution = new int[Model.myHeight][Model.myWidth];
        private int[][] myMatrixSudoku = new int[Model.myHeight][Model.myWidth];
        private int[][] myMatrixPrompting = new int[Model.myHeight *Model.myWidth][4];
        private int lineCounter = 0;
        String state = "!";
        
        public void startElement(String uri, String localName, String rawName, Attributes a) throws SAXException {
        	System.out.println(rawName);
        	state = rawName.toUpperCase();
            if (state.equals("CONDITION")) {
                lineCounter = 0;
                myCondition = true;  
            } else if (state.equals("SOLUTION")) {
                lineCounter = 0;
                myModel.setSudoku(myMatrixCondition);
                myCondition = false;
                mySolution  = true;
            } else if (state.equals("SUDOKU")) {
                lineCounter = 0;
                myModel.setSudokuSolution(myMatrixSolution);
                mySolution = false;
                mySudoku = true;
            } else if (state.equals("PROMPTING")) {
                lineCounter = 0;
                mySudoku = false;
                myPrompting = true;
            } else if (state.equals("LINE")) {
                lineCounter++;         
            }
        }
            
        public void characters(char[] ch, int start, int lenght) throws SAXException {
            String value = new String(ch, start, lenght);
            if (!Character.isISOControl(value.charAt(0))) {
            	System.out.println(state);
                if (state.equals("LINE")) {
                    if (myCondition) {
                        for (int i = 0; i < value.length(); i++) {
                            char s = value.charAt(i);
                            int num = Integer.valueOf(s);
                            myMatrixCondition[lineCounter - 1][i] = (num - 48);          
                        }    
                    } else if (mySolution) {
                        for (int i = 0; i < value.length(); i++) {
                            char s = value.charAt(i);
                            int num = Integer.valueOf(s);
                            myMatrixSolution[lineCounter - 1][i] = num - 48;          
                        }  
                    } else if (mySudoku) {
                        for (int i = 0; i < value.length(); i++) {
                            char s = value.charAt(i);
                            int num = Integer.valueOf(s);
                            myModel.setCell(lineCounter - 1, i, num - 48); 
                            System.out.println(num);
                        }      
                    } else if (myPrompting) {
                        for (int i = 0; i < value.length(); i++) {
                            char s = value.charAt(i);
                            int num = Integer.valueOf(s); 
                            myModel.setPrompting(lineCounter - 1, i, num - 48);
                        }         
                    }
                }
            }
        }
    }
    
}
