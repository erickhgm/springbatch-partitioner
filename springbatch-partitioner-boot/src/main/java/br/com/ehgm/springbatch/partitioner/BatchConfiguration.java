package br.com.ehgm.springbatch.partitioner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import br.com.ehgm.springbatch.partitioner.model.Customer;
import br.com.ehgm.springbatch.partitioner.step.MyPartitioner;
import br.com.ehgm.springbatch.partitioner.step.PartitionerItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	@StepScope
	public StaxEventItemReader<Customer> reader(@Value("#{stepExecutionContext[index]}") String index){
		return new StaxEventItemReaderBuilder<Customer>()
				.name("xmlItemReader")
				.resource(new FileSystemResource("/temp/customers-"+ index + ".xml"))
				.addFragmentRootElements(new String("customer"))
				.unmarshaller(new Jaxb2Marshaller() {{
					setClassesToBeBound(Customer.class);
				}})
				.build();
	}

	@Bean
	@StepScope
	public FlatFileItemWriter<Customer> writer(@Value("#{stepExecutionContext[index]}") String index) {
		return new FlatFileItemWriterBuilder<Customer>()
				.name("cvsFileWriter")
				.resource(new FileSystemResource("/temp/customers-" + index + ".csv"))
				.lineAggregator(new DelimitedLineAggregator<Customer>() {{
					setDelimiter(",");
					setFieldExtractor(new BeanWrapperFieldExtractor<Customer>() {{
						setNames(new String[] {"id", "name", "email"});
					}});
				}})
				.build();
	}

	@Bean
	public PartitionerItemProcessor processor() {
		return new PartitionerItemProcessor();
	}

	@Bean
	public MyPartitioner partitioner() {
		return new MyPartitioner();
	}

	@Bean
	public Job jobPartitioner() {
		return jobBuilderFactory.get("jobPartitioner")
				.incrementer(new RunIdIncrementer())
				.flow(masterStep())
				.end()
				.build();
	}

	@Bean
	public Step masterStep() {
		return stepBuilderFactory.get("masterStep")
				.partitioner("slaveStep", partitioner()).gridSize(3)
				.step(slaveStep())
				.taskExecutor(new SimpleAsyncTaskExecutor() {{
					setConcurrencyLimit(3);
				}})
				.build();
	}

	@Bean
	public Step slaveStep() {
		return stepBuilderFactory.get("slaveStep")
				.<Customer, Customer> chunk(10)
				.reader(reader(null))
				.processor(processor())
				.writer(writer(null))
				.build();
	}

}
