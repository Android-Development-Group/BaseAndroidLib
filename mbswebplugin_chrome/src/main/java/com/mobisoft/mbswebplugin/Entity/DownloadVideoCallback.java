package com.mobisoft.mbswebplugin.Entity;

/**
 * Author：Created by fan.xd on 2018/6/18.
 * Email：fang.xd@mobisoft.com.cn
 * Description：下载视频回掉
 */
public class DownloadVideoCallback {

	private int totalSize;//总进度
	private int currentSize;//当前进度
	private String course_no;// 课程
	private String courseItem_no;// 子课程
	private int progress;//进度

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public int getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}

	public String getCourse_no() {
		return course_no;
	}

	public void setCourse_no(String course_no) {
		this.course_no = course_no;
	}

	public String getCourseItem_no() {
		return courseItem_no;
	}

	public void setCourseItem_no(String courseItem_no) {
		this.courseItem_no = courseItem_no;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}
}
