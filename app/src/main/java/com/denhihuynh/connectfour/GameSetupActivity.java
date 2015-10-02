package com.denhihuynh.connectfour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.denhihuynh.connectfour.R;

import java.util.ArrayList;

public class GameSetupActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String FIRSTPLAYER = "firstPlayer";
    public final static String SECONDPLAYER = "secondPlayer";
    private EditText playerOneName, playerTwoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
        Button startButton = (Button) findViewById(R.id.startButton);
        playerOneName = (EditText) findViewById(R.id.edittext_firstplayer);
        playerTwoName = (EditText) findViewById(R.id.edittext_secondplayer);
        startButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Bundle extras = new Bundle();
        String firstPlayer = String.valueOf(playerOneName.getText());
        String secondPlayer = String.valueOf(playerTwoName.getText());
        extras.putString(FIRSTPLAYER,firstPlayer);
        extras.putString(SECONDPLAYER,secondPlayer);
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
