package com.mq.pcrf.jmx;


/**
 * @author naveen.beldari
 *
 */
public interface PcrfdMBean {
    public void start();

    public void stop();
        
    public void restartAll();

    public int getStatus();

    public String getStatusText();

    public String status();
    public void refreshService();
    public void refreshBandWidth();
    public void refreshRule();
    public void refreshGroup();
}

