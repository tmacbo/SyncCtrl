package com.irunsin.controller.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.model.RecommendSong;
import com.irunsin.controller.util.imageloader.ImageLoader;
import com.irunsin.syncctrl.R;

public class XiamiDailySongAdapter extends BaseAdapter {

	private List<RecommendSong> list;
	private Context context;
	private ListView listView;

	public XiamiDailySongAdapter(List<RecommendSong> list, Context context,
			ListView listView) {
		this.list = list;
		this.context = context;
		this.listView = listView;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		XiamiDailyListViewHolder holder;

		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.list_xiami_recomment_info, null);
			holder = new XiamiDailyListViewHolder();
			convertView.setTag(holder);

			holder.Image = (ImageView) convertView
					.findViewById(R.id.recommend_info_image);
			holder.mp3Name = (TextView) convertView
					.findViewById(R.id.recommend_info_mp3name);
			holder.singer = (TextView) convertView
					.findViewById(R.id.recommend_info_singer);

		} else {
			holder = (XiamiDailyListViewHolder) convertView.getTag();
		}

		holder.mp3Name.setText(list.get(position).getSong_name());
		holder.singer.setText(list.get(position).getSingers());
		String imageURL = list.get(position).getLogo();
		holder.Image.setTag(imageURL);
		ImageLoader.getInstances().displayImage(imageURL, holder.Image);
		return convertView;
	}

	class XiamiDailyListViewHolder {
		ImageView Image;
		TextView mp3Name;
		TextView singer;
	}

	public void setMyList(List<RecommendSong> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}
}
