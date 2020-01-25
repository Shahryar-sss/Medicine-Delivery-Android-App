package com.spark.medicinedelivery;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.Circle;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutUsActivity extends AppCompatActivity {

    private Button prev_btn;
    private CircleImageView shahryar_btn, anurita_btn, sohini_btn, sayangdipto_btn;
    String parentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        parentName = getIntent().getExtras().getString("parentName");

        prev_btn = (Button)findViewById(R.id.aboutus_page_prev_page_btn);
        shahryar_btn = (CircleImageView)findViewById(R.id.about_shahryar);
        anurita_btn = (CircleImageView) findViewById(R.id.about_anurita);
        sohini_btn = (CircleImageView)findViewById(R.id.about_sohini);
        sayangdipto_btn = (CircleImageView)findViewById(R.id.about_sayangdipto);

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent;

                if (parentName.equalsIgnoreCase("BuyerProfile"))
                    profileIntent = new Intent(AboutUsActivity.this, ProfileActivity.class);
                else
                    profileIntent = new Intent(AboutUsActivity.this, SellerProfileActivity.class);

                profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(profileIntent);
                finish();
            }
        });

        anurita_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutAnuritaIntent = new Intent(AboutUsActivity.this, AboutAnuritaActivity.class);
                aboutAnuritaIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                aboutAnuritaIntent.putExtra("parentName", parentName);
                startActivity(aboutAnuritaIntent);
                finish();
            }
        });

        sohini_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutSohiniIntent = new Intent(AboutUsActivity.this, AboutSohiniActivity.class);
                aboutSohiniIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                aboutSohiniIntent.putExtra("parentName", parentName);
                startActivity(aboutSohiniIntent);
                finish();
            }
        });

        sayangdipto_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutSohiniIntent = new Intent(AboutUsActivity.this, AboutSayangdiptoActivity.class);
                aboutSohiniIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                aboutSohiniIntent.putExtra("parentName", parentName);
                startActivity(aboutSohiniIntent);
                finish();
            }
        });

        shahryar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutShahryarIntent = new Intent(AboutUsActivity.this, AboutShahryarActivity.class);
                aboutShahryarIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                aboutShahryarIntent.putExtra("parentName", parentName);
                startActivity(aboutShahryarIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent;
        if (parentName.equalsIgnoreCase("BuyerProfile"))
            parentIntent = new Intent(AboutUsActivity.this, ProfileActivity.class);
        else
            parentIntent = new Intent(AboutUsActivity.this, SellerProfileActivity.class);
        //Intent parentIntent = NavUtils.getParentActivityIntent(AboutUsActivity.this);
        startActivity(parentIntent);
        finish();
    }
}
