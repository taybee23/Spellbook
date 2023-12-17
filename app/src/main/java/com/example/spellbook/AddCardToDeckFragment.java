package com.example.spellbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.spellbook.DB.AppDatabase;
import com.example.spellbook.DB.CardDAO;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCardToDeckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCardToDeckFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    CardDAO mCardDAO;

    private User mUser;

    private int mUserId;

    private int mDeckId;

    Button mAddCard;

    ImageButton mBack;
    public AddCardToDeckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCardToDeckFragment.
     */
    public static AddCardToDeckFragment newInstance(String param1, String param2) {
        AddCardToDeckFragment fragment = new AddCardToDeckFragment();
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

        View v = inflater.inflate(R.layout.fragment_add_card_to_deck, container, false);

        mUserId = getArguments().getInt("userId", -1);
        getDatabase();

        mDeckId = getArguments().getInt("deckId", -1);
        mDeckId = mCardDAO.getDeckIdByUserId(mUserId);
        mUser = mCardDAO.getUserByUserId(mUserId);

        List<Card> mAddedCards = mCardDAO.getCardByUserId(mUserId);

        mAddCard = v.findViewById(R.id.addCardToDeck_buttonAddCard);
        mBack = v.findViewById(R.id.addCardToDeck_buttonBack);

        Spinner spinner = v.findViewById(R.id.addCardToDeck_spinner);
        ArrayAdapter<Card> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mAddedCards);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card selectedCard = (Card) spinner.getSelectedItem();

                addCardToDeck(selectedCard);
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", mUserId);

                DeckListFragment deckListFragment = new DeckListFragment();
                deckListFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

                transaction.replace(R.id.frame_layout,deckListFragment)
                        .addToBackStack("name")
                        .commit();
            }
        });

        return v;
    }

    private void addCardToDeck(Card card) {
        int cardId = card.getCardId();

        //check if card exists in deck
        if(isCardinDeck(cardId)){
            Toast.makeText(getActivity(), "This card is already in your deck", Toast.LENGTH_SHORT).show();

        }else{
            DeckCard deckCard = new DeckCard(mDeckId, cardId);

            mCardDAO.insert(deckCard);
            Toast.makeText(getActivity(), "Card added", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCardinDeck(int cardId) {

        return mCardDAO.countByCardId(cardId) > 0;
    }

    private void getDatabase() {
        mCardDAO = Room.databaseBuilder(requireContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .CardDAO();
    }
}