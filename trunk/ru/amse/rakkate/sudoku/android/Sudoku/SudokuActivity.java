package ru.amse.rakkate.sudoku.android.Sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;

public class SudokuActivity extends Activity {
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		SudokuView sv = new SudokuView(this);
		setContentView(sv);
		
	}

}
