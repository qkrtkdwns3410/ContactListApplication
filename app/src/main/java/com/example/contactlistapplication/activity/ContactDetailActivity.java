package com.example.contactlistapplication.activity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

import com.example.contactlistapplication.R;
import com.example.contactlistapplication.listener.OnTabItemSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.orhanobut.logger.Logger;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactDetailActivity extends AppCompatActivity implements OnTabItemSelectedListener {
	  
	  private static final int EDIT_REQUEST_CODE = 101;
	  
	  private BottomNavigationView bottomNavigation;
	  private SwipeRefreshLayout swipeRefreshLayout;
	  
	  private String contactName, contactNumber, myID;
	  private TextView contactTV, nameTV;
	  private CircleImageView contactIV;
	  
	  @Override
	  public void onTabSelected(int position) {
			if (position == 0) {
				  bottomNavigation.setSelectedItemId(R.id.star);
			} else if (position == 1) {
				  bottomNavigation.setSelectedItemId(R.id.edit);
			} else if (position == 2) {
				  bottomNavigation.setSelectedItemId(R.id.share);
			}
	  }
	  
	  @SuppressLint("Range")
	  private Bitmap getContactsByID(String contactIdPara) {
			try {
				  
				  Logger.d("getContacts() called");
				  Logger.d("contactIdPara  " + contactIdPara);
				  String contactId = "";
				  Bitmap userImage = null;
				  
				  //??????????????? ???????????? ???????????????.
				  Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
					  ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
				  
				  if (cursor.getCount() > 0) {
						// ???????????? 0 ???????????? cursor??? ?????? ???????????? ~~~~~
						while (cursor.moveToNext()) {
							  int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
							  //???????????? ?????? ????????????!
							  if (hasPhoneNumber > 0) {
									contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
									
									if (Objects.equals(contactId, contactIdPara)) {
										  
										  userImage = BitmapFactory.decodeStream(openPhoto(Long.parseLong(contactId)));
										  
										  Logger.d("userImage  " + userImage);
										  
										  return userImage;
									}
							  }
						}
				  }
				  
				  cursor.close();
				  return userImage;
				  
			} catch (Exception e) {
				  e.printStackTrace();
				  return null;
			}
	  }
	  
	  public InputStream openPhoto(long contactId) {
			Logger.d("openPhoto() called with: contactId = [" + contactId + "]");
			
			Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
			Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
			Logger.d("photoUri  " + photoUri);
			
			Cursor cursor = getContentResolver().query(photoUri,
				new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
			Logger.d("cursor  " + cursor);
			
			if (cursor == null) {
				  Logger.d("???????????? null?????????.");
				  return null;
			}
			
			try {
				  Logger.d("????????? ??????");
				  
				  if (cursor.moveToFirst()) {
						Logger.d("moveToFirst ");
						byte[] data = cursor.getBlob(0);
						Logger.d("data  " + Arrays.toString(data));
						
						if (data != null) {
							  Logger.d("????????????");
							  return new ByteArrayInputStream(data);
						}
						Logger.d("???????????? null?????????.");
						
				  }
			} finally {
				  cursor.close();
			}
			return null;
	  }
	  
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			
			if (requestCode == EDIT_REQUEST_CODE) {
				  
				  if (resultCode != RESULT_OK) {
						Logger.d("?????????  ");
						return;
				  }
				  
				  Logger.d("requestCode  " + requestCode);
				  
				  String name = data.getStringExtra("name");
				  Logger.d("name  " + name);
				  String number = data.getStringExtra("number");
				  Logger.d("number  " + number);
				  
				  String callBackedID = data.getStringExtra("callBackID");
				  Logger.d("??????callBackedID  " + callBackedID);
				  
				  nameTV.setText(name);
				  contactTV.setText(number);
				  contactIV.setImageBitmap(BitmapFactory.decodeStream(openPhoto(Long.parseLong(callBackedID))));
				  
			}
	  }
	  
	  @Override
	  protected void onResume() {
			super.onResume();
			
			setContentView(R.layout.activity_contact_detail);
			
			swipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout);
			
			contactName = getIntent().getStringExtra("name");
			
			contactNumber = getIntent().getStringExtra("contact");
			
			myID = getIntent().getStringExtra("myRealID");
			Bitmap bitbit = getContactsByID(myID);
			
			contactTV = findViewById(R.id.idTVPhone);
			nameTV = findViewById(R.id.idTVContactName);
			contactIV = findViewById(R.id.idIVContact);
			
			nameTV.setText(contactName);
			contactTV.setText(contactNumber);
			
			if (bitbit == null) {
				  contactIV.setImageResource(R.drawable.default_image);
			} else {
				  contactIV.setImageBitmap(bitbit);
				  contactIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
			}
			
			bottomNavigation = findViewById(R.id.bottomNavi);
			bottomNavigation.setOnNavigationItemSelectedListener(item -> {
				  switch (item.getItemId()) {
						case R.id.star:
							  
							  return true;
						case R.id.edit:
							  Intent intent = new Intent(ContactDetailActivity.this, ContactEditActivity.class);
							  intent.putExtra("myID", myID);
							  intent.putExtra("name", contactName);
							  intent.putExtra("number", contactNumber);
							  
							  if (bitbit == null) {
									intent.putExtra("image", R.drawable.default_image);
							  } else {
									intent.putExtra("image", bitbit);
									
							  }
							  
							  startActivityForResult(intent, 101);
						case R.id.share:
							  
							  return true;
				  }
				  
				  return false;
			});
			
			swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				  @Override
				  public void onRefresh() {
						
						swipeRefreshLayout.setRefreshing(false);
				  }
			});
	  }
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
			Logger.d("onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_contact_detail);
			
			swipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout);
			
			contactName = getIntent().getStringExtra("name");
			
			contactNumber = getIntent().getStringExtra("contact");
			
			myID = getIntent().getStringExtra("myRealID");
			Bitmap bitbit = getContactsByID(myID);
			
			contactTV = findViewById(R.id.idTVPhone);
			nameTV = findViewById(R.id.idTVContactName);
			contactIV = findViewById(R.id.idIVContact);
			
			nameTV.setText(contactName);
			contactTV.setText(contactNumber);
			
			if (bitbit == null) {
				  contactIV.setImageResource(R.drawable.default_image);
			} else {
				  contactIV.setImageBitmap(bitbit);
				  contactIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
			}
			
			bottomNavigation = findViewById(R.id.bottomNavi);
			bottomNavigation.setOnNavigationItemSelectedListener(item -> {
				  switch (item.getItemId()) {
						case R.id.star:
							  
							  return true;
						case R.id.edit:
							  Intent intent = new Intent(ContactDetailActivity.this, ContactEditActivity.class);
							  intent.putExtra("myID", myID);
							  intent.putExtra("name", contactName);
							  intent.putExtra("number", contactNumber);
							  if (bitbit == null) {
									intent.putExtra("image", R.drawable.default_image);
							  } else {
									intent.putExtra("image", bitbit);
									
							  }
							  
							  startActivityForResult(intent, 101);
						case R.id.share:
							  
							  return true;
				  }
				  
				  return false;
			});
			
			swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				  @Override
				  public void onRefresh() {
						
						swipeRefreshLayout.setRefreshing(false);
				  }
			});
	  }
	  
	  @Override
	  protected void onDestroy() {
			Logger.d("onDestroy() called");
			super.onDestroy();
	  }
	  
	  @Override
	  protected void onPause() {
			Logger.d("onPause() called");
			super.onPause();
			
	  }
	  
	  @Override
	  protected void onStop() {
			Logger.d("onStop() called");
			super.onStop();
	  }
	  
}