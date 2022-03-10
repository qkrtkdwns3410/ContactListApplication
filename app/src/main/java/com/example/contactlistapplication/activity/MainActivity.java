package com.example.contactlistapplication.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.contactlistapplication.DTO.ContactsModal;
import com.example.contactlistapplication.R;
import com.example.contactlistapplication.adapter.ContactsRVAdapter;
import com.orhanobut.logger.Logger;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
	  
	  private ArrayList<ContactsModal> contactsModalArrayList;
	  
	  private IndexFastScrollRecyclerView contactRV;
	  private ContactsRVAdapter contactsRVAdapter;
	  private ContentObserver phoneObserver = new ContentObserver(new Handler()) {
			@Override
			public boolean deliverSelfNotifications() {
				  return super.deliverSelfNotifications();
			}
			
			@Override
			public void onChange(boolean selfChange) {
				  super.onChange(selfChange);
				  prepareContactRV();
				  getContacts();
			}
	  };
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
			
			Logger.d("onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
			
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_main);
			
			contactsModalArrayList = new ArrayList<>();
			contactRV = findViewById(R.id.fast_scroller_recycler);
			
			contactRV.setIndexBarVisibility(false);
			
			//리사이클러 뷰를 초기화
			prepareContactRV();
			
			getContacts();
			
			getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, false, phoneObserver);
			//권한 허용필요함
			
			Toolbar toolbar = findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			if (getSupportActionBar() != null) {
				  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				  getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_view_headline_black_24dp);
				  getSupportActionBar().setTitle("");
			}
			
			contactRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
				  @Override
				  public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
						super.onScrollStateChanged(recyclerView, newState);
						
						if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
							  contactRV.setIndexBarVisibility(true);
						} else {
							  new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										  contactRV.setIndexBarVisibility(false);
									}
							  }, 1000);
							  
						}
				  }
				  
				  @Override
				  public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
						super.onScrolled(recyclerView, dx, dy);
						contactRV.setIndexBarVisibility(true);
						contactRV.setIndexBarTransparentValue((float)0.4);
						contactRV.setIndexBarStrokeWidth(0);
				  }
			});
			
			AutoPermissions.Companion.loadAllPermissions(this, 101);
	  }
	  
	  @Override
	  protected void onResume() {
			super.onResume();
			
			setContentView(R.layout.activity_main);
			
			contactsModalArrayList = new ArrayList<>();
			contactRV = findViewById(R.id.fast_scroller_recycler);
			
			contactRV.setIndexBarVisibility(false);
			
			//리사이클러 뷰를 초기화
			prepareContactRV();
			
			getContacts();
			
			getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, false, phoneObserver);
			//권한 허용필요함
			
			Toolbar toolbar = findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			if (getSupportActionBar() != null) {
				  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				  getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_view_headline_black_24dp);
				  getSupportActionBar().setTitle("");
			}
			
			contactRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
				  @Override
				  public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
						super.onScrollStateChanged(recyclerView, newState);
						contactRV.setIndexBarTransparentValue((float)0.4);
						contactRV.setIndexBarStrokeWidth(0);
						
						if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
							  contactRV.setIndexBarVisibility(true);
						} else {
							  new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										  contactRV.setIndexBarVisibility(false);
									}
							  }, 1000);
						}
				  }
				  
				  @Override
				  public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
						super.onScrolled(recyclerView, dx, dy);
						contactRV.setIndexBarVisibility(true);
				  }
			});
	  }
	  
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
			Logger.d("onCreateOptionsMenu() called with: menu = [" + menu + "]");
			
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.search_menu, menu);
			MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
			final SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchViewItem);
			
			searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
				  @Override
				  public boolean onQueryTextSubmit(String query) {
						Logger.d("onQueryTextSubmit() called with: query = [" + query + "]");
						// on query submit we are clearing the focus for our search view.
						searchView.clearFocus();
						return false;
				  }
				  
				  @Override
				  public boolean onQueryTextChange(String newText) {
						Logger.d("onQueryTextChange() called with: newText = [" + newText + "]");
						
						// on changing the text in our search view we are calling
						// a filter method to filter our array list.
						filter(newText.toLowerCase());
						return false;
				  }
				  
			});
			return super.onCreateOptionsMenu(menu);
	  }
	  
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
			Logger.d("onOptionsItemSelected() called with: item = [" + item + "]");
			switch (item.getItemId()) {
				  case android.R.id.home:
						Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
						break;
				  
				  case R.id.add_contacts:
						Toast.makeText(this, "연락처 추가", Toast.LENGTH_SHORT).show();
						break;
				  
				  case R.id.app_bar_search:
						Toast.makeText(this, "서치", Toast.LENGTH_SHORT).show();
						break;
				  
				  case R.id.more_options:
						Toast.makeText(this, "더보기", Toast.LENGTH_SHORT).show();
						break;
				  
			}
			return super.onOptionsItemSelected(item);
	  }
	  
	  private void filter(String text) {
			// in this method we are filtering our array list.
			// on below line we are creating a new filtered array list.
			ArrayList<ContactsModal> filteredlist = new ArrayList<>();
			// on below line we are running a loop for checking if the item is present in array list.
			for (ContactsModal item : contactsModalArrayList) {
				  if (item.getUserName().toLowerCase().contains(text.toLowerCase())) {
						// on below line we are adding item to our filtered array list.
						filteredlist.add(item);
				  }
			}
			// on below line we are checking if the filtered list is empty or not.
			if (filteredlist.isEmpty()) {
				  Toast.makeText(this, "No Contact Found", Toast.LENGTH_SHORT).show();
			} else {
				  // passing this filtered list to our adapter with filter list method.
				  contactsRVAdapter.filterList(filteredlist);
			}
	  }
	  
	  //리사이클러 뷰 >리니어레이아웃을 가진 어댑터 아이템 장착
	  private void prepareContactRV() {
			contactsModalArrayList = new ArrayList<>();
			contactsRVAdapter = new ContactsRVAdapter(this, contactsModalArrayList);
			
			contactRV.setLayoutManager(new LinearLayoutManager(this));
			
			contactRV.setAdapter(contactsRVAdapter);
	  }
	  
	  //권한 못받는 불쌍한 친구들
	  @Override
	  public void onDenied(int i, String[] permissions) {
			Toast.makeText(this, "거절된 권한의 개수" + permissions.length, Toast.LENGTH_SHORT).show();
			
	  }
	  
	  //권한을 명받음
	  @Override
	  public void onGranted(int i, String[] permissions) {
			Toast.makeText(this, "허용된 권한의 개수" + permissions.length, Toast.LENGTH_SHORT).show();
			
	  }
	  
	  @Override
	  protected void onDestroy() {
			super.onDestroy();
			
			getContentResolver().unregisterContentObserver(phoneObserver);
	  }
	  
	  @SuppressLint("Range")
	  private void getContacts() {
			Logger.d("getContacts() called");
			String contactId = "";
			String displayName = "";
			Bitmap userImage;
			
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
							  displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
							  //유저 이미지 추가해야합니다.!!
							  userImage = GetContactPhoto(contactId);
							  
							  //전화나 문자를 위한 Cursor
							  Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
								  ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?"
								  , new String[] {
									  contactId
								  }, null);
							  
							  if (phoneCursor.moveToNext()) {
									String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
									contactsModalArrayList.add(new ContactsModal(contactId, displayName, phoneNumber, userImage));
									
							  }
							  //폰 커서를 닫습니다.
							  phoneCursor.close();
							  
						}
				  }
			}
			cursor.close();
			
			//리사이클러 뷰에서 notifyDataSetChanged는 리스트의 크기와 아이템이 둘 다 변경되는 경우에 사용하면 된다.
			contactsRVAdapter.notifyDataSetChanged();
			
	  }
	  
	  private Bitmap GetContactPhoto(String contactID) {
			
			Bitmap photo = null;
			
			try {
				  InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
					  ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactID)));
				  
				  if (inputStream != null) {
						photo = BitmapFactory.decodeStream(inputStream);
						
						//비트맵의 해상도 조절
						Bitmap.createScaledBitmap(
							photo
							, photo.getWidth() * 2
							, photo.getHeight() * 2
							, true);
						
						Logger.d("photo  " + photo);
				  } else {
						return null;
				  }
				  inputStream.close();
				  
			} catch (IOException e) {
				  e.printStackTrace();
			}
			return photo;
			
	  }
	  
	  @Override
	  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			
			AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
	  }
}