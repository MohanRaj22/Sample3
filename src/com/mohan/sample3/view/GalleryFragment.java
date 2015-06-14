package com.mohan.sample3.view;

import java.util.ArrayList;

import com.mohan.sample3.R;
import com.mohan.sample3.bo.CategoryBO;
import com.mohan.sample3.bo.DownlaodImageBO;
import com.mohan.sample3.model.MyApplication;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GalleryFragment extends Fragment {
	public static String TAG = "GalleryFragment";
	MyApplication mApplication;
	ListView mListView;
	ArrayList<CategoryBO> headerCategories;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreateView");
		View mView = inflater.inflate(R.layout.fragment_gallery, container,
				false);
		mListView = (ListView) mView.findViewById(R.id.galleryList);
		mApplication = (MyApplication) getActivity().getApplicationContext();
		setUpList();
		return mView;
	}

	void setUpList() {
		if (mApplication.getSelecCategoryBO() != null) {
			headerCategories = mApplication.getLevelFourCategories().get(
					mApplication.getSelecCategoryBO().getCategoryID());
			Log.i(TAG, "headerCategories : " + headerCategories.size());
			mListView.setAdapter(new MyListAdapter(headerCategories));
		}
	}

	class ViewHolder {
		int index;
		CategoryBO categoryBO;
		ArrayList<DownlaodImageBO> imageBOs;
		LinearLayout imagesLinearLayout;
		TextView titleTextView;
	}

	class MyListAdapter extends BaseAdapter {
		ArrayList<CategoryBO> headerCategories;

		public MyListAdapter(ArrayList<CategoryBO> headerCategories) {
			// TODO Auto-generated constructor stub
			this.headerCategories = headerCategories;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return headerCategories.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return headerCategories.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder mViewHolder;
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.gallery_list_item, parent, false);
				mViewHolder = new ViewHolder();
				mViewHolder.titleTextView = (TextView) convertView
						.findViewById(R.id.textView1);
				mViewHolder.imagesLinearLayout = (LinearLayout) convertView
						.findViewById(R.id.imageContainer);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			mViewHolder.index = position;
			mViewHolder.categoryBO = headerCategories.get(mViewHolder.index);
			mViewHolder.titleTextView.setText(mViewHolder.categoryBO
					.getCategoryName());
			Log.i(TAG, "cat : " + mViewHolder.categoryBO.getCategoryID());
			mViewHolder.imageBOs = mApplication.getDownloadImagesMap().get(
					mViewHolder.categoryBO.getCategoryID());
			if (mViewHolder.imageBOs != null && !mViewHolder.imageBOs.isEmpty()) {
				mViewHolder.imagesLinearLayout.removeAllViews();
				Log.i(TAG, "imageBOs : " + mViewHolder.imageBOs.size());
				LinearLayout mLinearLayout = new LinearLayout(getActivity());

				for (final DownlaodImageBO imageBO : mViewHolder.imageBOs) {
					ImageView mImageView = new ImageView(getActivity());
					mImageView.setPadding(5, 5, 5, 5);
					mImageView.setTag(imageBO);
					Log.i(TAG, "Data  : " + imageBO.getThumbImageByteArray());
					mImageView.setImageBitmap(BitmapFactory.decodeByteArray(
							imageBO.getThumbImageByteArray(), 0,
							imageBO.getThumbImageByteArray().length));
					mImageView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mApplication.setSelectedImageBO(imageBO);
							startActivity(new Intent(getActivity(),
									ImageDetailActivity.class));
						}
					});
					mLinearLayout.addView(mImageView);
				}
				mViewHolder.imagesLinearLayout.addView(mLinearLayout);
			} else
				Log.i(TAG, "imageBOs Null");

			return convertView;
		}

	}
}