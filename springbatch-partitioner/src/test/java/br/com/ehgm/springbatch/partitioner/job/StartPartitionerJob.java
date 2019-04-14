package br.com.ehgm.springbatch.partitioner.job;

import org.springframework.batch.core.launch.support.CommandLineJobRunner;

public class StartPartitionerJob {
	public static void main(String[] args) throws Exception {
		String[] arguments = { 
				"classpath:/br/com/ehgm/springbatch/partitioner/job/partitioner-context.xml",
				"jobPartitioner" };
		
		CommandLineJobRunner.main(arguments);
	}

	private StartPartitionerJob() {
		// Private constructor
	}
}
