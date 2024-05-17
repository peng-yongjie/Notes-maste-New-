package com.soulocean.Diary.entity;

/**
 * @author soulocean
 * @version 1.0
 * @date 0019, 2020年12月19日 下午 11:33:47
 */
public class Bean {
    private Integer id;
    private String name;
    private String backgroundImage;
    private String content;
    private String remarks;
    private String updateTime;

    public Bean() {
    }

    public Bean(String name, String content,String remarks) {
        this.name = name;
        this.content = content;
        this.remarks=remarks;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
