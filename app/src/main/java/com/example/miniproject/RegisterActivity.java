package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends Fragment {

    EditText user,pass, confpass, name, phone;
    Button register,reset;
    private FirebaseAuth firebaseAuth;
    static  String firename;
    private DatabaseReference mdb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_page, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = getView().findViewById(R.id.username);
        pass = getView().findViewById(R.id.password);
        confpass = getView().findViewById(R.id.confirm_password);
        name = getView().findViewById(R.id.name);
        phone = getView().findViewById(R.id.phone);
        register = getView().findViewById(R.id.register);
        reset=getView().findViewById(R.id.reset);
        firebaseAuth = FirebaseAuth.getInstance();
        mdb = FirebaseDatabase.getInstance().getReference().child("Users");
//        private DatabaseReference mdb;

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final String u = user.getText().toString().trim();
                final String p = pass.getText().toString().trim();
                final String n = name.getText().toString().trim();
                String cp = confpass.getText().toString().trim();
                final String ph = phone.getText().toString();

                if (name.getText().toString().isEmpty())
                    Toasty.info(getActivity(), "Please enter your name", Toast.LENGTH_SHORT).show();
                else if (phone.getText().toString().isEmpty())
                    Toasty.info(getActivity(), "Please enter your contact number", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(u))
                    Toasty.info(getActivity(), "Please enter email", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(p))
                    Toasty.info(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
                else if (p.length() < 6)
                    Toasty.info(getActivity(), "Password too short", Toast.LENGTH_SHORT).show();
                else if (!p.equals(cp))
                    Toasty.error(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                else
                {
                    firebaseAuth.createUserWithEmailAndPassword(u, p)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        firebaseAuth.signInWithEmailAndPassword(u, p);
                                        user u1 = new user(u, p, n, ph, 0, 0, 0, 0, false, false, false, false, false, false, false, false, false);
                                        FirebaseUser u2 = FirebaseAuth.getInstance().getCurrentUser();
                                        if (u2 != null) {
                                            String ui = "";
                                            for (int i = 0; i < u.length(); i++) {
                                                if (u.charAt(i) == '.' || u.charAt(i) == '#') {

                                                } else {
                                                    ui += u.charAt(i);
                                                }
                                            }
                                            firename = ui;
                                            mdb.child(ui).setValue(u1);
                                        }
                                        Toasty.success(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(view.getContext(), Preferences.class);
                                        getActivity().finish();
                                        startActivity(i);

                                    } else {

                                        Toasty.error(getActivity(), "Registration Failed", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setText("");
                pass.setText("");
                confpass.setText("");
                name.setText("");
                phone.setText("");
            }
        });
    }
}


