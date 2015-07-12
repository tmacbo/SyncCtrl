package com.irunsin.controller.adapter;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.model.CollectNoSong;
import com.alibaba.model.SimpleArtist;
import com.irunsin.controller.util.imageloader.ImageLoader;
import com.irunsin.syncctrl.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class XiamiRecommendArtistAdapter extends BaseAdapter{

	private ArrayList<SimpleArtist> list;
	private Context context;
	private ListView listView;

	public XiamiRecommendArtistAdapter(ArrayList<SimpleArtist> list, Context context,
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
		XiamiRecommendArtistViewHolder holder;

		if (convertView == null) {
			convertView = View
					.inflate(context, R.layout.item_recommend_artist, null);
			holder = new XiamiRecommendArtistViewHolder();
			convertView.setTag(holder);

			holder.Image = (ImageView) convertView
					.findViewById(R.id.artist_image);
			holder.Name = (TextView) convertView.findViewById(R.id.artist_name);
		} else {
			holder = (XiamiRecommendArtistViewHolder) convertView.getTag();
		}

		holder.Name.setText(list.get(position).getArtist_name());
		String imageURL = list.get(position).getLogo();
		holder.Image.setTag(imageURL);
		ImageLoader.getInstances().displayImage(imageURL, holder.Image);
		
		return convertView;
	}

	class XiamiRecommendArtistViewHolder {
		ImageView Image;
		TextView Name;
	}

	public void setMyList(ArrayList<SimpleArtist> rm) {
		if(rm!=null){
			this.list = (ArrayList<SimpleArtist>) rm.clone();
			this.notifyDataSetChanged();
		}
	}

}
