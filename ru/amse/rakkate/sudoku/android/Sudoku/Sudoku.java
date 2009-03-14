package ru.amse.rakkate.sudoku.android.Sudoku;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ru.amse.rakkate.sudoku.logic.IGeneratorCondition;
import ru.amse.rakkate.sudoku.logic.IModel;
import ru.amse.rakkate.sudoku.logic.impl.GeneratorCondition;
import ru.amse.rakkate.sudoku.logic.impl.Model;
import ru.amse.rakkate.sudoku.logic.impl.ReaderTXT;
import ru.amse.rakkate.sudoku.logic.impl.ReaderXML;
import ru.amse.rakkate.sudoku.logic.impl.WriterTXT;
import ru.amse.rakkate.sudoku.logic.impl.WriterXML;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;

public class Sudoku extends Activity {
	private SudokuView sv;
	
    private IModel myModel = null; 
    
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	menu.add(0, 0, 0, "Continue");
    	menu.add(0, 1, 0, "New game");
    	menu.add(0, 2, 0, "Help");
    	menu.add(0, 3, 0, "Solution");
    	return true;
    }
    
    public void showSudoku() {
    	sv = new SudokuView(this);
    	sv.setDialog(getDialog());
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
                    r = openFileInput("task.txt");
                    ReaderTXT reader = new ReaderTXT();
                    myModel = reader.readStream(r, myModel);
                    //for (int i = 0; i <9; i++) {
                    	//for (int j = 0; j <9; j++) {
                    	//System.out.println(myModel.getSudoku()[i][j]);
                    	//}
                    //}
                    showSudoku();
                } finally {
                    if (r != null) {
                        r.close();
                    } 
                }
            } catch (IOException e) {
            	e.printStackTrace();
            }  
            showSudoku();
    		return true;
    	case 1:
    		setContentView(R.layout.button);
    		Button buttonLight = (Button)findViewById(R.id.lightButton);
    		buttonLight.setOnClickListener(new Button.OnClickListener() {

				public void onClick(View v) {
					myModel = new Model();
		    		IGeneratorCondition generator = new GeneratorCondition();
		            int[][] matrix1 = generator.createCondition(42);
		            myModel.setSudoku(matrix1);	
		            showSudoku();
				}  			
    		});
    		
    		Button buttonMedium = (Button)findViewById(R.id.mediumButton);
    		buttonMedium.setOnClickListener(new Button.OnClickListener() {

				public void onClick(View v) {
					myModel = new Model();
		    		IGeneratorCondition generator = new GeneratorCondition();
		            int[][] matrix1 = generator.createCondition(50);
		            myModel.setSudoku(matrix1);	
		            showSudoku();
				}  			
    		});
    		
    		Button buttonDifficult = (Button)findViewById(R.id.difficultButton);
    		buttonDifficult.setOnClickListener(new Button.OnClickListener() {

				public void onClick(View v) {
					myModel = new Model();
		    		IGeneratorCondition generator = new GeneratorCondition();
		            int[][] matrix1 = generator.createCondition(55);
		            myModel.setSudoku(matrix1);		
		            showSudoku();
				}  			
    		});
    		return true;
    	case 2:
    		TextView help = new TextView(this);
    		try {
					help = readHelp(help);
				} catch (IOException e) {
					e.printStackTrace();
				}
    		setContentView(help);
    		return true;

    	case 3:
    		if (myModel != null) {
    		        myModel.setSudoku(myModel.getSudokuSolution());
    		        showSudoku();
    		} else {
    			AlertDialog.Builder d = new AlertDialog.Builder(this);
    			d.setMessage("Sudoku it is not loaded");
    			d.setPositiveButton("OK", new OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {						
    				}
    			}); 
    			d.show();
    		}
    	}
        return true;
    }

    private TextView readHelp(TextView help) throws IOException {
    	String s = null;
    	Resources myResources = getResources();
    	InputStream myFile = myResources.openRawResource(R.raw.help);
    	Scanner sc = new Scanner(myFile);
        ReaderTXT reader = new ReaderTXT();
        try {
            s = reader.readHelp(sc); 
        } finally {
            if (sc != null) {
                sc.close();
           }
           if (myFile != null) {
                myFile.close();
           }
        }
        System.out.println(s);
    	/*String s = " ";
    	Integer i = myFile.read();
    	while(i != -1) {
    		i = myFile.read();
    		s = s + i.toString();
    	}*/
    	help.setText(s);
    	return help;
    	
    }
    
    public AlertDialog.Builder getDialog() {
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
		return d;
    }
    
    public boolean onKeyDown(int KeyCode, KeyEvent event) {
    	if ((KeyCode == KeyEvent.KEYCODE_BACK)) {
	    	if (event.getAction() == KeyEvent.ACTION_DOWN) {
	    		System.out.println("LLLLL");
	    		saveSolution();
			}  	
	    }
		return false;
	}
    
    public void saveSolution() {
    	try {
            FileOutputStream v = null;
            try {
                v = openFileOutput("task.txt", Context.MODE_PRIVATE);
                WriterTXT writer = new WriterTXT(myModel);
                writer.writeStream(v, myModel);
            } finally { 
                if (v != null) {
                	v.close();
                }
            }
        } catch (IOException e) {
        	e.printStackTrace();
        }
    	
    }
    
  
}