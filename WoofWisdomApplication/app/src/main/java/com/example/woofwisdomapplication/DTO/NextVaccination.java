package com.example.woofwisdomapplication.DTO;

public class NextVaccination {
    private String name;
    private Integer inWeeks;
    private Integer inGeneral;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInWeeks() {
        return inWeeks;
    }

    public void setInWeeks(Integer inWeeks) {
        this.inWeeks = inWeeks;
    }

    public Integer getInGeneral() {
        return inGeneral;
    }

    public void setInGeneral(Integer inGeneral) {
        this.inGeneral = inGeneral;
    }
}
