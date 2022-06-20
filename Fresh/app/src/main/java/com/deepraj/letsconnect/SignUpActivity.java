package com.deepraj.letsconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.deepraj.letsconnect.Models.Users;
import com.deepraj.letsconnect.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    //for binding view
    ActivitySignUpBinding binding;
    //Authentication variable
    private FirebaseAuth mAuth;
    //Database variable
    FirebaseDatabase database;
    //Loading Feature
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //to hide action bar
        getSupportActionBar().hide();

        //to show the message when its loading
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Wait a minute, we're creating your account");



        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.txtUsername.getText().toString().isEmpty() && !binding.txtEmail.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty())
                {
                    //shows the dialog during creation of user account
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword
                            (binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //closes the dialog after successful creation of user
                                    progressDialog.dismiss();


                                    if(task.isSuccessful()){

                                        Users user = new Users(binding.txtUsername.getText().toString(),binding.txtEmail.getText().toString(),
                                                binding.txtPassword.getText().toString());

                                        //Taking User UID and storing User UID in database
                                        String id = task.getResult().getUser().getUid();
                                        database.getReference().child("Users").child(id).setValue(user);

                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignUpActivity.this,"Sign Up Successful",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignUpActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    //executes is any fields are empty
                    Toast.makeText(SignUpActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //on clicking  "Already have an account" it should jump to signIn activity
        binding.txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}