package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Entity.Video;
import com.example.demo.Service.VideoService;

@Controller
public class VideoController {

    @Autowired
    private VideoService videoService;


    // ==========================================
    // OPEN UPLOAD VIDEO PAGE
    // ==========================================

    @GetMapping("/uploadvideo")
    public String uploadVideoPage() {

        return "uploadVideo";
    }


    // ==========================================
    // SAVE VIDEO
    // ==========================================

    @PostMapping("/add-video")
    public String addVideo(
            @RequestParam String videoName,
            @RequestParam String heroName,
            @RequestParam String directorName,
            @RequestParam String bio,
            @RequestParam String videoUrl) {

        Video video = new Video();

        video.setVideoName(videoName);
        video.setHeroName(heroName);
        video.setDirectorName(directorName);
        video.setBio(bio);

        // Convert YouTube URL to embed URL
        String embedUrl = convertToEmbedUrl(videoUrl);

        video.setVideoUrl(embedUrl);

        // Save video into database
        videoService.addVideo(video);

        // After successful save
        // go to addVideo.html
        return "redirect:/add-video";
    }


    // ==========================================
    // SUCCESS PAGE
    // ==========================================

    @GetMapping("/add-video")
    public String addVideoSuccess(Model model) {

        model.addAttribute(
                "message",
                "Video Successfully Added!"
        );

        return "addVideo";
    }


    // ==========================================
    // CONVERT YOUTUBE URL
    // ==========================================

    private String convertToEmbedUrl(String url) {

        if (url.contains("watch?v=")) {

            String videoId =
                    url.substring(
                            url.indexOf("watch?v=") + 8
                    );

            if (videoId.contains("&")) {

                videoId =
                        videoId.substring(
                                0,
                                videoId.indexOf("&")
                        );
            }

            return "https://www.youtube.com/embed/" + videoId;
        }


        if (url.contains("youtu.be/")) {

            String videoId =
                    url.substring(
                            url.indexOf("youtu.be/") + 9
                    );

            if (videoId.contains("?")) {

                videoId =
                        videoId.substring(
                                0,
                                videoId.indexOf("?")
                        );
            }

            return "https://www.youtube.com/embed/" + videoId;
        }


        // Already an embed URL
        return url;
    }
}