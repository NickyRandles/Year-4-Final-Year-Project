package com.project.nicky.medicalmanager.Search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.project.nicky.medicalmanager.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void nameButtonClicked(View view) {
        Intent intent = new Intent(this, NameSearchActivity.class);
        startActivity(intent);
    }

    public void locationButtonClicked(View view) {
        Intent intent = new Intent(this, LocationSearchActivity.class);
        startActivity(intent);
    }
}
