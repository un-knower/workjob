package stream.util;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-21 13:16
 **/
public class Text {
    int category;
    int update_time;
    String word;
    String index_value;
    String platform_name;
    int platform;
    String record_id;
    String day;

    public Text() {
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getIndex_value() {
        return index_value;
    }

    public void setIndex_value(String index_value) {
        this.index_value = index_value;
    }

    public String getPlatform_name() {
        return platform_name;
    }

    public void setPlatform_name(String platform_name) {
        this.platform_name = platform_name;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "Text{" +
                "category=" + category +
                ", update_time=" + update_time +
                ", word='" + word + '\'' +
                ", index_value='" + index_value + '\'' +
                ", platform_name='" + platform_name + '\'' +
                ", platform=" + platform +
                ", record_id='" + record_id + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
