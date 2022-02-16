package com.gkemayo.batch.batchutils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;

@Slf4j
public class BatchStepSkipper implements SkipPolicy {

	private static Integer SKIP_THRESHOLD = 3;

	@Override
	public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
		if(t instanceof FlatFileParseException && skipCount < SKIP_THRESHOLD) {
			log.info(((FlatFileParseException) t).getLineNumber() + ": " +((FlatFileParseException) t).getInput() + " -- " + ((FlatFileParseException) t).getMessage());
			return true;
		}
		return false;
	}
	
	

}
