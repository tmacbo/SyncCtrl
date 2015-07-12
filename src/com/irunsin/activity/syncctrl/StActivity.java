package com.irunsin.activity.syncctrl;

import com.irunsin.syncctrl.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class StActivity extends Activity {
	int[] imageIds = new int[] { R.drawable.banner, R.drawable.banner,
			R.drawable.banner };

	ImageSwitcher imswitcher;
	int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imswitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
		imswitcher.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				ImageView imageView = new ImageView(StActivity.this);
				imageView.setBackgroundColor(0xff0000);
				imageView.setScaleType(ImageView.ScaleType.FIT_START);
				imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				return imageView;
			}
		});

		imswitcher.setImageResource(R.drawable.banner);

		imswitcher.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int p = i % 4;

				if (p >= 0) {

					Toast.makeText(getApplicationContext(), String.valueOf(p),
							Toast.LENGTH_SHORT).show();
				} else {

					int k = 4 + p;
					Toast.makeText(getApplicationContext(), String.valueOf(k),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	class DownloadTask extends AsyncTask {

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub

			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				publishProgress(null);

			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			// TODO Auto-generated method stub
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
			int p = i % 4;

			if (p >= 0) {

				imswitcher.setImageResource(imageIds[p]);
			} else {

				int k = 4 + p;
				imswitcher.setImageResource(imageIds[k]);
			}
		}
	}
}