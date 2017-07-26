package com.newtglobal.eFmFmFleet.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContextLoader{
	
	private static ApplicationContext ctx; 
	public static ApplicationContext getContext(){
		if(ctx==null){
			ctx=new ClassPathXmlApplicationContext(
					"efmfm-context.xml");
			return ctx;
		}
		return ctx;
	}
	
	
	@Override
	protected Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException("cloning not allowed of ApplicationContext");
		
	}
}
