package com.example.kapil.snakegame;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Activity_main activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        activity_main = (Activity_main) findViewById(R.id.main_canvas);
        TextView textView = (TextView) toolbar.findViewById(R.id.textview);
        activity_main.setTextView(textView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.playagain:
                activity_main.reset();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onUp (View View) {activity_main.moveUp();}

    public void onDown (View View) {activity_main.moveDown();}

    public void onLeft (View View) {
        activity_main.moveLeft();
    }

    public void onRight (View View) {
        activity_main.moveRight();
    }

}
