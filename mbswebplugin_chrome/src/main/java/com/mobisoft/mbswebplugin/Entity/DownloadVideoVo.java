package com.mobisoft.mbswebplugin.Entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author：Created by fan.xd on 2018/6/18.
 * Email：fang.xd@mobisoft.com.cn
 * Description：下载视频信息
 */
@Entity
public class DownloadVideoVo  implements Serializable  {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String title;// 视频标题

	private String video_description;// 视频简介

	private String coverForFeed;//

	@Unique
	private String course_no;// 课件编码

	@Convert(columnType = String.class, converter = VideosConverter.class)
	private List<Videos> videos;// 视频列表

	private String callBack;// 回掉方法

	private String status;// 下载状态
	private String column;// 栏目

	@Keep
	public DownloadVideoVo(Long id, String title, String video_description,
			String coverForFeed, String course_no, List<Videos> videos, String callBack,
			String status, String column) {
		this.id = id;
		this.title = title;
		this.video_description = video_description;
		this.coverForFeed = coverForFeed;
		this.course_no = course_no;
		this.videos = videos;
		this.callBack = callBack;
		this.status = status;
		this.column = column;
	}

	@Keep
	public DownloadVideoVo() {
	}

	public String getColumn() {

		return TextUtils.isEmpty(column) ? "column" : column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getStatus() {
		return status;
	}

	/**
	 * @param status notexist  不存在 downloading 正在下载 exist 已下载
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public String getCallBack() {
		return callBack;
	}

	public void setCallBack(String callBack) {
		this.callBack = callBack;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setVideo_description(String video_description) {
		this.video_description = video_description;
	}

	public String getVideo_description() {
		return this.video_description;
	}

	public void setCoverForFeed(String coverForFeed) {
		this.coverForFeed = coverForFeed;
	}

	public String getCoverForFeed() {
		return this.coverForFeed;
	}

	public void setCourse_no(String course_no) {
		this.course_no = course_no;
	}

	public String getCourse_no() {
		return this.course_no;
	}

	public void setVideos(List<Videos> videos) {
		this.videos = videos;
	}

	public List<Videos> getVideos() {
		return this.videos;
	}

	public boolean equals(DownloadVideoVo obj) {
		if (obj != null && TextUtils.equals(course_no, obj.course_no)) {
			return true;
		} else {
			return false;
		}

	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
