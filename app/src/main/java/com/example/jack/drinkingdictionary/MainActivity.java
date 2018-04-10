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
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mGameList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get recycler viwe and set details
        mGameList = (RecyclerView) findViewById(R.id.game_list) ;
        mGameList.setHasFixedSize(true);
        mGameList.setLayoutManager(new LinearLayoutManager(this));

        // Get Firebase Database Instance
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Games");

        // Get Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){

                    // Go to the log in screen user is not logged in
                    Intent logInIntent = new Intent(MainActivity.this, RegisterActivity.class);
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

        mAuth.addAuthStateListener(mAuthListener);

        // Create a Firebase Recycler Adapter for each view
        FirebaseRecyclerAdapter<DrinkingGame, GameViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DrinkingGame, GameViewHolder>(
                        DrinkingGame.class,
                        R.layout.game_row,
                        GameViewHolder.class,
                        mDatabase
                ) {
                    @Override
                    protected void populateViewHolder(GameViewHolder viewHolder, DrinkingGame model,
                                                      int position) {

                        viewHolder.setTitle(model.getGameName());
                        viewHolder.setShortDesc(model.getDescShort());
                        viewHolder.setImage(getApplicationContext(), model.getImageRef());

                    }
                };

        mGameList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class GameViewHolder extends RecyclerView.ViewHolder{
        View mView;

        // Get the view
        public GameViewHolder(View itemView){
            super(itemView);

            mView = itemView;
        }

        // Set the Title of the view
        public void setTitle(String name){
            TextView game_name = (TextView) mView.findViewById(R.id.game_name);
            game_name.setText(name);
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

}
