package com.irunsin.controller.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.irunsin.activity.syncctrl.HomeActivity;
import com.irunsin.controller.adapter.ListMusicAdpater;
import com.irunsin.controller.db.DbHelper;
import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.util.IrunSinApplication;
import com.irunsin.controller.util.Mp3Util;
import com.irunsin.syncctrl.R;

public class RecentlyListenMusicFragment extends Fragment {
	private View rootView;

	private String TAG = "RecentlyListenMusicFragment";
	private ListView listView;

	private List<Mp3Info> listdata = new ArrayList<Mp3Info>();


	private IrunSinApplication irunsin;
	private ImageView recentlyBack;
	private HomeActivity ma;
	private LinearLayout oneDelete;
	
	private ListMusicAdpater recentlyAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_recently, container,
				false);

		initData();
		findView();
		addAction();
		return rootView;
	}

	public void initData() {
		irunsin = (IrunSinApplication) getActivity().getApplicationContext();
		listdata = DbHelper.getInstence(getActivity()).getallRecentlyMusic();
		Log.i(TAG, "查询出已经记录至于最近听的音乐中的音乐  数量 为 = " + listdata.size());
		recentlyAdapter = new ListMusicAdpater(listdata,
				getActivity());
		
	}


	public void findView() {
		listView = (ListView) rootView.findViewById(R.id.recentlysong_list);
		listView.setAdapter(recentlyAdapter);
		recentlyBack = (ImageView) rootView
				.findViewById(R.id.recently_set_back);
		oneDelete = (LinearLayout) rootView
				.findViewById(R.id.recentlymusic_play);
	}

	public void addAction() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Mp3Util.play(getActivity(), arg2, listdata);
			}

		});

		recentlyBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});

		oneDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 删除所有的最近听的
				DbHelper.getInstence(getActivity()).deleteAllRecently();
				listdata = DbHelper.getInstence(getActivity())
						.getallRecentlyMusic();
				recentlyAdapter.setMyList(listdata);
			}
		});
	}

	
}
