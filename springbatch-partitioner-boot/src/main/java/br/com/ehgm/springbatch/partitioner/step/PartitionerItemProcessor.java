package br.com.ehgm.springbatch.partitioner.step;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.ehgm.springbatch.partitioner.model.Customer;
import br.com.ehgm.springbatch.partitioner.services.Services;

public class PartitionerItemProcessor implements ItemProcessor<Customer, Customer> {

	@Autowired
	private Services services;

	@Override
	public Customer process(Customer item) throws Exception {
		return this.services.process(item);
	}

}
