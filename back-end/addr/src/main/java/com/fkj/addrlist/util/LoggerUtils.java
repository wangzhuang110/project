package com.fkj.addrlist.util;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @Auther: developer-xtf
 * @Date: 2019/4/8 14:15
 * @Description:
 */
public abstract class LoggerUtils {
  private static final Logger log = (Logger) LoggerFactory.getLogger("usr-op");
  private static String time;
  private static String user;
  private static String seat;
  private static String sw;
  private static String msg;

  private LoggerUtils() {}

  public static String getTime() {
    return time;
  }

  public static void setTime(String time) {
    LoggerUtils.time = time;
  }

  public static String getUser() {
    return user;
  }

  public static void setUser(String user) {
    LoggerUtils.user = user;
  }

  public static String getSeat() {
    return seat;
  }

  public static void setSeat(String seat) {
    LoggerUtils.seat = seat;
  }

  public static String getSw() {
    return sw;
  }

  public static void setSw(String sw) {
    LoggerUtils.sw = sw;
  }

  public static String getMsg() {
    return msg;
  }

  public static void setMsg(String msg) {
    LoggerUtils.msg = msg;
  }

  public static void info (String time, String user, String seat, String sw, String msg) {
    MDC.put("time",time);
    MDC.put("user",user);
    MDC.put("seat",seat);
    MDC.put("sw",sw);
    log.info(msg);
  }

}
