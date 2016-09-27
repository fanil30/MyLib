package com.wang.java_program.video_download.website;

/*
import com.google.gson.Gson;
import com.wang.java_program.video_download.bean.Course;
import com.wang.java_program.video_download.bean.CourseDataFile;
import com.wang.java_program.video_download.bean.Video;
import com.wang.java_util.DebugUtil;
import com.wang.java_util.FileUtil;
import com.wang.java_util.HttpUtil;
import com.wang.java_util.JsonFormatUtil;
import com.wang.java_util.TextUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
*/
public class JiKeXueYuan {
/*
    public static final String url = "http://www.jikexueyuan.com";

    // 获取课程资料的url
    private static final String courseDataFileUrl = "http://www.jikexueyuan.com/course/downloadRes?course_id=";

    private static String cookie;

    @Override
    public Result getCourse(String strUrl) {

        Result result = new Result();

        String html;

        HttpUtil.Result r = HttpUtil.request(strUrl, "GET", getCookie());
        if (r.state == HttpUtil.OK) {
            html = r.result;
        } else {
            return null;
        }

        if (html == null) {
            result.status = IVideoWebSite.Result.ERROR;
            result.msg = DebugUtil.addMsg("html获取失败", false);
            return result;
        }

        // 获取课程名称
        String courseName = TextUtil.correctFileName(getCourseName(html), "_");

        // 如果有保存course对象的缓存文件，则直接读取并返回
        File cacheFile = new File(courseName + "/" + "cache.txt");
        if (cacheFile.exists()) {
            result.status = IVideoWebSite.Result.OK;
            result.course = new Gson().fromJson(FileUtil.read(cacheFile.getAbsolutePath()), Course.class);
            return result;

        }

        // 获取该课程每一节视频的标题
        List<String> videoTitles = getVideoTitles(html);
        int len = videoTitles.size();

        for (int i = 0; i < len; i++) {
            String s = TextUtil.correctFileName(videoTitles.get(i), "_");
            videoTitles.add(s);
        }

        // 先获取课程页面的视频（也就是第一个视频）的Url
        List<String> videoPageUrl = new ArrayList<>();
        List<String> videoRealUrl = new ArrayList<>();
        videoPageUrl.add(strUrl);
        videoRealUrl.add(getVideoUrl(html));

        // 获取第2，3，4...个视频的Url
        for (int i = 1; i < len; i++) {
            String nextUrl = strUrl.replace(".html", "_" + (i + 1) + ".html");
            videoPageUrl.add(nextUrl);
            r = HttpUtil.request(nextUrl, getCookie(), "GET");

            if (r.state == HttpUtil.OK) {
                videoRealUrl.add(getVideoUrl(r.result));

            } else {
                result.status = IVideoWebSite.Result.ERROR;
                result.msg = DebugUtil.addMsg("nextUrl的html获取失败", false);
                return result;
            }
        }

        // 获取课程资料文件的名字和url
        String courseId = "";
        CourseDataFile courseDataFile = null;
        int i = strUrl.indexOf("course/") + 7;
        int j = strUrl.indexOf(".html");
        if (i != -1 && j != -1) {
            courseId = strUrl.substring(i, j);
            courseDataFile = getCourseDataFile(courseId);
        }

        // 最后把所有信息汇总到course对象
        Course course = new Course();
        course.courseName = courseName;
        course.videos = new ArrayList<Video>();
        for (i = 0; i < len; i++) {
            Video video = new Video();
            video.title = videoTitles.get(i) + ".mp4";
            video.pageUrl = videoPageUrl.get(i);
            video.realUrl = videoRealUrl.get(i);
            course.videos.add(video);
        }
        course.courseDataFiles = new ArrayList<CourseDataFile>();
        course.courseDataFiles.add(courseDataFile);

        // 获取课程信息后保存course对象到缓存文件中，以备下次使用
        try {
            new File(TextUtil.correctFileName(courseName)).mkdirs();
//            FileUtil.writeObject(course, cacheFile.getAbsolutePath());
            String json = JsonFormatUtil.formatJson(new Gson().toJson(course));
            FileUtil.write(json, cacheFile.getAbsolutePath());
        } catch (Exception e) {
            DebugUtil.SystemOutPrintln("保存缓存文件失败: " + e.toString());
        }

        result.status = IVideoWebSite.Result.OK;
        result.course = course;
        return result;
    }

    // 获取课程名称
    private String getCourseName(String html) {
        int i = html.indexOf("<title>");
        int j = html.indexOf("</title>");
        return html.substring(i + 7, j);
    }

    // 获取该课程每一节视频的标题
    private List<String> getVideoTitles(String html) {
        List<String> videoTitles = new ArrayList<String>();

        Document doc = Jsoup.parse(html);
        Elements list = doc.getElementsByClass("lessonvideo-list");
        list = list.select("a[href]");

        for (int i = 0; i < list.size(); i++) {
            videoTitles.add(list.get(i).text());
        }

        return videoTitles;
    }

    // 获取该网页的视频的url
    private String getVideoUrl(String html) {
        int i = html.indexOf("source src");
        int j = html.indexOf("\" type=\"video");
        System.out.println("开始获取url");
        System.out.println("视频url： " + html.substring(i + 12, j));
        return html.substring(i + 12, j);
    }

    private CourseDataFile getCourseDataFile(String courseId) {

        CourseDataFile courseDataFile = new CourseDataFile();

        HttpUtil.Result r = HttpUtil.request(courseDataFileUrl + courseId,
                getCookie(), "GET", null);
        String json = r.result;
        String name;
        String url;
        // 先通过i,j获取资料文件的url
        int i = json.indexOf("http");
        int j = json.indexOf("\"}");
        if (i != -1 && j != -1) {
            url = json.substring(i, j);

        } else {
            return null;
        }

        // 再通过i,j获取资料文件名
        String postName = ".zip";
        i = json.indexOf("file/") + 5;
        j = json.indexOf(postName);
        if (j == -1) {
            postName = ".rar";
            j = json.indexOf(postName);
        }

        if (i != -1 && j != -1) {
            name = json.substring(i, j) + postName;

        } else {
            return null;
        }

        courseDataFile.name = TextUtil.correctFileName(name);
        courseDataFile.url = url;

        return courseDataFile;
    }

    @Override
    public String getCookie() {
        try {
            if (TextUtil.isEmpty(cookie)) {
                cookie = FileUtil.read("jikexueyuan");
            }
            return cookie;
        } catch (Exception e) {
            return "";
        }
    }
*/
}
