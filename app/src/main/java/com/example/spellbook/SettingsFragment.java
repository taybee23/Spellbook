package com.example.spellbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spellbook.DB.AppDatabase;
import com.example.spellbook.DB.CardDAO;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CardDAO mCardDAO;

    Button mDeleteCollection;
    Button mDeleteAccount;

    TextView mTotalCardDisplay;

    private SettingsFragment.SettingsFragmentListener mListener;
    private User mUser;

    private int mUserId;

    List<Card> mCardList;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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

    public interface SettingsFragmentListener{
        void onInputASent(CharSequence input);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        mUserId = getArguments().getInt("userId", -1);
        getDatabase();

        mUser = mCardDAO.getUserByUserId(mUserId);

        mDeleteCollection = v.findViewById(R.id.settingsFragment_buttonDeleteCollection);
        mDeleteAccount = v.findViewById(R.id.settingsFragment_buttonDeleteAccount);
        mTotalCardDisplay = v.findViewById(R.id.settingsFragment_textViewTotalCardDisplay);

        mDeleteCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Delete collection?", Toast.LENGTH_SHORT).show();
            }
        });

        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Delete Account?", Toast.LENGTH_SHORT).show();
            }
        });

        refreshDisplay();

        return v;
    }

    private void refreshDisplay() {
        mCardList = mCardDAO.getCardByUserId(mUserId);
        int count = 0;
        if(!mCardList.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for(Card card : mCardList){
                sb.append(card.toString());
                count++;
            }

            sb.append("Total Number of Cards: ").append(count);
            mTotalCardDisplay.setText("Total Number of Cards: ");
        }else{
            mTotalCardDisplay.setText("Total Number of Cards: 0");
        }
    }

    private void getDatabase() {
        mCardDAO = Room.databaseBuilder(requireContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .CardDAO();
    }
}