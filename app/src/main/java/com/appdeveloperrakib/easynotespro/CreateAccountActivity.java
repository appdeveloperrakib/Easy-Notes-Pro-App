package com.appdeveloperrakib.easynotespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {


    EditText emailEditText,passewordEditText,confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        emailEditText = findViewById(R.id.email_id_edittext);
        passewordEditText = findViewById(R.id.password_edittext);
        confirmPasswordEditText=findViewById(R.id.confirm_password_edittext);
        createAccountBtn = findViewById(R.id.create_account_button);
        progressBar =  findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_button);


        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 createAccount();
            }
        });
        loginBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

    }

    void createAccount(){

        //take all the input
        String email = emailEditText.getText().toString();
        String password = passewordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();


        boolean isValidated = validateData(email,password,confirmPassword);
        if(!isValidated){
            return;
        }

        createAccountInFirebase(email,password);

    }

    void createAccountInFirebase(String email,String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        changeInProgress(false);

                        if (task.isSuccessful()){

                            //create account done
                            Utility.showToast(CreateAccountActivity.this,"Sucessfully create account , Check email to verify");
                            //Toast.makeText(CreateAccountActivity.this, "Sucessfully create account , Check email to verify", Toast.LENGTH_SHORT).show();

                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                            

                        }
                        else {

                            //failur

                            Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());


                        }



                    }
                });




    }

    void changeInProgress (boolean inprogress){


        if(inprogress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);

        }



    }


    boolean validateData (String email, String password, String confirmPassword ) {

        //validate the data

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            Toast.makeText(this, ""+email, Toast.LENGTH_SHORT).show();
            //return false;
        }

        if(password.length()<6)
        {
            passewordEditText.setError("Password length is invalid");
            Toast.makeText(this, ""+password, Toast.LENGTH_SHORT).show();
           // return false;
        }

       if(!password.equals(confirmPassword)){

          confirmPasswordEditText.setError("Password not mathched");
           Toast.makeText(this, ""+confirmPassword, Toast.LENGTH_SHORT).show();
         // return false;
        }

        return true;



    }


}