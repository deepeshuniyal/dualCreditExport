package com.dualCredit.export.dualCreditExport;

import com.dualCredit.export.service.FileProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.dualCredit.repository")
@ComponentScan("com.dualCredit.export")
@EnableAutoConfiguration
@EntityScan(basePackages = {
		"com.dualCredit.entities"
})
public class DualCreditExportApplication implements CommandLineRunner {

	@Autowired
	FileProcess fileProcess;

	public static void main(String[] args) {
		SpringApplication.run(DualCreditExportApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		fileProcess.exlFileProcess();
	}

}

