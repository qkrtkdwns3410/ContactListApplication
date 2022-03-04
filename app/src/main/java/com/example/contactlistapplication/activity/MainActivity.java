package com.example.contactlistapplication.activity;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.contactlistapplication.DTO.ContactsModal;
import com.example.contactlistapplication.R;
import com.example.contactlistapplication.adapter.ContactsRVAdapter;
import com.orhanobut.logger.Logger;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import android.annotation.SuppressLint;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
	  
	  private ArrayList<ContactsModal> contactsModalArrayList;
	  private RecyclerView contactRV;
	  private ContactsRVAdapter contactsRVAdapter;
	  private ProgressBar loadingPB;
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
			Logger.d("onCreate 실행  :");
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			contactsModalArrayList = new ArrayList<>();
			contactRV = findViewById(R.id.idRVContacts);
			
			//리사이클러 뷰를 초기화
			prepareContactRV();
			
			//권한 허용필요함
			AutoPermissions.Companion.loadAllPermissions(this, 101);
			
			getContacts();
			
			getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, false, phoneObserver);
	  }
	  
	  //리사이클러 뷰 >리니어레이아웃을 가진 어댑터 아이템 장착
	  private void prepareContactRV() {
			contactsModalArrayList = new ArrayList<>();
			contactsRVAdapter = new ContactsRVAdapter(this, contactsModalArrayList);
			
			contactRV.setLayoutManager(new LinearLayoutManager(this));
			
			contactRV.setAdapter(contactsRVAdapter);
	  }
	  
	  //리사이클러뷰 초기화 셋팅
	  public void recyclerViewInitSetting() {
			
			LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
			contactRV.setLayoutManager(linearLayoutManager);
			contactsRVAdapter = new ContactsRVAdapter(this, contactsModalArrayList);
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
	  
	  @SuppressLint("Range")
	  private void getContacts() {
			String contactId = "";
			String displayName = "";
			
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
							  if (displayName.contains("xcccc")) {
									Logger.d("displayName  " + displayName);
							  }
							  //전화나 문자를 위한 Cursor
							  Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
								  ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?"
								  , new String[] {
									  contactId
								  }, null);
							  
							  if (phoneCursor.moveToNext()) {
									String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
									contactsModalArrayList.add(new ContactsModal(displayName, phoneNumber));
									
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
	  
}