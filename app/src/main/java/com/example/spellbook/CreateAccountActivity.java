package com.example.spellbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Insert;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.spellbook.DB.AppDatabase;
import com.example.spellbook.DB.CardDAO;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mPasswordConfirmField;

    private CardDAO mCardDAO;

    private String mUsername;
    private String mPassword;

    private User mUser;

    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        wireUpDisplay();

        getDatabase();
    }

    private void wireUpDisplay() {
        mUsernameField = findViewById(R.id.editTextCreateAccountUserName);
        mPasswordField = findViewById(R.id.editTextCreateAccountPassword);
        mPasswordConfirmField = findViewById(R.id.editTextCreateAccountConfirmPassword);

        mButton = findViewById(R.id.buttonCreateAccountRegister);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make sure no user exists with information
                //make sure password and password confirm match
                //if all pass create account and log user in
                Intent intent = MainActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                startActivity(intent);
            }
        });
    }

    private void getDatabase() {
        mCardDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .CardDAO();

    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, CreateAccountActivity.class);

        return intent;
    }
}