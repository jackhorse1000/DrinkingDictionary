package com.example.jack.drinkingdictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FullScreenActivity extends AppCompatActivity {

    private String mPostKey =  null;
    private DatabaseReference mDatabase;

    private TextView mGameName;
    private TextView mGameDesc;
    private ImageView mBackgroundImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Games");

        mPostKey = getIntent().getExtras().getString("gameId");

        mGameName = (TextView) findViewById(R.id.fs_game_name);
        mGameDesc = (TextView) findViewById(R.id.fs_game_descLong);
        mBackgroundImg = (ImageView) findViewById(R.id.fs_game_img);

        mDatabase.child(mPostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String postTitle = (String) dataSnapshot.child("gameName").getValue();
                String postDesc = (String) dataSnapshot.child("descLong").getValue();
                String postImg = (String) dataSnapshot.child("ImageRef").getValue();

                mGameName.setText(postTitle);
                mGameDesc.setText(postDesc);

                Picasso.get().load(postImg).into(mBackgroundImg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
