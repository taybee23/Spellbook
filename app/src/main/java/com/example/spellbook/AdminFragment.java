package com.example.spellbook;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CardDAO mCardDAO;

    Button mDeleteUser;

    TextView mUserLogDisplay;

    TextView mAdminViewMsg;

    EditText mDeleteUsername;

    EditText mDeleteUsernameConfirm;

    private String mUsername;
    private String mUsernameConfirm;

    private User mUser;

    private int mUserId;

    List<User> mUserList;
    public AdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminFragment newInstance(String param1, String param2) {
        AdminFragment fragment = new AdminFragment();
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
        View v = inflater.inflate(R.layout.fragment_admin, container, false);

        mUserId = getArguments().getInt("userId", -1);
        getDatabase();

        mUser = mCardDAO.getUserByUserId(mUserId);

        mDeleteUser = v.findViewById(R.id.adminFragment_buttonDeleteUser);
        mUserLogDisplay = v.findViewById(R.id.adminFragment_textViewUserLogDisplay);
        mAdminViewMsg = v.findViewById(R.id.adminFragment_textViewAdminViewMessage);
        mDeleteUsername = v.findViewById(R.id.adminFragment_editTextDeleteUser);
        mDeleteUsernameConfirm = v.findViewById(R.id.adminFragment_editTextDeleteUserConfirm);

        mAdminViewMsg.setText("Admin view: " + mUser.getUserName());
        mUserLogDisplay.setMovementMethod(new ScrollingMovementMethod());

        mDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();

                String userToDelete = mUsername = mDeleteUsername.getText().toString();

                if(!userToDelete.isEmpty()){
                    confirmDelete(userToDelete);
                }else{
                    Toast.makeText(getActivity(), "Enter a user to delete", Toast.LENGTH_SHORT).show();
                }
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
        mUserList = mCardDAO.getAllUsers();
        if(!mUserList.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for(User user : mUserList){
                sb.append(user.getUserName()).append("\n");
            }
            mUserLogDisplay.setText(sb.toString());
        }else{
            mUserLogDisplay.setText(R.string.no_cards_message);
        }
    }

    private void getValuesFromDisplay(){
        mUsername = mDeleteUsername.getText().toString();
        mUsernameConfirm = mDeleteUsernameConfirm.getText().toString();
    }

    private void confirmDelete(String userToDelete) {
        mCardDAO.deleteUserByUsername(userToDelete);
        refreshDisplay();
    }
}