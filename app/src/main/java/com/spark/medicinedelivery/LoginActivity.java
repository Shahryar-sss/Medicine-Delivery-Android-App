package com.spark.medicinedelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextView CreateAcc;
    private FirebaseAuth mAuth;
    private DatabaseReference BuyerDatabase, SellerDatabase, EmployeeDatabase;
    private ProgressDialog CheckAccProgress, loginProgress;
    private EditText user_email, user_password;
    private Button sign_in_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CreateAcc = (TextView)findViewById(R.id.CreateAccBtn);
        mAuth = FirebaseAuth.getInstance();
        BuyerDatabase = FirebaseDatabase.getInstance().getReference().child("Buyer");
        SellerDatabase = FirebaseDatabase.getInstance().getReference().child("Seller");
        EmployeeDatabase = FirebaseDatabase.getInstance().getReference().child("Employee");

        user_email = (EditText)findViewById(R.id.login_email_inp_box);
        user_password = (EditText)findViewById(R.id.login_password_inp_box);
        sign_in_btn = (Button) findViewById(R.id.sign_in_btn);

        CheckAccProgress = new ProgressDialog(this);
        loginProgress = new ProgressDialog(this);

        CreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(RegisterIntent);
                RegisterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
            }
        });

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_email.getText().toString();
                String password = user_password.getText().toString();

                if(email.equalsIgnoreCase("") || password.equalsIgnoreCase(""))
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                else
                {
                    loginProgress.setTitle("Logging in");
                    loginProgress.setMessage("Please wait while we check your credentials");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();

                    loginUser(email, password);
                }


            }
        });
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                    sendToMainActivity();
                else
                {
                    loginProgress.dismiss();
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendToMainActivity() {

        final String UID = mAuth.getCurrentUser().getUid();

        BuyerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(UID))
                {
                    loginProgress.dismiss();
                    Intent BuyerMainIntent = new Intent(LoginActivity.this, BuyerMainActivity.class);
                    BuyerMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(BuyerMainIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        SellerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(UID))
                {
                    loginProgress.dismiss();
                    Intent SellerMainIntent = new Intent(LoginActivity.this, SellerMainActivity.class);
                    SellerMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(SellerMainIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        EmployeeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(UID))
                {
                    loginProgress.dismiss();
                    Intent EmployeeMainIntent = new Intent(LoginActivity.this, EmployeeMainActivity.class);
                    EmployeeMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(EmployeeMainIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser!=null)
        {
            final String UID = currentUser.getUid();

            CheckAccProgress.setTitle("Loading Account Details");
            CheckAccProgress.setMessage("Please wait while we check for active accounts");
            CheckAccProgress.setCanceledOnTouchOutside(false);
            CheckAccProgress.show();

            BuyerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(UID))
                    {
                        CheckAccProgress.dismiss();
                        Intent BuyerMainIntent = new Intent(LoginActivity.this, BuyerMainActivity.class);
                        BuyerMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(BuyerMainIntent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            SellerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(UID))
                    {
                        CheckAccProgress.dismiss();
                        Intent SellerMainIntent = new Intent(LoginActivity.this, SellerMainActivity.class);
                        SellerMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(SellerMainIntent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });

            EmployeeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(UID))
                    {
                        CheckAccProgress.dismiss();
                        Intent EmployeeMainIntent = new Intent(LoginActivity.this, EmployeeMainActivity.class);
                        EmployeeMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(EmployeeMainIntent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(LoginActivity.this);
        startActivity(parentIntent);
        finish();
    }
}
