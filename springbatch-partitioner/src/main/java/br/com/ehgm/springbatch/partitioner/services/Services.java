package br.com.ehgm.springbatch.partitioner.services;

import javax.inject.Named;

import org.apache.log4j.Logger;

import br.com.ehgm.springbatch.partitioner.model.Customer;

@Named
public class Services {

	private static final Logger LOGGER = Logger.getLogger(Services.class);

	public Customer process(Customer customer) {
		LOGGER.info("Customer ID: " + customer.getId());
		return customer;
	}

}
