package com.example.tandoorpalace;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

public class AdminActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 1;
    private Button ibuttonChooseDish, ibuttonUploadDish, ibuttonDisplayDish;
    private EditText iFileTitle;
    private ProgressBar iProgressBar;
    private ImageView iUploadView;

    private Uri iImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ibuttonChooseDish = findViewById(R.id.Choose_Dish);
        ibuttonUploadDish = findViewById(R.id.Upload_Dish);
        ibuttonDisplayDish = findViewById(R.id.Display_Dish);
        iFileTitle = findViewById(R.id.File_Title);
        iProgressBar = findViewById(R.id.Upload_Progress_Bar);
        iUploadView = findViewById(R.id.Upload_View);

        ibuttonChooseDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        ibuttonUploadDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ibuttonDisplayDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void openFileChooser(){
        Intent intent =  new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            iImageUri = data.getData();
            Picasso.with(this).load(iImageUri).into(iUploadView);
        }

    }
}
