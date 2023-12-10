package com.example.spellbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.text.method.ScrollingMovementMethod;
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
 * Use the {@link DecksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DecksFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CardDAO mCardDAO;

    Button mAddADeck;

    TextView mDeckLogDisplay;
    TextView mDeckUserMsg;

    private DecksFragment.DecksFragmentListener mListener;
    private User mUser;

    private int mUserId;
//    List<Deck> mDeckList;

    public DecksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DecksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DecksFragment newInstance(String param1, String param2) {
        DecksFragment fragment = new DecksFragment();
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

    public interface DecksFragmentListener{
        void onInputASent(CharSequence input);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_decks, container, false);

        mUserId = getArguments().getInt("userId", -1);
        getDatabase();

        mUser = mCardDAO.getUserByUserId(mUserId);

        mAddADeck = v.findViewById(R.id.deckFragment_buttonAddDeck);
        mDeckLogDisplay = v.findViewById(R.id.deckFragment_textViewDeckLogDisplay);
        mDeckUserMsg = v.findViewById(R.id.deckFragment_textViewUserDecksMessage);

        mDeckUserMsg.setText(mUser.getUserName() + "'s Decks");
        mDeckLogDisplay.setMovementMethod(new ScrollingMovementMethod());

        mAddADeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Add a deck", Toast.LENGTH_SHORT).show();
            }
        });

//        refreshDisplay();

        return v;
    }

    private void getDatabase() {
        mCardDAO = Room.databaseBuilder(requireContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .CardDAO();
    }

//    private void refreshDisplay(){
//        mDeckList = mCardDAO.getDeckByUserId(mUserId);
//        if(!mDeckList.isEmpty()){
//            StringBuilder sb = new StringBuilder();
//            for(Deck deck : mDeckList){
//                sb.append(deck.toString());
//            }
//            mDeckLogDisplay.setText(sb.toString());
//        }else{
//            mDeckLogDisplay.setText(R.string.no_decks_yet_message);
//        }
//    }
}