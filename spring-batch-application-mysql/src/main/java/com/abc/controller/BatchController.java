package com.abc.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abc.entity.Contract;
import com.abc.repository.ContractRepository;

import lombok.SneakyThrows;

@RestController
public class BatchController {

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;

	@GetMapping("/get")
	public String saveDummyData() {

		ArrayList<Contract> arrayList = new ArrayList<>();
		for (int i = 1; i < 1000; i++) {

			Contract contract = new Contract();
			contract.setHolderName("name -" + i);
			contract.setDuration(new Random().nextInt());
			contract.setAmount((double) new Random().nextInt(55555));
contract.setCreateDate(LocalDate.now());
			contract.setStatus("inprogress");
			arrayList.add(contract);

		}

		contractRepository.saveAll(arrayList);
		return "saved";
	}

	@GetMapping("/start")
	@SneakyThrows
	public String startBatch() {

		JobParameters jobParametersBuilder = new JobParametersBuilder().toJobParameters();
		jobLauncher.run(job, jobParametersBuilder);
		return "started...";
	}

}
