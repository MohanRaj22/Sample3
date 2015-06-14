package com.mohan.sample3.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.mohan.sample3.R;
import com.mohan.sample3.R.drawable;
import com.mohan.sample3.bo.CategoryBO;
import com.mohan.sample3.model.MyApplication;
import com.mohan.sample3.model.StaticData;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends Activity {
	public static String TAG = "HomeActivity";
	MyApplication mApplication;
	Fragment fragment = null;
	ListView mListView;
	HashMap<String, CategoryBO> childCategories;
	ActionBarDrawerToggle mDrawerToggle;
	DrawerLayout mDrawerLayout;
	String mTitle, mDrawerTitle;
	ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mApplication = (MyApplication) getApplicationContext();
		mListView = (ListView) findViewById(R.id.listView);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mApplication.getParseHelper().downloadCategory(mHandler);
		mApplication.getParseHelper().retriveParseLocalData(mHandler);
		// mApplication.getParseHelper().downloadImages(mHandler);
		// mApplication.getParseHelper().loadParseImageData(mHandler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
	}

	private void setUpDrawer() {
		Log.i(TAG, "setUpDrawer Called");
		mApplication.getCategoryHelper().loadCategories();
		mApplication.getCategoryHelper().prepareCategoryData();
		mListView.setAdapter(new MyListAdapter(mApplication.getCategoryHelper()
				.getParentCategoryBOs()));
		mTitle = mDrawerTitle = (String) getTitle();
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(drawerView);
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();

			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		invalidateOptionsMenu();
		return true;
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		if (mDrawerToggle != null)
			mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	class MyListAdapter extends BaseAdapter {
		ArrayList<CategoryBO> categoryBOs;

		public MyListAdapter(ArrayList<CategoryBO> categoryBOs) {
			// TODO Auto-generated constructor stub
			this.categoryBOs = categoryBOs;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return categoryBOs.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return categoryBOs.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder mViewHolder;
			if (convertView == null) {
				mViewHolder = new ViewHolder();
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.layered_list_item,
						parent, false);
				mViewHolder.parentTextView = (TextView) convertView
						.findViewById(R.id.levelOneTextView);
				mViewHolder.subLinearLayout = (LinearLayout) convertView
						.findViewById(R.id.subParentLinearLayout);
				mViewHolder.subParentTextView = (TextView) convertView
						.findViewById(R.id.levelTwoTextView);
				mViewHolder.childLinearLayout = (LinearLayout) convertView
						.findViewById(R.id.levelThreeLinearLayout);
				mViewHolder.parentLinearLayout = (LinearLayout) convertView
						.findViewById(R.id.parentLinearLayout);
				mViewHolder.parentIcon = (ImageView) convertView
						.findViewById(R.id.parentIcon);
				mViewHolder.subParentIcon = (ImageView) convertView
						.findViewById(R.id.subParentIcon);
				convertView.setTag(mViewHolder);

			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			mViewHolder.index = position;
			mViewHolder.categoryBO = categoryBOs.get(mViewHolder.index);
			mViewHolder.chilCategoryBOs = new ArrayList<CategoryBO>();
			mViewHolder.chilCategoryBOs = mApplication
					.getLevelThreeCategories().get(
							mViewHolder.categoryBO.getCategoryID());
			mViewHolder.parentTextView.setTextAppearance(HomeActivity.this,
					R.style.TextAppearance_AppCompat_Medium);
			mViewHolder.subParentTextView.setTextAppearance(HomeActivity.this,
					R.style.TextAppearance_AppCompat_Medium);
			mViewHolder.resourceId = 0;
			mViewHolder.mDrawable = null;
			if (mViewHolder.categoryBO.getIconName() != null
					&& !mViewHolder.categoryBO.getIconName().isEmpty()) {
				mViewHolder.resourceId = getResources().getIdentifier(
						mViewHolder.categoryBO.getIconName(), "drawable",
						getPackageName());
			}
			Log.i(TAG, "Resource ID : " + mViewHolder.resourceId);
			if (mViewHolder.resourceId != 0)
				mViewHolder.mDrawable = getResources().getDrawable(
						mViewHolder.resourceId);

			mViewHolder.parentLinearLayout.setVisibility(View.GONE);
			mViewHolder.subLinearLayout.setVisibility(View.GONE);
			mViewHolder.childLinearLayout.setVisibility(View.GONE);

			mViewHolder.parentTextView
					.setTextColor(Color.parseColor("#FFFFFF"));
			mViewHolder.subParentTextView.setTextColor(Color
					.parseColor("#FFFFFF"));

			if (mViewHolder.categoryBO.getLevel() == 1) {
				mViewHolder.parentLinearLayout.setVisibility(View.VISIBLE);
				mViewHolder.parentTextView.setText(mViewHolder.categoryBO
						.getCategoryName());
				if (mViewHolder.mDrawable != null)
					mViewHolder.parentIcon
							.setImageDrawable(mViewHolder.mDrawable);
				else
					mViewHolder.parentIcon.setVisibility(View.INVISIBLE);
			} else if (mViewHolder.categoryBO.getLevel() == 2) {
				mViewHolder.subLinearLayout.setVisibility(View.VISIBLE);
				mViewHolder.subParentTextView.setText(mViewHolder.categoryBO
						.getCategoryName());
				if (mViewHolder.mDrawable != null)
					mViewHolder.subParentIcon
							.setImageDrawable(mViewHolder.mDrawable);
				else
					mViewHolder.subParentIcon.setVisibility(View.INVISIBLE);

			}
			if (mViewHolder.chilCategoryBOs != null
					&& !mViewHolder.chilCategoryBOs.isEmpty()) {
				mViewHolder.childLinearLayout.setVisibility(View.VISIBLE);
				mViewHolder.childLinearLayout.removeAllViews();

				for (final CategoryBO mCategoryBO : mViewHolder.chilCategoryBOs) {
					LinearLayout mChildLinearLayout = new LinearLayout(
							HomeActivity.this);
					mChildLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
					LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);
					ImageView imageView = new ImageView(HomeActivity.this);
					// imageView.setImageResource(R.drawable.activities);
					Drawable mDrawable = getResources().getDrawable(
							getResources().getIdentifier("activities",
									"drawable", getPackageName()));
					// imageView.setImageDrawable(mDrawable);

					TextView mTextView = new TextView(HomeActivity.this);
					mTextView.setTextAppearance(HomeActivity.this,
							R.style.TextAppearance_AppCompat_Medium);
					mTextView.setLayoutParams(mLayoutParams);
					mTextView.setTextColor(Color.parseColor("#FFFFFF"));

					mLayoutParams = new LinearLayout.LayoutParams(40, 40);
					imageView.setLayoutParams(mLayoutParams);
					mTextView.setText(mCategoryBO.getCategoryName());
					mChildLinearLayout.addView(imageView);
					mChildLinearLayout.addView(mTextView);
					mChildLinearLayout
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									mApplication
											.setSelecCategoryBO(mCategoryBO);
									displayView();

								}
							});
					mViewHolder.childLinearLayout.addView(mChildLinearLayout);
				}
			}
			mViewHolder.subLinearLayout
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (mViewHolder.childLinearLayout.getVisibility() == 0) {
								mViewHolder.childLinearLayout
										.setVisibility(View.GONE);
							} else {
								mViewHolder.childLinearLayout
										.setVisibility(View.VISIBLE);
							}
						}
					});
			return convertView;
		}
	}

	class ViewHolder {
		int index;
		int resourceId;
		ImageView parentIcon, subParentIcon;
		Drawable mDrawable;
		TextView parentTextView;
		TextView subParentTextView;
		LinearLayout parentLinearLayout;
		LinearLayout subLinearLayout;
		LinearLayout childLinearLayout;
		CategoryBO categoryBO;
		ArrayList<CategoryBO> chilCategoryBOs;
	}

	private void inItProgresDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(HomeActivity.this);
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}
		if (!mProgressDialog.isShowing())
			mProgressDialog.show();
	}

	public void downloadImages() {
		mApplication.getParseHelper().downloadImages(mHandler);

	}

	Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			Log.i(TAG, "msg.what : " + msg.what);
			switch (msg.what) {
			case StaticData.CATEGORY_DOWNLOAD_STARTED:
				inItProgresDialog();
				break;
			case StaticData.CATEGORY_DOWNLOAD_IN_PROGRESS:
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.setMessage(msg.obj.toString());
				}
				break;
			case StaticData.CATEGORY_DOWNLOAD_FINISHED:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				mApplication.getParseHelper().retriveParseLocalData(mHandler);
				break;
			case StaticData.LOADING_CATEGORY_FROM_LOACAL_IN_PROGRESS:
				inItProgresDialog();
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.setMessage(msg.obj.toString());
				}
				break;
			case StaticData.LOADING_CATEGORY_FROM_LOACAL_FINISHED:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				break;
			case StaticData.IMAGE_DOWNLOAD_STARTED:
				inItProgresDialog();
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.setMessage(msg.obj.toString());
				}
				break;
			case StaticData.IMAGE_DOWNLOAD_IN_PROGRESS:
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.setMessage(msg.obj.toString());
				}
				break;
			case StaticData.IMAGE_DOWNLOAD_FINISHED:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				mApplication.getParseHelper().loadParseImageData(mHandler);
				break;
			case StaticData.LOADING_IMAGES_FROM_LOACAL_IN_PROGRESS:
				inItProgresDialog();
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.setMessage(msg.obj.toString());
				}
				break;
			case StaticData.LOADING_IMAGES_FROM_LOACAL_FINISHED:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				displayView();
				break;
			case StaticData.IMAGE_DOWNLOAD_FAILED:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				mApplication.showDialogBox(HomeActivity.this, "OK", null,
						"Download time out !");
				break;
			case StaticData.CATEGORY_DATA_PROCESSING_FINISHED:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				setUpDrawer();
				downloadImages();
				break;

			}
		}
	};

	public void displayView() {
		Log.i(TAG, "displayView");

		CategoryBO categoryBO = new CategoryBO();
		categoryBO.setCategoryID("wic0lLoZcs");

		mApplication.setSelecCategoryBO(categoryBO);

		android.app.Fragment mFragment = new GalleryFragment();
		FragmentManager mFragmentManager = getFragmentManager();
		mFragmentManager.beginTransaction()
				.replace(R.id.content_Frame, mFragment)
				.commitAllowingStateLoss();
		mDrawerLayout.closeDrawer(mListView);
	}
}
