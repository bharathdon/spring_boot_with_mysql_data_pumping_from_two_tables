package com.abc.configuration;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.abc.ContractHistory;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ItemWriterConfiguration {

	@Bean
	public ItemWriter<ContractHistory> itemWriter(NamedParameterJdbcTemplate jdbcTemplate) {

		final String insert_query = "insert into contract_history(contract_id,holder_name,amount,create_date,status) values(:contractId,:holderName,:amount,:createDate,:status)";

		JdbcBatchItemWriter<ContractHistory> itemWriter = new JdbcBatchItemWriter<ContractHistory>() {
			@Override
			public void write(List<? extends ContractHistory> items) throws Exception {

				super.write(items);
				log.info("items processed  " + items.size());
				delete(items.stream().map(ContractHistory::getContractId).collect(Collectors.toList()), jdbcTemplate);
			}
		};

		itemWriter.setSql(insert_query);
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<ContractHistory>());
		itemWriter.setJdbcTemplate(jdbcTemplate);

		return itemWriter;

	}

	public void delete(final List<String> contractIdList, NamedParameterJdbcTemplate jdbcTemplate) {

		final String delete_query = "delete from contract where contract_id in(:contractId)";
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("contract_id", contractIdList);
		jdbcTemplate.update(delete_query, mapSqlParameterSource);
	}

}
