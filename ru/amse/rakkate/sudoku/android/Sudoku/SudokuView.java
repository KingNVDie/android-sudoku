package ru.amse.rakkate.sudoku.android.Sudoku;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.graphics.drawable.shapes.*;
import ru.amse.rakkate.sudoku.logic.impl.*;
import ru.amse.rakkate.sudoku.logic.*;


public class SudokuView extends View implements IModelListener{
	
	private static final String MYPREFS = null;
	private Paint myPaint;
	private final int myWidth  = Model.myWidth;
    private final int myHeight = Model.myWidth;
    private IModel myModel;
    private final int mySizeSquare = 30;
    private final int myLeft = 5;
    private final int myTop = 5;
    private int myX;
    private int myY;
    private AlertDialog.Builder myDialog;
    private AlertDialog.Builder myDialogFalse;
    private List<Integer> squareList;
    private boolean myIlluminate = true;
    private int myNum = 0;
    private Timer myTimer;
    private int myMinutes;
    private int mySeconds;

	
	public SudokuView(Context context) {
		super(context);
		initSudokuView(context);
	}
	
	public SudokuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSudokuView(context);
	}
	
	public SudokuView(Context context, AttributeSet attrs, int defaultStyle) {
		super(context, attrs, defaultStyle);
		initSudokuView(context);
	}
	
	protected void initSudokuView(Context context) {
		setFocusable(true);
		myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
		myPaint.setColor(Color.BLACK);
		myX = 0;
		myY = 0;
		squareList = new LinkedList<Integer>();
		/*myTimer = new Timer("TIME");
		TimerTask timerTask = new UpdateTimeTask();
		myTimer.schedule(timerTask, 0);*/
		
	}
	
	public void setModel(IModel m) {
		myModel = m;
	}
	
	public void setDialog(AlertDialog.Builder d) {
		myDialog = d;
	}
	
	public void setDialogFalse(AlertDialog.Builder d) {
		myDialogFalse = d;
	}
	
	private void drawList(Canvas canvas) {
		myPaint.setColor(Color.BLACK);
		myPaint.setStyle(Style.STROKE);
		for (int i = 0; i < myHeight ; i++) {
			canvas.drawRect(myLeft + i * mySizeSquare, myTop + (mySizeSquare * (myWidth + 1)), myLeft + (i + 1) * mySizeSquare, myTop + (mySizeSquare * (myWidth + 2)), myPaint); 
			String s = (i + 1) + "";
			canvas.drawText(s, myLeft + i * mySizeSquare + 9, myTop + (mySizeSquare * (myWidth + 1)) + 21, myPaint);
		}
		canvas.drawRect(myLeft + mySizeSquare * myHeight, myTop + (mySizeSquare * (myWidth + 1)), myLeft + (myHeight + 1) * mySizeSquare, myTop + (mySizeSquare * (myWidth + 2)), myPaint);
		String s = "X";
		canvas.drawText(s, myLeft + mySizeSquare * myHeight + 9, myTop + (mySizeSquare * (myWidth + 1)) + 21, myPaint);
		if (myIlluminate == false) {
			myPaint.setColor(Color.RED);
			canvas.drawRect(myLeft + myNum * mySizeSquare + 2, myTop + (mySizeSquare * (myWidth + 1)), myLeft + (myNum + 1) * mySizeSquare - 2, myTop + (mySizeSquare * (myWidth + 2)) - 2, myPaint);
		}
		myPaint.setColor(Color.BLACK);
		myPaint.setStyle(Style.FILL);
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		canvas.drawRect(myLeft, myTop, myLeft + (mySizeSquare * myHeight) + 4, myTop + (mySizeSquare * myWidth) + 4,myPaint);
		int[][] matrix = myModel.getSudoku();
		int[][] matrixCondition = myModel.getSudokuCondition();
		myPaint.setColor(Color.WHITE);
		for (int i = 0; i < myHeight; i++) {
			for (int j = 0; j < myWidth; j++) {
				int left  = myLeft + (i * mySizeSquare) + 3;
                int top  = myTop + (j * mySizeSquare) + 3;
                if (matrix[j][i] != 0) {
                    String s = matrix[j][i] + "";
                    canvas.drawRect(left,top,left + mySizeSquare - 2, top + mySizeSquare - 2, myPaint);
                    myPaint.setColor(Color.GRAY);
                    myPaint.setTextSize(20);
                    if (matrixCondition[j][i] == 0) {
                    	myPaint.setColor(Color.BLACK);
                    }
                    canvas.drawText(s, myLeft + (i * mySizeSquare) + 11, (myTop + ((j + 1) * (mySizeSquare)) - 7), myPaint);
                    myPaint.setColor(Color.WHITE);
                } else {
                	canvas.drawRect(left,top,left + mySizeSquare - 2, top + mySizeSquare - 2, myPaint);
                }
			}
		}
		myPaint.setColor(Color.BLACK);
        canvas.drawRect(myLeft + (mySizeSquare * 3 ) + 2, myTop + 2, myLeft + (mySizeSquare * 3) + 4, myTop + (mySizeSquare * myHeight)+ 2, myPaint);
        canvas.drawRect(myLeft + (mySizeSquare * 6 ) + 2, myTop + 2, myLeft + (mySizeSquare * 6) + 4, myTop + (mySizeSquare * myHeight)+ 2, myPaint);
        canvas.drawRect(myLeft + 2, myTop + (mySizeSquare * 3), myLeft + (mySizeSquare * myHeight) + 2, myTop + (mySizeSquare * 3) + 2, myPaint);
        canvas.drawRect(myLeft + 2, myTop + (mySizeSquare * 6), myLeft + (mySizeSquare * myHeight) + 2, myTop + (mySizeSquare * 6) + 2, myPaint);	
        myPaint.setColor(Color.MAGENTA);
        myPaint.setStyle(Style.STROKE);
	    for (int i = 0; i < squareList.size(); i++) {
            int l = squareList.get(i) / Model.myHeight;
            int m = squareList.get(i) % Model.myHeight;
            canvas.drawRect((myLeft + (m * mySizeSquare) + 4), (myTop + (l * mySizeSquare) + 4), myLeft + (m * mySizeSquare) + mySizeSquare,  myTop + (l * mySizeSquare) + mySizeSquare,myPaint);
	    }
	    if (myIlluminate) {
	        myPaint.setColor(Color.RED);
	    } else {
	    	myPaint.setColor(Color.GRAY);
	    }
		canvas.drawRect(myLeft + (myX * mySizeSquare) + 4, myTop + (myY * mySizeSquare) + 4,myLeft + (myX * mySizeSquare) + mySizeSquare,  myTop + (myY * mySizeSquare) + mySizeSquare,myPaint);
	    myPaint.setStyle(Style.FILL);
	    myPaint.setColor(Color.BLACK);
	   // canvas.drawText(myString, 80, 150, myPaint);
	    drawList(canvas);
	}
	
	public void showDialog() {
		if (myModel.isAccuracy() == false) {
            int[][] matrix = myModel.getSudokuSolution();
            for (int i = 0; i < Model.myHeight; i++) {
                for (int j = 0; j < Model.myWidth; j++) {
                    if ((myModel.getCell(i, j) != matrix[i][j])) {
                        squareList.add(i * Model.myHeight + j);    
                    } else {
                        int number = squareList.indexOf(i * Model.myHeight + j);
                        if (number != -1) {
                            squareList.remove(number);
                        }
                    }  
                }
            }
            invalidate();
            myDialogFalse.show();
		} else {
		    myDialog.show();
		}
	}
	
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
		    if ((KeyCode >= 7) && (KeyCode <= 16)) {
		    	if (myModel.getSudokuCondition()[myY][myX] == 0) {
		    		myModel.setCell(myY, myX, KeyCode - 7);
	            	squareList.clear();  
		            if (myModel.isFull()) {
		                showDialog();
		            }
		    	}
		        return true;
		    }
		    if ((KeyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {
		    	if (myIlluminate) {
		    	    if (myY < 8) {
			           myY = myY + 1;
			           invalidate();
			           return true;
			       }
			   }
		    }
		    if ((KeyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
		    	if (myIlluminate) {
		    	    myIlluminate = false;
		    	    invalidate();
		    	} else {
		    		if (myNum == 9) {
		    			myModel.setCell(myY, myX, 0);
		    		} else {
		    		    myModel.setCell(myY, myX, (myNum + 1));
		    		}
		    		myNum = 0;
		    		myIlluminate = true;
		    		invalidate();
		    	}
		    	return true;
		    }
		    if ((KeyCode == KeyEvent.KEYCODE_DPAD_UP)) {
		    	if (myIlluminate) {
			        if (myY > 0) {
			            myY = myY - 1;
			            invalidate();
			            return true;
			        }
			    }
		     }
		    if (KeyCode == 62) {
		    	if (myIlluminate) {
		    	     int i = myX;
		             int j = myY;
		             int num = myModel.getSudokuSolution()[j][i];
		             myModel.setCell(j, i, num);
		    	}
		    	return true;	
		    }
		    if ((KeyCode == KeyEvent.KEYCODE_DPAD_LEFT)) {
		    	if (myIlluminate) {
			        if (myX > 0) {
			           myX = myX - 1;
			           invalidate();
			           return true;
			        }
		    	} else {
		    		if (myNum > 0) {
		    			myNum--;
		    			invalidate();
		    			return true;
		    		} else if (myNum == 0) {
		    			myNum = Model.myHeight;
		    			invalidate();
		    			return true;
		    		}
		    	}
		    }
		    if ((KeyCode == KeyEvent.KEYCODE_DPAD_RIGHT)) {
		    	if (myIlluminate) {
			        if (myX < 8) {
			           myX = myX + 1;
			           invalidate();
			           return true;
			        }
		    	} else {
		    		if (myNum < 9) {
		    			myNum++;
		    			invalidate();
		    			return true;
		    		} else if (myNum == 9) {
		    			myNum = 0;
		    			invalidate();
		    			return true;
		    		}
		    	}
		    }
		}
		return false;
	}
	
	 public void update(IModel m) {
	    myModel = m;
	    invalidate();
	 }
	 
	 public void fillMap() {
	     int [][] map = myModel.getSudokuSolution();
	     for (int i = 0; i < Model.myHeight; i++) {
	         for (int j = 0; j < Model.myWidth; j++) {
	             myModel.setCell(i, j, map[i][j]);   
	         }
	     }
	 }
	 

	 
}
