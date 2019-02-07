package com.example.tandoorpalace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tandoorpalace.Model.Users;
import com.example.tandoorpalace.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity
{

    private Button Registerbutton;
    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink;
    private String ParentDBaseName = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Registerbutton = (Button) findViewById(R.id.signup_input);


        Registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        LoginButton = (Button) findViewById(R.id.login_button);
        InputPhoneNumber = (EditText) findViewById(R.id.loginphone_number);
        InputPassword = (EditText) findViewById(R.id.loginpassword_input);
        AdminLink = (TextView)findViewById(R.id.Admin_input);
        NotAdminLink = (TextView)findViewById(R.id.Not_Admin_input);
        loadingBar = new ProgressDialog(this);
        Paper.init(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        }

        );
                AdminLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        LoginButton.setText("Admin Login");
                        AdminLink.setVisibility(View.INVISIBLE);
                        NotAdminLink.setVisibility(View.VISIBLE);
                        ParentDBaseName = "Admins";
                    }
                });

                NotAdminLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        LoginButton.setText("Login");
                        InputPhoneNumber.setHint("Phone Number");
                        AdminLink.setVisibility(View.VISIBLE);
                        NotAdminLink.setVisibility(View.INVISIBLE);
                        ParentDBaseName = "Users";
                    }
                });
                AdminLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                public void onClick(View v)
            {
                InputPhoneNumber.setHint("Restaurant Number");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                ParentDBaseName = "Admins";
            }

            });
    }

    private void LoginUser()
    {
        String Phone = InputPhoneNumber.getText().toString();
        String Password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(Phone)){
        Toast.makeText(this, "Input Phone Number", Toast.LENGTH_SHORT).show();
    }
    else if (TextUtils.isEmpty(Password)){
        Toast.makeText(this, "Input Password", Toast.LENGTH_SHORT).show();
    }
        else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Verifying Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(Phone, Password);
        }
        
    }

    private void AllowAccessToAccount(final String Phone, final String Password)
    {
        if (LoginButton.isClickable())
        {
            Paper.book().write(Prevalent.UserPhoneKey,Phone);
            Paper.book().write(Prevalent.UserPasswordKey,Password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(ParentDBaseName).child(Phone).exists())
                {
                    Users userData = dataSnapshot.child(ParentDBaseName).child(Phone).getValue(Users.class);

                            if (userData.getPhone().equals(Phone))
                            {
                                if (userData.getPassword().equals(Password))
                                {
                                    if(ParentDBaseName.equals("Admins"))
                                    {
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                        startActivity(intent);
                                        InputPassword.setText("");
                                    }

                                    else if(ParentDBaseName.equals("Users"))
                                    {
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        InputPassword.setText("");
                                    }
                                }
                            }
                }
                else
                {   loadingBar.dismiss();

                        InputPhoneNumber.setText("");
                        InputPassword.setText("");
                        Toast.makeText(LoginActivity.this, "Incorrect Password/PhoneNumber", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}