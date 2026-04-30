package com.gla.glaways;

public class PlacementModel {
    String courseName, placedPercentage, highestPackage, averagePackage, totalStudents, topCompanies;

    public PlacementModel() {} // Khali constructor

    public PlacementModel(String courseName, String placedPercentage, String highestPackage, String averagePackage, String totalStudents, String topCompanies) {
        this.courseName = courseName;
        this.placedPercentage = placedPercentage;
        this.highestPackage = highestPackage;
        this.averagePackage = averagePackage;
        this.totalStudents = totalStudents;
        this.topCompanies = topCompanies;
    }

    public String getCourseName() { return courseName; }
    public String getPlacedPercentage() { return placedPercentage; }
    public String getHighestPackage() { return highestPackage; }
    public String getAveragePackage() { return averagePackage; }
    public String getTotalStudents() { return totalStudents; }
    public String getTopCompanies() { return topCompanies; }
}