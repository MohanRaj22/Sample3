package com.mohan.sample3.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mohan.sample3.bo.CategoryBO;
import com.mohan.sample3.bo.DownlaodImageBO;
import com.mohan.sample3.model.MyApplication;

public class CategoryHelper {
	public static String TAG = "CategoryHelper";
	MyApplication mApplication;
	static CategoryHelper mCategoryHelper;
	ArrayList<CategoryBO> categoryBOs;
	ArrayList<CategoryBO> levelFourListForGallery;
	HashMap<String, ArrayList<DownlaodImageBO>> imageHashMap;

	public ArrayList<CategoryBO> getCategoryBOs() {
		return categoryBOs;
	}

	public void setCategoryBOs(ArrayList<CategoryBO> categoryBOs) {
		this.categoryBOs = categoryBOs;
	}

	ArrayList<CategoryBO> levelOneCatgories;
	Map<String, ArrayList<CategoryBO>> levelTwoCategories;
	Map<String, ArrayList<CategoryBO>> levelThreeCategories;
	Map<String, ArrayList<CategoryBO>> levelFourCategories;
	ArrayList<CategoryBO> parentCategoryBOs;
	Map<String, ArrayList<CategoryBO>> childCatagoriesBOs;

	public static CategoryHelper inItCategoryHelper(Context mContext) {
		return mCategoryHelper = new CategoryHelper(mContext);
	}

	public CategoryHelper(Context mContext) {
		// TODO Auto-generated constructor stub
		mApplication = (MyApplication) mContext.getApplicationContext();
	}

	public ArrayList<CategoryBO> getParentCategoryBOs() {
		return parentCategoryBOs;
	}

	public void setParentCategoryBOs(ArrayList<CategoryBO> parentCategoryBOs) {
		this.parentCategoryBOs = parentCategoryBOs;
	}

	public Map<String, ArrayList<CategoryBO>> getChildCatagoriesBOs() {
		return childCatagoriesBOs;
	}

	public void setChildCatagoriesBOs(
			Map<String, ArrayList<CategoryBO>> childCatagoriesBOs) {
		this.childCatagoriesBOs = childCatagoriesBOs;
	}

	public void loadCategories() {
		levelOneCatgories = new ArrayList<CategoryBO>();
		levelTwoCategories = new HashMap<String, ArrayList<CategoryBO>>();
		levelThreeCategories = new HashMap<String, ArrayList<CategoryBO>>();
		levelFourCategories = new HashMap<String, ArrayList<CategoryBO>>();
		for (CategoryBO mCategoryBO : categoryBOs) {
			if (mCategoryBO.getLevel() == 1) {
				levelOneCatgories.add(mCategoryBO);
			} else if (mCategoryBO.getLevel() == 2) {
				Log.i(TAG, "mCategoryBO : " + mCategoryBO.getCategoryName()
						+ " Level : " + mCategoryBO.getLevel() + " Parent : "
						+ mCategoryBO.getParentID());
				if (levelTwoCategories.get(mCategoryBO.getParentID()) == null) {
					ArrayList<CategoryBO> valueList = new ArrayList<CategoryBO>();
					valueList.add(mCategoryBO);
					levelTwoCategories
							.put(mCategoryBO.getParentID(), valueList);
				} else
					levelTwoCategories.get(mCategoryBO.getParentID()).add(
							mCategoryBO);
			} else if (mCategoryBO.getLevel() == 3) {
				if (levelThreeCategories.get(mCategoryBO.getParentID()) == null) {
					ArrayList<CategoryBO> valueList = new ArrayList<CategoryBO>();
					valueList.add(mCategoryBO);
					levelThreeCategories.put(mCategoryBO.getParentID(),
							valueList);
				} else
					levelThreeCategories.get(mCategoryBO.getParentID()).add(
							mCategoryBO);
			} else if (mCategoryBO.getLevel() == 4) {
				if (mCategoryBO.getParentID().equals("wic0lLoZcs"))
					Log.i(TAG, "Yes ! ! ");
				if (levelFourCategories.get(mCategoryBO.getParentID()) == null) {
					ArrayList<CategoryBO> valueList = new ArrayList<CategoryBO>();
					valueList.add(mCategoryBO);
					levelFourCategories.put(mCategoryBO.getParentID(),
							valueList);
				} else
					levelFourCategories.get(mCategoryBO.getParentID()).add(
							mCategoryBO);
			}
		}
		Log.i(TAG, "levelTwoCategories : " + levelTwoCategories.size());
		mApplication.setCategoryBOs(categoryBOs);
		mApplication.setLevelOneCatgories(sortcategories(levelOneCatgories));
		mApplication.setLevelTwoCategories(levelTwoCategories);
		mApplication.setLevelThreeCategories(levelThreeCategories);
		mApplication.setLevelFourCategories(levelFourCategories);
	}

	public ArrayList<CategoryBO> prepareCategoryData() {
		parentCategoryBOs = new ArrayList<CategoryBO>();
		childCatagoriesBOs = new HashMap<String, ArrayList<CategoryBO>>();
		parentCategoryBOs = new ArrayList<CategoryBO>();
		for (CategoryBO categoryBO : levelOneCatgories) {
			parentCategoryBOs.add(categoryBO);
			Log.i(TAG, "categoryBO level 1 : " + categoryBO.getCategoryID()
					+ " name : " + categoryBO.getCategoryName());
			ArrayList<CategoryBO> subParentCategoryBO = levelTwoCategories
					.get(categoryBO.getCategoryID());
			if (subParentCategoryBO != null && !subParentCategoryBO.isEmpty()) {
				Log.i(TAG, "Adding level 2");
				parentCategoryBOs.addAll(subParentCategoryBO);
			}

		}
		Log.i(TAG, "parentCategoryBOs : " + parentCategoryBOs.size());
		return parentCategoryBOs;
	}

	ArrayList<CategoryBO> sortcategories(ArrayList<CategoryBO> categoryBOs) {
		Collections.sort(categoryBOs, new CategoryPositionComparator());
		for (CategoryBO cat : categoryBOs)
			Log.e(TAG, "Name : " + cat + " Posi : " + cat.getPosition());
		return categoryBOs;
	}

	private class CategoryPositionComparator implements Comparator<CategoryBO> {

		@Override
		public int compare(CategoryBO lhs, CategoryBO rhs) {
			// TODO Auto-generated method stub
			return lhs.getPosition() - rhs.getPosition();
		}

	}
}
