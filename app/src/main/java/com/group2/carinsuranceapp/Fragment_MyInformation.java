package com.group2.carinsuranceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group2.databaseclasses.UserCar;
import com.group2.databaseclasses.UserData;

public class Fragment_MyInformation extends Fragment {

    private TextView name;
    private TextView surname;
    private TextView birthdate;
    private TextView sex;
    private Button editButton;

    // Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;

    private String userID;
    FirebaseUser firebaseUser;
    private static final String TAG = "DEBUGGING - ";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_information,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialise database variables
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();
        //userID = mAuth.getCurrentUser().getUid();
        myRef = mFirebaseDatabase.getReference("User");


        //Views
        name = view.findViewById(R.id.text_first_name_myinfo_changable);
        surname = view.findViewById(R.id.text_last_name_myinfo_changeable);
        birthdate = view.findViewById(R.id.text_birthdate_myinfo_changeable);
        sex = view.findViewById(R.id.text_sex_myinfo_changeable);
        editButton = view.findViewById(R.id.edit_button);

        //edit information
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), EditPersonalInformationActivity.class));
            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserData udata = dataSnapshot.child(userID).getValue(UserData.class);

                name.setText(udata.getFirstName());
                surname.setText(udata.getLastName());
                birthdate.setText(udata.getDob());
                sex.setText(udata.getGender());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                /*
                on child added appends the next child added (User)

                UserData udata = dataSnapshot.child(userID).getValue(UserData.class);

                name.setText(udata.getFirstName());
                surname.setText(udata.getLastName());
                birthdate.setText(udata.getDob());
                sex.setText(udata.getGender());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
    }
}
