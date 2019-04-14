package br.com.ehgm.springbatch.partitioner.step;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

public class MyPartitioner implements Partitioner {

	private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(MyPartitioner.class);

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		LOGGER.debug("Start partition");

		Map<String, ExecutionContext> partitionMap = new HashMap<String, ExecutionContext>();

		for (int index = 1; index <= gridSize; index++) {
			ExecutionContext stepContextMap = new ExecutionContext();
			stepContextMap.putLong("index", index);
			partitionMap.put("Thread:" + index, stepContextMap);
		}

		LOGGER.debug("End partition: Created partitions of size: "+ partitionMap.size());
		return partitionMap;
	}

}
