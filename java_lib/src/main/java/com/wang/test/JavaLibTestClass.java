package com.wang.test;

import com.wang.java_util.GsonUtil;
import com.wang.demo.file_system.FileContent;
import com.wang.demo.file_system.FileNode;

import java.util.ArrayList;
import java.util.List;

public class JavaLibTestClass {

    public static void main(String[] args) {

        FileNode seaMusic = new FileNode(new FileContent("sea.mp3", ""), null, false);
        FileNode topMusic = new FileNode(new FileContent("top.mp3", ""), null, false);
        List<FileNode> musicList = new ArrayList<>();
        musicList.add(seaMusic);
        musicList.add(topMusic);
        FileNode musicDir = new FileNode(new FileContent("music", ""), musicList, true);

        FileNode seaVideo = new FileNode(new FileContent("sea.mp4", ""), null, false);
        FileNode topVideo = new FileNode(new FileContent("top.mp4", ""), null, false);
        List<FileNode> videoList = new ArrayList<>();
        videoList.add(seaVideo);
        videoList.add(topVideo);
        FileNode videoDir = new FileNode(new FileContent("video", ""), videoList, true);

        List<FileNode> mediaList = new ArrayList<>();
        mediaList.add(musicDir);
        mediaList.add(videoDir);
        FileNode mediaDir = new FileNode(new FileContent("media", ""), mediaList, true);

        GsonUtil.printFormatJson(mediaDir);

    }

}
