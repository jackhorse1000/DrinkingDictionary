package com.example.jack.drinkingdictionary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUsernameField;
    private EditText mEmailField;
    private EditText mPasswordField;

    private Button mSignUpBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress = new ProgressDialog(this);

        mUsernameField = (EditText) findViewById(R.id.txt_username);
        mEmailField = (EditText) findViewById(R.id.txt_email);
        mPasswordField = (EditText) findViewById(R.id.txt_password);

        mSignUpBtn = (Button) findViewById(R.id.btn_signup);

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignUp();
            }
        });
    }
    // Registers the User after they have signed up
    private void startSignUp() {

        final String uName = mUsernameField.getText().toString().trim();
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        mProgress.setMessage("Signing Up....");
        mProgress.show();

        // Make sure fields are filled
        if(!TextUtils.isEmpty(uName) || !TextUtils.isEmpty(email)|| !TextUtils.isEmpty(password) ){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        String userId = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUser = mDatabase.child(userId);
                        currentUser.child("uName").setValue(uName);
                        currentUser.child("image").setValue("default");
                        mProgress.dismiss();

                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);


                    }
                }
            });
        }

    }
}
