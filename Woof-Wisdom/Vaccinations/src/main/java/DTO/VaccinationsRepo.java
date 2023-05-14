package DTO;

public class VaccinationsRepo {
    private Integer id;
    private String name;
    private Integer recommendedAge;
    private Integer everyYears;

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

    public Integer getRecommendedAge() {
        return recommendedAge;
    }

    public void setRecommendedAge(Integer recommendedAge) {
        this.recommendedAge = recommendedAge;
    }

    public Integer getEveryYears() {
        return everyYears;
    }

    public void setEveryYears(Integer everyYears) {
        this.everyYears = everyYears;
    }
}
