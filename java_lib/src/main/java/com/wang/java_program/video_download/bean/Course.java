package com.wang.java_program.video_download.bean;

import java.util.List;

public class Course {

    private String courseName;// 课程名称
    private List<String> chapterNames;// 章节名称
    private List<Video> videos;
    private List<CourseDataFile> courseDataFiles;// 课程资料
    private String courseHint;// 课程学习提示

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<String> getChapterNames() {
        return chapterNames;
    }

    public void setChapterNames(List<String> chapterNames) {
        this.chapterNames = chapterNames;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<CourseDataFile> getCourseDataFiles() {
        return courseDataFiles;
    }

    public void setCourseDataFiles(List<CourseDataFile> courseDataFiles) {
        this.courseDataFiles = courseDataFiles;
    }

    public String getCourseHint() {
        return courseHint;
    }

    public void setCourseHint(String courseHint) {
        this.courseHint = courseHint;
    }
}
