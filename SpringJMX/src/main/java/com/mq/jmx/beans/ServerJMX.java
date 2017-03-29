package com.mq.jmx.beans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mq.pcrf.jmx.Pcrfd;

/**
 * @author naveen.beldari
 * 
 */
public class ServerJMX
{

	static final String MQPCRF_HOME_PROPERTY = "MQPcrf.home";
	public static String serverStatus = null;
	static Pcrfd pcrfd;

	public static void main(String[] args) throws Exception
	{

		ApplicationContext context = new ClassPathXmlApplicationContext("conf\\spring-main.xml");

		pcrfd = (Pcrfd) context.getBean("pcrfBeanRef");

		serverStatus = pcrfd.status();
		// System.out.println("Server Status " + serverStatus);

		System.setProperty("MQPcrf.home", "D:\\MyWorks\\SpringJMX");

		if (!serverStatus.equalsIgnoreCase("RUNNING") || serverStatus.equalsIgnoreCase("STOPPED"))
		{

			System.out.println("Spring JMX Server started");

			onStart();

			Thread.sleep(Long.MAX_VALUE);
		}

	}

	private static boolean onStart()
	{
		pcrfd.start();
		boolean serverStarted = true;
		return serverStarted;
	}

}
