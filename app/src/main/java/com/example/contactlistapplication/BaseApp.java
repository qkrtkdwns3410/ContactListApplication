package com.example.contactlistapplication;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import android.app.Application;

/**
 *packageName    : com.example.contactlistapplication
 * fileName       : BaseApp
 * author         : letscombine
 * date           : 2022-03-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-03-03        letscombine       최초 생성
 */
public class BaseApp extends Application {
	  @Override
	  public void onCreate() {
			super.onCreate();
			Logger.addLogAdapter(new AndroidLogAdapter());
	  }
}

























