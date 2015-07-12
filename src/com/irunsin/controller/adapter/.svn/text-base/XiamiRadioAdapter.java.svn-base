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

import com.alibaba.model.Radio;
import com.irunsin.controller.util.imageloader.ImageLoader;
import com.irunsin.syncctrl.R;

public class XiamiRadioAdapter extends BaseAdapter {

	private List<Radio> radioList;
	private Context context;
	private ListView listView;

	public XiamiRadioAdapter(List<Radio> rm, Context context, ListView listView) {
		this.radioList = rm;
		this.context = context;
		this.listView = listView;
	}

	@Override
	public int getCount() {

		return radioList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		XiamiMusicListViewHolder holder;

		if (convertView == null) {
			convertView = View
					.inflate(context, R.layout.list_xiami_radio, null);
			holder = new XiamiMusicListViewHolder();
			convertView.setTag(holder);

			holder.radioImage = (ImageView) convertView
					.findViewById(R.id.radio_image);
			holder.radioName = (TextView) convertView
					.findViewById(R.id.radio_name);
		} else {
			holder = (XiamiMusicListViewHolder) convertView.getTag();
		}

		holder.radioName.setText(radioList.get(position).getName());
		String imageURL = radioList.get(position).getLogo();

		// holder.imageDesc = new ImageDesc(radioList.get(position).getId() +
		// "",
		// ImageDesc.TYPE_HOSPITAL_PIC, imageURL);
		//
		// Bitmap bitmap =
		// ImageManager.getInstance().getBitmap(holder.imageDesc);
		// if (bitmap != null) {
		// holder.radioImage.setImageBitmap(bitmap);
		// } else {
		// holder.radioImage.setImageResource(R.drawable.ico_logo);
		// }
		// System.out.println("ppppppppppppptt 图片地址" + imageURL);

		holder.radioImage.setImageResource(R.drawable.ic_launcher);
		holder.radioImage.setTag(imageURL);
		ImageLoader.getInstances().displayImage(imageURL, holder.radioImage);

		return convertView;
	}

	class XiamiMusicListViewHolder {
		ImageView radioImage;
		TextView radioName;
	}

	public void setMyList(List<Radio> rm) {
		this.radioList = rm;
		this.notifyDataSetChanged();
	}
}
