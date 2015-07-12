package com.irunsin.controller.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.model.RankList;
import com.alibaba.model.RankModel;
import com.irunsin.activity.syncctrl.HomeActivity;
import com.irunsin.controller.adapter.XiamiRankExpandAdapter;
import com.irunsin.syncctrl.R;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.TopParameters;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;

/**
 * 虾米榜单
 * @author Administrator
 *
 */
public class XiamiRankFragment extends Fragment{

	private View rootView;
    private ExpandableListView mListView = null;
    private XiamiRankExpandAdapter mAdapter = null;
    private List<RankList> listdata = new ArrayList<RankList>();
	private HomeActivity ma;

	private TopAndroidClient client = TopAndroidClient
			.getAndroidClientByAppKey("23014544");
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_rank, container,
				false);		
		
		initData();
		findViews();
		addAction();

		/**
		 * 音乐源界面间的跳转
		 */
		return rootView;
	}
	
	public void initData(){
		loadrankdata();
	}
	
	public void findViews(){
		mListView = (ExpandableListView) rootView.findViewById(R.id.rank_list_view);
		mAdapter = new XiamiRankExpandAdapter(getActivity(), listdata, mListView);
		mListView.setAdapter(mAdapter);		
	}
	
	public void addAction(){
		
//		mListView.setOnChildClickListener(new OnChildClickListener() {
//				
//				@Override
//				public boolean onChildClick(ExpandableListView parent, View v,
//						int groupPosition, int childPosition, long id) {
//					// TODO Auto-generated method stub
//					Fragment fragment = new XiamiRanksongFragment();
//					Bundle bundle = new Bundle();
//					bundle.putString("describe", listdata.get(groupPosition).getItems().get(childPosition).getTitle());
//					bundle.putString("rankcontent", listdata.get(groupPosition).getItems().get(childPosition).getType());
//					fragment.setArguments(bundle);
//					switchFragment(fragment);
//					return true;
//				}
//			});
	}
	
	public void loadrankdata(){
		// 榜单查询
		TopParameters params2 = new TopParameters();
		params2.setMethod("alibaba.xiami.api.rank.index.get");// API方法
		client.api(params2, null, new TopApiListener() {

			@Override
			public void onComplete(JSONObject json) {
				JSONObject j1 = null;
				try {
					j1 = (JSONObject) json.get("user_get_response");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				RankModel r1 = JSON.parseObject(j1.toString(), RankModel.class);
				listdata = r1.getData();
				Message msg = Message.obtain();
				msg.obj = true;
				msg.what = 1;
				refreshrankHandler.sendMessage(msg);
			}

			@Override
			public void onError(ApiError error) {
			}

			@Override
			public void onException(Exception e) {

			}
		}, true);
		
	

	}
//	private void switchFragment(Fragment fragment) {		
//		if (getActivity() == null) {
//			return;
//		}
//		if (getActivity() instanceof MainActivity) {
//
//			ma = (MainActivity) getActivity();
//			if(fragment instanceof XiamiNewAblumsMoreFragment){
//				ma.newablumsmoreFragment = (XiamiNewAblumsMoreFragment) fragment;
//			}
//			ma.switchFragment(fragment);// (fragment,
//										// Global.CHILD_FRAGMENT_TAG);
//		}
//	}
	private Handler refreshrankHandler = new Handler() {
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				mAdapter.setMyList(listdata);
				int groupCount = mListView.getCount();
			    for (int i=0; i<groupCount; i++) {
				   mListView.expandGroup(i);
			    }
				break;
			default:
				break;
			}
		}
	};
}
