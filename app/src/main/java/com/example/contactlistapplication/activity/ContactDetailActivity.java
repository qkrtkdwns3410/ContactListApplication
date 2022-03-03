package com.example.contactlistapplication.activity;

import com.example.contactlistapplication.R;
import com.example.contactlistapplication.fragment.Fragment1;
import com.example.contactlistapplication.fragment.Fragment2;
import com.example.contactlistapplication.fragment.Fragment3;
import com.example.contactlistapplication.listener.OnTabItemSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.orhanobut.logger.Logger;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ContactDetailActivity extends AppCompatActivity implements OnTabItemSelectedListener {
	  
	  Fragment1 fragment1;
	  Fragment2 fragment2;
	  Fragment3 fragment3;
	  
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
			
			fragment1 = new Fragment1();
			fragment2 = new Fragment2();
			fragment3 = new Fragment3();
			
			bottomNavigation = findViewById(R.id.bottomNavi);
			bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
				  @Override
				  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
						switch (item.getItemId()) {
							  case R.id.star:
									getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
									Toast.makeText(getApplicationContext(), "즐겨찾기가 눌림", Toast.LENGTH_SHORT).show();
									
									return true;
							  case R.id.edit:
									getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
									
									return true;
							  case R.id.share:
									getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment3).commit();
									
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