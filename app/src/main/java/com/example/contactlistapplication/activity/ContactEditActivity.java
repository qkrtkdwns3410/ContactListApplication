package com.example.contactlistapplication.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.SimpleTimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ContactEditActivity extends AppCompatActivity {
	  private EditText nameEdit, phoneEdit, emailEdit, groupEdit;
	  private Button btnCancel, btnSave;
	  
	  private ImageView contactIV;
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
			Logger.d("onCreate 실행  :");
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_contact_edit);
			
			contactIV = findViewById(R.id.idIVContact);
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
										  
										  updateContact(name, phone, email, String.valueOf(currentId));
										  
										  Intent callBackIntent = new Intent();
										  callBackIntent.putExtra("name", name);
										  Logger.d("name  " + name);
										  callBackIntent.putExtra("number", phone);
										  Logger.d("phone  " + phone);
										  
										  setResult(RESULT_OK, callBackIntent);
										  
										  finish();
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
	  
	  public boolean updateContact(String name, String number, String email, String ContactId) {
			Logger.d("name  " + name);
			Logger.d("number  " + number);
			Logger.d("email  " + email);
			Logger.d("ContactId  " + ContactId);
			
			boolean success = true;
			String phnumexp = "^[0-9]*$";
			
			try {
				  name = name.trim();
				  email = email.trim();
				  number = number.trim();
				  Logger.d("name  " + name);
				  Logger.d("number  " + number);
				  Logger.d("email  " + email);
				  
				  if (name.equals("") && number.equals("") && email.equals("")) {
						success = false;
				  } else if ((!number.equals("")) && (!match(number, phnumexp))) {
						success = false;
				  } else if ((!email.equals("")) && (!isEmailValid(email))) {
						success = false;
				  } else {
						ContentResolver contentResolver = getContentResolver();
						
						String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
						
						String[] emailParams = new String[] {ContactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};
						String[] nameParams = new String[] {ContactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
						String[] numberParams = new String[] {ContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
						
						ArrayList<android.content.ContentProviderOperation> ops = new ArrayList<android.content.ContentProviderOperation>();
						
						if (!email.equals("")) {
							  Logger.d("이메일 쿼리");
							  ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
								  .withSelection(where, emailParams)
								  .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
								  .build());
						}
						
						if (!name.equals("")) {
							  Logger.d("이름 쿼리");
							  
							  ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
								  .withSelection(where, nameParams)
								  .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
								  .build());
						}
						
						if (!number.equals("")) {
							  Logger.d("넘버 쿼리");
							  
							  ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
								  .withSelection(where, numberParams)
								  .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
								  .build());
						}
						Logger.d("ops  " + ops);
						contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
				  }
			} catch (Exception e) {
				  e.printStackTrace();
				  success = false;
			}
			return success;
	  }
	  // To get COntact Ids of all contact use the below method
	  
	  /**
	   * @return arraylist containing id's  of all contacts <br/>
	   *         empty arraylist if no contacts exist <br/><br/>
	   * <b>Note: </b>This method requires permission <b>android.permission.READ_CONTACTS</b>
	   */
	  public ArrayList<String> getAllConactIds() {
			ArrayList<String> contactList = new ArrayList<String>();
			
			Cursor cursor = managedQuery(ContactsContract.Contacts.CONTENT_URI, null, null, null, "display_name ASC");
			
			if (cursor != null) {
				  if (cursor.moveToFirst()) {
						do {
							  @SuppressLint("Range") int _id = cursor.getInt(cursor.getColumnIndex("_id"));
							  contactList.add("" + _id);
							  
						}
						while (cursor.moveToNext());
				  }
			}
			
			return contactList;
	  }
	  
	  private boolean isEmailValid(String email) {
			String emailAddress = email.toString().trim();
			if (emailAddress == null)
				  return false;
			else if (emailAddress.equals(""))
				  return false;
			else if (emailAddress.length() <= 6)
				  return false;
			else {
				  String expression = "^[a-z][a-z|0-9|]*([_][a-z|0-9]+)*([.][a-z|0-9]+([_][a-z|0-9]+)*)?@[a-z][a-z|0-9|]*\\.([a-z][a-z|0-9]*(\\.[a-z][a-z|0-9]*)?)$";
				  CharSequence inputStr = emailAddress;
				  Pattern pattern = Pattern.compile(expression,
					  Pattern.CASE_INSENSITIVE);
				  Matcher matcher = pattern.matcher(inputStr);
				  if (matcher.matches())
						return true;
				  else
						return false;
			}
	  }
	  
	  private boolean match(String stringToCompare, String regularExpression) {
			boolean success = false;
			Pattern pattern = Pattern.compile(regularExpression);
			Matcher matcher = pattern.matcher(stringToCompare);
			if (matcher.matches())
				  success = true;
			return success;
	  }
	  
}