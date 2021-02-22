package com.abc.configuration;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.abc.ContractHistory;
import com.abc.entity.Contract;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j

public class ItemProcessorConfiguration {

	private AtomicInteger atomicInteger = new AtomicInteger();

	private ObjectMapper mapper = new ObjectMapper();

	@Bean
	public ItemProcessor<Contract, ContractHistory> itemProcessor() {

		return new ItemProcessor<Contract, ContractHistory>() {
			@Override
			public ContractHistory process(Contract item) throws Exception {

				log.info("processinf the data " + item.getContractId() + "record no is: "
						+ atomicInteger.incrementAndGet());
				return mapper.convertValue(item, ContractHistory.class);
			}
		};
	}
}
