package com.mohan.sample3.bo;

import com.parse.ParseFile;
import com.parse.ParseObject;

public class DownlaodImageBO {
	String description, title, objectID, subCategoryID, superParentID;
	ParseFile actualImageParseFile;
	ParseObject mParseObject;

	public ParseObject getmParseObject() {
		return mParseObject;
	}

	public void setmParseObject(ParseObject mParseObject) {
		this.mParseObject = mParseObject;
	}

	public ParseFile getActualImageParseFile() {
		return actualImageParseFile;
	}

	public void setActualImageParseFile(ParseFile imageParseFile) {
		this.actualImageParseFile = imageParseFile;
	}

	public String getSuperParentID() {
		return superParentID;
	}

	public void setSuperParentID(String superParentID) {
		this.superParentID = superParentID;
	}

	byte[] thumbImageByteArray, actualImageByteArray;

	public String getDescription() {
		return description;
	}

	public String getSubCategoryID() {
		return subCategoryID;
	}

	public void setSubCategoryID(String subCategoryID) {
		this.subCategoryID = subCategoryID;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getObjectID() {
		return objectID;
	}

	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

	public byte[] getThumbImageByteArray() {
		return thumbImageByteArray;
	}

	public void setThumbImageByteArray(byte[] thumbImageByteArray) {
		this.thumbImageByteArray = thumbImageByteArray;
	}

	public byte[] getActualImageByteArray() {
		return actualImageByteArray;
	}

	public void setActualImageByteArray(byte[] actualImageByteArray) {
		this.actualImageByteArray = actualImageByteArray;
	}

}
