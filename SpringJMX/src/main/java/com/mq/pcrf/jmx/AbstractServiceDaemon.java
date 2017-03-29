
package com.mq.pcrf.jmx;


import java.net.URL;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import com.mq.mqpcrf.controller.log.RootCategory;
import com.mq.osslog.ServerLog;


/**
 * @author naveen.beldari
 *
 */
public abstract class AbstractServiceDaemon implements ServiceDaemon,SpringServiceDaemon {
	
    public final void afterPropertiesSet() throws Exception {
        init();
    }

    /**
     * The current status of this fiber
     */
    private int m_status;

    private String m_name;
    
    private Object m_statusLock = new Object();

    abstract protected void onInit();

    protected void onPause() {}

    protected void onResume() {}

    protected void onStart() {}

    protected void onStop() {}
    
    protected void onReStart() {}

    final public String getName() { return m_name; }
    
    protected void onRefreshNas() {}
    
    protected void onRefreshService() {}
    
    protected void onRefreshRealm() {}
    
    protected void onRefreshBandWidth() {}
    
    protected void onRefreshBusinessEntity() {}
    
    protected void onRefreshRule() {}
    
    protected void onRefreshGroup() {}
    
    protected void onRefreshDHCPTemplate() {}
    
    protected void onRefreshDHCPPool() {}

    protected AbstractServiceDaemon(String name) {
        m_name = name;
        setStatus(START_PENDING);
    }

    protected void setStatus(int status) {
        synchronized (m_statusLock) {
            m_status = status;
            m_statusLock.notifyAll();
        }
    }
    
    protected void waitForStatus(int status, long timeout) throws InterruptedException {
        synchronized (m_statusLock) {
            
            long last = System.currentTimeMillis();
            long waitTime = timeout;
            while (status != m_status && waitTime > 0) {
                m_statusLock.wait(waitTime);
                long now = System.currentTimeMillis();
                waitTime -= (now - last);
            }
        
        }
    }

    protected void waitForStatus(int status) throws InterruptedException {
        synchronized (m_statusLock) {
            while (status != m_status) {
                m_statusLock.wait();
            }
        }
    }

    public int getStatus() {
    	
        synchronized (m_statusLock) {        	
            return m_status;
        }
    }

    public String getStatusText() {
        return STATUS_NAMES[getStatus()];
    }

    public String status() {
        return getStatusText();
    }

    protected synchronized boolean isStartPending() {
        return getStatus() == START_PENDING;
    }

    protected synchronized boolean isRunning() {
        return getStatus() == RUNNING;
    }

    protected synchronized boolean isPaused() {
        return getStatus() == PAUSED;
    }

    protected synchronized boolean isStarting() {
        return getStatus() == STARTING;
    }   

    final public void init() {
        String prefix = RootCategory.getPrefix();
        try {
            
            RootCategory.setPrefix(getName());
            ServerLog.debug(getName()+" initializing.");

            onInit();

            ServerLog.debug(getName()+" initialization complete.");
        } finally {
            RootCategory.setPrefix(prefix);
        }
    }



    final public void pause() {
        String prefix = RootCategory.getPrefix();
        try {
            RootCategory.setPrefix(getName());

            if (!isRunning())
                return;

            setStatus(PAUSE_PENDING);

            onPause();

            setStatus(PAUSED);

            ServerLog.debug(getName()+" paused.");

        } finally {
            RootCategory.setPrefix(prefix);
        }
    }

    final public void resume() {
        String prefix = RootCategory.getPrefix();
        try {
            RootCategory.setPrefix(getName());
            if (!isPaused())
                return;

            setStatus(RESUME_PENDING);

            onResume();

            setStatus(RUNNING);

            ServerLog.debug(getName()+" resumed.");
        } finally {
            RootCategory.setPrefix(prefix);
        }
    }

    final public synchronized void start() {
    	
    	loadLog4jProperties();
        String prefix = RootCategory.getPrefix();
        try {
            RootCategory.setPrefix(getName());
            ServerLog.debug(getName()+" starting.");

            setStatus(STARTING);

            onStart();

            setStatus(RUNNING);

            ServerLog.info(getName()+" started.");
        } finally {
            RootCategory.setPrefix(prefix);
        }

    }
    
    
    final public synchronized void restartAll() {
        String prefix = RootCategory.getPrefix();
        try {
            RootCategory.setPrefix(getName());
            ServerLog.debug(getName()+" restarting.");

            setStatus(STARTING);

            onReStart();

            setStatus(RUNNING);

            ServerLog.info(getName()+" restarted.");
        } finally {
            RootCategory.setPrefix(prefix);
        }

    }

