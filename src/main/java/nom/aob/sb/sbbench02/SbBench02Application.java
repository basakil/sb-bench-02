package nom.aob.sb.sbbench02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SbBench02Application {

	public static void main(String[] args) {
		SpringApplication.run(SbBench02Application.class, args);
	}

}
