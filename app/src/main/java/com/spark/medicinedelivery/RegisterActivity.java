package com.spark.medicinedelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.iid.FirebaseInstanceId;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Spinner user_category;
    private EditText mUsername, mPassword, mEmail;
    private Button CreateAcc, prev_page_btn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user_category = (Spinner) findViewById(R.id.user_type);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.usertype_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        user_category.setAdapter(adapter);

        mUsername = (EditText)findViewById(R.id.username_inp);
        mPassword = (EditText)findViewById(R.id.passWord_inp);
        mEmail = (EditText)findViewById(R.id.email_inp);
        CreateAcc = (Button)findViewById(R.id.create_acc_btn);
        prev_page_btn = (Button)findViewById(R.id.prev_page_btn);

        mRegProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        prev_page_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(LoginIntent);
                finish();
            }
        });

        CreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();
                String user_cat = user_category.getSelectedItem().toString();

                if (username.trim().equalsIgnoreCase(""))
                    mUsername.setError("Field cannot be blank");
                else if (password.trim().equalsIgnoreCase(""))
                    mPassword.setError("Field cannot be blank");
                else if (email.trim().equalsIgnoreCase(""))
                    mEmail.setError("Field cannot be blank");
                else if (!isEmailValid(email))
                    mEmail.setError("Invalid email ID");
                else if (password.length()<6)
                    mPassword.setError("Password must have atleast 6 characters");
                else
                {
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(username, email, password, user_cat);
                }

            }
        });

    }

    private void register_user(final String username, final String email, String password, final String user_cat) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String UID = current_user.getUid();

                    String current_user_id = mAuth.getCurrentUser().getUid();
                    //String deviceToken = FirebaseInstanceId.get

//                    Date date = Calendar.getInstance().getTime();
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
//                    String current_date = simpleDateFormat.format(date);

                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String current_date = simpleDateFormat.format(date);

                    Map<String, String> info = new HashMap<String, String>();
                    info.put("username", username);
                    info.put("email", email);
                    info.put("image", "default");
                    info.put("thumb_image", "default");
                    info.put("reg_date", current_date);
                    info.put("last_login_date", "no data available");


                    if (user_cat.equalsIgnoreCase("Buyer"))
                    {
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Buyer").child(UID);

                        mDatabase.setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    mRegProgress.dismiss();
                                    Intent BuyerReg = new Intent(RegisterActivity.this, BuyerRegisterActivity.class);
                                    BuyerReg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(BuyerReg);
                                    finish();
                                }
                            }
                        });
                    }
                    else  if (user_cat.equalsIgnoreCase("Seller"))
                    {
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Seller").child(UID);

                        mDatabase.setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    mRegProgress.dismiss();
                                    Intent SellerReg = new Intent(RegisterActivity.this, SellerRegisterActivity.class);
                                    SellerReg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(SellerReg);
                                    finish();
                                }
                            }
                        });
                    }
                    else if (user_cat.equalsIgnoreCase("Employee"))
                    {
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Employee").child(UID);

                        mDatabase.setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    mRegProgress.dismiss();
                                    Intent EmployeeReg = new Intent(RegisterActivity.this, EmployeeRegisterActivity.class);
                                    EmployeeReg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(EmployeeReg);
                                    finish();
                                }
                            }
                        });
                    }

                    mRegProgress.dismiss();

                }
                else
                {
                    mRegProgress.hide();
                    Toast.makeText(RegisterActivity.this, "Please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}



