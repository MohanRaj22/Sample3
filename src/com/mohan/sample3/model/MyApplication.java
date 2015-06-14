package com.mohan.sample3.model;

import java.util.ArrayList;
import java.util.Map;

import com.mohan.sample3.bo.CategoryBO;
import com.mohan.sample3.bo.DownlaodImageBO;
import com.mohan.sample3.helper.CategoryHelper;
import com.mohan.sample3.helper.ParseHelper;
import com.parse.Parse;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class MyApplication extends Application {
	ParseHelper parseHelper;
	CategoryHelper categoryHelper;
	ArrayList<CategoryBO> categoryBOs;
	ArrayList<CategoryBO> levelOneCatgories;
	Map<String, ArrayList<CategoryBO>> levelTwoCategories;
	Map<String, ArrayList<CategoryBO>> levelThreeCategories;
	Map<String, ArrayList<CategoryBO>> levelFourCategories;
	Map<String, ArrayList<DownlaodImageBO>> downloadImagesMap;

	public Map<String, ArrayList<DownlaodImageBO>> getDownloadImagesMap() {
		return downloadImagesMap;
	}

	public void setDownloadImagesMap(
			Map<String, ArrayList<DownlaodImageBO>> downloadImagesMap) {
		this.downloadImagesMap = downloadImagesMap;
	}

	CategoryBO selecCategoryBO;
	DownlaodImageBO selectedImageBO;

	public DownlaodImageBO getSelectedImageBO() {
		return selectedImageBO;
	}

	public void setSelectedImageBO(DownlaodImageBO selectedImageBO) {
		this.selectedImageBO = selectedImageBO;
	}

	public CategoryBO getSelecCategoryBO() {
		return selecCategoryBO;
	}

	public void setSelecCategoryBO(CategoryBO selecCategoryBO) {
		this.selecCategoryBO = selecCategoryBO;
	}

	public ArrayList<CategoryBO> getCategoryBOs() {
		return categoryBOs;
	}

	public void setCategoryBOs(ArrayList<CategoryBO> categoryBOs) {
		this.categoryBOs = categoryBOs;
	}

	public CategoryHelper getCategoryHelper() {
		return categoryHelper != null ? categoryHelper : CategoryHelper
				.inItCategoryHelper(getApplicationContext());
	}

	public void setCategoryHelper(CategoryHelper categoryHelper) {
		this.categoryHelper = categoryHelper;
	}

	public ArrayList<CategoryBO> getLevelOneCatgories() {
		return levelOneCatgories;
	}

	public void setLevelOneCatgories(ArrayList<CategoryBO> levelOneCatgories) {
		this.levelOneCatgories = levelOneCatgories;
	}

	public Map<String, ArrayList<CategoryBO>> getLevelTwoCategories() {
		return levelTwoCategories;
	}

	public void setLevelTwoCategories(
			Map<String, ArrayList<CategoryBO>> levelTwoCategories) {
		this.levelTwoCategories = levelTwoCategories;
	}

	public Map<String, ArrayList<CategoryBO>> getLevelThreeCategories() {
		return levelThreeCategories;
	}

	public void setLevelThreeCategories(
			Map<String, ArrayList<CategoryBO>> levelThreeCategories) {
		this.levelThreeCategories = levelThreeCategories;
	}

	public Map<String, ArrayList<CategoryBO>> getLevelFourCategories() {
		return levelFourCategories;
	}

	public void setLevelFourCategories(
			Map<String, ArrayList<CategoryBO>> levelFourCategories) {
		this.levelFourCategories = levelFourCategories;
	}

	public ParseHelper getParseHelper() {
		return parseHelper != null ? parseHelper : ParseHelper
				.initParseHelper(getApplicationContext());
	}

	public void setParseHelper(ParseHelper parseHelper) {
		this.parseHelper = parseHelper;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Parse.enableLocalDatastore(getApplicationContext());
		Parse.initialize(this, "PMipl2gmbDK4b1UTaBBb9XTU8VADHnpPtiZxoVEo",
				"12DLcXOAgsqNZLn6lp7oYwKsSoqWPNdi3WjDJ6T2");
		setParseHelper(ParseHelper.initParseHelper(getApplicationContext()));
		setCategoryHelper(CategoryHelper
				.inItCategoryHelper(getApplicationContext()));
	}

	public void showDialogBox(final Context mContext, String controlMessage1,
			String controlMessage2, String content) {

		final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
				mContext);
		alertDialog.setMessage(content);
		alertDialog.setCancelable(false);
		alertDialog.setPositiveButton(controlMessage1, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (mContext.getClass().toString()
						.equalsIgnoreCase("ImageDetailActivity")) {
					((Activity) mContext).finish();
					alertDialog.show();
					return;
				} else if (mContext.getClass().toString()
						.equalsIgnoreCase("HomeActivity")) {
					alertDialog.show();
					return;
				}
			}
		});
		if (controlMessage2 != null) {
			alertDialog.setNegativeButton(controlMessage2,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
		}
		alertDialog.show();
	}
}
