package com.example.jack.drinkingdictionary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddGameActivity extends AppCompatActivity {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference gameRef = mDatabase.child("Games");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Button button= (Button) findViewById(R.id.btn_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText txt_id = (EditText) findViewById(R.id.txt_id);
                String id = txt_id.getText().toString();

                EditText txt_name = (EditText) findViewById(R.id.txt_name);
                String name = txt_name.getText().toString();

                EditText txt_descShort = (EditText) findViewById(R.id.txt_short);
                String descShort = txt_descShort.getText().toString();

                EditText txt_descLong = (EditText) findViewById(R.id.txt_long);
                String descLong = txt_descLong.getText().toString();

                EditText txt_diff = (EditText) findViewById(R.id.txt_difficulty);
                String difficulty = txt_diff.getText().toString();




                writeNewUser(id, name, descShort, descLong, difficulty);
            }
        });

    }

    private void writeNewUser(String gameId, String gameName, String descShort, String descLong, String difficulty) {
        DrinkingGame game = new DrinkingGame(Integer.parseInt(gameId), gameName, descShort, descLong, difficulty);

        gameRef.setValue(game);
    }
}
