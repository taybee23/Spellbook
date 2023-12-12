package com.example.spellbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spellbook.DB.AppDatabase;
import com.example.spellbook.DB.CardDAO;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CardDAO mCardDAO;

    private User mUser;

    private int mUserId;

    EditText mCardName;
    EditText mCardType;
    EditText mCardManaCost;
    EditText mCardRarity;
    EditText mCardText;

    Button mAddCard;

    public AddCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCardFragment newInstance(String param1, String param2) {
        AddCardFragment fragment = new AddCardFragment();
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
        View v = inflater.inflate(R.layout.fragment_add_card, container, false);

        mUserId = getArguments().getInt("userId", -1);
        getDatabase();

        mUser = mCardDAO.getUserByUserId(mUserId);

        mCardName = v.findViewById(R.id.addCardFragment_editTextCardName);
        mCardType = v.findViewById(R.id.addCardFragment_editTextCardType);
        mCardManaCost = v.findViewById(R.id.addCardFragment_editTextCardManaCost);
        mCardRarity = v.findViewById(R.id.addCardFragment_editTextCardRarity);
        mCardText = v.findViewById(R.id.addCardFragment_editTextCardText);
        mAddCard = v.findViewById(R.id.addCardFragment_buttonAddCard);

        mAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCard();

                Toast.makeText(getActivity(), "Card " + mCardName.getText().toString() + " added", Toast.LENGTH_SHORT).show();

                mCardName.setText("");
                mCardType.setText("");
                mCardManaCost.setText("");
                mCardRarity.setText("");
                mCardText.setText("");
            }
        });

        return v;
    }

    private void getDatabase() {
        mCardDAO = Room.databaseBuilder(requireContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
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
}