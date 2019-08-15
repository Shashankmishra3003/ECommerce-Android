package com.shashank.ecommerce.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.ecommerce.R;
import com.shashank.ecommerce.model.Users;


public class CustomDialog extends DialogFragment {

    private static final String TAG = "CustomDialog";
    public interface OnInputListner
    {
        void sendInput(Users users);
    }

    public OnInputListner onInputListner;

    private EditText firstName, lastName, address, addrCity, addrState, addrZipcode, addrcountry, mobileNo;
    private Button btnSave, btnCancel;
    DatabaseReference databaseReference;
    Users users;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.customer_info_dialog,container,false);

        firstName = view.findViewById(R.id.od_first_name);
        lastName = view.findViewById(R.id.od_last_name);
        address = view.findViewById(R.id.user_address);
        addrCity = view.findViewById(R.id.addr_city);
        addrState = view.findViewById(R.id.addr_state);
        addrZipcode = view.findViewById(R.id.addr_zipcode);
        addrcountry = view.findViewById(R.id.addr_country);
        mobileNo = view.findViewById(R.id.user_phone);
        btnSave = view.findViewById(R.id.btn_save);
        btnCancel = view.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        users = new Users();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName,lName,userAddress,city,state,zipcode,country, mobile;

                fName = firstName.getText().toString();
                lName = lastName.getText().toString();
                userAddress = address.getText().toString();
                city = addrCity.getText().toString();
                state = addrState.getText().toString();
                country = addrcountry.getText().toString();
                mobile = mobileNo.getText().toString();
                zipcode = addrZipcode.getText().toString();


                users.setFirstName(fName);
                users.setLastName(lName);
                users.setAddress(userAddress);
                users.setCity(city);
                users.setState(state);
                users.setCountry(country);
                users.setMobile(mobile);
                users.setZipCode(zipcode);

                //Sending data to activity
                onInputListner.sendInput(users);

                //Sending data to firebase
                databaseReference.child(acct.getId()).setValue(users);

                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onInputListner = (OnInputListner) getActivity();

        }catch (ClassCastException e)
        {
            Log.e(TAG,"onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}
