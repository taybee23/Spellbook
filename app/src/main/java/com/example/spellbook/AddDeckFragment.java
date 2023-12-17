package com.example.spellbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spellbook.DB.AppDatabase;
import com.example.spellbook.DB.CardDAO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddDeckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDeckFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    EditText mDeckName;
    Button mCreateDeck;

    CardDAO mCardDAO;

    private User mUser;

    private int mUserId;

    public AddDeckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddDeckFragment.
     */
    public static AddDeckFragment newInstance(String param1, String param2) {
        AddDeckFragment fragment = new AddDeckFragment();
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

        View v = inflater.inflate(R.layout.fragment_add_deck, container, false);
        mUserId = getArguments().getInt("userId", -1);
        getDatabase();

        mUser = mCardDAO.getUserByUserId(mUserId);

        mCreateDeck = v.findViewById(R.id.addDeckFragment_buttonCreateDeck);
        mDeckName = v.findViewById(R.id.addDeckFragment_editTextDeckName);

        mCreateDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDeck();

                Bundle bundle = new Bundle();
                bundle.putInt("userId", mUserId);

                DecksFragment decksFragment = new DecksFragment();
                decksFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

                transaction.replace(R.id.frame_layout,decksFragment)
                        .addToBackStack("name")
                        .commit();
            }
        });

        return v;
    }

    private void createDeck() {
        String deckName = mDeckName.getText().toString();

        if(!deckName.isEmpty()){
            Deck deck = new Deck(deckName,mUserId);

            mCardDAO.insert(deck);
        }else{
            Toast.makeText(getActivity(), "You must enter a deck name", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDatabase() {
        mCardDAO = Room.databaseBuilder(requireContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .CardDAO();
    }
}