package com.example.contactlistapplication;

import java.util.ArrayList;
import java.util.TreeMap;

import com.example.contactlistapplication.DTO.ContactsModal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 *packageName    : com.example.contactlistapplication.adapter
 * fileName       : ContactsRVAdapter
 * author         : letscombine
 * date           : 2022-03-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-03-03        letscombine       최초 생성
 */
public class ContactsRVAdapter extends RecyclerView.Adapter<ContactsRVAdapter.ViewHolder> {
	  
	  private Context context;
	  private ArrayList<ContactsModal> contactsModalArrayList;
	  
	  public ContactsRVAdapter(Context context, ArrayList<ContactsModal> contactsModalArrayList) {
			this.context = context;
			this.contactsModalArrayList = contactsModalArrayList;
	  }
	  
	  
	  
	  @NonNull
	  @Override
	  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return new ContactsRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.contacts_rv_items, parent, true));
	  }
	  
	  @Override
	  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
			ContactsModal modal = contactsModalArrayList.get(position);
		 
			//뷰홀더에 텍스트 설정
			holder.contactTextView.setText(modal.getContactName());
		 
			ColorGenerater generater = new ColorGenerater.MATERIAL;
			
	  }
	  
	  @Override
	  public int getItemCount() {
			return 0;
	  }
	  
	  public class ViewHolder extends RecyclerView.ViewHolder {
			private ImageView contactImageView;
			private TextView contactTextView;
			
			public ViewHolder(@NonNull View itemView) {
				  super(itemView);
				  
				  contactImageView =
			}
	  }
	  
}

























