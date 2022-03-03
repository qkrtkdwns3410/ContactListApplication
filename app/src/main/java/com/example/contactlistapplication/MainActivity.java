package com.example.contactlistapplication;

import java.util.ArrayList;

import com.example.contactlistapplication.DTO.ContactsModal;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import android.database.Cursor;
import android.provider.ContactsContract;
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
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
		 
			contactsModalArrayList = new ArrayList<>();
			contactRV = findViewById(R.id.idRVContainer);
			loadingPB = findViewById(R.id.idPBLoading);
		 
			//리사이클러 뷰를 초기화
			prepareContactRV();
			
			
			
			//권한 허용필요함
			AutoPermissions.Companion.loadAllPermissions(this, 101);
			
	  }
	  
	  //리사이클러 뷰 초기화
	  private void prepareContactRV() {
			contactsRVAdapter = new ContactsRVAdapter(this, contactsModalArrayList);
		 
			contactRV.setLayoutManager(new LinearLayoutManager(this));
		 
			contactRV.setAdapter(contactsRVAdapter);
	  }
	  
	  @Override
	  public void onGranted(int i, String[] permissions) {
			Toast.makeText(this,"허용된 권한의 개수"+permissions.length, Toast.LENGTH_SHORT ) .show();
			
			getContacts();
			
	  }
	  
	  @Override
	  public void onDenied(int i, String[] permissions) {
			Toast.makeText(this, "거절된 권한의 개수" + permissions.length, Toast.LENGTH_SHORT).show();
		 
	  }
	  
	  private void getContacts() {
			String contactId = "";
			String displayName = "";
		 
			//핸드폰에서 데이터를 들고옵니다.
			Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
		  
			if (cursor.getCount() > 0) {
				  // 카운트가 0 초과라면 cursor를 계속 진행진행 ~~~~~
				  if (cursor.moveToNext()) {
						int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
						
				  }
				  
			}
			
			
	  }
	  

	  
}