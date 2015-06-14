package com.mohan.sample3.helper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mohan.sample3.bo.CategoryBO;
import com.mohan.sample3.bo.DownlaodImageBO;
import com.mohan.sample3.bo.UploadImageBO;
import com.mohan.sample3.model.MyApplication;
import com.mohan.sample3.model.StaticData;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ParseHelper {
	public String TAG = "ParseHelper";
	ParseHelper mParseHelper;
	MyApplication mApplication;
	Context mContext;
	Handler mHandler;
	ArrayList<UploadImageBO> uploadImageList;
	ArrayList<DownlaodImageBO> mDownlaodImageBOs;
	Message mMessage;

	public ArrayList<DownlaodImageBO> getmDownlaodImageBOs() {
		return mDownlaodImageBOs;
	}

	public void setmDownlaodImageBOs(
			ArrayList<DownlaodImageBO> mDownlaodImageBOs) {
		this.mDownlaodImageBOs = mDownlaodImageBOs;
	}

	public static ParseHelper initParseHelper(Context mContext) {
		return new ParseHelper(mContext);
	}

	public ParseHelper(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		mApplication = (MyApplication) mContext.getApplicationContext();
	}

	public void saveParseData(Context mContext, Handler mHandler,
			ArrayList<UploadImageBO> uploadImageList) {
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.uploadImageList = uploadImageList;
		new UploadImageAsyncTask().execute();
	}

	public class UploadImageAsyncTask extends AsyncTask<Void, Void, Void> {
		int successImageCount = 0;
		int totalImageCount = uploadImageList.size();
		Message mMessage = new Message();
		ParseObject mParseObject = null;
		ParseFile mParseFile = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			for (UploadImageBO uploadImageBO : uploadImageList) {
				File file = new File(uploadImageBO.getPath());
				if (!file.exists()) {
					Log.i(TAG, "No Image Found at " + file.getAbsolutePath());
					continue;
				} else {
					mParseFile = null;
					try {
						mParseFile = new ParseFile("image.jpg",
								readFile(file.getAbsolutePath()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.i(TAG, "mParseFile : " + mParseFile == null ? "Yes"
							: "No");
					mMessage = new Message();
					mMessage.arg1 = 0;
					mMessage.obj = "Connecting Serevr";
					mHandler.sendMessage(mMessage);
					mParseFile.saveInBackground(new SaveCallback() {

						@Override
						public void done(ParseException arg0) {
							// TODO Auto-generated method stub
							if (arg0 == null) {
								Log.i(TAG, " mParseFile Success");
								mParseObject = new ParseObject("Mohan");
								mParseObject.put("image", mParseFile);
								mMessage = new Message();
								mMessage.arg1 = 0;
								mMessage.obj = "Uploading Image 0 Out Of "
										+ totalImageCount;
								mHandler.sendMessage(mMessage);
								mParseObject
										.saveInBackground(new SaveCallback() {

											@Override
											public void done(
													ParseException error) {
												// TODO Auto-generated method
												// stub

												if (error == null) {
													// Success
													Log.i(TAG,
															" mParseObject Success");
													successImageCount++;
													mMessage = new Message();
													mMessage.arg1 = 0;
													mMessage.obj = "Uploading Image "
															+ successImageCount
															+ " Out Of "
															+ totalImageCount;
													mHandler.sendMessage(mMessage);
												} else {
													// Failure
													Log.i(TAG,
															"mParseObject Failure");
													error.printStackTrace();
													mMessage = new Message();
													mMessage.arg1 = -1;
													mMessage.obj = "Upload Failure ! Try Again ! !";
													mHandler.sendMessage(mMessage);

												}
												Log.i(TAG,
														"successImageCount  : "
																+ successImageCount
																+ " totalImageCount : "
																+ totalImageCount);
												if (successImageCount == totalImageCount) {
													mMessage = new Message();
													mMessage.arg1 = 1;
													mMessage.obj = "Upload Successful";
													if (mHandler != null) {
														mHandler.sendMessage(mMessage);
													}
												}

											}
										});

							} else {
								Log.i(TAG, "mParseFile Failure");
								arg0.printStackTrace();
							}

						}

					});

				}
			}
			return null;
		}
	}

	public void downloadCategory(Handler mHandler) {
		this.mHandler = mHandler;
		mMessage = new Message();
		new DownloadCategory().execute();
	}

	public void retriveParseLocalData(Handler mHandler) {
		this.mHandler = mHandler;
		mMessage = new Message();
		new RetriveParseCategoriesLocalData().execute();
	}

	public class DownloadCategory extends AsyncTask<Void, Void, Void> {
		int pinnedCount = 0;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Message mMessage = new Message();
			ParseQuery<ParseObject> mParseQuery = ParseQuery
					.getQuery("Category");
			mParseQuery.setLimit(1000);
			mMessage.arg1 = StaticData.CATEGORY_DOWNLOAD_STARTED;
			mMessage.obj = "Connecting Server";
			mHandler.sendMessage(mMessage);
			// mMessage.what = 0;
			// mMessage.obj = "Connecting to server";
			// mHandler.sendMessage(mMessage);
			mParseQuery.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(final List<ParseObject> result,
						ParseException exception) {
					// TODO Auto-generated method stub
					if (exception == null) {
						Log.i(TAG, "Downloaded");
						mMessage = new Message();
						mMessage.what = StaticData.CATEGORY_DOWNLOAD_IN_PROGRESS;
						mMessage.obj = "Proccesing Data";
						mHandler.sendMessage(mMessage);
						processcategoryParseData(result);
						for (ParseObject mParseObject : result) {
							Log.i(TAG,
									"Pinning id : "
											+ mParseObject.getObjectId());
							mParseObject.pinInBackground(new SaveCallback() {

								@Override
								public void done(ParseException exception) {
									// TODO Auto-generated method stub
									if (exception == null) {
										pinnedCount++;
										if (pinnedCount == result.size()) {
											Log.i(TAG, "Pinning Done");
											mMessage = new Message();
											mMessage.what = StaticData.CATEGORY_DOWNLOAD_FINISHED;
											mMessage.obj = "Download Completed";
											mHandler.sendMessage(mMessage);
											Log.i(TAG, "Done");
										}
									}
								}
							});
						}

					} else {
						exception.printStackTrace();
						mMessage = new Message();
						mMessage.what = StaticData.CATEGORY_DOWNLOAD_FAILED;
						mMessage.obj = "Download Failed";
						mHandler.sendMessage(mMessage);
					}
				}
			});
			return null;
		}
	}

	public class RetriveParseCategoriesLocalData extends
			AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			mMessage = new Message();
			mMessage.what = StaticData.LOADING_CATEGORY_FROM_LOACAL_IN_PROGRESS;
			mMessage.obj = "RetriveParseCategoriesLocalData : Fetching Data";
			mHandler.sendMessage(mMessage);
			ParseQuery<ParseObject> mParseQuery = ParseQuery
					.getQuery("Category");
			mParseQuery.setLimit(1000);
			mParseQuery.fromLocalDatastore();
			Log.i(TAG, "Limit : " + mParseQuery.getLimit());
			mParseQuery.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> results, ParseException error) {
					// TODO Auto-generated method stub
					if (error == null) {
						Log.i(TAG, " RetriveParseLocalData Results : "
								+ results.size());
						processcategoryParseData(results);
						mMessage = new Message();
						mMessage.what = StaticData.LOADING_CATEGORY_FROM_LOACAL_FINISHED;
						mMessage.obj = "Fetching Data";
						mHandler.sendMessage(mMessage);

					} else
						error.printStackTrace();
				}
			});
			return null;
		}
	}

	public void downloadImages(Handler mHandler) {
		this.mHandler = mHandler;
		new DownloadImagesAsycTask().execute();
	}

	private class DownloadImagesAsycTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			mMessage = new Message();
			mMessage.what = StaticData.IMAGE_DOWNLOAD_STARTED;
			mMessage.obj = "DownloadImagesAsycTask : Connecting Server";
			mHandler.sendMessage(mMessage);
			ParseQuery<ParseObject> mParseQuery = ParseQuery.getQuery("Beach");
			// mParseQuery.whereEqualTo("SuperParentId", mApplication
			// .getSelecCategoryBO().getCategoryID());
			mParseQuery.whereEqualTo("SuperParentId", "wic0lLoZcs");

			mParseQuery.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> results, ParseException error) {
					// TODO Auto-generated method stub
					if (error == null) {
						Log.i(TAG,
								"DownloadImagesAsycTask result : "
										+ results.size());
						mMessage = new Message();
						mMessage.what = StaticData.IMAGE_DOWNLOAD_IN_PROGRESS;
						mMessage.obj = "DownloadImagesAsycTask : Downloading Data";
						mHandler.sendMessage(mMessage);
						for (ParseObject mParseObject : results) {
							mParseObject.pinInBackground(new SaveCallback() {
								@Override
								public void done(ParseException exception) {
									// TODO Auto-generated method stub

								}
							});
						}
						mMessage = new Message();
						mMessage.what = StaticData.IMAGE_DOWNLOAD_FINISHED;
						mMessage.obj = "Downloading Data";
						mHandler.sendMessage(mMessage);
						// done
					} else {
						error.printStackTrace();
						mMessage = new Message();
						mMessage.what = StaticData.IMAGE_DOWNLOAD_FINISHED;
						mMessage.obj = "Download Failed";
						mHandler.sendMessage(mMessage);
					}
				}
			});
			return null;
		}
	}

	public void loadParseImageData(Handler mHandler) {
		this.mHandler = mHandler;
		new LoadParseImageData().execute();

	}

	public class LoadParseImageData extends AsyncTask<Void, Void, Void> {
		HashMap<String, ArrayList<DownlaodImageBO>> imagesHashMap;
		int processedImageCount = 0;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			mMessage = new Message();
			mMessage.what = StaticData.LOADING_IMAGES_FROM_LOACAL_IN_PROGRESS;
			mMessage.obj = "LoadParseImageData : Loading Data";
			mHandler.sendMessage(mMessage);
			ParseQuery<ParseObject> mParseQuery = ParseQuery.getQuery("Beach");
			mParseQuery.fromLocalDatastore();
			mParseQuery.whereEqualTo("SuperParentId", "wic0lLoZcs");
			mParseQuery.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(final List<ParseObject> results,
						ParseException exception) {
					// TODO Auto-generated method stub
					if (exception == null) {
						imagesHashMap = new HashMap<String, ArrayList<DownlaodImageBO>>();
						mMessage = new Message();
						mMessage.what = StaticData.LOADING_IMAGES_FROM_LOACAL_IN_PROGRESS;
						mMessage.obj = "LoadParseImageData : Processing Data";
						mHandler.sendMessage(mMessage);
						for (final ParseObject parseObject : results) {
							final DownlaodImageBO downlaodImageBO = new DownlaodImageBO();
							ParseFile parseFile = (ParseFile) parseObject
									.get("thumbnail");
							parseFile
									.getDataInBackground(new GetDataCallback() {
										@Override
										public void done(byte[] data,
												ParseException exception) {
											// TODO Auto-generated method stub
											if (exception == null) {
												Log.i(TAG,
														"parseFile.getDataInBackground");
												processedImageCount++;
												downlaodImageBO
														.setmParseObject(parseObject);
												downlaodImageBO
														.setThumbImageByteArray(data);
												downlaodImageBO
														.setDescription(parseObject
																.getString("Description"));
												downlaodImageBO
														.setObjectID(parseObject
																.getObjectId());
												downlaodImageBO
														.setSubCategoryID(parseObject
																.getString("LinkId"));
												downlaodImageBO
														.setSuperParentID(parseObject
																.getString("SuperParentId"));

												ParseFile actualImageFile = (ParseFile) parseObject
														.get("imageFile");
												downlaodImageBO
														.setActualImageParseFile(actualImageFile);
												// parseObject.put(
												// "localThumbNail", data);
												parseObject.pinInBackground();

												if ((imagesHashMap.get(downlaodImageBO
														.getSubCategoryID()) == null)
														|| imagesHashMap
																.get(downlaodImageBO
																		.getSubCategoryID())
																.isEmpty()) {
													ArrayList<DownlaodImageBO> downlaodImageBOs = new ArrayList<DownlaodImageBO>();
													downlaodImageBOs
															.add(downlaodImageBO);
													imagesHashMap.put(
															downlaodImageBO
																	.getSubCategoryID(),
															downlaodImageBOs);
												} else
													imagesHashMap
															.get(downlaodImageBO
																	.getSubCategoryID())
															.add(downlaodImageBO);
												Log.i(TAG,
														"getSubCategoryID() : "
																+ downlaodImageBO
																		.getSubCategoryID()
																+ " Link : "
																+ downlaodImageBO
																		.getSuperParentID());
												Log.i(TAG,
														"results.size() : "
																+ results
																		.size()
																+ " processedImageCount : "
																+ processedImageCount);
												if (results.size() == processedImageCount) {
													Log.i(TAG,
															"imagesHashMap : "
																	+ imagesHashMap
																			.size());
													mApplication
															.setDownloadImagesMap(imagesHashMap);
													mMessage = new Message();
													mMessage.what = StaticData.LOADING_IMAGES_FROM_LOACAL_FINISHED;
													mMessage.obj = "Download Success";
													mHandler.sendMessage(mMessage);
												}

											} else {
												exception.printStackTrace();
											}
										}

									});
						}
						Log.i(TAG, "Done ");

					} else {
						exception.printStackTrace();
						mMessage = new Message();
						mMessage.what = StaticData.IMAGE_DOWNLOAD_FAILED;
						mMessage.obj = "Download Failed";
						mHandler.sendMessage(mMessage);
					}
				}
			});
			return null;
		}
	}

	public ParseHelper() {
		// TODO Auto-generated constructor stub
	}

	public void downloadActualImage(Handler mHandler) {
		this.mHandler = mHandler;

		new DownloadActualImage().execute();
	}

	public class DownloadActualImage extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ParseFile parseFile = mApplication.getSelectedImageBO()
					.getActualImageParseFile();
			mMessage = new Message();
			mMessage.what = StaticData.IMAGE_DOWNLOAD_STARTED;
			mMessage.obj = "DownloadActualImage : Connecting to server";
			mHandler.sendMessage(mMessage);
			parseFile.getDataInBackground(new GetDataCallback() {

				@Override
				public void done(byte[] data, ParseException error) {
					// TODO Auto-generated method stub
					if (error == null) {
						mMessage = new Message();
						mMessage.what = StaticData.ACTUAL_IMAGE_DOWNLOAD_IN_PROGRESS;
						mMessage.obj = "DownloadActualImage : Processing";
						mHandler.sendMessage(mMessage);
						int index = mApplication
								.getDownloadImagesMap()
								.get(mApplication.getSelectedImageBO()
										.getSubCategoryID())
								.indexOf(mApplication.getSelectedImageBO());
						Log.i(TAG, "index : " + index);
						mApplication
								.getDownloadImagesMap()
								.get(mApplication.getSelectedImageBO()
										.getSubCategoryID()).get(index)
								.setActualImageByteArray(data);
						mApplication.getSelectedImageBO()
								.setActualImageByteArray(data);
						Log.i(TAG, "data : " + data);
						// mApplication.getSelectedImageBO().getmParseObject()
						// .put("actualImageByteArray", data);
						mApplication.getSelectedImageBO().getmParseObject()
								.pinInBackground();
						mMessage = new Message();
						mMessage.what = StaticData.ACTUAL_IMAGE_DOWNLOAD_FINISHED;
						mMessage.obj = "Download Completed";
						mHandler.sendMessage(mMessage);
					} else {
						error.printStackTrace();
						mMessage = new Message();
						mMessage.what = StaticData.ACTUAL_IMAGE_DOWNLOAD_FAILED;
						mMessage.obj = "Download Failed";
						mHandler.sendMessage(mMessage);
					}
				}
			});
			return null;
		}
	}

	public static byte[] readFile(String file) throws IOException {
		return readFile(new File(file));
	}

	public static byte[] readFile(File file) throws IOException {
		// Open file
		RandomAccessFile f = new RandomAccessFile(file, "r");
		try {
			// Get and check length
			long longlength = f.length();
			int length = (int) longlength;
			if (length != longlength)
				throw new IOException("File size >= 2 GB");
			// Read file and return data
			byte[] data = new byte[length];
			f.readFully(data);
			return data;
		} finally {
			f.close();
		}
	}

	public void processcategoryParseData(List<ParseObject> parseObjects) {
		ArrayList<CategoryBO> categoryBOs = new ArrayList<CategoryBO>();
		for (ParseObject mParseObject : parseObjects) {
			Log.i(TAG,
					"mParseObject : " + mParseObject.getString("CategoryName")
							+ " ID  :" + mParseObject.getObjectId());
			CategoryBO mCategoryBO = new CategoryBO();
			mCategoryBO.setActive(mParseObject.getInt("isActive") == 1);
			mCategoryBO.setCategoryID(mParseObject.getObjectId());
			mCategoryBO.setCategoryName(mParseObject.getString("CategoryName"));
			mCategoryBO.setLevel(mParseObject.getInt("Level"));
			mCategoryBO.setModifiedDate(mParseObject.getString("updatedAt"));
			mCategoryBO.setParentID(mParseObject.getString("ParentId"));
			mCategoryBO.setHasChild(mParseObject.getInt("hasChild") == 1);
			mCategoryBO.setPosition(mParseObject.getInt("position"));
			mCategoryBO.setIconName(mParseObject.getString("iconName"));

			categoryBOs.add(mCategoryBO);
		}
		mApplication.getCategoryHelper().setCategoryBOs(categoryBOs);
		mMessage = new Message();
		mMessage.what = StaticData.CATEGORY_DATA_PROCESSING_FINISHED;
		mMessage.obj = "Proccesing Data";
		mHandler.sendMessage(mMessage);
	}
}
