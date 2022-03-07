package com.example.contactlistapplication.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.SimpleTimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.contactlistapplication.R;
import com.orhanobut.logger.Logger;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactEditActivity extends AppCompatActivity {
	  
	  private static final int GET_GALLERY_IMAGE = 200;
	  
	  private String myID;
	  
	  private EditText nameEdit, phoneEdit, emailEdit, groupEdit;
	  private Button btnCancel, btnSave;
	  
	  private Bitmap bitmapImage;
	  
	  private CircleImageView contactIV;
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
	  
	  @SuppressLint("Range")
	  private Bitmap getContactsByID(String contactIdPara) {
			Logger.d("getContactsByID() called with: contactIdPara = [" + contactIdPara + "]");
			
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
									
									userImage = GetContactPhoto(contactId);
									
									return userImage;
							  }
							  
						}
				  }
			}
			cursor.close();
			return userImage;
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
	  protected void onCreate(Bundle savedInstanceState) {
			Logger.d("onCreate 실행  :");
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_contact_edit);
			
			try {
				  contactIV = findViewById(R.id.idIVContact);
				  nameEdit = findViewById(R.id.idEdtName);
				  phoneEdit = findViewById(R.id.idEdtPhoneNumber);
				  //여기는 일단 비워두겠음..
				  emailEdit = findViewById(R.id.idEdtEmail);
				  groupEdit = findViewById(R.id.idEdtGroup);
				  
				  btnSave = findViewById(R.id.idBtnSave);
				  btnCancel = findViewById(R.id.idBtnCancel);
				  
				  myID = getIntent().getStringExtra("myID");
				  
				  bitmapImage = getContactsByID(myID);
				  
				  nameEdit.setText(getIntent().getStringExtra("name"));
				  phoneEdit.setText(getIntent().getStringExtra("number"));
				  if (bitmapImage == null) {
						Logger.d("이미지가 널입니다.");
						contactIV.setImageResource(R.drawable.default_image);
						
				  } else {
						Logger.d("이미지가 널이 아닙니다..");
						
						contactIV.setImageBitmap(bitmapImage);
				  }
				  
			} catch (Exception e) {
				  e.printStackTrace();
			}
			//세팅
			//
			
			btnSave.setOnClickListener(new View.OnClickListener() {
				  @Override
				  public void onClick(View view) {
						Logger.d("저장을 눌렀습니다.");
						
						String name = nameEdit.getText().toString();
						String phone = phoneEdit.getText().toString();
						String email = emailEdit.getText().toString();
						BitmapDrawable image = (BitmapDrawable)contactIV.getDrawable();
						
						Logger.d("name + " + name);
						Logger.d("phone + " + phone);
						Logger.d("email + " + email);
						Logger.d("image  " + image);
						
						String oldName = ContactEditActivity.this.getIntent().getStringExtra("name");
						String oldPhoneNumber = ContactEditActivity.this.getIntent().getStringExtra("number");
						
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
										  
										  updateContact(name, phone, email, image, String.valueOf(currentId));
										  
										  Intent callBackIntent = new Intent();
										  callBackIntent.putExtra("name", name);
										  Logger.d("name  " + name);
										  callBackIntent.putExtra("number", phone);
										  Logger.d("phone  " + phone);
										  callBackIntent.putExtra("callBackID", myID);
										  
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
	  
	  public boolean updateContact(String name, String number, String email, BitmapDrawable image, String ContactId) {
			Logger.d(
				"updateContact() called with: name = [" + name + "], number = [" + number + "], email = [" + email + "], image = [" + image + "], ContactId = ["
					+ ContactId + "]");
			
			boolean success = true;
			String phnumexp = "/^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;";
			
			try {
				  name = name.trim();
				  number = number.trim();
				  number = number.replace("-", "");
				  Logger.d("name  " + name);
				  Logger.d("number  " + number);
				  
				  if (name.equals("") && number.equals("")) {
						success = false;
				  } else if ((number.equals(""))) {
						
						success = false;
				  } else {
						ContentResolver contentResolver = getContentResolver();
						
						String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
						
						String[] emailParams = new String[] {ContactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};
						String[] nameParams = new String[] {ContactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
						String[] numberParams = new String[] {ContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
						String[] imageParams = new String[] {ContactId, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE};
						
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
						
						if (image != null) {
							  Logger.d("이미지 쿼리");
							  Bitmap newImage = image.getBitmap();
							  Logger.d("newImage  " + newImage);
							  
							  ByteArrayOutputStream out = new ByteArrayOutputStream();
							  newImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
							  
							  byte[] b = out.toByteArray();
							  
							  ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
								  .withSelection(where, imageParams)
								  .withValue(ContactsContract.CommonDataKinds.Photo.DATA15, b)
								  .build());
						}
						contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
				  }
			} catch (Exception e) {
				  e.printStackTrace();
				  success = false;
			}
			return success;
	  }
	  // To get COntact Ids of all contact use the below method
	  
	  public InputStream openPhoto(long contactId) {
			Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
			Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
			Cursor cursor = getContentResolver().query(photoUri,
				new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
			if (cursor == null) {
				  return null;
			}
			try {
				  if (cursor.moveToFirst()) {
						byte[] data = cursor.getBlob(0);
						if (data != null) {
							  return new ByteArrayInputStream(data);
						}
				  }
			} finally {
				  cursor.close();
			}
			return null;
	  }
	  
	  //이미지 클릭시 갤러리 호출및 이미지 임시변경! >> 저장누르면 이미지가 최종 변경되어야합니다.
	  public void modifyImageView(View view) {
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent, GET_GALLERY_IMAGE);
			
	  }
	  
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			
			if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
				  
				  Uri selectedImageUri = data.getData();
				  contactIV.setImageURI(selectedImageUri);
			}
	  }
}
