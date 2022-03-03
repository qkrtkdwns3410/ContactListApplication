package com.example.contactlistapplication.activity;

import java.util.ArrayList;

import com.example.contactlistapplication.R;
import com.orhanobut.logger.Logger;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ContactEditActivity extends AppCompatActivity {
	  private EditText nameEdit, phoneEdit, emailEdit, groupEdit;
	  private Button btnCancel, btnSave;
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_contact_edit);
			
			nameEdit = findViewById(R.id.idEdtName);
			phoneEdit = findViewById(R.id.idEdtPhoneNumber);
			emailEdit = findViewById(R.id.idEdtEmail);
			groupEdit = findViewById(R.id.idEdtGroup);
			
			btnSave = findViewById(R.id.idBtnSave);
			btnCancel = findViewById(R.id.idBtnCancel);
			
			btnSave.setOnClickListener(new View.OnClickListener() {
				  @Override
				  public void onClick(View view) {
						Logger.d("저장을 눌렀습니다.");
						
						String name = nameEdit.getText().toString();
						String phone = phoneEdit.getText().toString();
						String email = emailEdit.getText().toString();
						
						Logger.d("name + " + name);
						Logger.d("phone + " + phone);
						Logger.d("email + " + email);
						
						String oldName = getIntent().getStringExtra("name");
						String oldPhoneNumber = getIntent().getStringExtra("number");
						Logger.d("oldPhoneNumber + " + oldPhoneNumber);
						
						if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(phone)) {
							  Toast.makeText(ContactEditActivity.this, "데이터를 입력해주세요", Toast.LENGTH_SHORT).show();
						} else {
							  updateNameAndNumber(ContactEditActivity.this, oldPhoneNumber, name, phone);
							  finish();
							  
						}
				  }
			});
			
			btnCancel.setOnClickListener(new View.OnClickListener() {
				  @Override
				  public void onClick(View view) {
						Logger.d("취소를 눌렀습니다.");
						
						finish();
				  }
			});
	  }
	  
	  private final static String[] DATA_COLS = {
		  ContactsContract.Data.MIMETYPE,
		  ContactsContract.Data.DATA1,
		  ContactsContract.Data.CONTACT_ID
	  };
	  
	  public boolean updateNameAndNumber(final Context context, String number, String newName, String newNumber) {
			Logger.d("number + " + number);
			Logger.d("context + " + context);
			Logger.d("newName + " + newName);
			Logger.d("newNumber + " + newNumber);
			
			if (context == null || number == null || number.trim().isEmpty()) {
				  
				  return false;
			}
			
			if (newNumber != null && newNumber.trim().isEmpty()) {
				  
				  newNumber = null;
			}
			
			if (newNumber == null) {
				  
				  return false;
			}
			
			String contactId = getContactId(context, number);
			Logger.d("contactId + " + contactId);
			
			if (contactId == null) {
				  return false;
			}
			
			//selection for name
			String where = String.format(
				"%s = '%s' AND %s = ?",
				DATA_COLS[0], //mimetype
				ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
				DATA_COLS[2]/*contactId*/);
			
			String[] args = {contactId};
			
			ArrayList<ContentProviderOperation> operations = new ArrayList<>();
			
			operations.add(
				ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					.withSelection(where, args)
					.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, newName)
					.build()
			);
			
			//change selection for number
			where = String.format(
				"%s = '%s' AND %s = ?",
				DATA_COLS[0],//mimetype
				ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
				DATA_COLS[1]/*number*/);
			
			//change args for number
			args[0] = number;
			
			operations.add(
				ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					.withSelection(where, args)
					.withValue(DATA_COLS[1]/*number*/, newNumber)
					.build()
			);
			
			try {
				  ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);
				  
				  for (ContentProviderResult result : results) {
						
						Log.d("Update Result", result.toString());
				  }
				  
				  return true;
			} catch (Exception e) {
				  e.printStackTrace();
			}
			
			return false;
	  }
	  
	  public static String getContactId(Context context, String number) {
			Logger.d("context : " + context);
			Logger.d("number : " + number);
			
			Cursor cursor = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] {ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER},
				ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
				new String[] {number},
				null
			);
			Logger.d("cursor : " + cursor);
			
			if (cursor == null || cursor.getCount() == 0) {
				  Logger.d("cursor : " + cursor);
				  return null;
			}
			
			cursor.moveToFirst();
			
			@SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
			Logger.d("id : " + id);
			cursor.close();
			return id;
	  }
}