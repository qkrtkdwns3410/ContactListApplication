package com.example.contactlistapplication.activity;

import java.io.ByteArrayOutputStream;

import com.example.contactlistapplication.R;
import com.example.contactlistapplication.listener.OnTabItemSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.orhanobut.logger.Logger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactDetailActivity extends AppCompatActivity implements OnTabItemSelectedListener {
	  
	  private static final int EDIT_REQUEST_CODE = 101;
	  
	  private BottomNavigationView bottomNavigation;
	  private SwipeRefreshLayout swipeRefreshLayout;
	  private FrameLayout container;
	  
	  private String contactName, contactNumber;
	  private Bitmap contactBitmap;
	  private TextView contactTV, nameTV;
	  private CircleImageView contactIV;
	  
	  @Override
	  protected void onResume() {
			Logger.d("onResume() called");
			
			super.onResume();
			
			setContentView(R.layout.activity_contact_detail);
			
			try {
				  swipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout);
				  
				  contactName = getIntent().getStringExtra("name");
				  Logger.d("d.contactName=" + contactName);
				  
				  contactNumber = getIntent().getStringExtra("contact");
				  Logger.d("d.contactNumber = " + contactNumber);
				  //이전의 비트맵 프로필
				  Bitmap previousBitmap = (Bitmap)getIntent().getExtras().get("image");
				  //콜백된 이미지
				  Bitmap editedBitmap = (Bitmap)getIntent().getExtras().get("callBackImage");
				  
				  Logger.d("contactBitmap  " + contactBitmap);
				  
				  contactTV = findViewById(R.id.idTVPhone);
				  nameTV = findViewById(R.id.idTVContactName);
				  contactIV = findViewById(R.id.idIVContact);
				  
				  nameTV.setText(contactName);
				  contactTV.setText(contactNumber);
				  if (contactBitmap == null) {
						contactIV.setImageResource(R.drawable.default_image);
				  } else {
						contactIV.setImageBitmap(contactBitmap);
						contactIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
				  }
				  bottomNavigation = findViewById(R.id.bottomNavi);
				  bottomNavigation.setOnNavigationItemSelectedListener(item -> {
						switch (item.getItemId()) {
							  case R.id.star:
									
									return true;
							  case R.id.edit:
									Intent intent = new Intent(ContactDetailActivity.this, ContactEditActivity.class);
									intent.putExtra("name", contactName);
									intent.putExtra("number", contactNumber);
									if (contactBitmap == null) {
										  intent.putExtra("image", R.drawable.default_image);
									} else {
										  intent.putExtra("image", contactBitmap);
										  
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
							  contactName = getIntent().getStringExtra("name");
							  Logger.d("d.contactName=" + contactName);
							  
							  contactNumber = getIntent().getStringExtra("contact");
							  Logger.d("d.contactNumber = " + contactNumber);
							  
							  swipeRefreshLayout.setRefreshing(false);
						}
				  });
			} catch (Exception e) {
				  e.printStackTrace();
			}
	  }
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
			Logger.d("onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_contact_detail);
			
			swipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout);
			
			contactName = getIntent().getStringExtra("name");
			Logger.d("d.contactName=" + contactName);
			
			contactNumber = getIntent().getStringExtra("contact");
			Logger.d("d.contactNumber = " + contactNumber);
			contactBitmap = getIntent().getParcelableExtra("image");
			
			Logger.d("contactBitmap  " + contactBitmap);
			
			contactTV = findViewById(R.id.idTVPhone);
			nameTV = findViewById(R.id.idTVContactName);
			contactIV = findViewById(R.id.idIVContact);
			
			nameTV.setText(contactName);
			contactTV.setText(contactNumber);
			if (contactBitmap == null) {
				  contactIV.setImageResource(R.drawable.default_image);
			} else {
				  contactIV.setImageBitmap(contactBitmap);
				  contactIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
			}
			bottomNavigation = findViewById(R.id.bottomNavi);
			bottomNavigation.setOnNavigationItemSelectedListener(item -> {
				  switch (item.getItemId()) {
						case R.id.star:
							  
							  return true;
						case R.id.edit:
							  Intent intent = new Intent(ContactDetailActivity.this, ContactEditActivity.class);
							  intent.putExtra("name", contactName);
							  intent.putExtra("number", contactNumber);
							  if (contactBitmap == null) {
									intent.putExtra("image", R.drawable.default_image);
							  } else {
									intent.putExtra("image", contactBitmap);
									
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
						contactName = getIntent().getStringExtra("name");
						Logger.d("d.contactName=" + contactName);
						
						contactNumber = getIntent().getStringExtra("contact");
						Logger.d("d.contactNumber = " + contactNumber);
						
						swipeRefreshLayout.setRefreshing(false);
				  }
			});
	  }
	  
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
				  
				  Bitmap bitmapImageByEdited = data.getParcelableExtra("image");
				  
				  nameTV.setText(name);
				  contactTV.setText(number);
				  contactIV.setImageBitmap(bitmapImageByEdited);
				  
			}
	  }
}