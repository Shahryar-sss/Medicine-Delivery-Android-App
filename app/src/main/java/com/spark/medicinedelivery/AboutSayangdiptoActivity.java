package com.spark.medicinedelivery;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class AboutSayangdiptoActivity extends AppCompatActivity {

    String parentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about_sayangdipto);

        parentName = getIntent().getExtras().getString("parentName");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(AboutSayangdiptoActivity.this);
        parentIntent.putExtra("parentName", parentName);
        startActivity(parentIntent);
        finish();
    }
}
