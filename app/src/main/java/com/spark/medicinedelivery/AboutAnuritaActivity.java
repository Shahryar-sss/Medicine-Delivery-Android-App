package com.spark.medicinedelivery;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class AboutAnuritaActivity extends AppCompatActivity {

    String parentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_anurita);
        parentName = getIntent().getExtras().getString("parentName");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(AboutAnuritaActivity.this);
        parentIntent.putExtra("parentName", parentName);
        startActivity(parentIntent);
        finish();
    }
}
