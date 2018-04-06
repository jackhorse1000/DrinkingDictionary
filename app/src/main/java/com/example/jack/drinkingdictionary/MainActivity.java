package com.example.jack.drinkingdictionary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mGameList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGameList = (RecyclerView) findViewById(R.id.game_list) ;
        mGameList.setHasFixedSize(true);
        mGameList.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Games");

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

        public GameViewHolder(View itemView){
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String name){
            TextView game_name = (TextView) mView.findViewById(R.id.game_name);
            game_name.setText(name);
        }

        public void setShortDesc(String desc){
            TextView game_shortDesc = (TextView) mView.findViewById(R.id.game_descShort);
            game_shortDesc.setText(desc);
        }

        public void setImage(Context context, String imageUrl){

            ImageView game_img = (ImageView) mView.findViewById(R.id.game_img);
            Picasso.get().load(imageUrl).into(game_img);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_add){
            goToDrinkGameActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToDrinkGameActivity(){
        Intent intent = new Intent(this, AddGameActivity.class);
        startActivity(intent);
    }

}
