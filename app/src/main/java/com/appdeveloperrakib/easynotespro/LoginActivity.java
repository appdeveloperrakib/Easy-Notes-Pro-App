package com.appdeveloperrakib.easynotespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText,passewordEditText;
    Button logInBtn;
    ProgressBar progressBar;
    TextView createAccountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_id_edittext);
        passewordEditText = findViewById(R.id.password_edittext);
        logInBtn = findViewById(R.id.log_In_button);
        progressBar = findViewById(R.id.progress_bar);
        createAccountTextView = findViewById(R.id.create_account_text_button);

        logInBtn.setOnClickListener(view -> loginUser());
        createAccountTextView.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class)));


    }

    void loginUser(){

        String email = emailEditText.getText().toString();
        String password = passewordEditText.getText().toString();

        boolean isValidated = validateData(email,password);
        if(!isValidated){
            return;
        }

        loginAccountInFirebase(email,password);


    }

    void loginAccountInFirebase(String email, String password){

        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                changeInProgress(false);

                if(task.isSuccessful()){
                    //login is success

                    if(firebaseAuth.getCurrentUser().isEmailVerified()){

                        //go to mainActivity
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }
                    else{

                        Utility.showToast(LoginActivity.this,"Email isn't verified, Please verify your email");
                    }

                }

                else {

                    //login failed
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());


                }


            }
        });



    }



    void changeInProgress (boolean inprogress){


        if(inprogress){
            progressBar.setVisibility(View.VISIBLE);
            logInBtn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            logInBtn.setVisibility(View.VISIBLE);

        }



    }

    boolean validateData (String email, String password) {

        //validate the data

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
           // Toast.makeText(this, ""+email, Toast.LENGTH_SHORT).show();
            //return false;
        }

        if(password.length()<6)
        {
            passewordEditText.setError("Password length is invalid");
           // Toast.makeText(this, ""+password, Toast.LENGTH_SHORT).show();
            // return false;
        }

        return true;



    }




}