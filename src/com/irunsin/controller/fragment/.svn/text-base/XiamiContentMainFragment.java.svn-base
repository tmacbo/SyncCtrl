package com.irunsin.controller.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.model.CollectNoSong;
import com.alibaba.model.Radio;
import com.irunsin.activity.syncctrl.HomeActivity;
import com.irunsin.controller.indicator.TabPageIndicator;
import com.irunsin.controller.sys.Global;
import com.irunsin.syncctrl.R;
import com.taobao.top.android.TopAndroidClient;

public class XiamiContentMainFragment extends Fragment {

	private static final String[] TITLE = new String[] { "推荐", "榜单", "精选集",
			"电台" };//

	private String TAG = "XiamiContentMainFragment";
	private View parentView;
	private ViewPager m_vp;
	// 页面列表
	private ArrayList<Fragment> fragmentList;
	// 标题列表
	ArrayList<String> titleList = new ArrayList<String>();
	private XiamiNewsFragment mfragment1;
	private XiamiRankFragment mfragment2;
	private XiamiRecommendFragment mfragment3;
	private XiamiRadioFragment mfragment4;
	private HomeActivity ma;
	private TopAndroidClient client = TopAndroidClient
			.getAndroidClientByAppKey("23014544");

	private List<Radio> list;
	private List<CollectNoSong> listdatarecommend;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Global.debug("activity 进入xiamicontentmainfragment oncreat");
		parentView = inflater.inflate(R.layout.fragment_xiamicontent_main,
				container, false);
		// ViewPager的adapter
		ma = (HomeActivity) getActivity();
		FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(
				ma.getSupportFragmentManager());
		ViewPager pager = (ViewPager) parentView.findViewById(R.id.viewpager);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(3);

		// 实例化TabPageIndicator然后设置ViewPager与之关联
		TabPageIndicator indicator = (TabPageIndicator) parentView
				.findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		fragmentList = new ArrayList<Fragment>();
		mfragment1 = new XiamiNewsFragment();
		mfragment2 = new XiamiRankFragment();
		mfragment3 = new XiamiRecommendFragment();
		mfragment4 = new XiamiRadioFragment();
		fragmentList.add(mfragment1);
		fragmentList.add(mfragment2);
		fragmentList.add(mfragment3);
		fragmentList.add(mfragment4);
		

		return parentView;

	}

	/**
	 * ViewPager适配器
	 * 
	 * @author tmacbo
	 * 
	 */
	class TabPageIndicatorAdapter extends FragmentPagerAdapter {
		public TabPageIndicatorAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			
			return fragmentList.get(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLE[position % TITLE.length];
		}

		@Override
		public int getCount() {

			return TITLE.length;
		}
	}

}
