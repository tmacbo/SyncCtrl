package com.irunsin.controller.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.model.CollectNoSong;
import com.irunsin.controller.util.imageloader.ImageLoader;
import com.irunsin.syncctrl.R;

public class XiamiRecommendAdapter extends BaseAdapter {

	private ArrayList<CollectNoSong> list;
	private Context context;
	private ListView listView;

	public XiamiRecommendAdapter(ArrayList<CollectNoSong> list, Context context,
			ListView listView2) {
		this.list = list;
		this.context = context;
		this.listView = listView2;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		XiamiRecommendListViewHolder holder;

		if (convertView == null) {
			convertView = View
					.inflate(context, R.layout.list_xiami_radio, null);
			holder = new XiamiRecommendListViewHolder();
			convertView.setTag(holder);

			holder.Image = (ImageView) convertView
					.findViewById(R.id.radio_image);
			holder.Name = (TextView) convertView.findViewById(R.id.radio_name);
		} else {
			holder = (XiamiRecommendListViewHolder) convertView.getTag();
		}

		holder.Name.setText(list.get(position).getCollect_name());
		String imageURL = list.get(position).getLogo();
		holder.Image.setTag(imageURL);
		ImageLoader.getInstances().displayImage(imageURL, holder.Image);

		return convertView;
	}

	class XiamiRecommendListViewHolder {
		ImageView Image;
		TextView Name;
	}

	public void setMyList(ArrayList<CollectNoSong> rm) {
		if(rm!=null){
			this.list = (ArrayList<CollectNoSong>) rm.clone();
			this.notifyDataSetChanged();
		}
	}
}
