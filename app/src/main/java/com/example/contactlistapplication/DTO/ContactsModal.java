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
public class ContactsModal {
	  
	  private Bitmap userImage;
	  
	  private String userName;
	  private String contactName;
	  
	  public ContactsModal(String displayName, String phoneNumber, Bitmap userImage) {
			this.userName = displayName;
			this.contactName = phoneNumber;
			this.userImage = userImage;
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
	  
	  public void setContactName(String contactName) {
			this.contactName = contactName;
	  }
	  
}


























