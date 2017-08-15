package com.wiseweb.cat.base;

import com.wiseweb.tools.Util;
import org.apache.log4j.Logger;

/**
 * Created by 贾承斌 on 15/11/7.
 */
public abstract class BaseClass {
//    protected Logger logger = LoggerFactory.getLogger();
    Logger logger = Logger.getLogger(this.getClass());

    protected void info(String message){
        logger.info("[info][" + Util.getDateFormat() + "]["+this.getClass().getName()+"]----->"+ message);
    }
    protected void debug(String message){
        logger.debug("[info][" + Util.getDateFormat() + "][" + this.getClass().getName() + "]----->" + message);
    }
    protected void error(String message){
        logger.error("[info][" + Util.getDateFormat() + "][" + this.getClass().getName() + "]----->" + message);
    }
    protected void error(String message,Exception e){
        logger.error("[info][" + Util.getDateFormat() + "][" +this.getClass().getName()+"]----->"+message,e);
    }
    protected void warning(String message){
        logger.warn("[info][" + Util.getDateFormat() + "][" + this.getClass().getName() + "]----->" + message);
    }



}
