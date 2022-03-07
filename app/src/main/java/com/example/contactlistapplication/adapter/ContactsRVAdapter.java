package com.example.contactlistapplication.adapter;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.load.model.stream.QMediaStoreUriLoader;
import com.example.contactlistapplication.activity.ContactDetailActivity;
import com.example.contactlistapplication.DTO.ContactsModal;
import com.example.contactlistapplication.R;
import com.orhanobut.logger.Logger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
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
public class ContactsRVAdapter extends RecyclerView.Adapter<ContactsRVAdapter.ViewHolder> implements SectionIndexer {
	  
	  private Context context;
	  private ArrayList<ContactsModal> contactsModalArrayList;
	  private ArrayList<Integer> mSectionPositions;
	  
	  public ContactsRVAdapter(Context context, ArrayList<ContactsModal> contactsModalArrayList) {
			this.context = context;
			
			Collections.sort(contactsModalArrayList);
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
			
			if (modal.getUserImage() != null) {
				  holder.contactImageView.setImageBitmap(modal.getUserImage());
			} else {
				  holder.contactImageView.setImageDrawable(drawable2);
				  
			}
			//TextDrawable 설정을 해당 이미지뷰에 설정합니다.
			
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				  @Override
				  public void onClick(View view) {
						Intent intent = new Intent(context, ContactDetailActivity.class);
						intent.putExtra("image", modal.getUserImage());
						intent.putExtra("name", modal.getUserName());
						intent.putExtra("contact", modal.getContactName());
						intent.putExtra("myRealID", modal.getContactId());
						context.startActivity(intent);
				  }
			});
	  }
	  
	  @Override
	  public int getItemCount() {
			//아이템들의 개수를 들고옵니다.
			return contactsModalArrayList.size();
	  }
	  
	  public class ViewHolder extends RecyclerView.ViewHolder {
			//리사이클러뷰 뷰홀더 설정
			private final ImageView contactImageView;
			private final TextView contactTextView;
			
			public ViewHolder(@NonNull View itemView) {
				  super(itemView);
				  
				  contactImageView = itemView.findViewById(R.id.idIVContact);
				  contactTextView = itemView.findViewById(R.id.idTVContactName);
			}
	  }
	  
	  @Override
	  public Object[] getSections() {
			Logger.d("getSections() called");
			//가나다 알파벳 섹션 지정하기
			
			List<String> sections = new ArrayList<>();
			mSectionPositions = new ArrayList<>();
			
			for (int index = 0, size = contactsModalArrayList.size(); index < size; index++) {
				  String section = String.valueOf(contactsModalArrayList.get(index).getUserName().charAt(0)).toUpperCase();
				  char sectionCh = section.charAt(0);
				  Logger.d("sectionCh  " + sectionCh);
				  
				  section = getInitialSound(String.valueOf(sectionCh));
				  
				  if (!sections.contains(section)) {
						sections.add(section);
						mSectionPositions.add(index);
				  }
			}
			return sections.toArray(new String[0]);
	  }
	  
	  public String getInitialSound(String text) {
			
			// 초성 19자
			final String[] initialChs = {
				"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ",
				"ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ",
				"ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ",
				"ㅋ", "ㅌ", "ㅍ", "ㅎ"
			};
			
			// 중성 21자
			final String[] medialChs = {
				"ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ",
				"ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ",
				"ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ",
				"ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ",
				"ㅣ"
			};
			
			// 종성 없는 경우 포함하여 28자
			final String[] finalChs = {
				" ", "ㄱ", "ㄲ", "ㄳ", "ㄴ",
				"ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ",
				"ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ",
				"ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ",
				"ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ",
				"ㅌ", "ㅍ", "ㅎ"
			};
			
			// 19: 초성
			// 21: 중성
			// 28: 종성
			if (text.length() > 0) {
				  char chName = text.charAt(0);
				  if (chName >= 0xAC00 && chName <= 0xD7A3) {  // 0xAC00(가) ~ 0xD7A3(힣)
						
						int uniVal = chName - 0xAC00;
						int initialCh = ((uniVal) / (21 * 28)); // 초성 index
						System.out.println(initialChs[initialCh]);
						
						// 중성
						int medialCh = ((uniVal % (28 * 21)) / 28);
						System.out.println(medialChs[medialCh]);
						
						// 종성
						int finalCh = ((uniVal % 28));
						System.out.println(finalChs[finalCh]);
						
						return initialChs[initialCh];
				  } else {
						return "" + chName;
				  }
			}
			
			return "";
	  }
	  
	  @Override
	  public int getPositionForSection(int sectionIndex) {
			return mSectionPositions.get(sectionIndex);
	  }
	  
	  @Override
	  public int getSectionForPosition(int position) {
			return 0;
	  }
}

























