package br.com.ehgm.springbatch.partitioner.model.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import br.com.ehgm.springbatch.partitioner.model.Customer;

public class CustomerMapper implements FieldSetMapper<Customer>{

	@Override
	public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
		Customer customer = new Customer();
		customer.setId(fieldSet.readLong(0));
		customer.setName(fieldSet.readString(1));
		customer.setEmail(fieldSet.readString(2));
		return customer;
	}

}
