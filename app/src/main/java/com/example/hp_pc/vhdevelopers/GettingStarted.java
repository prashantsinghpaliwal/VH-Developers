package com.example.hp_pc.vhdevelopers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GettingStarted extends AppCompatActivity implements View.OnClickListener {

    Button s,g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);
        s= (Button) findViewById(R.id.sign_in);
        g= (Button) findViewById(R.id.get_started);
        s.setOnClickListener(this);
        g.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.sign_in){
            Intent i=new Intent(GettingStarted.this,LoginActivity.class);
            startActivity(i);
        }
        else if (id==R.id.get_started){
            Intent i=new Intent(GettingStarted.this,RegisterActivity.class);
            startActivity(i);
        }
    }
}
