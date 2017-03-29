package com.mq.pcrf.jmx;



/**
 * @author naveen.beldari
 *
 */
public class Pcrfd implements PcrfdMBean {
	
    public void start() {
    	com.mq.pcrf.core.Pcrfd pcrfd = com.mq.pcrf.core.Pcrfd.getInstance();
        pcrfd.start();
    }

    public void stop() {
    	com.mq.pcrf.core.Pcrfd pcrfd = com.mq.pcrf.core.Pcrfd.getInstance();
        pcrfd.stop();
    }

    public int getStatus() {
    	com.mq.pcrf.core.Pcrfd pcrfd = com.mq.pcrf.core.Pcrfd.getInstance();
        return pcrfd.getStatus();
    }

    public String status() {
        return com.mq.mqpcrf.controller.util.Fiber.STATUS_NAMES[getStatus()];
    }

    public String getStatusText() {
        int status = getStatus();
        String statusText =com.mq.mqpcrf.controller.util.Fiber.STATUS_NAMES[status];
        return statusText;
    }

	
	public void restartAll() {
		com.mq.pcrf.core.Pcrfd pcrfd = com.mq.pcrf.core.Pcrfd.getInstance();
		pcrfd.restartAll();
		
	}

	public void refreshService() {
		com.mq.pcrf.core.Pcrfd pcrfd = com.mq.pcrf.core.Pcrfd.getInstance();
		pcrfd.refreshService();
    }
	
	
	public void refreshBandWidth() {
		com.mq.pcrf.core.Pcrfd pcrfd = com.mq.pcrf.core.Pcrfd.getInstance();
		pcrfd.refreshBandWidth();
    }
	public void refreshRule() {
		com.mq.pcrf.core.Pcrfd pcrfd = com.mq.pcrf.core.Pcrfd.getInstance();
		pcrfd.refreshRule();
    }
	
	public void refreshGroup() {
		com.mq.pcrf.core.Pcrfd pcrfd = com.mq.pcrf.core.Pcrfd.getInstance();
        pcrfd.refreshGroup();
    }
}
