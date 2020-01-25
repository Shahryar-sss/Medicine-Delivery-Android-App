package com.spark.medicinedelivery;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutShahryarActivity extends AppCompatActivity {

    String parentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_shahryar);

        parentName = getIntent().getExtras().getString("parentName");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(AboutShahryarActivity.this);
        parentIntent.putExtra("parentName", parentName);
        startActivity(parentIntent);
        finish();
    }
}