    /**
     * Stops the currently running service. If the service is not running then
     * the command is silently discarded.
     */
    final public synchronized void stop() {
        String prefix = RootCategory.getPrefix();
        try {
            RootCategory.setPrefix(getName());
            ServerLog.debug(getName()+" stopping.");
            setStatus(STOP_PENDING);

            onStop();

            setStatus(STOPPED);

            ServerLog.info(getName()+" stopped.");
        } finally {
            RootCategory.setPrefix(prefix);
        }
    }
    
    public void loadLog4jProperties() {
    	
    	try{
			URL log4jPropUrl=Thread.currentThread().getContextClassLoader().getResource("log4j.properties");
			if(log4jPropUrl!=null){
				LogManager.resetConfiguration();
				PropertyConfigurator.configure(log4jPropUrl);
			}
    	}catch (Exception e) {
    		//If an exception occurs while loading log4j properties no need to interrupt the service,
    		//hence not doing anything here. 
		}
		
	}
    
    final public synchronized String refreshNas()
    {
        String prefix = RootCategory.getPrefix();
        try 
        {
        	RootCategory.setPrefix(getName());
            ServerLog.debug(getName()+" caching Nas Details starting.");
            
            onRefreshNas();
            
            ServerLog.debug(getName()+" caching Nas Details stopped.");
        } 
        finally 
        {
            RootCategory.setPrefix(prefix);
        }
        return prefix;
    }
    
    final public synchronized String refreshService()
    {
        String prefix = RootCategory.getPrefix();
        try 
        {
        	RootCategory.setPrefix(getName());
            ServerLog.debug(getName()+" caching Service Details starting.");
            
            onRefreshService();
            
            ServerLog.debug(getName()+" caching Service Details stopped.");
        } 
        finally 
        {
            RootCategory.setPrefix(prefix);
        }
        return prefix;
    }
    
    final public synchronized String refreshRealm()
    {
        String prefix = RootCategory.getPrefix();
        try 
        {
        	RootCategory.setPrefix(getName());
            ServerLog.debug(getName()+" caching Realm Details starting.");
            
            onRefreshRealm();
            
            ServerLog.debug(getName()+" caching Realm Details stopped.");
        } 
        finally 
        {
            RootCategory.setPrefix(prefix);
        }
        return prefix;
    }
    
    final public synchronized String refreshBandWidth()
    {
        String prefix = RootCategory.getPrefix();
        try 
        {
        	RootCategory.setPrefix(getName());
            ServerLog.debug(getName()+" caching BandWidth Details starting.");
            
            onRefreshBandWidth();
            
            ServerLog.debug(getName()+" caching BandWidth Details stopped.");
        } 
        finally 
        {
            RootCategory.setPrefix(prefix);
        }
        return prefix;
    }
    
    final public synchronized String refreshBusinessEntity()
    {
        String prefix = RootCategory.getPrefix();
        try 
        {
        	RootCategory.setPrefix(getName());
            ServerLog.debug(getName()+" caching Business Entity Details starting.");
            
            onRefreshRule();
            
            ServerLog.debug(getName()+" caching Business Entity Details stopped.");
        } 
        finally 
        {
            RootCategory.setPrefix(prefix);
        }
        return prefix;
    }
    
    final public synchronized String refreshRule()
    {
        String prefix = RootCategory.getPrefix();
        try 
        {
        	RootCategory.setPrefix(getName());
            ServerLog.debug(getName()+" caching Rule Details starting.");
            
            onRefreshRule();
            
            ServerLog.debug(getName()+" caching Rule Details stopped.");
        } 
        finally 
        {
            RootCategory.setPrefix(prefix);
        }
        return prefix;
    }
    
    final public synchronized String refreshGroup()
    {
        String prefix = RootCategory.getPrefix();
        try 
        {
        	RootCategory.setPrefix(getName());
            ServerLog.debug(getName()+" caching Group Details starting.");
            
            onRefreshGroup();
            
            ServerLog.debug(getName()+" caching Group Details stopped.");
        } 
        finally 
        {
            RootCategory.setPrefix(prefix);
        }
        return prefix;
    }
    final public synchronized String refreshPoolDetails() {
    	 String prefix = RootCategory.getPrefix();
		System.out.println("refreshPoolDetails");
		 return prefix;
	}
  
  }
