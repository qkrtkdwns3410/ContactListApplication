package com.example.contactlistapplication.adapter;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Random;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.load.model.stream.QMediaStoreUriLoader;
import com.example.contactlistapplication.activity.ContactDetailActivity;
import com.example.contactlistapplication.DTO.ContactsModal;
import com.example.contactlistapplication.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			View itemView = inflater.inflate(R.layout.contacts_rv_items, parent, false);
			return new ViewHolder(itemView);
	  }
	  
	  public void filterList(ArrayList<ContactsModal> filterllist) {
			// on below line we are passing filtered
			// array list in our original array list
			contactsModalArrayList = filterllist;
			notifyDataSetChanged();
	  }
	  
	  @Override
	  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
			ContactsModal modal = contactsModalArrayList.get(position);
			
			//뷰홀더에 텍스트 설정
			holder.contactTextView.setText(modal.getUserName());
			
			Random rnd = new Random();
			int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
			
			TextDrawable drawable2 = TextDrawable.builder()
				.beginConfig()
				.width(100)
				.height(100)
				.endConfig()
				.buildRound(modal.getUserName().substring(0, 1), color);
			
			//TextDrawable 설정을 해당 이미지뷰에 설정합니다.
			holder.contactImageView.setImageDrawable(drawable2);
			
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				  @Override
				  public void onClick(View view) {
						Intent intent = new Intent(context, ContactDetailActivity.class);
						intent.putExtra("name", modal.getUserName());
						intent.putExtra("contact", modal.getContactName());
						
						context.startActivity(intent);
				  }
			});
	  }
	  
	  @Override
	  public int getItemCount() {
			return contactsModalArrayList.size();
	  }
	  
	  public class ViewHolder extends RecyclerView.ViewHolder {
			private final ImageView contactImageView;
			private final TextView contactTextView;
			
			public ViewHolder(@NonNull View itemView) {
				  super(itemView);
				  
				  contactImageView = itemView.findViewById(R.id.idIVContact);
				  contactTextView = itemView.findViewById(R.id.idTVContactName);
			}
	  }
	  
}

























