package ru.amse.rakkate.sudoku.android.Sudoku;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

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
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class Sudoku extends Activity {
	private SudokuView sv;
	private final int MENU_CONTINUE = Menu.FIRST;
	private final int MENU_NEW = Menu.FIRST + 1;
	private final int MENU_HELP = Menu.FIRST + 2;
	private final int MENU_SOL = Menu.FIRST + 3;
	private final int MENU_RECORD = Menu.FIRST + 4;
    private IModel myModel = null; 
    private boolean myContinue;
    private int myButtonNumber;
    private Timer myTimer;
    private int mySeconds;
    private List<String> myListName;
    private List<Integer> myListRecords;
    private TextView myTextRecords;
    private EditText myTextName;
    
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.setTheme(R.style.Sudoku);
        myListName = new LinkedList<String>();
        myListRecords = new LinkedList<Integer>();
        myTextRecords = new TextView(this);
        myTextName = new EditText(this);
        loadPreferences();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuItem itemContinue = menu.add(0, MENU_CONTINUE, Menu.NONE, "Continue");
    	MenuItem itemNew = menu.add(0, MENU_NEW, Menu.NONE, "New game");
    	MenuItem itemHelp = menu.add(0, MENU_HELP, Menu.NONE, "Help");
    	MenuItem itemSolution = menu.add(0, MENU_SOL, Menu.NONE, "Solution");
    	MenuItem itemRecord = menu.add(0, MENU_RECORD, Menu.NONE, "Records");
    	itemContinue.setIcon(R.drawable.load);
    	itemNew.setIcon(R.drawable.open);
    	itemHelp.setIcon(R.drawable.help);
    	itemSolution.setIcon(R.drawable.solution);  
    	
    	return true;
    }
    
    public boolean onPrepareOptionsMenu(Menu menu) {
    	super.onPrepareOptionsMenu(menu);
    	MenuItem menuContinue = menu.findItem(MENU_CONTINUE);
    	if (myContinue == true) {
    		menuContinue.setVisible(true);
    	} else {
    	    menuContinue.setVisible(false);
    	}
    	MenuItem menuNew = menu.findItem(MENU_NEW);
    	menuNew.setVisible(true);
    	MenuItem menuHelp = menu.findItem(MENU_HELP);
    	menuHelp.setVisible(true);
    	MenuItem menuSol = menu.findItem(MENU_SOL);
    	if ((myModel != null) && (myModel.isAccuracy() == false)) { 
    	    menuSol.setVisible(true);
    	} else {
    		menuSol.setVisible(false);
    	}
    	MenuItem menuRec = menu.findItem(MENU_RECORD);
    	menuRec.setVisible(true);
    	return true;
    	}
    
    public void showSudoku() {
    	sv = new SudokuView(this);
    	sv.setDialog(getDialogTrue());
    	sv.setDialogFalse(getDialogFalse());
    	myModel.addListener(sv);
    	sv.setModel(myModel);
		setContentView(sv);
		myTimer = new Timer("TIME");
		TimerTask timerTask = new UpdateTimeTask();
		myTimer.schedule(timerTask, 100);
    }
    
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case MENU_CONTINUE:
    		if (myContinue == true) {
    		    try {
                    InputStream r = null;
                    try {
                        r = openFileInput("task.txt");
                        ReaderTXT reader = new ReaderTXT();
                        myModel = reader.readStream(r, myModel);
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
    		} else {
    			AlertDialog.Builder d = new AlertDialog.Builder(this);
    			d.setMessage("Sudoku it is not loaded");
    			d.setPositiveButton("OK", new OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {						
    				}
    			}); 
    			d.show();
    		}
    		return true;
    	case MENU_NEW:
    		setContentView(R.layout.button);
    		Button buttonLight = (Button)findViewById(R.id.lightButton);
    		Button buttonMedium = (Button)findViewById(R.id.mediumButton);
    		Button buttonDifficult = (Button)findViewById(R.id.difficultButton);
    		if (myButtonNumber == 0) {
    			
    		} else if (myButtonNumber == 1) {
    			
    		} else if (myButtonNumber == 2) {
    			
    		}
    		buttonLight.setOnClickListener(new Button.OnClickListener() {

				public void onClick(View v) {
					myModel = new Model();
					myButtonNumber = 0;
		    		IGeneratorCondition generator = new GeneratorCondition();
		            int[][] matrix1 = generator.createCondition(42);
		            myModel.setSudoku(matrix1);	
		            showSudoku();
				}  			
    		});
    		
    		buttonMedium.setOnClickListener(new Button.OnClickListener() {

				public void onClick(View v) {
					myModel = new Model();
					myButtonNumber = 1;
		    		IGeneratorCondition generator = new GeneratorCondition();
		            int[][] matrix1 = generator.createCondition(50);
		            myModel.setSudoku(matrix1);	
		            showSudoku();
				}  			
    		});
    		buttonDifficult.setOnClickListener(new Button.OnClickListener() {

				public void onClick(View v) {
					myModel = new Model();
					myButtonNumber = 2;
		    		IGeneratorCondition generator = new GeneratorCondition();
		            int[][] matrix1 = generator.createCondition(55);
		            myModel.setSudoku(matrix1);		
		            showSudoku();
				}  			
    		});
    		return true;
    	case MENU_HELP:
    		TextView help = new TextView(this);
    		try {
					help = readHelp(help);
				} catch (IOException e) {
					e.printStackTrace();
				}
    		setContentView(help);
    		return true;

    	case MENU_SOL:
    		if (myModel != null) {
    		        myModel.setSudoku(myModel.getSudokuSolution());
    		        myContinue = false;
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
    		return true;
    	
       case MENU_RECORD:
    		showRecords();
           return true;
    	}
    	return true;
    }
    
    private void showRecords() {
    	setContentView(R.layout.button);
		Button buttonLight1 = (Button)findViewById(R.id.lightButton);
		Button buttonMedium1 = (Button)findViewById(R.id.mediumButton);
		Button buttonDifficult1 = (Button)findViewById(R.id.difficultButton);
		buttonLight1.setOnClickListener(new Button.OnClickListener() {
          public void onClick(View v) {
			String text = "\n" +"\n" + "\n";
			for (int i = 0; i < 3; i++) {
				if (myListRecords.get(i) == 0) {
					 text = text + (i + 1) + "." + " ------------" + "\n";	
				} else {	
					text  = text + (i + 1) + "." + myListName.get(i) + ":  " + (myListRecords.get(i) / 60) + ":" + (myListRecords.get(i) % 60) + "\n";
				}
					text = text + "\n" +"\n" + "\n";
				}
				myTextRecords.setText(text);
				myTextRecords.setTextColor(Color.BLUE);
				myTextRecords.setTextSize(25);
				setContentView(myTextRecords);
			}  			
		  });
		
		   buttonMedium1.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					String text = "\n" + "\n" + "\n";
					for (int i = 0; i < 3; i++) {
						if (myListRecords.get(i + 3) == 0) {
						    text = text + (i + 1) + "." + " ------------" + "\n";	
						} else {	
						    text  = text + (i + 1) + "." + myListName.get(i + 3) + ":  " + (myListRecords.get(i + 3) / 60) + ":" + (myListRecords.get(i + 3) % 60) + "\n";
						}
						text = text + "\n" +"\n" + "\n";
					}
					myTextRecords.setText(text);
					myTextRecords.setTextColor(Color.BLUE);
					myTextRecords.setTextSize(25);
					setContentView(myTextRecords);	
				}  			
		   });
		   buttonDifficult1.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					String text = "\n" + "\n" + "\n";
					for (int i = 0; i < 3; i++) {
						if (myListRecords.get(i + 6) == 0) {
						    text = text + (i + 1) + "." + " ------------" + "\n";	
						} else {	
						    text  = text + (i + 6) + "." + myListName.get(i + 6) + ":  " + (myListRecords.get(i + 6) / 60) + ":" + (myListRecords.get(i + 6) % 60) + "\n";
						}
						text = text + "\n" +"\n" + "\n";
					}
					myTextRecords.setText(text);
					myTextRecords.setTextColor(Color.BLUE);
					myTextRecords.setTextSize(25);
					setContentView(myTextRecords);
				}  			
		   });	
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
    	help.setText(s);
    	help.setTextColor(Color.BLACK);
    	return help;
    	
    }
    
    private void changeRecords(int i) {
    	 if (myListRecords.get(i + 2) > mySeconds) {
    		 setContentView(myTextName);
    		 Editable text = myTextName.getText();
             String s = text.toString();
 	    	 if (myListRecords.get(i + 1) > mySeconds) {
 	    		if (myListRecords.get(i) > mySeconds) {
 	    			myListRecords.set((i + 2), myListRecords.get(1));
 	    			myListRecords.set((i + 1), myListRecords.get(0));
 	    			myListRecords.set(i, mySeconds);
 	    			myListName.set((i + 2), myListName.get(1));
 	    			myListName.set((i + 1), myListName.get(0));
 	    			myListName.set(i, s);
 	    		} else {
 	    			myListRecords.set((i + 2), myListRecords.get(1));
 	    			myListRecords.set((i + 1), mySeconds);
 	    			myListName.set((i + 2), myListName.get(1));
 	    			myListName.set((i + 1), s);	
 	    		}
 	    	} else {
 	    		myListRecords.set((i + 2), mySeconds);
                myListName.set((i + 2), s);
 	    	}
 	    }
    	
    }
    
    public void setRecord() {
    	if (myButtonNumber == 0) {
    		changeRecords(0);
    		showRecords();
    	} else if (myButtonNumber == 1) {
    	    changeRecords(3);	
    	    showRecords();
    	} else if (myButtonNumber == 2) {
    	     changeRecords(6);
    	     showRecords();
    	}
        	
    }
    
    public AlertDialog.Builder getDialogTrue() {
    	AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setMessage("You have won!");
		d.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {						
			}
		}); 			
		return d;
    }
    
    public AlertDialog.Builder getDialogFalse() {
    	AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setMessage("The answer incorrect");	
		d.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {						
			}
		});
		return d;
    }
    
    public boolean onKeyDown(int KeyCode, KeyEvent event) {
    	if ((KeyCode == KeyEvent.KEYCODE_BACK)) {
	    	if (event.getAction() == KeyEvent.ACTION_DOWN) {
	    		saveSolution();
	    		saveActivityPreferences();
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
    
    protected void saveActivityPreferences(){
        SharedPreferences activityPreferences = getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        if ((myModel != null) && (myModel.isAccuracy() == false)) {
            editor.putBoolean("isTrue", true);
        } else {
        	editor.putBoolean("isTrue", false);
        }
        editor.putInt("wholeNumber", myButtonNumber);
        editor.putString("FirstLight", myListName.get(0));
        editor.putString("SecondLight", myListName.get(1));
        editor.putString("ThirdLight", myListName.get(2));
        editor.putString("FirstMedium", myListName.get(3));
        editor.putString("SecondMedium", myListName.get(4));
        editor.putString("ThirdMedium", myListName.get(5));
        editor.putString("FirstDifficult", myListName.get(6));
        editor.putString("SecondDifficult", myListName.get(7));
        editor.putString("ThirdDifficult", myListName.get(8));
        editor.putInt("FirstLight", myListRecords.get(0));
        editor.putInt("SecondLight", myListRecords.get(1));
        editor.putInt("ThirdLight", myListRecords.get(2));
        editor.putInt("FirstMedium", myListRecords.get(3));
        editor.putInt("SecondMedium", myListRecords.get(4));
        editor.putInt("ThirdMedium", myListRecords.get(5));
        editor.putInt("FirstDifficult", myListRecords.get(6));
        editor.putInt("SecondDifficult", myListRecords.get(7));
        editor.putInt("ThirdDifficult", myListRecords.get(8));     
        editor.commit();
    }
    
    public void loadPreferences() {
    	SharedPreferences activityPreferences = getPreferences(Activity.MODE_PRIVATE);
    	myContinue = activityPreferences.getBoolean("isTrue", false);
        myButtonNumber = activityPreferences.getInt("wholeNumber", 0);
        myListName.add(activityPreferences.getString("FirstLight", ""));
        myListName.add(activityPreferences.getString("SecondLight", ""));
        myListName.add(activityPreferences.getString("ThirdLight", ""));
        myListName.add(activityPreferences.getString("FirstMedium", ""));
        myListName.add(activityPreferences.getString("SecondMedium", ""));
        myListName.add(activityPreferences.getString("ThirdMedium", ""));
        myListName.add(activityPreferences.getString("FirstDifficult", ""));
        myListName.add(activityPreferences.getString("SecondDifficult", ""));
        myListName.add(activityPreferences.getString("ThirdDifficult", ""));
        myListRecords.add(activityPreferences.getInt("FirstLight", 0));
        myListRecords.add(activityPreferences.getInt("SecondLight", 0));
        myListRecords.add(activityPreferences.getInt("ThirdLight", 0));
        myListRecords.add(activityPreferences.getInt("FirstMedium", 0));
        myListRecords.add(activityPreferences.getInt("SecondMedium", 0));
        myListRecords.add(activityPreferences.getInt("ThirdMedium", 0));
        myListRecords.add(activityPreferences.getInt("FirstDifficult", 0));
        myListRecords.add(activityPreferences.getInt("SecondDifficult", 0));
        myListRecords.add(activityPreferences.getInt("ThirdDifficult", 0));
        
    }
    
    class UpdateTimeTask extends TimerTask {
		   public void run() {
		       long millis = System.currentTimeMillis();
		       int seconds = (int) (millis / 1000);
		       seconds     = seconds % 60;
		       mySeconds = seconds;
		   }
		}
    
  
}