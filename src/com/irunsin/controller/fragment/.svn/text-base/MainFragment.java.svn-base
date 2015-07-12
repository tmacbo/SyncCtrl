package com.irunsin.controller.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.irunsin.activity.syncctrl.HomeActivity;
import com.irunsin.activity.syncctrl.Rotate3D;
import com.irunsin.syncctrl.R;

public class MainFragment extends Fragment{
	
		private View parentView;
		
		int[] imageIds = new int[] { R.drawable.banner, R.drawable.banner_2_,
				R.drawable.banner_1 };
		ImageView[] views = new ImageView[3];
		ImageSwitcher imswitcher;
		GestureDetector mGestureDetector;
		int i = 0;
	
		Runnable r;
		private Handler myhandler;
		
		private TextView localText;
		private TextView recentlyText;
		private TextView collecText;
		private TextView xiamiText;
		private HomeActivity home;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			parentView = inflater.inflate(R.layout.fragment_main,
					container, false);
			
			imswitcher = (ImageSwitcher) parentView.findViewById(R.id.imageSwitcher1);
			imswitcher.setFactory(new ViewFactory() {
				@Override
				public View makeView() {
					ImageView imageView = new ImageView(getActivity());
					imageView.setBackgroundColor(0xff0000);
					imageView.setScaleType(ImageView.ScaleType.FIT_START);
					imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					return imageView;
				}
			});
			imswitcher.setImageResource(R.drawable.banner);
			ImageView v1 = (ImageView) parentView.findViewById(R.id.view1);
			ImageView v2 = (ImageView) parentView.findViewById(R.id.view2);
			ImageView v3 = (ImageView) parentView.findViewById(R.id.view3);
			views[0] = v1;
			views[1] = v2;
			views[2] = v3;
	
