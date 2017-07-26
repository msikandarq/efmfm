package com.newtglobal.eFmFmFleet;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBatchInitialization {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
//	new ClassPathXmlApplicationContext("planned.xml");
//	new ClassPathXmlApplicationContext("driverAutoCheck.xml");
	new ClassPathXmlApplicationContext("scheduling.xml");
//	new ClassPathXmlApplicationContext("itemOrientedJob.xml");
//	new ClassPathXmlApplicationContext("requestReader.xml");
	}
		
}
