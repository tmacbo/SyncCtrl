package com.irunsin.controller.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.model.StandardAlbum;
import com.irunsin.controller.util.imageloader.ImageLoader;
import com.irunsin.syncctrl.R;

public class XiamiNewAlbumAdapter extends BaseAdapter {
	private List<StandardAlbum> list;
	private Context context;
	private GridView listView;

	public XiamiNewAlbumAdapter(List<StandardAlbum> list, Context context,
			GridView listView) {
		this.list = list;
		this.context = context;
		this.listView = listView;
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
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		XiamiNewAblumsNewViewHolder holder;

		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.item_gridview_newalbums, null);
			holder = new XiamiNewAblumsNewViewHolder();
			convertView.setTag(holder);

			holder.ablumsImage = (ImageView) convertView
					.findViewById(R.id.image);
			holder.ablumsName = (TextView) convertView.findViewById(R.id.title);

		} else {
			holder = (XiamiNewAblumsNewViewHolder) convertView.getTag();
		}

		holder.ablumsName.setText(list.get(position).getAlbum_name());
		String imageURL = list.get(position).getLogo();
		holder.ablumsImage.setTag(imageURL);
		
		ImageLoader.getInstances().displayImage(imageURL, holder.ablumsImage);
		
	
		return convertView;
	}

	class XiamiNewAblumsNewViewHolder {
		ImageView ablumsImage;
		TextView ablumsName;
	}

	public void setMyList(List<StandardAlbum> rm) {
		this.list = rm;
		this.notifyDataSetChanged();
	}
}
