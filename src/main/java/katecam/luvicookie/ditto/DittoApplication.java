package katecam.luvicookie.ditto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DittoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DittoApplication.class, args);
	}

}
