package com.denhihuynh.connectfour;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.denhihuynh.connectfour.R;

import interfaces.OnFragmentInteractionListener;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        tableLayout = (TableLayout) findViewById(R.id.gameTable);
        addGameBoardRows();
    }


    /**
     * Adds rows to the tablelayout which represents the game board.
     */
    private void addGameBoardRows() {
        LayoutInflater inflater = getLayoutInflater();
        for(int i = 0;i<6;i++){
            TableRow rowView =(TableRow) inflater.inflate(R.layout.tablerow_game, null, false);
            Button col1 = (Button) rowView.getChildAt(0);
            Button col2 = (Button) rowView.getChildAt(1);
            Button col3 = (Button) rowView.getChildAt(2);
            Button col4 = (Button) rowView.getChildAt(3);
            Button col5 = (Button) rowView.getChildAt(4);
            Button col6 = (Button) rowView.getChildAt(5);
            col1.setOnClickListener(this);
            col2.setOnClickListener(this);
            col3.setOnClickListener(this);
            col4.setOnClickListener(this);
            col5.setOnClickListener(this);
            col6.setOnClickListener(this);
            tableLayout.addView(rowView);
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_column_1:
                System.out.println("---------------------------- col1");
                break;
            case R.id.button_column_2:
                System.out.println("---------------------------- col2");
                break;
            case R.id.button_column_3:
                System.out.println("---------------------------- col3");
                break;
            case R.id.button_column_4:
                System.out.println("---------------------------- col4");
                break;
            case R.id.button_column_5:
                System.out.println("---------------------------- col5");
                break;
            case R.id.button_column_6:
                System.out.println("---------------------------- col6");
                break;
        }
    }
}
