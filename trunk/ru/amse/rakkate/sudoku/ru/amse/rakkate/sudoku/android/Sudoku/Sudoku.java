package ru.amse.rakkate.sudoku.android.Sudoku;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

public class Sudoku extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	menu.add(0, 0, 0, "Continue");
    	menu.add(0, 1, 0, "New game");
    	menu.add(0, 2, 0, "Direction");
    	menu.add(0, 3, 0, "Tuning");
    	return true;
    }
    
    public void showSudoku() {
    	SudokuView sv = new SudokuView(this);
		setContentView(sv);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case 0:
    		return true;
    	case 1:
    		showSudoku();
    		return true;
    	case 2:
    		return true;
    	case 3:
    		return true;
    	
    	}
    	return true;
    }
}