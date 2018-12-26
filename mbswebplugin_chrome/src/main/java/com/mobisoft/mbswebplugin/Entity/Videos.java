package com.mobisoft.mbswebplugin.Entity;

import android.util.Log;

import java.io.Serializable;

/**
 * Author：Created by fan.xd on 2018/6/18.
 * Email：fang.xd@mobisoft.com.cn
 * Description：下载视频的 信息
 */
public class Videos implements Serializable {
	private static final long serialVersionUID = 1L;

	private String courseItem_no;

	private String playUrl;

	private boolean isDownload = false;

	/**
	 * 默认替换图片
	 */
	private String placeholderurl;
	/**
	 * 默认替换图片
	 */
	private String coverForFeed;


	private String liveFlag;

	private String videoPercentage;

	private String course_no;


	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean download) {
		Log.i("测试i", "setIsDownload--->" + isDownload);
		isDownload = download;
	}

	public String getCoverForFeed() {
		return coverForFeed;
	}

	public void setCoverForFeed(String coverForFeed) {
		this.coverForFeed = coverForFeed;
	}

	public void setCourseItem_no(String courseItem_no) {
		this.courseItem_no = courseItem_no;
	}

	public String getCourseItem_no() {
		return this.courseItem_no;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public String getPlayUrl() {

//		if (BuildConfig.DEBUG)
//			return "http://140.207.247.17/vlive.qqvideo.tc.qq.com" +
//					"/Aa-93tj-7SvJMWUfymSiTEsQqrTHRWf8bw9sdnsJrIv4/m0200lnno7i.p2" +
//					"01.1.mp4?vkey=3B2C4447A9B5CF1A970ACF78C5C8766DAD0F43246492585B4C" +
//					"D55EEB5BB085C3963059CEA310ABA883D5E4450FB77C6F95550E01D6791B4135" +
//					"CD5AF64A78118C7686AB1CDC53495B84C06C9247FA782D8386470D52790875D6" +
//					"CCD000F33BD6C4DD574A56982C6F38436F77509A26B1453E2B8825B0354E6D&pl" +
//					"atform=10201&sdtfrom=&fmt=shd&level" +
//					"=0&locid=c20b7698-fd12-4653-8d82-8446242116da&size=2500998&ocid=211948972\n";
//		else
		return this.playUrl;
	}

	public String getPlaceholderurl() {
		return placeholderurl;
	}

	public void setPlaceholderurl(String placeholderurl) {
		this.placeholderurl = placeholderurl;
	}

	public String getLiveFlag() {
		return liveFlag;
	}

	public void setLiveFlag(String liveFlag) {
		this.liveFlag = liveFlag;
	}

	public String getVideoPercentage() {
		return videoPercentage;
	}

	public void setVideoPercentage(String videoPercentage) {
		this.videoPercentage = videoPercentage;
	}

	public String getCourse_no() {
		return course_no;
	}

	public void setCourse_no(String course_no) {
		this.course_no = course_no;
	}
}
