package com.example.contactlistapplication.activity;

import com.example.contactlistapplication.R;
import com.example.contactlistapplication.fragment.Fragment1;
import com.example.contactlistapplication.fragment.Fragment2;
import com.example.contactlistapplication.fragment.Fragment3;
import com.example.contactlistapplication.listener.OnTabItemSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.orhanobut.logger.Logger;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ContactDetailActivity extends AppCompatActivity implements OnTabItemSelectedListener {
	  
	  BottomNavigationView bottomNavigation;
	  
	  private String contactName, contactNumber;
	  private TextView contactTV, nameTV;
	  private ImageView contactIV;
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_contact_detail);
			
			contactName = getIntent().getStringExtra("name");
			Logger.d("d.contactName=" + contactName);
			contactNumber = getIntent().getStringExtra("contact");
			Logger.d("d.contactNumber = " + contactNumber);
			
			contactTV = findViewById(R.id.idTVPhone);
			nameTV = findViewById(R.id.idTVContactName);
			contactIV = findViewById(R.id.idIVContact);
			
			nameTV.setText(contactName);
			contactTV.setText(contactNumber);
			
			bottomNavigation = findViewById(R.id.bottomNavi);
			bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
				  @Override
				  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
						switch (item.getItemId()) {
							  case R.id.star:
									
									return true;
							  case R.id.edit:
									Intent intent = new Intent(ContactDetailActivity.this, ContactEditActivity.class);
									intent.putExtra("name", contactName);
									intent.putExtra("number", contactNumber);
									
									startActivity(intent);
							  case R.id.share:
									
									return true;
						}
						
						return false;
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
	  
}