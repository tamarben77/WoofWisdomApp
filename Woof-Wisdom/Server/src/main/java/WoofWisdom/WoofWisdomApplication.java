package WoofWisdom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"WoofWisdom", "HandleBreedRequests"})
public class WoofWisdomApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoofWisdomApplication.class, args);
	}

}
