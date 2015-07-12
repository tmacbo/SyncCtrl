package com.irunsin.controller.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.model.DailySongModel;
import com.alibaba.model.NewAlbumsModel;
import com.alibaba.model.RecommendSong;
import com.alibaba.model.SimpleArtisModel;
import com.alibaba.model.SimpleArtist;
import com.alibaba.model.StandardAlbum;
import com.irunsin.activity.syncctrl.HomeActivity;
import com.irunsin.controller.adapter.XiamiDailySongAdapter;
import com.irunsin.controller.adapter.XiamiNewAlbumAdapter;
import com.irunsin.controller.adapter.XiamiRecommendArtistAdapter;
import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.service.MusicPlayService;
import com.irunsin.controller.util.IntegerUtil;
import com.irunsin.controller.util.IrunSinApplication;
import com.irunsin.syncctrl.R;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.TopParameters;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;

public class XiamiNewsFragment extends Fragment {

	private String TAG = "NewsFragment";
	private View parentView;

	private GridView xiami_album;

	private ListView xiami_daily;

	private ListView xiami_artist;
	private TopAndroidClient client = TopAndroidClient
			.getAndroidClientByAppKey("23014544");

	private List<StandardAlbum> listdata;

	private List<RecommendSong> listdatarecommend;

	private ArrayList<SimpleArtist> listdataartist;
	private XiamiNewAlbumAdapter xiaminewalbumsAdapter;
	private XiamiRecommendArtistAdapter xiamirecommendartistAdapter;
	private XiamiDailySongAdapter xiamidailysongAdapter;
	private HomeActivity ma;

	private IrunSinApplication irunsin;

	private TextView moreAlbum;

	private TextView ingroup;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.fragment_xiami_main, container, false);
		initData();
		findViews();
		addAction();
		return parentView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public void initData() {
		listdata = new ArrayList<StandardAlbum>();
		listdatarecommend = new ArrayList<RecommendSong>();
		listdataartist = new ArrayList<SimpleArtist>();
		irunsin = (IrunSinApplication) getActivity().getApplicationContext();

		// 新专辑
		TopParameters params = new TopParameters();
		params.setMethod("alibaba.xiami.api.rank.newAlbum.get");// API方法
		params.addParam("limit", 30 + "");
		params.addParam("type", "all");

		client.api(params, null, new TopApiListener() {

			@Override
			public void onComplete(JSONObject json) {
				JSONObject j1 = null;
				JSONObject j2 = null;
				try {
					j1 = (JSONObject) json.get("user_get_response");
					j2 = (JSONObject) j1.get("data");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				NewAlbumsModel r1 = JSON.parseObject(j2.toString(),
						NewAlbumsModel.class);
				listdata.clear();
				for (int i = 0; i < r1.getAlbums().size(); i++) {
					if (listdata.size() < 6) {
						if (r1.getAlbums().get(i).getSong_count() > 4) {
							listdata.add(r1.getAlbums().get(i));
						}
					} else {
						break;
					}
				}
				Message msg = Message.obtain();
				msg.obj = true;
				msg.what = 1;
				refreshLocalHandler.sendMessage(msg);
			}

			@Override
			public void onError(ApiError error) {

			}

			@Override
			public void onException(Exception e) {
				Log.i(TAG, "异常了 = " + e.getMessage());
				Intent intent = new Intent();
				intent.setAction("com.mp3.netexception");
				getActivity().sendBroadcast(intent);
			}
		}, true);

		// 精选集
		TopParameters params1 = new TopParameters();
		params1.setMethod("alibaba.xiami.api.recommend.dailySongs.get");// API方法
		params1.addParam("limit", 10 + "");
		client.api(params1, null, new TopApiListener() {

			@Override
			public void onComplete(JSONObject json) {
				JSONObject j1 = null;
				JSONObject j2 = null;
				try {
					j1 = (JSONObject) json.get("user_get_response");
					j2 = (JSONObject) j1.get("data");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DailySongModel r = JSON.parseObject(j2.toString(),
						DailySongModel.class);
				listdatarecommend = r.getSongs();
				Message msg = Message.obtain();
				msg.obj = true;
				msg.what = 2;
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

		//推荐艺人数据
		loadrecommendArtist();
				
	}

	public void findViews() {
		xiami_album = (GridView) parentView
				.findViewById(R.id.list_gridView_album);
		xiami_daily = (ListView) parentView.findViewById(R.id.list_dailysong);
		xiami_artist = (ListView) parentView
				.findViewById(R.id.list_gridView_hotsong);
		xiaminewalbumsAdapter = new XiamiNewAlbumAdapter(listdata,
				getActivity(), xiami_album);
		xiami_album.setAdapter(xiaminewalbumsAdapter);

		xiamidailysongAdapter = new XiamiDailySongAdapter(listdatarecommend,
				getActivity(), xiami_daily);
		xiami_daily.setAdapter(xiamidailysongAdapter);
		xiamirecommendartistAdapter = new XiamiRecommendArtistAdapter(listdataartist, getActivity(), xiami_artist);
		xiami_artist.setAdapter(xiamirecommendartistAdapter);

		moreAlbum = (TextView) parentView.findViewById(R.id.more_newalbum);
		ingroup = (TextView) parentView.findViewById(R.id.inagroup);
	}

	public void addAction() {
		xiami_album.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				Long albumsid = listdata.get(arg2).getAlbum_id();
//				Fragment fragment = new XiamiNewAblumsSongInfoFragment();
//				Bundle bundle = new Bundle();
//				bundle.putString("describe", listdata.get(arg2).getAlbum_name());
//				bundle.putLong("albumns_id", albumsid);
//
//				fragment.setArguments(bundle);
//				switchFragment(fragment);
			}
		});

		xiami_daily.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				List<Mp3Info> mp3list = new ArrayList<Mp3Info>();
				// TODO Auto-generated method stub
				for (int i = 0; i < listdatarecommend.size(); i++) {
					Mp3Info mp3info = new Mp3Info();
					mp3info.setAlbumid(listdatarecommend.get(i).getAlbum_id());
					mp3info.setArtist(listdatarecommend.get(i).getSingers());
					mp3info.setDisplay_name(listdatarecommend.get(i)
							.getSong_name());
					mp3info.setDuration(1000);
					mp3info.setFilepath(listdatarecommend.get(i)
							.getListen_file());
					mp3info.setMp3id(listdatarecommend.get(i).getSong_id() + "");
					mp3info.setLoOnFlag(1);
					mp3info.setMp3Name(listdatarecommend.get(i).getSong_name());
					mp3info.setMp3Size("12");
					mp3info.setLogo(listdatarecommend.get(i).getLogo());
					mp3list.add(mp3info);
				}
				irunsin.setListmp3(mp3list);
				play(getActivity(), arg2);
			}

		});
		xiami_artist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// 跳转推荐艺人的专辑
