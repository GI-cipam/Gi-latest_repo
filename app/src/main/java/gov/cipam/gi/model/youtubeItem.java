package gov.cipam.gi.model;

/**
 * Created by hp on 1/1/2018.
 */

public class youtubeItem {
    private String title ;
    private String description;
    private String publishedAt;
    private String url;
    private String videoLink;

    public youtubeItem(String title, String description, String publishedAt, String url, String videoLink) {
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.url = url;
        this.videoLink = videoLink;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getUrl() {
        return url;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }
}
