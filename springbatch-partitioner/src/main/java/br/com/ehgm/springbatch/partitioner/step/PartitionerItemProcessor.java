package br.com.ehgm.springbatch.partitioner.step;

import javax.inject.Inject;

import org.springframework.batch.item.ItemProcessor;

import br.com.ehgm.springbatch.partitioner.model.Customer;
import br.com.ehgm.springbatch.partitioner.services.Services;

public class PartitionerItemProcessor implements ItemProcessor<Customer, Customer> {

	@Inject
	private Services services;

	@Override
	public Customer process(Customer item) throws Exception {
		return this.services.process(item);
	}

}
