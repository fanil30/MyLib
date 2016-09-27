package com.wang.android_lib.util;

import java.util.List;

/**
 * Created by Administrator on 2016/3/3.
 */
public class BaiduWeather {

    /**
     * error : 0
     * status : success
     * date : 2016-03-03
     * results : [{"currentCity":"广州","pm25":"51","index":[{"title":"穿衣","zs":"舒适","tipt":"穿衣指数","des":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"},{"title":"洗车","zs":"较适宜","tipt":"洗车指数","des":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"},{"title":"旅游","zs":"适宜","tipt":"旅游指数","des":"天气较好，温度适宜，是个好天气哦。这样的天气适宜旅游，您可以尽情地享受大自然的风光。"},{"title":"感冒","zs":"较易发","tipt":"感冒指数","des":"昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。"},{"title":"运动","zs":"较适宜","tipt":"运动指数","des":"天气较好，无雨水困扰，较适宜进行各种运动，但因气温较低，在户外运动请注意增减衣物。"},{"title":"紫外线强度","zs":"中等","tipt":"紫外线强度指数","des":"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"}],"weather_data":[{"date":"周四 03月03日 (实时：24℃)","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/qing.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/duoyun.png","weather":"晴转多云","wind":"微风","temperature":"24 ~ 13℃"},{"date":"周五","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/duoyun.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/duoyun.png","weather":"多云","wind":"微风","temperature":"24 ~ 15℃"},{"date":"周六","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/yin.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/yin.png","weather":"阴","wind":"微风","temperature":"23 ~ 16℃"},{"date":"周日","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/duoyun.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/yin.png","weather":"多云转阴","wind":"微风","temperature":"25 ~ 16℃"}]}]
     */

    private int error;
    private String status;
    private String date;
    /**
     * currentCity : 广州
     * pm25 : 51
     * index : [{"title":"穿衣","zs":"舒适","tipt":"穿衣指数","des":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"},{"title":"洗车","zs":"较适宜","tipt":"洗车指数","des":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"},{"title":"旅游","zs":"适宜","tipt":"旅游指数","des":"天气较好，温度适宜，是个好天气哦。这样的天气适宜旅游，您可以尽情地享受大自然的风光。"},{"title":"感冒","zs":"较易发","tipt":"感冒指数","des":"昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。"},{"title":"运动","zs":"较适宜","tipt":"运动指数","des":"天气较好，无雨水困扰，较适宜进行各种运动，但因气温较低，在户外运动请注意增减衣物。"},{"title":"紫外线强度","zs":"中等","tipt":"紫外线强度指数","des":"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"}]
     * weather_data : [{"date":"周四 03月03日 (实时：24℃)","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/qing.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/duoyun.png","weather":"晴转多云","wind":"微风","temperature":"24 ~ 13℃"},{"date":"周五","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/duoyun.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/duoyun.png","weather":"多云","wind":"微风","temperature":"24 ~ 15℃"},{"date":"周六","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/yin.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/yin.png","weather":"阴","wind":"微风","temperature":"23 ~ 16℃"},{"date":"周日","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/duoyun.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/yin.png","weather":"多云转阴","wind":"微风","temperature":"25 ~ 16℃"}]
     */

    private List<Result> results;

    public void setError(int error) {
        this.error = error;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public int getError() {
        return error;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public List<Result> getResults() {
        return results;
    }

    public static class Result {
        /**
         * date : 周四 03月03日 (实时：24℃)
         * dayPictureUrl : http://api.map.baidu.com/images/weather/day/qing.png
         * nightPictureUrl : http://api.map.baidu.com/images/weather/night/duoyun.png
         * weather : 晴转多云
         * wind : 微风
         * temperature : 24 ~ 13℃
         */

        private List<WeatherData> weather_data;

        public void setWeather_data(List<WeatherData> weather_data) {
            this.weather_data = weather_data;
        }

        public List<WeatherData> getWeather_data() {
            return weather_data;
        }

        public static class WeatherData {
            private String date;
            private String weather;
            private String wind;
            private String temperature;

            public void setDate(String date) {
                this.date = date;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getDate() {
                return date;
            }

            public String getWeather() {
                return weather;
            }

            public String getWind() {
                return wind;
            }

            public String getTemperature() {
                return temperature;
            }
        }
    }
}
