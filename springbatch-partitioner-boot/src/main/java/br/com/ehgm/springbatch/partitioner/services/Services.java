package br.com.ehgm.springbatch.partitioner.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import br.com.ehgm.springbatch.partitioner.model.Customer;

@Component
public class Services {

	private static final Logger LOGGER = LogManager.getLogger(Services.class);

	public Customer process(Customer customer) {
		LOGGER.info("Customer ID: " + customer.getId());
		return customer;
	}

}
