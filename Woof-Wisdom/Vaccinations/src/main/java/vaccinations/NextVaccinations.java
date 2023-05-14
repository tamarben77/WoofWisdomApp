package vaccinations;

import DTO.VaccinationsRepo;
import ManagmentDB.MySQLConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NextVaccinations {
    public static List<String> getNextVaccinations(int dogAgeInWeeks) throws JsonProcessingException {

        // Query the database for all vaccinations
        List<VaccinationsRepo> allVaccinations = mapToVaccinationsRepoList(MySQLConnector.getTable("all_vaccinations"));

        // Create a list to store the names of recommended vaccinations
        List<VaccinationsRepo> filteredVaccinations = allVaccinations.stream()
                .filter(v -> v.getRecommendedAge() >= dogAgeInWeeks || v.getEveryYears() != 0)
                .collect(Collectors.toList());
        List<Object> mappedList = filteredVaccinations.stream()
                .map(vaccination -> Map.of(
                        "name", vaccination.getName(),
                        "recommendedAge", vaccination.getRecommendedAge(),
                        "everyYears", vaccination.getEveryYears()
                ))
                .collect(Collectors.toList());

        // Convert the mappedList to a JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        return Collections.singletonList(objectMapper.writeValueAsString(mappedList));
    }

    public static List<VaccinationsRepo> mapToVaccinationsRepoList(List<Map<String, Object>> tableData) {
        List<VaccinationsRepo> vaccinationsRepoList = new ArrayList<>();

        for (Map<String, Object> row : tableData) {
            VaccinationsRepo vaccinationsRepo = new VaccinationsRepo();
            vaccinationsRepo.setId((Integer) row.get("id"));
            vaccinationsRepo.setName((String) row.get("name"));
            vaccinationsRepo.setRecommendedAge((Integer) row.get("recommended_age"));
            vaccinationsRepo.setEveryYears((Integer) row.get("every_years"));
            vaccinationsRepoList.add(vaccinationsRepo);
        }

        return vaccinationsRepoList;
    }

}
