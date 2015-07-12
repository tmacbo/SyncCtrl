package com.irunsin.controller.adapter;

import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.util.GB2Alpha;
import com.irunsin.controller.util.IrunSinApplication;
import com.irunsin.controller.util.Mp3Util;
import com.irunsin.syncctrl.R;

public class LocalMusicAdapter extends BaseAdapter {

	private List<Mp3Info> list;
	private Context context;
	private IrunSinApplication irunsin;
	private AnimationDrawable mPeakOneAnimation, mPeakTwoAnimation;
	

	public LocalMusicAdapter(Context context, List<Mp3Info> list) {
		this.list = list;
		this.context = context;

		irunsin = (IrunSinApplication) context.getApplicationContext();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LocalMusicListViewHolder holder;

		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.list_local_music, null);
			holder = new LocalMusicListViewHolder();
			convertView.setTag(holder);
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			holder.audioimage = (ImageView) convertView.findViewById(R.id.mysong_list_image);
			holder.music_display = (TextView) convertView
					.findViewById(R.id.musiclist_name);
			holder.music_artis = (TextView) convertView
					.findViewById(R.id.musiclist_desib);
			holder.image2 = (ImageView) convertView.findViewById(R.id.peak_two);
			holder.image1 = (ImageView) convertView.findViewById(R.id.peak_one);
		} else {
			holder = (LocalMusicListViewHolder) convertView.getTag();
		}
								
		String display = list.get(position).getDisplay_name();
		if(display.endsWith("mp3")){		
			holder.audioimage.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.mp3));			
		}else if(display.endsWith("ape")){
			holder.audioimage.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ape));				
		}else if(display.endsWith("flac")){
			holder.audioimage.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.flac));				
		}else if(display.endsWith("wav")){
			holder.audioimage.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.wav));				
		}else{
			holder.audioimage.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.mp3));					
		}
		
		holder.music_display.setText(Mp3Util.toGb2312(list.get(position)
				.getDisplay_name()));
		holder.music_artis.setText(Mp3Util.toGb2312(list.get(position)
				.getArtist()));
		GB2Alpha stringzh = new GB2Alpha();
		String currentStr = getAlpha(stringzh.String2Alpha(list.get(position)
				.getDisplay_name()));
		String previewStr = (position - 1) >= 0 ? getAlpha(stringzh
				.String2Alpha(list.get(position - 1).getDisplay_name())) : " ";
		if (!previewStr.equals(currentStr)) {
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
		String listmp3id = list.get(position).getMp3id();// 得到当前listview的对应的mp3的MP3id
		int nowposition = irunsin.getPosition();
		if (nowposition != -1) {
			String nowmp3id = irunsin.getListmp3().get(nowposition).getMp3id();
			if (listmp3id.equals(nowmp3id)) {
				holder.image2.setImageResource(R.anim.peak_meter_1);
				holder.image1.setImageResource(R.anim.peak_meter_2);
				mPeakOneAnimation = (AnimationDrawable) holder.image2
						.getDrawable();
				mPeakTwoAnimation = (AnimationDrawable) holder.image1
						.getDrawable();

				if (irunsin.getPLAY_STATE() == 0) {
					mPeakOneAnimation.start();
					mPeakTwoAnimation.start();
				} else {
					mPeakOneAnimation.stop();
					mPeakTwoAnimation.stop();
				}

			} else {
				holder.image2.setImageResource(0);
				holder.image1.setImageResource(0);
			}
		}
		return convertView;
	}

	static class LocalMusicListViewHolder {
		ImageView audioimage;
		TextView alpha;
		TextView music_display;
		TextView music_artis;
		ImageView image2;
		ImageView image1;
	}

	public void setMyList(List<Mp3Info> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	// 获得汉语拼音首字母
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}
}
