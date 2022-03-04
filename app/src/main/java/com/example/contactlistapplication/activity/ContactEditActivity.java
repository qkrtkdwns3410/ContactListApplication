package com.example.contactlistapplication.activity;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.contactlistapplication.R;
import com.orhanobut.logger.Logger;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
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
	  // The Cursor that contains the Contact row
	  private Cursor mCursor;
	  // The index of the lookup key column in the cursor
	  private int lookupKeyIndex;
	  // The index of the contact's _ID value
	  private int idIndex;
	  // The lookup key from the Cursor
	  private String currentLookupKey;
	  // The _ID value from the Cursor
	  private long currentId;
	  // A content URI pointing to the contact
	  private Uri selectedContactUri;
	  
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
						
						String oldName = ContactEditActivity.this.getIntent().getStringExtra("name");
						String oldPhoneNumber = ContactEditActivity.this.getIntent().getStringExtra("number");
						Logger.d("oldPhoneNumber + " + oldPhoneNumber);
						
						if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(phone)) {
							  Toast.makeText(ContactEditActivity.this, "데이터를 입력해주세요", Toast.LENGTH_SHORT).show();
						} else {
							  try {
									
									/*
									 * Once the user has selected a contact to edit,
									 * this gets the contact's lookup key and _ID values from the
									 * cursor and creates the necessary URI.
									 */
									
									mCursor = ContactEditActivity.this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
										ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?", new String[] {oldName}, null);
									
									while (mCursor.moveToNext()) {
										  
										  // Gets the lookup key column index
										  lookupKeyIndex = mCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
										  // Gets the lookup key value
										  currentLookupKey = mCursor.getString(lookupKeyIndex);
										  Logger.d("currentLookupKey : " + currentLookupKey);
										  
										  // Gets the _ID column index
										  idIndex = mCursor.getColumnIndex(ContactsContract.Contacts._ID);
										  Logger.d("idIndex : " + idIndex);
										  
										  currentId = mCursor.getLong(idIndex);
										  Logger.d("currentId :" + currentId);
										  
										  selectedContactUri =
											  ContactsContract.Contacts.getLookupUri(currentId, currentLookupKey);
										  Logger.d("selectedContactUri  " + selectedContactUri);
										  
										  // Intent editIntent = new Intent(Intent.ACTION_EDIT);
										  // editIntent.setDataAndType(selectedContactUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
										  // editIntent.putExtra("finishActivityOnSaveCompleted", true);
										  // startActivity(editIntent);
										  String[] items = {ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID,
											  ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
										  
										  String phoneNumber = phone;
										  String phoneNumberFormatNumber = PhoneNumberUtils.formatNumber(phoneNumber);
										  
										  //RawContactId를 가져올 where문 > Phone 정보를
										  String rawContactWhere =
											  ContactsContract.CommonDataKinds.Phone.NUMBER + " IN('" + phoneNumber + "', " + phoneNumberFormatNumber + "') ";
										  
										  Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, items, rawContactWhere,
											  null, null);
										  
										  cursor.moveToFirst();
										  @SuppressLint("Range") int rawContactId = cursor.getInt(
											  cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
										  
										  ArrayList<ContentProviderOperation> ops = new ArrayList<>();
										  String where = ContactsContract.Data.CONTACT_ID + " =? AND  " + ContactsContract.Contacts.Data.MIMETYPE + " = ?";
										  
										  //이름정보 변경
										  if (!name.equals("")) {
												String[] nameParams = new String[] {
													ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
												};
												Cursor nameCursor =
													getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, nameParams, null);
												
												//edit if exits
												if (nameCursor.getCount() > 0) {
													  ops.add(
														  ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
															  .withValue(where, nameParams)
															  .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name).build());
													  
												} else {
													  ContentValues contentValues = new ContentValues();
													  contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
													  contentValues.put(ContactsContract.Data.MIMETYPE,
														  ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
													  contentValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
													  
													  ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
														  .withValues(contentValues)
														  .build());
												}
										  }
										  
										  modifyContacts(idIndex);
									}
									
							  } catch (Exception e) {
									e.printStackTrace();
							  }
							  
						}
				  }
			});
			
			btnCancel.setOnClickListener(view -> {
				  Logger.d("취소를 눌렀습니다.");
				  
				  finish();
			});
	  }
	  
	  private void modifyContacts(int idIndex) {
			ArrayList<ContentProviderOperation> operationArrayList = new ArrayList<>();
			
			ContentProviderOperation opt = operationArrayList.get(idIndex);
			
	  }
	  
}