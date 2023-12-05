package com.example.spellbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.spellbook.DB.AppDatabase;
import com.example.spellbook.DB.CardDAO;
import com.example.spellbook.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.spellbook.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.spellbook.PREFERENCES_KEY";


    ActivityMainBinding binding;

    TextView mMainDisplay;

    EditText mCardName;
    EditText mCardType;
    EditText mCardManaCost;
    EditText mCardRarity;
    EditText mCardText;

    Button mAddACard;

    Button mLogout;

    CardDAO mCardDAO;

    List<Card> mCardList;

    private int mUserId = -1;

    private SharedPreferences mPreferences;

    private User mUser;

    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        getDatabase();

        checkForUser();
        addUserToPreference(mUserId);
        loginUser(mUserId);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mMainDisplay = binding.mainCardLogDisplay;
        mCardName = binding.mainCardNameEditText;
        mCardType = binding.mainCardTypeEditText;
        mCardManaCost = binding.mainCardManaCostEditText;
        mCardRarity = binding.mainCardRarityEditText;
        mCardText = binding.mainCardTextEditText;
        mAddACard = binding.mainAddCardButton;
        mLogout = binding.mainLogoutButton;

        mMainDisplay.setMovementMethod(new ScrollingMovementMethod());

        refreshDisplay();

        mAddACard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCard();
                refreshDisplay();
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
                refreshDisplay();
            }
        });
    }

    private void loginUser(int userId) {
        mUser = mCardDAO.getUserByUserId(userId);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mUser != null){
            MenuItem item = menu.findItem(R.id.userMenuLogout);
            item.setTitle(mUser.getUserName());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void checkForUser() {
        //do we have a user in the intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        //do we have a user in the preferences?
        if(mUserId != -1){
            return;
        }

        if(mPreferences == null){
            getPrefs();
        }

        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1){
            return;
        }

        //do we have any users at all?
        List<User> users = mCardDAO.getAllUsers();
        if(users.size() <= 0){
            User defaultUser = new User("testuser1", "testuser1");
            User altUser = new User("admin2", "admin2");
            mCardDAO.insert(defaultUser,altUser);
        }

        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearUserFromIntent();
                clearUserFromPref();
                mUserId = -1;
                checkForUser();
            }
        });
        alertBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //nothing needs to be done
            }
        });

        alertBuilder.create().show();
    }


    private void getDatabase() {
        mCardDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .CardDAO();
    }

    private void submitCard(){
        String cardName = mCardName.getText().toString();
        String cardType = mCardType.getText().toString();
        String cardManaCost = mCardManaCost.getText().toString();
        String cardRarity = mCardRarity.getText().toString();
        String cardText = mCardText.getText().toString();

        Card card = new Card(cardName,cardType,cardManaCost,cardRarity,cardText, mUserId);

        card.setUserId(mUser.getUserId());

        mCardDAO.insert(card);
    }

    private void refreshDisplay(){
        mCardList = mCardDAO.getCardByUserId(mUserId);
        if(!mCardList.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for(Card card : mCardList){
                sb.append(card.toString());
            }
            mMainDisplay.setText(sb.toString());
        }else{
            mMainDisplay.setText(R.string.no_cards_message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.userMenuLogout){
            logoutUser();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE);
    }

    private void clearUserFromIntent(){
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    private void clearUserFromPref(){
        addUserToPreference(-1);
    }

    private void addUserToPreference(int i) {
        if(mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, i);
    }
}