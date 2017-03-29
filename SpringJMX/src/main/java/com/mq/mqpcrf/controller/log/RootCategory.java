

package com.mq.mqpcrf.controller.log;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;


public class RootCategory extends Category {
    private static final String DEFAULT_CATEGORY = "Default";
    
    
    private static InheritableThreadLocal<String> s_threadCategory = new InheritableThreadLocal<String>();

    
    protected RootCategory(String name) {
        super(name);
    }

   
    public static Category getInstance(Class c) {
        String prefix = getPrefix();

        if ((prefix != null) && !prefix.equals("")) {
            return Logger.getLogger(prefix + "." + c.getName());
        } else {
            return Logger.getLogger(c.getName());
        }
    }

   
    public static Category getInstance(String cname) {
        String prefix = getPrefix();

        if ((prefix != null) && !prefix.equals("")) {
            return Logger.getLogger(prefix + "." + cname);
        } else {
            return Logger.getLogger(cname);
        }
    }

    
    public static Category getInstance() {
        String prefix = getPrefix();
        
        if (prefix != null) {
            return Logger.getLogger(prefix);
        } else {
            
            return Logger.getLogger(DEFAULT_CATEGORY);
        }
    }

    
    public static void setPrefix(String prefix) {
        s_threadCategory.set(prefix);
    }

    
    public static String getPrefix() {
        return s_threadCategory.get();
    }
}
