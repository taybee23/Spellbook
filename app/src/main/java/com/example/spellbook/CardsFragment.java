package com.example.spellbook;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.spellbook.databinding.ActivityMainBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CardDAO mCardDAO;

    Button mAddACard;
    Button mAdmin;
    Button mCardSearch;
    Button mViewCollectionStats;

    TextView mCardLogDisplay;
    TextView mCardUserMsg;

    private CardsFragmentListener mListener;
    private User mUser;

    private int mUserId;
    List<Card> mCardList;

    public CardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardsFragment newInstance(String param1, String param2) {
        CardsFragment fragment = new CardsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { //called before onCreateView, non-graphical initialisations (https://stackoverflow.com/questions/28929637/difference-and-uses-of-oncreate-oncreateview-and-onactivitycreated-in-fra)
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    public interface CardsFragmentListener{
        void onInputASent(CharSequence input);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { //assign View variables, graphical initialisations

        View v = inflater.inflate(R.layout.fragment_cards, container, false);

        mUserId = getArguments().getInt("userId", -1);
        getDatabase();

        mUser = mCardDAO.getUserByUserId(mUserId);

        mAdmin = v.findViewById(R.id.cardFragment_buttonAdmin);
        mAddACard = v.findViewById(R.id.cardFragment_buttonAddCard);
        mCardSearch = v.findViewById(R.id.cardFragment_buttonSearch);
        mViewCollectionStats = v.findViewById(R.id.cardFragment_buttonCollectionStats);
        mCardLogDisplay = v.findViewById(R.id.cardFragment_textViewCardLogDisplay);
        mCardUserMsg = v.findViewById(R.id.cardFragment_textViewUserCardsMessage);

        mCardUserMsg.setText(mUser.getUserName() + "'s Cards");
        mCardLogDisplay.setMovementMethod(new ScrollingMovementMethod());

        if(mUser.isAdmin()){
            mAdmin.setVisibility(View.VISIBLE);
        }else{
            mAdmin.setVisibility(View.GONE);
        }

        mAddACard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", mUserId);

                AddCardFragment addCardFragment = new AddCardFragment();
                addCardFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

                transaction.replace(R.id.frame_layout,addCardFragment)
                        .addToBackStack("name")
                        .commit();
            }
        });

        mAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", mUserId);

                AdminFragment adminFragment = new AdminFragment();
                adminFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

                transaction.replace(R.id.frame_layout,adminFragment)
                        .addToBackStack("name")
                        .commit();
            }
        });

        mCardSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", mUserId);

                CardSearchFragment searchFragment = new CardSearchFragment();
                searchFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

                transaction.replace(R.id.frame_layout,searchFragment)
                        .addToBackStack("name")
                        .commit();
            }
        });

        mViewCollectionStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "View stats", Toast.LENGTH_SHORT).show();
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
        mCardList = mCardDAO.getCardByUserId(mUserId);
        if(!mCardList.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for(Card card : mCardList){
                sb.append(card.toString());
            }
            mCardLogDisplay.setText(sb.toString());
        }else{
            mCardLogDisplay.setText(R.string.no_cards_message);
        }
    }
//below this makes app crash
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if(context instanceof  CardsFragmentListener){
//            mListener = (CardsFragmentListener) context;
//        }else {
//            throw new RuntimeException(context.toString()
//            + " must implement CardsFragmentListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
}