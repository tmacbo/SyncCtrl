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

import com.alibaba.fastjson.JSON;
import com.alibaba.model.CollectNoSong;
import com.alibaba.model.RecommendModel;
import com.irunsin.activity.syncctrl.HomeActivity;
import com.irunsin.controller.adapter.XiamiRecommendAdapter;
import com.irunsin.controller.sys.Global;
import com.irunsin.controller.view.CustomListView;
import com.irunsin.controller.view.CustomListView.OnLoadMoreListener;
import com.irunsin.controller.view.CustomListView.OnRefreshListener;
import com.irunsin.syncctrl.R;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.TopParameters;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;

/**
 * 精选集
 * @author Administrator
 *
 */
public class XiamiRecommendFragment extends Fragment {

	private String TAG = "XiamiRecommendFragment";
	private View rootView;
	private HomeActivity ma;

	private CustomListView listView;
	public ArrayList<CollectNoSong> listdata;
	private XiamiRecommendAdapter xiamirecommendAdapter;
	private TopAndroidClient client = TopAndroidClient
			.getAndroidClientByAppKey("23014544");
	
	private int page = 1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_xiamirecommend,
				container, false);

		initData();
		findViews();
		addAction();

		/**
		 * 音乐源界面间的跳转
		 */
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if(listdata ==null){		
			listdata = new ArrayList<CollectNoSong>();			
		}
		xiamirecommendAdapter.setMyList(listdata);
		loadXiamiData(1);
		super.onResume();
	}
	public void initData() {
		listdata = new ArrayList<CollectNoSong>();		
	}

	public void findViews() {
		listView = (CustomListView) rootView
				.findViewById(R.id.xiami_recommend_list);
		xiamirecommendAdapter = new XiamiRecommendAdapter(listdata,
				getActivity(), listView);
		listView.setAdapter(xiamirecommendAdapter);
	}

	public void addAction() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int position = arg2-1;
//				Fragment fragment = new XiamiRecommentInfoFragment();
//				Bundle bundle = new Bundle();
//				bundle.putLong("list_id", listdata.get(position).getList_id());
//				bundle.putString("collect_name", listdata.get(position)
//						.getCollect_name());
//				fragment.setArguments(bundle);
//				switchFragment(fragment, Global.CHILD_FRAGMENT_TAG);
			}
		});

		listView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO 下拉刷新
				Log.e(TAG, "精选集数据下拉刷新onRefresh");
				loadXiamiData(200);
			}
		});
		
		listView.setOnLoadListener(new OnLoadMoreListener() {			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				page++;
				loadXiamiData(page);
			}
		});
	}

	private void switchFragment(Fragment fragment, String tag) {
//		if (getActivity() == null) {
//			return;
//		}
//		if (getActivity() instanceof MainActivity) {
//
//			ma = (MainActivity) getActivity();
//			ma.switchFragment(fragment);
//		}
	}

	public Handler refreshrecommendHandler = new Handler() {
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				xiamirecommendAdapter.setMyList(listdata);
				break;
			case 2:
				xiamirecommendAdapter.setMyList(listdata);
				listView.onRefreshComplete();
				break;
			case 3:
				xiamirecommendAdapter.setMyList(listdata);
				listView.onLoadMoreComplete();
				break;
			default:
				break;
			}
		}
	};

	// 参数w代表首次获取数据还是下来刷新获取
	public void loadXiamiData(final int w) {
		TopParameters params = new TopParameters();
		params.setMethod("alibaba.xiami.api.collect.recommend.get");// API方法
		if(w ==200){
			params.addParam("page", 1+"");
			page =1;
		}else{
			params.addParam("page", w+"");
		}
		params.addParam("limit", "15");
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
				RecommendModel r1 = JSON.parseObject(j1.toString(),
						RecommendModel.class);
				if(w ==200){
					listdata = (ArrayList<CollectNoSong>) r1.getData().getCollects();
				}else{
					List<CollectNoSong> list = r1.getData().getCollects();
					for (int i = 0; i < list.size(); i++) {
						listdata.add(list.get(i));
					}
				}
				
				Message msg = Message.obtain();
				msg.obj = true;
				if(w ==1){
					msg.what = w;
				}else if(w == 200){
					msg.what = 2;	
				}else{
					msg.what =3;
				}				
				refreshrecommendHandler.sendMessage(msg);
			}

			@Override
			public void onError(ApiError error) {

			}

			@Override
			public void onException(Exception e) {

			}
		}, true);
	}
}
