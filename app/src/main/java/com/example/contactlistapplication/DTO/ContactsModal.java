package com.example.contactlistapplication.DTO;

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
	  
	  private String userImage;
	  
	  private String userName;
	  private String contactName;
	  
	  public ContactsModal(String displayName, String phoneNumber, String userImage) {
			this.userName = displayName;
			this.contactName = phoneNumber;
			this.userImage = userImage;
	  }
	  
	  public String getUserImage() {
			return userImage;
	  }
	  
	  public void setUserImage(String userImage) {
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


























