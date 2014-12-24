package com.eujeux.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameInfo implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	
	public long id;
	public String name;
	public String blobKeyString;
	public int downloads;
	public Date uploadDate;
	public long creatorId;
	public String creatorName;
	public int majorVersion, minorVersion;
	public String description;
	public long lastEdited;
	public int rating, ratingImpressive, ratingFun, ratingCreative;
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat();	

	public String getVersionString() {
		return majorVersion + "." + minorVersion;
	}
	
	public String getUploadDateString() {
		return dateFormat.format(uploadDate);
	}
	
	@Override
	public GameInfo clone() {
		try {
			GameInfo cloned = (GameInfo)super.clone();
			cloned.uploadDate = (Date)uploadDate.clone();
			return cloned;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Override
	public String toString() {
		return name + " " + getVersionString();
	}
}
