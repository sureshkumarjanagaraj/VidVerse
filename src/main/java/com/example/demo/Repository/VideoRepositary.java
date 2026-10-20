package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Video;

public interface VideoRepositary extends JpaRepository<Video, Integer>{

}
