package HandleBreedRequest;

public class DogObject {
    public DogObject(String breedName, String breedType, String breedDescription,
                     String furColor, String origin, String minHeightInches, String maxHeightInches,String minWeightPounds,
                     String maxWeightPounds, String maxLifeSpan) {
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
}
