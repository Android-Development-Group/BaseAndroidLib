package com.cnc.mediaplayer.sdk.widget.playlist;

import com.cnc.mediaplayer.sdk.widget.quality.MediaPlayerVideoModule;

import java.util.List;

/**
 * @包名 ：com.cnc.mediaplayer.sdk.widget.playlist
 * @类名 ：MediaPlayerVideoEpisode
 * @时间 ：2018/02/05 10:44
 * @作者 ：linby （注释）
 * @版本 ：1.6.1
 * @描述 ：播放器的视频剧情
 */
public class MediaPlayerVideoEpisode {

    private String mUrl;
    private MediaPlayerVideoModule mDefaultQuality;
    private List<MediaPlayerVideoModule> mQualityList;
    /**
     * 全屏时显示在左上角的标题
     */
    private String mTitle;

    /**
     * 视频url
     * @param url
     */
    public MediaPlayerVideoEpisode(String url) {
        this.mUrl = url;
    }
    /**
     *
     * @param quality 包含视频url和清晰度
     * @param qualityList 与url对应的清晰度列表
     */
    public MediaPlayerVideoEpisode(MediaPlayerVideoModule quality, List<MediaPlayerVideoModule> qualityList) {
        this.mDefaultQuality = quality;
        this.mQualityList = qualityList;
    }

    public MediaPlayerVideoEpisode(MediaPlayerVideoModule quality, List<MediaPlayerVideoModule> qualityList, String title) {
        this.mDefaultQuality = quality;
        this.mQualityList = qualityList;
        this.mTitle = title;
    }

    /**
     * 当前剧情的url
     * @return
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * 获取清晰度剧情列表
     * @return
     */
    public List<MediaPlayerVideoModule> getQualityList() {
        return mQualityList;
    }

    /**
     * 获取默认剧情
     * @return 默认剧情
     */
    public MediaPlayerVideoModule getDefaultQuality() {
        return mDefaultQuality;
    }

    /**
     * 获取剧情标题
     * @return 剧情标题
     */
    public String getTitle() {
        return mTitle;
    }
}
