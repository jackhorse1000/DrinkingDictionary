package com.example.jack.drinkingdictionary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddGameActivity extends AppCompatActivity {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference gameRef = mDatabase.child("Games");

    private ImageButton mSelectImage;
    private Uri mImageUri = null;

    private EditText mTxt_name;
    private EditText mTxt_descShort;
    private EditText mTxt_descLong;
    private EditText mTxt_diff;

    private StorageReference mStorage;
    private ProgressDialog mProgress;

    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        mTxt_name = (EditText) findViewById(R.id.txt_name);
        mTxt_descShort = (EditText) findViewById(R.id.txt_short);
        mTxt_descLong = (EditText) findViewById(R.id.txt_long);
        mTxt_diff = (EditText) findViewById(R.id.txt_difficulty);

        mStorage = FirebaseStorage.getInstance().getReference();
        mTxt_name = (EditText) findViewById(R.id.txt_name);
        mTxt_descShort = (EditText) findViewById(R.id.txt_short);
        mTxt_descLong = (EditText) findViewById(R.id.txt_long);
        mTxt_diff = (EditText) findViewById(R.id.txt_difficulty);
        mProgress = new ProgressDialog(this);

        Button button= (Button) findViewById(R.id.btn_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });

        mSelectImage = (ImageButton) findViewById(R.id.add_img);


        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });




    }

    private void startPosting(){

        String name = mTxt_name.getText().toString().trim();
        String descShort = mTxt_descShort.getText().toString().trim();
        String descLong = mTxt_descLong.getText().toString().trim();
        String difficulty = mTxt_diff.getText().toString().trim();

        // make sure fields are populated before you write
        if(!difficulty.equals("") || !name.equals("") || !descLong.equals("") || mImageUri != null){

            writeNewGame("1",name, descShort, descLong, difficulty);


        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please fill in the fields", Toast.LENGTH_LONG);
            toast.show();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            mImageUri = data.getData();

            mSelectImage.setImageURI(mImageUri);
        }
    }

    private void writeNewGame(String gameId, String gameName, String descShort, String descLong, String difficulty) {
        DrinkingGame game = new DrinkingGame(Integer.parseInt(gameId), gameName, descShort, descLong, difficulty);

        StorageReference filepath = mStorage.child("Game_Images").child(mImageUri.getLastPathSegment());

        mProgress.setMessage("Posting online...");
        mProgress.show();

        filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mTxt_name.setText("");
                mTxt_descShort.setText("");
                mTxt_descLong.setText("");
                mTxt_diff.setText("");
                mProgress.dismiss();
            }
        });


        gameRef.child(gameId).setValue(game);
    }
}
