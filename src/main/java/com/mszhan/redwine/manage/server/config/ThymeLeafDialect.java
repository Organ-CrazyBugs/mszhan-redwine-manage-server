package com.mszhan.redwine.manage.server.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;


public class ThymeLeafDialect extends AbstractDialect   implements ApplicationContextAware {


	private static ApplicationContext applicationContext = null;

 
	 
 
	
	
 
	//private HasRoleProcessor hasRoleProcessor=new HasRoleProcessor() ;
	
	 public ThymeLeafDialect() {
		    super();
		  }

		  //
		  // All of this dialect's attributes and/or tags
		  // will start with 'hello:'
		  //
		  public String getPrefix() {
		    return "role";
		  }


		  //
		  // The processors.
		  //
		  @Override
		  public Set<IProcessor> getProcessors() {
		    final Set<IProcessor> processor = new HashSet<IProcessor>();
		    processor.add(new HasRoleProcessor());
		    return processor;
		  }

			@Override
			public void setApplicationContext(ApplicationContext applicationContext)
					throws BeansException {
				// TODO Auto-generated method stub
				
				if(ThymeLeafDialect.applicationContext == null){
					ThymeLeafDialect.applicationContext  = applicationContext;
			       }
			 
				 
			}
			
			  //获取applicationContext
		    public static ApplicationContext getApplicationContext() {
		       return applicationContext;
		    }
		   
		    //通过name获取 Bean.
		    public static Object getBean(String name){
		       return getApplicationContext().getBean(name);
		    }
		   
		    //通过class获取Bean.
		    public static <T> T getBean(Class<T> clazz){
		       return getApplicationContext().getBean(clazz);
		    }
		   
		    //通过name,以及Clazz返回指定的Bean
		    public static <T> T getBean(String name,Class<T> clazz){
		       return getApplicationContext().getBean(name, clazz);
		    }




   
}
