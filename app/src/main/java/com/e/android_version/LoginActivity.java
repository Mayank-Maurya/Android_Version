package com.e.android_version;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public String name="";
    private FirebaseAuth Auth;
    private EditText username;
    private EditText password;
    private Button login;
    private TextView signup;
    private TextView forgot;
    private String m_Text="";
    private FirebaseFirestore firebasedb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Auth=FirebaseAuth.getInstance();
        username=findViewById(R.id.etusername);
        password=findViewById(R.id.etpassword);
        firebasedb=FirebaseFirestore.getInstance();
        login=findViewById(R.id.btnlogin);
        signup=findViewById(R.id.donthaveanaccount);
        forgot=findViewById(R.id.tvforgotpass);
        login.setOnClickListener(view -> {
            String user=username.getText().toString();
            String pass=password.getText().toString();
            Authenticate(user,pass);
        });
        signup.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            finish();
        });

        forgot.setOnClickListener(view -> {
            m_Text=username.getText().toString();
            if(m_Text.isEmpty())
            {
                Toast.makeText(LoginActivity.this, "Email can't be empty.", Toast.LENGTH_SHORT).show();
            }else{
                if(isValidEmail(m_Text))
                {

                    Auth.sendPasswordResetEmail(m_Text)
                            .addOnCompleteListener(task -> {
                                Toast.makeText(LoginActivity.this, "we have sent you an email on your email address.", Toast.LENGTH_SHORT).show();
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Password updated succesfully", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Password didn't updated succesfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                }else{
                    Toast.makeText(LoginActivity.this, "Sorry:Email is not in a valid format", Toast.LENGTH_SHORT).show();
                }

            }



        });
    }
    void Authenticate(String user,String pass)
    {
        if(!user.isEmpty() && !pass.isEmpty()) {
            Auth.signInWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {

                            DocumentReference documentReference=firebasedb.collection("users")
                                    .document(Auth.getCurrentUser().getUid());
                            documentReference.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        }
                                    })
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task1) {

                                            if(task1.isSuccessful())
                                            {


                                                DocumentSnapshot doc=task1.getResult();


                                                //logging in first time


                                                if(! doc.exists())
                                                {
                                                    Map<String,Object> initialdata=new HashMap<>();
                                                    initialdata.put("name", "Not Provided");
                                                    initialdata.put("dob", "_");
                                                    initialdata.put("email", Auth.getCurrentUser().getEmail());
                                                    initialdata.put("phone", "_");
                                                    initialdata.put("query_asked", 0);
                                                    initialdata.put("total_answer", 0);
                                                    initialdata.put("total_views", 0);
                                                    initialdata.put("profileimg", "");

                                                    Map<String,Object> temp_data=new HashMap<>();
                                                    temp_data.put("achievements","_");
                                                    temp_data.put("contribution","_");
                                                    Map<String,Object> temp_data1=new HashMap<>();
                                                    temp_data1.put("codechef","_");
                                                    temp_data1.put("codeforces","_");
                                                    temp_data1.put("github","_");
                                                    temp_data.put("profileLinks",temp_data1);
                                                    temp_data.put("projects", Arrays.asList("",""));
                                                    temp_data.put("tags",Arrays.asList("",""));
                                                    temp_data.put("achievements","_");
                                                    temp_data.put("achievements","_");
                                                    initialdata.put("about",temp_data);

                                                    firebasedb.collection("users").document(Auth.getCurrentUser().getUid())
                                                            .set(initialdata)
                                                            .addOnCompleteListener(task2->{
                                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                                finish();



                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Toast.makeText(LoginActivity.this,"Something went wrong! Login Again"
                                                                        ,Toast.LENGTH_SHORT).show();

                                                            });

                                                }else{
                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    finish();

                                                }
                                            }

                                        }
                                    });








                        } else {
                            Log.i("signInWithEmail:failure", task.getException() + "");
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