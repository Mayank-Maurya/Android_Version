package com.e.android_version;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    String u="";
    String p="";
    private TextView logintv;
    private FirebaseAuth mAuth;
    private EditText useremail;
    private EditText username;
    private EditText userpassword;
    private EditText userconfirmpass;
    private Button Signup;
    private CheckBox checkBox;
    private TextView terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //initializing
        mAuth = FirebaseAuth.getInstance();
        logintv=findViewById(R.id.logintv);
        useremail=findViewById(R.id.useremail);
        username=findViewById(R.id.username);
        userpassword=findViewById(R.id.userpassword);
        userconfirmpass=findViewById(R.id.userconfirmpass);
        Signup=findViewById(R.id.btnsignup);
        checkBox=findViewById(R.id.checkbox);
        terms=findViewById(R.id.policy);



        //listening
        logintv.setOnClickListener(view -> {
            startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            finish();
        });
        terms.setOnClickListener(view -> new AlertDialog.Builder(SignupActivity.this)
                .setTitle("policy")
                .setMessage("The Terms And Conditions For Registering You as a reponsible user is :\n"+"1: You don't Post Unneccesary questions.\n"
                        +"2: always give your original email address.\n"+"3:be humble to answer everybody.\n")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // Continue with delete operation
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());


        //validating


        Signup.setOnClickListener(view -> {

            if(isvaliddetails())
            {
                signitup(u,p);
            }else{
                Toast.makeText(SignupActivity.this, "Validation failed Signup again",
                        Toast.LENGTH_SHORT).show();

            }


        });


    }
    void signitup(String useremail,String userpass)
    {
        mAuth.createUserWithEmailAndPassword(useremail,userpass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignupActivity.this, "User Registered Succesfully: login again", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                            finish();
                        }else{

                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    boolean isvaliddetails()
    {
        String uemail=useremail.getText().toString();
        String usernm=username.getText().toString();
        String upass=userpassword.getText().toString();
        String ucfmpass=userconfirmpass.getText().toString();
        if(! isValidEmail(uemail))
        {
            Toast.makeText(this, "Sorry: your email is not in a valid format", Toast.LENGTH_SHORT).show();
        }else{
            if(usernm.length()<=3)
            {
                Toast.makeText(this, "Sorry: username length must be greater than 3", Toast.LENGTH_SHORT).show();
            }else{
                if(upass.length()<6)
                {
                    Toast.makeText(this, "Sorry: your password length must be larger than 6", Toast.LENGTH_SHORT).show();
                }else{
                    if(!ucfmpass.equals(upass))
                    {
                        Toast.makeText(this, "Sorry: your password is not matching with comfirm password", Toast.LENGTH_SHORT).show();
                    }else{
                        if(checkBox.isChecked()) {
                            u=uemail;
                            p=upass;
                            return true;
                        }else{
                            Toast.makeText(this, "Sorry: You haven't selected terms and policy.", Toast.LENGTH_SHORT).show();

                        }

                    }
                }
            }
        }
        return false;

    }
    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}