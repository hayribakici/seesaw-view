package com.thehayro.android.seesawview.sample;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.thehayro.android.R;


public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
      final View seesaw = findViewById(R.id.image);
      Button button = (Button) findViewById(R.id.btn);
      button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          seesaw.performClick();
        }
      });

    }
}
