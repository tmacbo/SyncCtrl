package com.irunsin.controller.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.model.RankList;
import com.irunsin.controller.util.imageloader.ImageLoader;
import com.irunsin.syncctrl.R;

public class XiamiRankExpandAdapter extends BaseExpandableListAdapter{
	
	private Context context;
	private List<RankList> listdata;
	private ExpandableListView listview;
	public XiamiRankExpandAdapter(Context context,List<RankList> listdata,ExpandableListView listview){
		this.context = context;
		this.listdata = listdata;
		this.listview = listview;
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return listdata.get(groupPosition).getItems().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if(groupPosition>=listdata.size()){
			return 0;
		}
		return listdata.get(groupPosition).getItems().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return listdata.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		int size = listdata.size();
		return size;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.expandable_rank_group, null);
			holder = new GroupViewHolder();
			convertView.setTag(holder);
			holder.mGroupName = (TextView) convertView
					.findViewById(R.id.group_name);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}
		
		holder.mGroupName.setText(listdata.get(groupPosition).getCategory());
		convertView.setClickable(true);
	    return convertView;
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChildViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.expandable_rank_child, null);
			holder = new ChildViewHolder();
			convertView.setTag(holder);
			holder.mIcon = (ImageView) convertView
					.findViewById(R.id.rank_img);
			holder.mChildName = (TextView) convertView.findViewById(R.id.rank_item_name);
			holder.mtime = (TextView) convertView.findViewById(R.id.rank_item_time);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}
		holder.mChildName.setText(listdata.get(groupPosition).getItems().get(childPosition).getTitle());
		holder.mtime.setText(listdata.get(groupPosition).getItems().get(childPosition).getUpdate_date());
		String imageURL = listdata.get(groupPosition).getItems().get(childPosition).getLogo();
		holder.mIcon.setTag(imageURL);
		ImageLoader.getInstances().displayImage(imageURL, holder.mIcon);

	    return convertView;
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

    private class GroupViewHolder {
        TextView mGroupName;
    }

    private class ChildViewHolder {
        ImageView mIcon;
        TextView mChildName;
        TextView mtime;
    }
    
    public void setMyList(List<RankList> listdata){
    	this.listdata = listdata;
    	this.notifyDataSetChanged();
    }
}
