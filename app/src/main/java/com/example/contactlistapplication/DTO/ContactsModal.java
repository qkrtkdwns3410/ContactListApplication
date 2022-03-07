package com.example.contactlistapplication.DTO;

import android.graphics.Bitmap;

/**
 *packageName    : com.example.contactlistapplication.DTO
 * fileName       : ContactsDetailModal
 * author         : letscombine
 * date           : 2022-03-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-03-03        letscombine       최초 생성
 */
public class ContactsModal implements Comparable<ContactsModal> {
	  
	  private Bitmap userImage;
	  
	  private String userName;
	  private String contactName;
	  
	  private String contactId;
	  
	  public ContactsModal(String contactId, String displayName, String phoneNumber, Bitmap userImage) {
			this.userName = displayName;
			this.contactName = phoneNumber;
			this.userImage = userImage;
			this.contactId = contactId;
	  }
	  
	  public String getContactId() {
			return contactId;
	  }
	  
	  public void setContactId(String contactId) {
			this.contactId = contactId;
	  }
	  
	  public Bitmap getUserImage() {
			return userImage;
	  }
	  
	  public void setUserImage(Bitmap userImage) {
			this.userImage = userImage;
	  }
	  
	  public String getUserName() {
			return userName;
	  }
	  
	  public void setUserName(String userName) {
			this.userName = userName;
	  }
	  
	  public String getContactName() {
			return contactName;
	  }
	  
	  @Override
	  public int compareTo(ContactsModal contactsModal) {
			//여긴 이름을 기준으로 정렬
			
			//이건 내림 차순
			
			//문자열은 이런 식으로 가능
			
			//		return this.name.compareTo(_people.name);
			
			//이건 오름차순
			
			//문자열은 이런 식으로 가능
			
			return contactsModal.contactName.compareTo(this.contactName);
			
	  }
	  
	  public void setContactName(String contactName) {
			
			this.contactName = contactName;
	  }
	  
}


























