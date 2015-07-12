package com.irunsin.controller.fragment;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.irunsin.controller.activity.ScannerLocalMusicActivity;
import com.irunsin.controller.adapter.LocalMusicAdapter;
import com.irunsin.controller.db.DbHelper;
import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.util.Mp3Util;
import com.irunsin.controller.view.LocalMusicRightListView;
import com.irunsin.controller.view.LocalMusicRightListView.OnTouchingLetterChangedListener;
import com.irunsin.syncctrl.R;

public class LocalMusicFragment extends Fragment {

	private View parentView;

	private List<Mp3Info> localList;// 本地音乐列表
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private ImageButton scanner;
	private LocalMusicAdapter localmusicAdpter;

	private LocalMusicRightListView musicRightView;

	private ListView localMusic;
	private ImageButton localmuiscBack;
	private LinearLayout localmusic_play;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		parentView = inflater.inflate(R.layout.fragment_local_music, container,
				false);

		initData();
		findViews();
		addAction();
		return parentView;
	}

	public void initData() {
		// 先查询本地音乐
		localList = DbHelper.getInstence(getActivity()).getLocalMusicList();
		alphaIndexer = new HashMap<String, Integer>();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.mp3.begin");
		getActivity().registerReceiver(musicRefeshReceiver, filter);
	}

	public void findViews() {
		localmuiscBack = (ImageButton) parentView
				.findViewById(R.id.musiclist_set_back);
		localMusic = (ListView) parentView.findViewById(R.id.mysong_list);
		localmusicAdpter = new LocalMusicAdapter(getActivity(), localList);
		localMusic.setAdapter(localmusicAdpter);
		localmusic_play = (LinearLayout) parentView
				.findViewById(R.id.localmusic_play);
		scanner = (ImageButton) parentView.findViewById(R.id.newmp3_scanner);

		musicRightView = (LocalMusicRightListView) parentView
				.findViewById(R.id.MyLetterListView01);
	}

	public void addAction() {
		localmusic_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Mp3Util.play(getActivity(), 0, localList);
			}
		});
		localMusic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Mp3Util.play(getActivity(), arg2, localList);
			}

		});

		localMusic.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				final boolean state = true;
				new AlertDialog.Builder(getActivity())
						.setTitle("是否确定删除此歌曲")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Mp3Info mp3 = localList.get(arg2);
										try {
											Mp3Util.RealdeletSong(
													getActivity(),
													Integer.parseInt(mp3
															.getMp3id()), mp3
															.getKeyId());
											List<Mp3Info> list = DbHelper
													.getInstence(getActivity())
													.getLocalMusicList();
											localList = list;
											localmusicAdpter.setMyList(list);
										} catch (Exception e) {
										}
									}
								}).setNegativeButton("取消", null).show();
				return state;
			}
		});
		localmuiscBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		scanner.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ScannerLocalMusicActivity.class);
				startActivityForResult(intent, 1);
			}
		});

		musicRightView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());

	}

	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				localMusic.setSelection(position);
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 12) {
			localList = DbHelper.getInstence(getActivity())
					.getLocalMusicList();
			localmusicAdpter.setMyList(localList);
		} 
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(musicRefeshReceiver);
	}

	protected BroadcastReceiver musicRefeshReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 收到播放的广播的时候 则开启 进度条线程
			if (action.endsWith("com.mp3.begin")) {
				localmusicAdpter.notifyDataSetChanged();
			}
		}
	};
}
