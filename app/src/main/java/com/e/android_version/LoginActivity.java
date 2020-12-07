package com.e.android_version;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth Auth;
    private EditText username;
    private EditText password;
    private Button login;
    private TextView signup;
    private TextView forgot;
    private String m_Text="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Auth=FirebaseAuth.getInstance();

        username=findViewById(R.id.etusername);
        password=findViewById(R.id.etpassword);
        login=findViewById(R.id.btnlogin);
        signup=findViewById(R.id.donthaveanaccount);
        forgot=findViewById(R.id.tvforgotpass);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user=username.getText().toString();
                String pass=password.getText().toString();
                Authenticate(user,pass);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                finish();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_Text=username.getText().toString();
                if(m_Text.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Email can't be empty.", Toast.LENGTH_SHORT).show();
                }else{
                    if(isValidEmail(m_Text))
                    {

                        Auth.sendPasswordResetEmail(m_Text)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(LoginActivity.this, "we have sent you an email on your email address.", Toast.LENGTH_SHORT).show();
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "Password updated succesfully", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(LoginActivity.this, "Password didn't updated succesfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }else{
                        Toast.makeText(LoginActivity.this, "Sorry:Email is not in a valid format", Toast.LENGTH_SHORT).show();
                    }

                }



            }
        });
    }
    void Authenticate(String user,String pass)
    {
        if(!user.isEmpty() && !pass.isEmpty()) {
            Auth.signInWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();

                            } else {
                                Log.i("signInWithEmail:failure", task.getException() + "");
                            }
                        }
                    });
        }else{
            Toast.makeText(LoginActivity.this, "Details can't be empty.", Toast.LENGTH_SHORT).show();
        }

    }
    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}