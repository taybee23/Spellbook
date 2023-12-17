package com.example.spellbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spellbook.DB.AppDatabase;
import com.example.spellbook.DB.CardDAO;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeckListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeckListFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    CardDAO mCardDAO;

    Button mAddCard;
    Button mDeleteDeck;
    Button mBack;
    TextView mDeckNameMsg;
    TextView mNumCards;
    TextView mDeckCardList;

    private User mUser;

    private int mUserId;

    private int mDeckId;

    List<Card> mCardList;

    public DeckListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeckListFragment.
     */
    public static DeckListFragment newInstance(String param1, String param2) {
        DeckListFragment fragment = new DeckListFragment();
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

        View v = inflater.inflate(R.layout.fragment_deck_list, container, false);

        mUserId = getArguments().getInt("userId", -1);
        getDatabase();

        mUser = mCardDAO.getUserByUserId(mUserId);
        mDeckId = getArguments().getInt("deckId", -1);
        mDeckId = mCardDAO.getDeckIdByUserId(mUserId);

        String deckName = mCardDAO.getDeckNameByUserId(mUserId);

        mDeckNameMsg = v.findViewById(R.id.deckListFragment_textViewDeckNameMessage);
        mAddCard = v.findViewById(R.id.deckListFragment_buttonAddCard);
        mDeleteDeck = v.findViewById(R.id.deckListFragment_buttonDeleteDeck);
        mDeckCardList = v.findViewById(R.id.deckListFragment_textViewDeckCardListDisplay);
        mNumCards = v.findViewById(R.id.deckListFragment_textViewNumCards);


        mDeckNameMsg.setText(deckName + " deck");

        mDeckCardList.setMovementMethod(new ScrollingMovementMethod());

        mAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", mUserId);

                AddCardToDeckFragment addCardToDeckFragment = new AddCardToDeckFragment();
                addCardToDeckFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

                transaction.replace(R.id.frame_layout,addCardToDeckFragment)
                        .addToBackStack("name")
                        .commit();
            }
        });

        mDeleteDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardDAO.deleteDecksByUserId(mUserId);

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

        refreshDisplay();

        return v;
    }

    private void getDatabase() {
        mCardDAO = Room.databaseBuilder(requireContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .CardDAO();
    }

    private void refreshDisplay(){
        mCardList = mCardDAO.getCardsByDeckId(mDeckId);
        int count = 0;
        if(!mCardList.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for(Card card : mCardList){
                sb.append(card.toString());
                count++;
            }
            mNumCards.setText(count + " cards");
            mDeckCardList.setText(sb.toString());
        }else{
            mNumCards.setText(count + " cards");
            mDeckCardList.setText("Click 'Add Card' to get started!");
        }
    }
}