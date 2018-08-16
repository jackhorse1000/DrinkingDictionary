package com.example.jack.drinkingdictionary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mGameList;
    private DatabaseReference mDatabaseGames;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mProcessLike = false;
    private DatabaseReference mDatabaseLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get recycler viwe and set details
        mGameList = (RecyclerView) findViewById(R.id.game_list) ;
        mGameList.setHasFixedSize(true);
        mGameList.setLayoutManager(new LinearLayoutManager(this));

        // Get Firebase Database Instance
        mDatabaseGames = FirebaseDatabase.getInstance().getReference().child("Games");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");

        mDatabaseUsers.keepSynced(true);
        mDatabaseGames.keepSynced(true);

        // Get Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){

                    // Go to the log in screen user is not logged in
                    Intent logInIntent = new Intent(MainActivity.this, LoginActivity.class);
                    // User will not be able to go back
                    logInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logInIntent);
                }
            }
        };

        //TODO (Remove later will not be used in final build)
        Button button= (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDrinkGameActivity();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //checkUserExist();

        mAuth.addAuthStateListener(mAuthListener);

        // Create a Firebase Recycler Adapter for each view
        FirebaseRecyclerAdapter<DrinkingGame, GameViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DrinkingGame, GameViewHolder>(
                        DrinkingGame.class,
                        R.layout.game_row,
                        GameViewHolder.class,
                        mDatabaseGames
                ) {
                    @Override
                    protected void populateViewHolder(GameViewHolder viewHolder, DrinkingGame model,
                                                      int position) {

                        final String postKey = getRef(position).getKey();
                        final String likeCount = "count";

                        viewHolder.setTitle(model.getGameName());
                        viewHolder.setShortDesc(model.getDescShort());
                        viewHolder.setImage(getApplicationContext(), model.getImageRef());
                        viewHolder.setmLikeButton(postKey);

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Intent to go full screen with the view clicked
                                Intent fullScreenIntent = new Intent(MainActivity.this, FullScreenActivity.class );
                                fullScreenIntent.putExtra("gameId", postKey);
                                startActivity(fullScreenIntent);


                            }
                        });


                        viewHolder.mLikeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mProcessLike = true;


                                    mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (mProcessLike) {
                                                if (dataSnapshot.child(postKey).hasChild(mAuth.getCurrentUser().getUid())) {
                                                    mDatabaseLike.child(postKey).child(mAuth.getCurrentUser().getUid()).removeValue();
                                                    mDatabaseLike.child(postKey).child(likeCount).setValue(1);
                                                    mProcessLike = false;

                                                } else {
                                                    mDatabaseLike.child(postKey).child(likeCount).setValue(2);
                                                    mDatabaseLike.child(postKey).child(mAuth.getCurrentUser().getUid()).setValue("random value");
                                                    mProcessLike = false;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                            }
                        });

                    }
                };

        mGameList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class GameViewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageButton mLikeButton;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;
        // Get the view
        public GameViewHolder(View itemView){
            super(itemView);

            mView = itemView;

            mLikeButton = (ImageButton) mView.findViewById(R.id.like_button);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();
            mDatabaseLike.keepSynced(true);


        }

        // Set the Title of the view
        public void setTitle(String name){
            TextView game_name = (TextView) mView.findViewById(R.id.game_name);
            game_name.setText(name);
        }

        public void setmLikeButton(final String postKey) {
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(postKey).hasChild(mAuth.getCurrentUser().getUid())) {
                        mLikeButton.setImageResource(R.drawable.heart_full_button);
                    } else {
                        mLikeButton.setImageResource(R.drawable.heart_empty_button);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        // Set the Short Description of the view
        public void setShortDesc(String desc){
            TextView game_shortDesc = (TextView) mView.findViewById(R.id.game_descShort);
            game_shortDesc.setText(desc);
        }

        // Set the background Image of the view
        public void setImage(Context context, String imageUrl){

            ImageView game_img = (ImageView) mView.findViewById(R.id.game_img);
            Picasso.get().load(imageUrl).into(game_img);
        }
    }

    //Create the Menu Options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    // When the add menu button is pressed call the go to drinking game activity method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_add){
            goToDrinkGameActivity();
        }

        if (item.getItemId() == R.id.action_logout){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }

    // Goes to the drinking game activity
    private void goToDrinkGameActivity(){
        Intent intent = new Intent(this, AddGameActivity.class);
        startActivity(intent);
    }

    private void checkUserExist() {

        final String userId = mAuth.getCurrentUser().getUid();

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(userId)){

                    // Go to the setup screen user is not logged in
                    Intent setupIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    // User will not be able to go back
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
