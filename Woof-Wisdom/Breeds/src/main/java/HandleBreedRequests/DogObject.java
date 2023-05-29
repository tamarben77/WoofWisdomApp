package HandleBreedRequests;

public class DogObject {
    public DogObject(String breedName, String breedType, String breedDescription,
                     String furColor, String origin, String minHeightInches, String maxHeightInches,String minWeightPounds,
                     String maxWeightPounds, String maxLifeSpan, String adaptability, String healthAndGrooming, String trainability , String exerciseNeeds, String friendliness) {
        this.breedName = breedName;
        this.breedType = breedType;
        this.breedDescription = breedDescription;
        this.furColor = furColor;
        this.origin = origin;
        double minHeightCentimeters = Double.parseDouble(minHeightInches) * 2.54;
        double maxHeightCentimeters = Double.parseDouble(maxHeightInches) * 2.54;
        double avgHeightCentimeters = minHeightCentimeters + maxHeightCentimeters / 2;
        this.avgHeightCentimeters = String.valueOf(avgHeightCentimeters);
        double minWeightKilos = Double.parseDouble(minWeightPounds) * 0.453592;
        double maxWeightKilos = Double.parseDouble(maxWeightPounds) * 0.453592;
        double avgWeightKilos = (minWeightKilos + maxWeightKilos) / 2;
        this.avgWeight = String.valueOf(avgWeightKilos);
        this.maxLifeSpan = maxLifeSpan;
        this.adaptability = adaptability;
        this.healthAndGrooming = healthAndGrooming;
        this.trainability = trainability;
        this.exerciseNeeds = exerciseNeeds;
        this.friendliness = friendliness;
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

    public String getAvgHeight() {
        return avgHeightCentimeters;
    }

    public void setAvgHeight(String avgHeight) {
        this.avgHeightCentimeters = avgHeight;
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

    private String breedName;
    private String breedType;
    private String breedDescription;
    private String furColor;
    private String origin;
    private String avgHeightCentimeters;
    private String avgWeight;
    private String maxLifeSpan;
    private String adaptability;
    private String healthAndGrooming;
    private String trainability;
    private String exerciseNeeds;
    public String getFriendliness() {
        return friendliness;
    }

    public void setFriendliness(String friendliness) {
        this.friendliness = friendliness;
    }

    private String friendliness;

    public String getAvgHeightCentimeters() {
        return avgHeightCentimeters;
    }

    public void setAvgHeightCentimeters(String avgHeightCentimeters) {
        this.avgHeightCentimeters = avgHeightCentimeters;
    }

    public String getAdaptability() {
        return adaptability;
    }

    public void setAdaptability(String adaptability) {
        this.adaptability = adaptability;
    }

    public String getHealthAndGrooming() {
        return healthAndGrooming;
    }

    public void setHealthAndGrooming(String healthAndGrooming) {
        this.healthAndGrooming = healthAndGrooming;
    }

    public String getTrainability() {
        return trainability;
    }

    public void setTrainability(String trainability) {
        this.trainability = trainability;
    }

    public String getExerciseNeeds() {
        return exerciseNeeds;
    }

    public void setExerciseNeeds(String exerciseNeeds) {
        this.exerciseNeeds = exerciseNeeds;
    }


}