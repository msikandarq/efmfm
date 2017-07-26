package com.newtglobal.eFmFmFleet.launcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

public class MyLauncher {

	private static Log log = LogFactory.getLog(MyLauncher.class);	
	private JobLauncher jobLauncher;
	private Job job;
	
	public void launch(){
		long l1 = System.currentTimeMillis();
		JobParameters jobParameters = new JobParametersBuilder().addLong("time",
		        System.currentTimeMillis()).toJobParameters();
		    try {
		      JobExecution execution = jobLauncher.run(job, jobParameters);
		      log.info("Exit Status of Job " + job.getName()+ " : " + execution.getStatus());
		    } catch (Exception e) {
		      log.error("Error of Job: " + job.getName()+ " : " + e);
		    }
			long l2 = System.currentTimeMillis();
			log.debug("Total Time Taken by for Job: " + job.getName()+ " : " +(l2-l1)+"ms");    
	}
	
	public JobLauncher getJobLauncher() {
		return jobLauncher;
	}

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}	
	
}