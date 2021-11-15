package com.example.androidnotes;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    TextView androidnotes,copyright,version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        androidnotes=findViewById(R.id.androidnotes);
        copyright=findViewById(R.id.copyright);
        version=findViewById(R.id.version);

    }

}