			mGestureDetector = new GestureDetector(getActivity(), new MyGestureListener());
			imswitcher.setOnTouchListener(new OnTouchListener() {
	
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					mGestureDetector.onTouchEvent(event);
					return true;
				}
			});
	
			DownloadTask dTask = new DownloadTask();
			dTask.execute(100);
			
			
			initData();
			findViews();
			addAction();
			
			return parentView;		
		}
		
		public void initData(){
			
		}
		
		public void findViews(){
			localText = (TextView) parentView.findViewById(R.id.local_music_text);
			recentlyText = (TextView) parentView.findViewById(R.id.recently_music_text);
			collecText = (TextView) parentView.findViewById(R.id.collec_music_text);
			xiamiText = (TextView) parentView.findViewById(R.id.xiami_music_text);
		}
		
		public void addAction(){
			
			localText.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					switchFragment(new LocalMusicFragment(),0);
				}
			});
			
			recentlyText.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					switchFragment(new RecentlyListenMusicFragment(),0);
				}
			});
			
			collecText.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					switchFragment(new CollectionMusicFragment(),0);
				}
			});
			
			xiamiText.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					switchFragment(null,1);
				}
			});
		}
		
		public void switchFragment(Fragment fragment,int index){
			
			home = (HomeActivity) getActivity();
			
			switch(index){
			case 0:
				home.switchFragment(fragment);
				break;
			case 1:
				home.switchContent(1);
				break;
			}
		}
		public void setpic(int m) {
	
			for (int i = 0; i < views.length; i++) {
				if (i == m) {
					views[i].setBackgroundResource(R.drawable.icon_dot);
				} else {
					views[i].setBackgroundResource(R.drawable.icon_dot_);
				}
	
			}
	
		}
	
		private class MyGestureListener implements
				GestureDetector.OnGestureListener {
	
			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
	
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
	
				if (velocityX > 0) {
					// Toast.makeText(getApplicationContext(), "�ٶȴ���0",
					// Toast.LENGTH_SHORT).show();
	
					float halfWidth = imswitcher.getWidth() / 2.0f;
					float halfHeight = imswitcher.getHeight() / 2.0f;
					int duration = 500;
					int depthz = 0;// viewFlipper.getWidth()/2;
	
					Rotate3D rdin = new Rotate3D(-75, 0, 0, halfWidth, halfHeight);
					rdin.setDuration(duration);
					rdin.setFillAfter(true);
					imswitcher.setInAnimation(rdin);
					Rotate3D rdout = new Rotate3D(15, 90, 0, halfWidth, halfHeight);
	
					rdout.setDuration(duration);
					rdout.setFillAfter(true);
					imswitcher.setOutAnimation(rdout);
	
					i = (i - 1);
	
					Log.i("i��ֵ", String.valueOf(i));
					int p = i % 3;
					Log.i("p��ֵ", String.valueOf(p));
					if (p >= 0) {
						setpic(p);
						imswitcher.setImageResource(imageIds[p]);
	
					} else {
	
						int k = 3 + p;
						setpic(k);
						imswitcher.setImageResource(imageIds[k]);
	
					}
	
				}
				if (velocityX < 0) {
					// Toast.makeText(getApplicationContext(), "�ٶ�С��0",
					// Toast.LENGTH_SHORT).show();
	
					float halfWidth = imswitcher.getWidth() / 2.0f;
					float halfHeight = imswitcher.getHeight() / 2.0f;
					int duration = 500;
					int depthz = 0;// viewFlipper.getWidth()/2;
	
					Rotate3D rdin = new Rotate3D(75, 0, 0, halfWidth, halfHeight);
					rdin.setDuration(duration);
					rdin.setFillAfter(true);
					imswitcher.setInAnimation(rdin);
					Rotate3D rdout = new Rotate3D(-15, -90, 0, halfWidth,
							halfHeight);
	
					rdout.setDuration(duration);
					rdout.setFillAfter(true);
					imswitcher.setOutAnimation(rdout);
	
					i = (i + 1);
					int p = i % 3;
	
					if (p >= 0) {
						setpic(p);
						imswitcher.setImageResource(imageIds[p]);
	
					} else {
	
						int k = 3 + p;
						setpic(k);
						imswitcher.setImageResource(imageIds[k]);
	
					}
				}
				return true;
			}
	
			@Override
			public void onLongPress(MotionEvent e) {
	
			}
	
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				return false;
			}
	
			@Override
			public void onShowPress(MotionEvent e) {
	
			}
	
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				int p = i % 3;
	
				if (p >= 0) {
	
					Toast.makeText(getActivity(), String.valueOf(p),
							Toast.LENGTH_SHORT).show();
				} else {
	
					int k = 3 + p;
					Toast.makeText(getActivity(), String.valueOf(k),
							Toast.LENGTH_SHORT).show();
				}
	
				return true;
			}
	
		}
	
		class DownloadTask extends AsyncTask {
	
			@Override
			protected Object doInBackground(Object... arg0) {
	
				for (int i = 0; i < Integer.MAX_VALUE; i++) {
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	
					publishProgress(null);
	
				}
	
				return null;
			}
	
			@Override
			protected void onProgressUpdate(Object... values) {
				super.onProgressUpdate(values);
	
				float halfWidth = imswitcher.getWidth() / 2.0f;
				float halfHeight = imswitcher.getHeight() / 2.0f;
				int duration = 500;
				int depthz = 0;// viewFlipper.getWidth()/2;
	
				Rotate3D rdin = new Rotate3D(75, 0, 0, halfWidth, halfHeight);
				rdin.setDuration(duration);
				rdin.setFillAfter(true);
				imswitcher.setInAnimation(rdin);
				Rotate3D rdout = new Rotate3D(-15, -90, 0, halfWidth, halfHeight);
	
				rdout.setDuration(duration);
				rdout.setFillAfter(true);
				imswitcher.setOutAnimation(rdout);
	
				i = (i + 1);
				int p = i % 3;
	
				if (p >= 0) {
					setpic(p);
					imswitcher.setImageResource(imageIds[p]);
				} else {
	
					int k = 3 + p;
					setpic(k);
					imswitcher.setImageResource(imageIds[k]);
				}
			}
		}
}
