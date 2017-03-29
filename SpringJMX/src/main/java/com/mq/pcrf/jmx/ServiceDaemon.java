package com.mq.pcrf.jmx;

import com.mq.mqpcrf.controller.util.PausableFiber;



public interface ServiceDaemon extends PausableFiber {
	
	 public String status();

}

