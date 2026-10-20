package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Video;
import com.example.demo.Repository.VideoRepositary;

@Service
public class VideoService {

    @Autowired
    private VideoRepositary videoRepository;

    // Add video
    public Video addVideo(Video video) {
        return videoRepository.save(video);
    }

    // Get all videos
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    // Get video by ID
    public Video getVideoById(int id) {
        return videoRepository.findById(id).orElse(null);
    }

    // Delete video
    public void deleteVideo(int id) {
        videoRepository.deleteById(id);
    }
}