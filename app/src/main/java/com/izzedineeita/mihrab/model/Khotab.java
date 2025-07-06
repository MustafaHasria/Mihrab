package com.izzedineeita.mihrab.model;

import java.io.Serializable;

/**
 * Created by Izzedine Eita on 3/4/2021
 */

public class Khotab implements Serializable {
    private int Id; // رقم الخطبة ، لا يتكرر
    public String Title; // عنوان الخطبة بالعربي ،،، ،، لازم يكون في نص ، يعني لازم يرجع الك
    private String Body;// نص الخطبة بالعربي ،، لازم يكون في نص ، يعني لازم يرجع الك
    private String DateKhotab; // تاريخ الخطبة ،، غالبا يكون تاريخ ايام جمعة فقط
    private String UpdatedAt; // اخر تحديث
    private int isDeleted; // هل هو محذوف :: نعم يعني محذوف
    public String Description; // الوصف ،، زيادة ،، غير اجبارية
    private String Title1;// عنوان الترجمة الاولى  اللغة 1
    private String Body1; // ترجمة الخطبة للغة 1
    private String Title2; // ترجمة العنوان للغة 2
    private String Body2; // ترجمة نص الخطبة للغة 2
    private String Title3; // ترجمة العنوان للغة 3
    private String Body3; // ترجمة نص الخطبة للغة 3
    private String UrlVideoDeaf; // رابط فيديو لغة الاشارة ،، اذا موجود بكون في فيديو ، واذا فاضي اذا لا يحتوي على لغة اشارة
    private int TimeExpected;// وقت الخطبة المقدر ،، الوقت بالدقائق ،،، يستخدم ل زمن اظهار الترجمة على الشاشة وزمن تسجيل الخطبة من بعد الاذان ب 2 دقيقة مثلا
    private int isException;
    private int TranslationSpeed;
    private boolean Direction1RTL;
    private boolean Direction2RTL;
    private int textShowTime;
    private int ShowTime;


    public Khotab() {
    }

    public Khotab(int Id, String Title, String Body, String DateKhotab, String UpdatedAt, int isDeleted, String Description
            , String Title1, String Body1, String Title2, String Body2, String UrlVideoDeaf, int TimeExpected, int isException) {
        this.Id = Id;
        this.Title = Title;
        this.Body = Body;
        this.DateKhotab = DateKhotab;
        this.UpdatedAt = UpdatedAt;
        this.isDeleted = isDeleted;
        this.Description = Description;
        this.Title2 = Title2;
        this.Title1 = Title1;
        this.Body1 = Body1;
        this.Body2 = Body2;
        this.UrlVideoDeaf = UrlVideoDeaf;
        this.TimeExpected = TimeExpected;
        this.isException = isException;
    }

    public Khotab(int id, String title, String body, String dateKhotab, String updatedAt,
                  int isDeleted, String description, String title1, String body1, String title2,
                  String body2, String urlVideoDeaf, int timeExpected, int isException,
                  int translationSpeed, boolean direction1RTL,
                  boolean direction2RTL, int textShowTime, int ShowTime) {
        Id = id;
        Title = title;
        Body = body;
        DateKhotab = dateKhotab;
        UpdatedAt = updatedAt;
        this.isDeleted = isDeleted;
        Description = description;
        Title1 = title1;
        Body1 = body1;
        Title2 = title2;
        Body2 = body2;
        UrlVideoDeaf = urlVideoDeaf;
        TimeExpected = timeExpected;
        this.isException = isException;
        TranslationSpeed = translationSpeed;
        Direction1RTL = direction1RTL;
        Direction2RTL = direction2RTL;
        this.textShowTime = textShowTime;
        this.ShowTime = ShowTime;
    }

    public int getTextShowTime() {
        return textShowTime;
    }

    public void setTextShowTime(int textShowTime) {
        this.textShowTime = textShowTime;
    }

    public int getTranslationSpeed() {
        return TranslationSpeed;
    }

    public void setTranslationSpeed(int translationSpeed) {
        TranslationSpeed = translationSpeed;
    }

    public boolean isDirection1RTL() {
        return Direction1RTL;
    }

    public void setDirection1RTL(boolean direction1RTL) {
        Direction1RTL = direction1RTL;
    }

    public boolean isDirection2RTL() {
        return Direction2RTL;
    }

    public void setDirection2RTL(boolean direction2RTL) {
        Direction2RTL = direction2RTL;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getDateKhotab() {
        return DateKhotab;
    }

    public void setDateKhotab(String dateKhotab) {
        DateKhotab = dateKhotab;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTitle1() {
        return Title1;
    }

    public void setTitle1(String title1) {
        Title1 = title1;
    }

    public String getBody1() {
        return Body1;
    }

    public void setBody1(String body1) {
        Body1 = body1;
    }

    public String getTitle2() {
        return Title2;
    }

    public void setTitle2(String title2) {
        Title2 = title2;
    }

    public String getBody2() {
        return Body2;
    }

    public void setBody2(String body2) {
        Body2 = body2;
    }


    public String getTitle3() {
        return Title3;
    }

    public void setTitle3(String title3) {
        Title3 = title3;
    }

    public String getBody3() {
        return Body3;
    }

    public void setBody3(String body3) {
        Body3 = body3;
    }


    public String getUrlVideoDeaf() {
        return UrlVideoDeaf;
    }

    public void setUrlVideoDeaf(String urlVideoDeaf) {
        UrlVideoDeaf = urlVideoDeaf;
    }

    public int getTimeExpected() {
        return TimeExpected;
    }

    public void setTimeExpected(int timeExpected) {
        TimeExpected = timeExpected;
    }

    public int getIsException() {
        return isException;
    }

    public void setIsException(int isException) {
        this.isException = isException;
    }

    public int getShowTime() {
        return ShowTime;
    }

    public void setShowTime(int ShowTime) {
        this.ShowTime = ShowTime;
    }
}
