package ru.amse.rakkate.sudoku.android.Sudoku;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import android.os.Handler;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.*;
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
	}
	
	public void setModel(IModel m) {
		myModel = m;
		//invalidate();
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		canvas.drawRect(myLeft, myTop, myLeft + (mySizeSquare * myHeight) + 4, myTop + (mySizeSquare * myWidth) + 4,myPaint);
		myPaint.setColor(Color.RED);
		canvas.drawRect(myLeft + (myX * mySizeSquare) + 1, myTop + (myY * mySizeSquare) + 2,myLeft + (myX * mySizeSquare) + 2 + mySizeSquare ,  myTop + (myY * mySizeSquare) + mySizeSquare + 3,myPaint);
		myPaint.setColor(Color.WHITE);
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
	}
	
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
		    if ((KeyCode >= 8) && (KeyCode <= 16)) {
		    	if (myModel.getSudokuCondition()[myY][myX] == 0) {
		            myModel.setCell(myY, myX, KeyCode - 7);
		    	}
		        return true;
		    }
		    if ((KeyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {
			    if (myY < 8) {
			       myY = myY + 1;
			       invalidate();
			       return true;
			   }
		    }
		    if ((KeyCode == KeyEvent.KEYCODE_DPAD_UP)) {
			    if (myY > 0) {
			       myY = myY - 1;
			       invalidate();
			       return true;
			    }
		     }
		    if ((KeyCode == KeyEvent.KEYCODE_DPAD_LEFT)) {
			    if (myX > 0) {
			       myX = myX - 1;
			       invalidate();
			       return true;
			    }
		    }
		    if  ((KeyCode == KeyEvent.KEYCODE_DPAD_RIGHT)) {
			    if (myX < 8) {
			       myX = myX + 1;
			       invalidate();
			       return true;
			    }
		    }
		    if ((KeyCode == KeyEvent.KEYCODE_BACK)) {
		    	saveSolution();
		    }
		}
		return false;
	}
	
	private void saveSolution() {
		try {
            FileWriter v = null;
            try {
            	v = new FileWriter("task.xml");
                v = new FileWriter("task.xml");
                WriterXML writer = new WriterXML();
                writer.writeModel(v, myModel);
            } finally {
                if (v != null) {
                    v.close();
                }
            }   
        } catch (IOException e) {
        	e.printStackTrace();
        }
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
