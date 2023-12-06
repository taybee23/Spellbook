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
import android.widget.Toast;

import com.example.spellbook.DB.AppDatabase;
import com.example.spellbook.DB.CardDAO;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mPasswordConfirmField;

    private CardDAO mCardDAO;

    private String mUsername;
    private String mPassword;
    private String mPasswordConfirm;
    private boolean mIsAdmin;

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
                getValuesFromDisplay();
                if(checkForUserInDatabase()){ //if user exists..
                    if (!validateAccountCreation()){ //if passwords don't match
                        Toast.makeText(CreateAccountActivity.this,"Passwords don't match", Toast.LENGTH_SHORT).show();
                    }else{
                        User newUser = new User(mUsername, mPassword, mIsAdmin);
                        mCardDAO.insert(newUser);
                        Intent intent = MainActivity.intentFactory(getApplicationContext(), newUser.getUserId()); //this is not taking us to mainActivity
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean validateAccountCreation() {
        return mPassword.equals(mPasswordConfirm);
    }

    private boolean checkForUserInDatabase() {
        mUser = mCardDAO.getUserByUsername(mUsername);
        if(mUser != null){
            Toast.makeText(this, "User " + mUsername + " already exists", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void getValuesFromDisplay() {
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
        mPasswordConfirm = mPasswordConfirmField.getText().toString();
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