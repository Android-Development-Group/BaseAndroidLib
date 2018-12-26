package com.cnc.mediaplayer.sdk.widget.quality;

/**
 * @包名 ：com.cnc.mediaplayer.sdk.widget.quality
 * @类名 ：MediaPlayerVideoModule
 * @时间 ：2018/02/05 10:58
 * @作者 ：linby （注释）
 * @版本 ：1.6.1
 * @描述 ：播放器的视频剧情
 */
public class MediaPlayerVideoModule {

    private String mName;
    private String mUrl;
    
    public MediaPlayerVideoModule(String name, String url) {
        this.mName = name;
        this.mUrl = url;
    }

    /**
     * 获取剧情名字
     * @return 剧情名字
     */
    public String getName() {
        return mName;
    }

    /**
     * 获取剧情url
     * @return 剧情url
     */
    public String getUrl() {
        return mUrl;
    }

}
