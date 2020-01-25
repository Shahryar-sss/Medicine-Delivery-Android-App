package com.spark.medicinedelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SellerProfileActivity extends AppCompatActivity {

    private TextView email_tv, shopname_tv;
    private EditText phone_et, address_et, shopkeeper_et;
    Button prev_btn, logout_btn, update_btn, camera_btn;
    CircleImageView profie_picture;

    DatabaseReference dbref;
    FirebaseAuth mAuth;

    private final static int GALLERY_PICK = 1;

    private FirebaseUser mCurrentUser;
    private StorageReference mImageStorage;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);

        email_tv = (TextView)findViewById(R.id.sellerprofile_screen_email);
        shopname_tv = (TextView)findViewById(R.id.sellerprofile_screen_shopname);
        phone_et = (EditText)findViewById(R.id.sellerprofile_screen_phone_number);
        address_et = (EditText)findViewById(R.id.sellerprofile_screen_user_address);
        logout_btn = (Button)findViewById(R.id.sellerprofile_screen_logout_btn);
        update_btn = (Button)findViewById(R.id.sellerprofile_screen_update_btn);
        camera_btn = (Button)findViewById(R.id.sellerprofile_screen_image_upload_btn);
        profie_picture = (CircleImageView)findViewById(R.id.sellerprofile_screen_imageview);
        prev_btn = (Button)findViewById(R.id.sellerprofile_screen_prev_btn);
        shopkeeper_et = (EditText)findViewById(R.id.sellerprofile_screen_storeowner);

        mProgressDialog = new ProgressDialog(this);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();

        dbref = FirebaseDatabase.getInstance().getReference().child("Seller").child(UID);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shopname_tv.setText(dataSnapshot.child("Shop_Name").getValue().toString());
                email_tv.setText(dataSnapshot.child("email").getValue().toString());
                phone_et.setText(dataSnapshot.child("Shop_Phone").getValue().toString());
                address_et.setText(dataSnapshot.child("Shop_Address").getValue().toString());
                shopkeeper_et.setText(dataSnapshot.child("username").getValue().toString());

                String image = dataSnapshot.child("image").getValue().toString();
                if (!image.equalsIgnoreCase("default"))
                    Picasso.get().load(image).placeholder(R.drawable.shop_avatar).into(profie_picture);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sellerMainIntent = new Intent(SellerProfileActivity.this, SellerMainActivity.class);
                sellerMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(sellerMainIntent);
                finish();
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map new_info = new HashMap();

                String new_phone = phone_et.getText().toString();
                String new_address = address_et.getText().toString();
                String new_shopkeeper = shopkeeper_et.getText().toString();

                new_info.put("Shop_Address", new_address);
                new_info.put("Shop_Phone", new_phone);
                new_info.put("username", new_shopkeeper);

                dbref.updateChildren(new_info).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(SellerProfileActivity.this, "Updated Succesfully", Toast.LENGTH_LONG).show();
                            Intent sellerMainIntent = new Intent(SellerProfileActivity.this, SellerMainActivity.class);
                            sellerMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(sellerMainIntent);
                            finish();
                        }
                    }
                });
            }
        });
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent loginIntent = new Intent(SellerProfileActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                finish();
            }
        });

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(SellerProfileActivity.this);
        startActivity(parentIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)
            {

                mProgressDialog = new ProgressDialog(SellerProfileActivity.this);
                mProgressDialog.setTitle("Uploading Image...");
                mProgressDialog.setMessage("Please wait while we upload and process the image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();

                final File thumb_filePath = new File(resultUri.getPath());
                String current_user_id = mCurrentUser.getUid();

                Bitmap thumb_bitmap = null;

                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(100)
                            .compressToBitmap(thumb_filePath);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");
                final StorageReference thumb_filepath = mImageStorage.child("profile_images").child("thumbs").child(current_user_id +".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            final String download_url = task.getResult().getDownloadUrl().toString();
                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    String thumb_downloadURL = thumb_task.getResult().getDownloadUrl().toString();
                                    if(thumb_task.isSuccessful())
                                    {
                                        Map update_hashMap = new HashMap<>();
                                        update_hashMap.put("image", download_url);
                                        update_hashMap.put("thumb_image", thumb_downloadURL);

                                        dbref.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    mProgressDialog.dismiss();
                                                }
                                            }
                                        });

                                    }
                                    else
                                    {
                                        Toast.makeText(SellerProfileActivity.this, "Error while uploading thumbnail", Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();
                                    }
                                }
                            });


                        }
                        else
                        {
                            Toast.makeText(SellerProfileActivity.this, "Error encountered while updating profile picture", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }


                    }
                });
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }

    public void showsellerPopup(View v)
    {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.profile_menu, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.about_us:
                        Intent aboutIntent = new Intent(SellerProfileActivity.this, AboutUsActivity.class);
                        aboutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        aboutIntent.putExtra("parentName", "SellerProfile");
                        startActivity(aboutIntent);
                        finish();
                        return true;

                    default:return false;
                }
            }
        });
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
