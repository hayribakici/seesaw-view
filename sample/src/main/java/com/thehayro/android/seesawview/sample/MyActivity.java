package com.thehayro.android.seesawview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.thehayro.android.R;


public class MyActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final View seesaw = findViewById(R.id.seesaw_view);
        seesaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "View Clicked! *v* yay", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
