package com.example.jack.drinkingdictionary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private Button mLogin;
    private Button mRegister;

    private EditText mEmail;
    private EditText mPassword;

    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;

    private static final String TAG = "LoginActivity";

    private SignInButton mGoogleBtn;

    private static final int RC_SIGN_IN = 1;

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.jack.drinkingdictionary",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.btn_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });


        mProgress = new ProgressDialog(this);

        mGoogleBtn = (SignInButton) findViewById(R.id.btn_google);

        mLogin = (Button) findViewById(R.id.btn_login);
        mRegister = (Button) findViewById(R.id.btn_login_register);

        mEmail = (EditText) findViewById(R.id.login_email);
        mPassword = (EditText) findViewById(R.id.login_password);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });




        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Go to the log in screen user is not logged in
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);

            }
        });
    }

    // Checks if the user's details are correct
    private void checkLogin() {

        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
            mProgress.setMessage("Check Login....");
            mProgress.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        mProgress.dismiss();

                        checkUserExist();

                    }else{
                        mProgress.dismiss();
                        Toast.makeText(LoginActivity.this,"Error Login", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

    }


    // sign in intent for google sign in
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        mProgress.setMessage("Starting google sign in...");
        mProgress.show();

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                mProgress.dismiss();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Go to the log in screen user is not logged in
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                            finish();

                            mProgress.dismiss();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.login_activity), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            checkUserExist();

                        }

                        // ...
                    }
                });
    }

    // Checks the user exists in my database, if sign in with google or something else
    // they won't exist
    private void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {
            final String userId = mAuth.getCurrentUser().getUid();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(userId)) {

                        // Go to the log in screen user is not logged in
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        // User will not be able to go back
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                        finish();

                    } else {
                        // Go to the log in screen user is not logged in
                        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                        // User will not be able to go back
                        registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(registerIntent);
                        finish();

                        Toast.makeText(LoginActivity.this, "Need to register an account", Toast.LENGTH_LONG).show();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Go to the log in screen user is not logged in
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                            finish();

                            mProgress.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            checkUserExist();
                        }
                    }
                });
    }
}
