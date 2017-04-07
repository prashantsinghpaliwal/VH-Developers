package com.example.hp_pc.vhdevelopers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText email,password;
    Button reg;
    FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(this);
        email= (EditText) findViewById(R.id.email_register);
        password= (EditText) findViewById(R.id.password_register);
        reg= (Button) findViewById(R.id.register);
        toolBar= (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setMessage("Registering...");
                String e=email.getText().toString().trim();
                String p=password.getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(e,p).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mDialog.dismiss();
                        String err=e.getMessage();
                        Toast.makeText(getApplicationContext(),"Couldn't register due to "+err,Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

    }
}
