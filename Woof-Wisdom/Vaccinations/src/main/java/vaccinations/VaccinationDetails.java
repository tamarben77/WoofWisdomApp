package vaccinations;

public class VaccinationDetails {
    private String username;
    private String vaccination_name;
    private String date;
    private String description;
    private String location;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVaccination_name() {
        return vaccination_name;
    }

    public void setVaccination_name(String vaccination_name) {
        this.vaccination_name = vaccination_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
