package ru.amse.rakkate.sudoku.android.Sudoku;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ru.amse.rakkate.sudoku.logic.IGeneratorCondition;
import ru.amse.rakkate.sudoku.logic.IModel;
import ru.amse.rakkate.sudoku.logic.impl.GeneratorCondition;
import ru.amse.rakkate.sudoku.logic.impl.Model;
import ru.amse.rakkate.sudoku.logic.impl.ReaderXML;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;

public class Sudoku extends Activity {
	
    private IModel myModel; 
    
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	menu.add(0, 0, 0, "Continue");
    	menu.add(0, 1, 0, "New game");
    	menu.add(0, 2, 0, "Help");
    	menu.add(0, 3, 0, "Settings");
    	menu.add(0, 4, 0, "Solution");
    	menu.add(0, 5, 0, "Check");
    	return true;
    }
    
    public void showSudoku() {
    	SudokuView sv = new SudokuView(this);
    	myModel.addListener(sv);
    	sv.setModel(myModel);
		setContentView(sv);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case 0:
    		try {
                InputStream r = null;
                try {
                    r = openFileInput("task.xml");
                    ReaderXML reader = new ReaderXML();
                    myModel = reader.readModel(r);
                } finally {
                    if (r != null) {
                        r.close();
                    }
                }
    		} catch (ParserConfigurationException e) {   
    			e.printStackTrace();
            } catch (SAXException e) {   
            	e.printStackTrace();
            } catch (IOException e) {
            	e.printStackTrace();
            }  
            showSudoku();
    		return true;
    	case 1:
    		myModel = new Model();
    		IGeneratorCondition generator = new GeneratorCondition();
            int[][] matrix1 = generator.createCondition(42);
            myModel.setSudoku(matrix1);
    		showSudoku();
    		return true;
    	case 2:
    		return true;
    	case 3:
    		return true;
    	case 4:
    		myModel.setSudoku(myModel.getSudokuSolution());
    		showSudoku();
    		return true;
    	case 5: 
    		AlertDialog.Builder d = new AlertDialog.Builder(this);
    		if (myModel.isAccuracy()) {
    			d.setMessage("You have won!");
    			d.setPositiveButton("OK", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {						
					}
    			});   			
    		} else {
    		    d.setMessage("The answer incorrect");	
    		    d.setPositiveButton("OK", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {						
					}
    			});
    		}
    		d.show();
    		return true;
    	
    	}
    	return true;
    }
    
  
}