//				Fragment fragment = new XiamiRecommendArtistAlbumsFragment();
//				Bundle bundle = new Bundle();
//				bundle.putLong("artistId", listdataartist.get(arg2).getArtist_id());
//				bundle.putString("artistId_albums", listdataartist.get(arg2).getArtist_name());
//				fragment.setArguments(bundle);
//				switchFragment(fragment);
			}
		});

		moreAlbum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转更多的 虾米推荐专辑页
//				Fragment fragment = new XiamiNewAblumsMoreFragment();
//				switchFragment(fragment);
			}
		});

		ingroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadrecommendArtist();
			}
		});
	}
	
	public void loadrecommendArtist(){
		// 推荐艺人
		TopParameters params2 = new TopParameters();
		params2.setMethod("alibaba.xiami.api.artist.promotions.get");// API方法
		params2.addParam("page", 1+"");
		params2.addParam("limit", "20");
		client.api(params2, null, new TopApiListener() {

			@Override
			public void onComplete(JSONObject json) {
				JSONObject j1 = null;
				try {
					j1 = (JSONObject) json.get("user_get_response");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				SimpleArtisModel sim = JSON.parseObject(j1.toString(),
						SimpleArtisModel.class);
				List<SimpleArtist> list = sim.getData();
				//随机获取四个
				List<Integer> zs = IntegerUtil.getrandom(list.size());
				listdataartist.clear();
				for (int i = 0; i < zs.size(); i++) {
					listdataartist.add(list.get(zs.get(i)));
				}							
				Message msg = Message.obtain();
				msg.obj = true;
				msg.what = 3;
				refreshLocalHandler.sendMessage(msg);
			}

			@Override
			public void onError(ApiError error) {
			}

			@Override
			public void onException(Exception e) {

			}
		}, true);
	}

	private Handler refreshLocalHandler = new Handler() {
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				xiaminewalbumsAdapter.setMyList(listdata);
				break;
			case 2:
				xiamidailysongAdapter.setMyList(listdatarecommend);
				break;
			case 3:
				xiamirecommendartistAdapter.setMyList(listdataartist);				
				break;
			default:
				break;
			}
		}
	};

//	private void switchFragment(Fragment fragment) {
//		if (getActivity() == null) {
//			return;
//		}
//		if (getActivity() instanceof MainActivity) {
//
//			ma = (MainActivity) getActivity();
//			if (fragment instanceof XiamiNewAblumsMoreFragment) {
//				ma.newablumsmoreFragment = (XiamiNewAblumsMoreFragment) fragment;
//			}else if(fragment instanceof XiamiRecommendArtistAlbumsFragment){
//				ma.recommendartistalbumsFragment = (XiamiRecommendArtistAlbumsFragment) fragment;
//			}
//			ma.switchFragment(fragment);// (fragment,
//										// Global.CHILD_FRAGMENT_TAG);
//		}
//	}

	/*
	 * 开始播放
	 */
	public void play(Context context, int position) {
		irunsin.setPosition(position);
		Intent intent = new Intent();
		intent.putExtra("position", position);
		intent.putExtra("FLAG", 0); // 代表切歌
		intent.setClass(context, MusicPlayService.class);
		context.startService(intent);
	}
}
