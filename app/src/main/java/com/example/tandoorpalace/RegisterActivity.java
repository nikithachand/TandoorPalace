package com.example.tandoorpalace;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{

    private Button CreateAccountButton;
    private EditText UserName, PhoneNumber, Password;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button) findViewById(R.id.register_input);
        UserName = (EditText) findViewById(R.id.username_input);
        PhoneNumber = (EditText) findViewById(R.id.phone_input);
        Password = (EditText) findViewById(R.id.password_input);
        loadingBar = new ProgressDialog(this);
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                CreateAccount();
                
            }
        });
    }

    private void CreateAccount() {
        String username = UserName.getText().toString();
        String phone = PhoneNumber.getText().toString();
        String password = Password.getText().toString();


        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Input User Name", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Input Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Input Password", Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Verifying Credentials ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatephoneNumber(username, phone, password);

        }
    }

    private void ValidatephoneNumber(final String username, final String phone, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                    if (!(dataSnapshot.child("Users").child(phone).exists()))
                    {
                        HashMap<String, Object> userDataMap = new HashMap<>();
                        userDataMap.put("Phone", phone);
                        userDataMap.put("Password", password);
                        userDataMap.put("UserName", username);

                        RootRef.child("Users").child(phone).updateChildren(userDataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(RegisterActivity.this, "Account has been Created.",Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Network Error, Please try again later.",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "This" + phone + "already exists.",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Please use is a Valid Phone Number",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
