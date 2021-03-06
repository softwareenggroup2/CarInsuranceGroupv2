package com.group2.carinsuranceapp;

import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    EditText input_email, input_password;
    TextView btnSignup, btnForgotPass;

    RelativeLayout activity_main;

    private FirebaseAuth auth;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.login_btn_login);
        input_email = (EditText)findViewById(R.id.login_email);
        input_password = (EditText)findViewById(R.id.login_password);
        btnSignup = (TextView)findViewById(R.id.login_btn_signup);
        btnForgotPass = (TextView)findViewById(R.id.login_btn_forgot_password);
        activity_main = (RelativeLayout)findViewById(R.id.activity_main);

        btnSignup.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //init Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Check already session, if okay go dashboard.
        if(auth.getCurrentUser() != null)
            startActivity(new Intent(MainActivity.this, LoggedInMainActivity.class));

        if (!isNetworkAvailable()){
            Toast.makeText(this, "User is not connected to the network", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.login_btn_forgot_password){
            startActivity(new Intent(MainActivity.this,ForgotPassword.class));
            finish();
        }
        else if(view.getId() == R.id.login_btn_signup){
            startActivity(new Intent(MainActivity.this,SignUp.class));
            finish();
        }
        else if(view.getId() == R.id.login_btn_login){
            if(!input_email.getText().toString().equals("") && !input_password.getText().toString().equals("") ){
            loginUser(input_email.getText().toString(),input_password.getText().toString()); }
            else{
              Toast.makeText(getApplicationContext(),"Input fields empty",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void loginUser(final String email, final String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            toast = Toast.makeText(getApplicationContext(),task.getException().getMessage(),Snackbar.LENGTH_LONG);
                            toast.show();

                        }
                        else{
                            startActivity(new Intent(MainActivity.this, LoggedInMainActivity.class));
                        }
                    }
                });

    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null) && (activeNetworkInfo.isConnected());
    }


}
