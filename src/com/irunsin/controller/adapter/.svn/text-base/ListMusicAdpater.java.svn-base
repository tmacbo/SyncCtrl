package com.irunsin.controller.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.irunsin.controller.model.Mp3Info;
import com.irunsin.controller.util.Mp3Util;
import com.irunsin.controller.util.imageloader.ImageLoader;
import com.irunsin.syncctrl.R;

/**
 * 无特殊需求公共歌曲列表的adapter
 * @author Administrator
 *
 */
public class ListMusicAdpater extends BaseAdapter{

	private List<Mp3Info> list;
	private Context context;
	
	public ListMusicAdpater(List<Mp3Info> list,Context context){
		this.list = list;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.list_music, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.audioimage = (ImageView) convertView.findViewById(R.id.list_image);
			holder.music_display = (TextView) convertView
					.findViewById(R.id.music_name);
			holder.music_artis = (TextView) convertView
					.findViewById(R.id.music_desib);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.music_display.setText(Mp3Util.toGb2312(list.get(position)
				.getDisplay_name()));
		holder.music_artis.setText(Mp3Util.toGb2312(list.get(position)
				.getArtist()));
		
		if(list.get(position).getLoOnFlag()==0){
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
		}else if(list.get(position).getLoOnFlag() ==1){
			String imageURL = list.get(position).getLogo();
			holder.audioimage.setTag(imageURL);
			ImageLoader.getInstances().displayImage(imageURL, holder.audioimage);
		}
		
		
		return convertView;
	}
	
	class ViewHolder {
		ImageView audioimage;
		TextView music_display;
		TextView music_artis;		
	}
	
	public void setMyList(List<Mp3Info> list){
		if(list!=null){
			this.list = list;
			this.notifyDataSetChanged();
		}
	}
}
