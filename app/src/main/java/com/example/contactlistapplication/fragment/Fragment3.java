package com.example.contactlistapplication.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contactlistapplication.R;

public class Fragment3 extends Fragment {
	  
	  @Nullable
	  @Override
	  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_share, container, false);
			
			initUI(rootView);
			
			return rootView;
	  }
	  
	  private void initUI(ViewGroup rootView) {
			
	  }
}