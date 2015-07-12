package com.irunsin.controller.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irunsin.controller.db.DbHelper;
import com.irunsin.controller.model.Mp3Info;
import com.irunsin.syncctrl.R;

public class CollectionMusicFragment extends Fragment{

	private View rootView;
	private List<Mp3Info> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_collection, container,
				false);
		
		initData();
		findViews();
		addAction();
		return rootView;
	}
	
	
	public void initData(){
		list = DbHelper.getInstence(getActivity()).getFavoriteMusic();
	}
	
	public void findViews(){
		
	}
	
	public void addAction(){
		
	}
}
