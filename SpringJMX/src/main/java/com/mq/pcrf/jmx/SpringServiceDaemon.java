
package com.mq.pcrf.jmx;

import org.springframework.beans.factory.InitializingBean;


public interface SpringServiceDaemon extends InitializingBean {
    public void start() throws Exception;
}
