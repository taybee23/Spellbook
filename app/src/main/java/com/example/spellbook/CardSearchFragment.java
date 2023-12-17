package com.example.spellbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
 * Use the {@link CardSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardSearchFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    CardDAO mCardDAO;

    private User mUser;

    private int mUserId;

    TextView SearchMsg;
    TextView mSearchResultsDisplay;
    EditText mCardName;
    EditText mCardColor;
    EditText mCardRarity;
    EditText mCardType;

    Button mClear;
    Button mSearch;

    List<Card> mCardList;

    public CardSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardSearchFragment.
     */
    public static CardSearchFragment newInstance(String param1, String param2) {
        CardSearchFragment fragment = new CardSearchFragment();
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
        View v = inflater.inflate(R.layout.fragment_card_search, container, false);

        mUserId = getArguments().getInt("userId", -1);
        getDatabase();

        mUser = mCardDAO.getUserByUserId(mUserId);

        mClear = v.findViewById(R.id.cardSearchFragment_buttonClear);
        mSearch = v.findViewById(R.id.cardSearchFragment_buttonSearch);
        mCardName = v.findViewById(R.id.cardSearchFragment_editTextCardName);
        mCardColor = v.findViewById(R.id.cardSearchFragment_editTextCardColor);
        mCardType = v.findViewById(R.id.cardSearchFragment_editTextCardType);
        mCardRarity = v.findViewById(R.id.cardSearchFragment_editTextCardRarity);
        mSearchResultsDisplay = v.findViewById(R.id.cardSearchFragment_textViewSearchResultsDisplay);

        mSearchResultsDisplay.setMovementMethod(new ScrollingMovementMethod());

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardName.setText("");
                mCardColor.setText("");
                mCardType.setText("");
                mCardRarity.setText("");

                mCardList.clear();
                mSearchResultsDisplay.setText("Search Results: ");
            }
        });

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Searching", Toast.LENGTH_SHORT).show();
                searchCollectionCards();
                refreshDisplay();
            }
        });

        return v;
    }

    private void searchCollectionCards() {
        String name = mCardName.getText().toString();
        String color = mCardColor.getText().toString();
        String cardType = mCardType.getText().toString();
        String rarity = mCardRarity.getText().toString();

        mCardList = mCardDAO.searchCollection(name,cardType,color,rarity);
        refreshDisplay();
    }

    private void getDatabase() {
        mCardDAO = Room.databaseBuilder(requireContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .CardDAO();
    }

    private void refreshDisplay(){
        if(!mCardList.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for(Card card : mCardList){
                sb.append(card.toString());
            }
            mSearchResultsDisplay.setText("Search Results: \n\n " + sb.toString());
        }else{
            mSearchResultsDisplay.setText("Search Results: none found...");
        }
    }
}