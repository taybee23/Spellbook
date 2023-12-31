package com.example.spellbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.spellbook.DB.AppDatabase;
import com.example.spellbook.DB.CardDAO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogoutFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SharedPreferences mPreferences;

    private static final String USER_ID_KEY = "com.example.spellbook.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.spellbook.PREFERENCES_KEY";

    private String mParam1;
    private String mParam2;
    private int mUserId;
    private User mUser;

    CardDAO mCardDAO;

    public LogoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogoutFragment.
     */
    public static LogoutFragment newInstance(String param1, String param2) {
        LogoutFragment fragment = new LogoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_logout, container, false);

        mUserId = getArguments().getInt("userId", -1);
        getDatabase();

        mUser = mCardDAO.getUserByUserId(mUserId);

        Button mLogout = v.findViewById(R.id.logoutFragment_buttonLogout);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Goodbye!", Toast.LENGTH_SHORT).show();
                logout();
            }
        });


        return v;
    }

    private void logout() {
        mUserId = -1;
        clearUserFromIntent();
        clearUserFromPref();
        mUserId = -1;


        startActivity(new Intent(requireContext(), LoginActivity.class));
        requireActivity().finish();
    }

    private void clearUserFromIntent(){
        Intent intent = getActivity().getIntent();
        intent.putExtra(USER_ID_KEY, -1);
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
        editor.apply();
    }

    private void getPrefs() {
        mPreferences = getActivity().getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void getDatabase() {
        mCardDAO = Room.databaseBuilder(requireContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .CardDAO();
    }
}

