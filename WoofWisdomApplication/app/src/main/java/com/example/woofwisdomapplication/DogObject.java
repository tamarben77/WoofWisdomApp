package com.example.woofwisdomapplication;

public class DogObject {
    private String breedName;
    private String breedType;
    private String breedDescription;
    private String furColor;
    private String origin;
    private String avgWeight;
    private String maxLifeSpan;
    private String avgHeight;
    public DogObject(String breedName, String breedType, String breedDescription,
                     String furColor, String origin,String avgWeight,String maxLifeSpan,String avgHeight) {
        this.breedName = breedName;
        this.breedType = breedType;
        this.breedDescription = breedDescription;
        this.furColor = furColor;
        this.origin = origin;
        this.avgWeight = avgWeight;
        this.maxLifeSpan = maxLifeSpan;
        this.avgHeight = avgHeight;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public String getBreedType() {
        return breedType;
    }

    public void setBreedType(String breedType) {
        this.breedType = breedType;
    }

    public String getBreedDescription() {
        return breedDescription;
    }

    public void setBreedDescription(String breedDescription) {
        this.breedDescription = breedDescription;
    }

    public String getFurColor() {
        return furColor;
    }

    public void setFurColor(String furColor) {
        this.furColor = furColor;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAvgWeight() {
        return avgWeight;
    }

    public void setAvgWeight(String avgWeight) {
        this.avgWeight = avgWeight;
    }

    public String getMaxLifeSpan() {
        return maxLifeSpan;
    }

    public void setMaxLifeSpan(String maxLifeSpan) {
        this.maxLifeSpan = maxLifeSpan;
    }


}
