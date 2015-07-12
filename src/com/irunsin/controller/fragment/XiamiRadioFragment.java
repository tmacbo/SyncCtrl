package com.irunsin.controller.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.model.Radio;
import com.alibaba.model.RadioModel;
import com.alibaba.model.RadioSongMode;
import com.alibaba.model.StandardSong;
import com.irunsin.controller.adapter.XiamiRadioAdapter;
import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.util.IrunSinApplication;
import com.irunsin.controller.util.Mp3Util;
import com.irunsin.syncctrl.R;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.TopParameters;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;

public class XiamiRadioFragment extends Fragment {

	private String TAG = "XiamiRadioFragment";

	private View rootView;

	private ListView listView;

	private IrunSinApplication irunsin;
	public List<Radio> list;
	private XiamiRadioAdapter xiamiradioAdapter;
	private TopAndroidClient client = TopAndroidClient
			.getAndroidClientByAppKey("23014544");

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_xiami_radiao, container,
				false);

		initData();
		findViews();
		addAction();

		/**
		 * 音乐源界面间的跳转
		 */
		return rootView;
	}

	public void initData() {

		irunsin = (IrunSinApplication) getActivity().getApplicationContext();
		list = new ArrayList<Radio>();

		TopParameters params = new TopParameters();
		params.setMethod("alibaba.xiami.api.radio.info");// API方法
		client.api(params, null, new TopApiListener() {

			@Override
			public void onComplete(JSONObject json) {
				JSONObject j1 = null;
				try {
					j1 = (JSONObject) json.get("user_get_response");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				RadioModel r1 = JSON.parseObject(j1.toString(),
						RadioModel.class);
				list = new ArrayList<Radio>();
				for (int i = 0; i < r1.getData().size(); i++) {
					for (int j = 0; j < r1.getData().get(i).getRadios().size(); j++) {
						list.add(r1.getData().get(i).getRadios().get(j));
					}
				}
				Message msg = Message.obtain();
				msg.obj = true;
				msg.what = 1;
				refreshLocalHandler.sendMessage(msg);
			}

			@Override
			public void onError(ApiError error) {
				Log.e(TAG, error.toString());
			}

			@Override
			public void onException(Exception e) {

			}
		}, true);
	}

	public void findViews() {
		listView = (ListView) rootView.findViewById(R.id.xiami_radio_list);
		xiamiradioAdapter = new XiamiRadioAdapter(list, getActivity(), listView);
		listView.setAdapter(xiamiradioAdapter);
	}

	public void addAction() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Long type = list.get(arg2).getType();
				Long oid = list.get(arg2).getId();

				TopParameters params = new TopParameters();
				params.setMethod("alibaba.xiami.api.radio.songs.get");// API方法
				params.addParam("type", type + "");
				params.addParam("oid", oid + "");
				params.addParam("limit", "20");
				client.api(params, null, new TopApiListener() {
					@Override
					public void onComplete(JSONObject json) {
						JSONObject j1 = null;
						try {
							j1 = (JSONObject) json.get("user_get_response");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						RadioSongMode r1 = JSON.parseObject(j1.toString(),
								RadioSongMode.class);
						List<StandardSong> listdata = r1.getData();
						List<Mp3Info> mp3list = new ArrayList<Mp3Info>();
						for (int i = 0; i < listdata.size(); i++) {
							Mp3Info mp3info = new Mp3Info();
							mp3info.setAlbumid(listdata.get(i).getAlbum_id());
							mp3info.setArtist(listdata.get(i).getSingers());
							mp3info.setDisplay_name(listdata.get(i).getTitle());
							mp3info.setDuration(1000);
							mp3info.setFilepath(listdata.get(i)
									.getListen_file());
							mp3info.setMp3id(listdata.get(i).getSong_id() + "");
							mp3info.setLoOnFlag(1);
							mp3info.setMp3Name(listdata.get(i).getSong_name());
							mp3info.setMp3Size("12");
							mp3info.setLogo(listdata.get(i).getLogo());
							mp3list.add(mp3info);
						}						
						Mp3Util.play(getActivity(), 0, mp3list);
					}

					@Override
					public void onError(ApiError error) {
						Log.e(TAG, "error" + error.toString());
					}

					@Override
					public void onException(Exception e) {
						Log.i(TAG, "Exception");
					}
				}, true);

			}
		});

	}

	

	public Handler refreshLocalHandler = new Handler() {
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				xiamiradioAdapter.setMyList(list);

				break;
			default:
				break;
			}
		}
	};
}
