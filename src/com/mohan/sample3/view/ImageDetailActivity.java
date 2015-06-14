package com.mohan.sample3.view;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

import com.mohan.sample3.R;
import com.mohan.sample3.model.MyApplication;
import com.mohan.sample3.model.StaticData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageDetailActivity extends Activity {
	public static String TAG = "ImageDetailActivity";
	ImageViewTouch imageView;
	ProgressDialog mProgressDialog;
	MyApplication mApplication;
	Bitmap mBitmap;
	LinearLayout imageLinearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_detail);
		imageView = (ImageViewTouch) findViewById(R.id.imageView);
		imageLinearLayout = (LinearLayout) findViewById(R.id.imageLinearLayout);
		mApplication = (MyApplication) getApplicationContext();
		loadImage();
	}

	private void loadImage() {
		if (mApplication.getSelectedImageBO().getActualImageByteArray() == null) {
			Log.i(TAG, "loadImage Null");
			mBitmap = BitmapFactory
					.decodeByteArray(mApplication.getSelectedImageBO()
							.getThumbImageByteArray(), 0,
							mApplication.getSelectedImageBO()
									.getThumbImageByteArray().length);
			mApplication.getParseHelper().downloadActualImage(mHandler);
			imageView.setImageBitmap(mBitmap);
		} else {
			Log.i(TAG, "Not Null"
					+ mApplication.getSelectedImageBO()
							.getActualImageByteArray());
			mBitmap = BitmapFactory
					.decodeByteArray(mApplication.getSelectedImageBO()
							.getActualImageByteArray(), 0,
							mApplication.getSelectedImageBO()
									.getActualImageByteArray().length);
			imageView.setImageBitmap(mBitmap);
			imageView.setImageBitmapReset(mBitmap, true);
			Log.i(TAG, "Height : " + imageView.getHeight() + " Width : "
					+ imageView.getWidth());
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
	}

	private void inItProgresDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(ImageDetailActivity.this);
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}
		if (!mProgressDialog.isShowing())
			mProgressDialog.show();
	}

	Handler mHandler = new Handler()

	{
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case StaticData.ACTUAL_IMAGE_DOWNLOAD_STARTED:
				inItProgresDialog();
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.setMessage(msg.obj.toString());
				}
				break;
			case StaticData.ACTUAL_IMAGE_DOWNLOAD_IN_PROGRESS:
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.setMessage(msg.obj.toString());
				}
				break;
			case StaticData.ACTUAL_IMAGE_DOWNLOAD_FINISHED:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				loadImage();
				break;
			case StaticData.ACTUAL_IMAGE_DOWNLOAD_FAILED:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				mApplication.showDialogBox(ImageDetailActivity.this, "OK",
						null, "Download time out !");
				break;
			}
		};
	};
}
