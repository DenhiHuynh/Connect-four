package com.denhihuynh.connectfour;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import fragments.GameFragment;
import fragments.GameSetupFragment;
import fragments.StartScreenFragment;
import interfaces.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    public static final String SETUPGAME = "setupGame";
    public static final String START = "start";
    public static final String RESUME = "resume";
    public static final String HIGHSCORE = "highScore";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call

        StartScreenFragment startScreenFragment = new StartScreenFragment();
        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, startScreenFragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * Method used by fragments in order to communicate with the activity. Used to switch fragments.
     * @param command
     */
    @Override
    public void onFragmentInteraction(String command , ArrayList<String> extras) {
        switch (command){
            case SETUPGAME:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, GameSetupFragment.newInstance()).commit();
                break;
            case START:
                android.support.v7.app.ActionBar bar = getSupportActionBar();
                if(bar != null){
                    bar.hide();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, GameFragment.newInstance(extras)).commit();

                break;
            case RESUME:
                break;
            case HIGHSCORE:
                break;
        }

    }
}
