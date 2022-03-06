package com.example.contactlistapplication.activity;

import java.io.IOException;
import java.io.InputStream;
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
			Logger.d("getContacts() called");
			Logger.d("contactIdPara  " + contactIdPara);
			String contactId = "";
			Bitmap userImage = null;
			
			//핸드폰에서 데이터를 들고옵니다.
			Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
			
			if (cursor.getCount() > 0) {
				  // 카운트가 0 초과라면 cursor를 계속 진행진행 ~~~~~
				  while (cursor.moveToNext()) {
						int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
						//폰넘버를 가진 친구라면!
						if (hasPhoneNumber > 0) {
							  contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
							  
							  if (Objects.equals(contactId, contactIdPara)) {
									
									userImage = loadContactPhoto(getContentResolver(), Long.parseLong(contactId));
									Logger.d("userImage  " + userImage);
									
									return userImage;
							  }
							  
						}
				  }
			}
			cursor.close();
			return userImage;
	  }
	  
	  public static Bitmap loadContactPhoto(ContentResolver cr, long contact_id) {
			
			Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contact_id);
			
			InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
			
			if (input == null) {
				  
				  return null;
				  
			}
			
			return BitmapFactory.decodeStream(input);
			
	  }
	  
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			
			if (requestCode == EDIT_REQUEST_CODE) {
				  
				  if (resultCode != RESULT_OK) {
						Logger.d("리턴됨  ");
						return;
				  }
				  
				  Logger.d("requestCode  " + requestCode);
				  
				  String name = data.getStringExtra("name");
				  Logger.d("name  " + name);
				  String number = data.getStringExtra("number");
				  Logger.d("number  " + number);
				  
				  String callBackedID = data.getStringExtra("callBackID");
				  Logger.d("체크callBackedID  " + callBackedID);
				  
				  nameTV.setText(name);
				  contactTV.setText(number);
				  contactIV.setImageBitmap(getContactsByID(callBackedID));
				  
			}
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