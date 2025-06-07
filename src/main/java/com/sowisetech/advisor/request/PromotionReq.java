package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class PromotionReq {

	private long promotionId;
	private String title;
	private String aboutVideo;
	private String video;
	private String imagePath;

	public long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(long promotionId) {
		this.promotionId = promotionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAboutVideo() {
		return aboutVideo;
	}

	public void setAboutVideo(String aboutVideo) {
		this.aboutVideo = aboutVideo;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
