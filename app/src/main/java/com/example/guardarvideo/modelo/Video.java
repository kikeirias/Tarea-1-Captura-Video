package com.example.guardarvideo.modelo;

public class Video {
    private String videoPath;

    public Video() {
    }

    public Video(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}