package com.izzedineeita.mihrab.model;// Created by Izzedine Eita on 1/13/2021.

// Created by Izzedine Eita on 1/13/2021.

public class AzkarModel {
    private int Id;
    private String TextAzakar;
    private String UpdatedAt;
    private boolean isDeleted;
    public int sort;
    private boolean Fajr;
    private boolean Dhuhr;
    private boolean Asr;
    private boolean Magrib;
    private boolean Isha;
    private int Count;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTextAzakar() {
        return TextAzakar;
    }

    public void setTextAzakar(String textAzakar) {
        TextAzakar = textAzakar;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isFajr() {
        return Fajr;
    }

    public void setFajr(boolean fajr) {
        Fajr = fajr;
    }

    public boolean isDhuhr() {
        return Dhuhr;
    }

    public void setDhuhr(boolean dhuhr) {
        Dhuhr = dhuhr;
    }

    public boolean isAsr() {
        return Asr;
    }

    public void setAsr(boolean asr) {
        Asr = asr;
    }

    public boolean isMagrib() {
        return Magrib;
    }

    public void setMagrib(boolean magrib) {
        Magrib = magrib;
    }

    public boolean isha() {
        return Isha;
    }

    public void setIsha(boolean isha) {
        Isha = isha;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }


}