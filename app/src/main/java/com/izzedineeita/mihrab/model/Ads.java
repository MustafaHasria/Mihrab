package com.izzedineeita.mihrab.model;

import java.io.Serializable;

/**
 * Created by Izzedine Eita on 1/24/2021
 */
public class Ads implements Serializable {
    private int id;
    private int MasjedID;
    private int Type;
    private String Title;
    private String Text;
    private String Image;
    private String Image1;
    private String Image2;
    private String Image3;
    private String Image4;
    private String Image5;
    private String Image6;
    private int ImageSec;
    private String Video;
    private String Video1;
    private String Video2;
    private String StartDate;
    private String EndDate;

    private String StartTime;
    private String EndTime;

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public Ads() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMasjedID() {
        return MasjedID;
    }

    public void setMasjedID(int masjedID) {
        MasjedID = masjedID;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image) {
        Image1 = image;
    }

    public String getImage2() {
        return Image2;
    }

    public void setImage2(String image) {
        Image2 = image;
    }

    public String getImage3() {
        return Image3;
    }

    public void setImage3(String image) {
        Image3 = image;
    }

    public String getImage4() {
        return Image4;
    }

    public void setImage4(String image) {
        Image4 = image;
    }

    public String getImage5() {
        return Image5;
    }

    public void setImage5(String image) {
        Image5 = image;
    }

    public String getImage6() {
        return Image6;
    }

    public void setImage6(String image) {
        Image6 = image;
    }

    public int getImageSec() {
        return ImageSec;
    }

    public void setImageSec(int imageSec) {
        ImageSec = imageSec;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public String getVideo1() {
        return Video1;
    }

    public void setVideo1(String video) {
        Video1 = video;
    }

    public String getVideo2() {
        return Video2;
    }

    public void setVideo2(String video) {
        Video2 = video;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String start) {
        StartDate = start;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String end) {
        EndDate = end;
    }

}
