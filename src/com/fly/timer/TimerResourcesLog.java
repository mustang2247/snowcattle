
package com.fly.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.fly.entity.Resourceslog;
import com.fly.uibutil.ToolOS;

/**
 * 定时记录系统资源负载情况
 */
public class TimerResourcesLog extends Timer
{

  private static Logger log = Logger.getLogger(TimerResourcesLog.class);

  /**
   * 定时任务对象
   */
  private static TimerResourcesLog timer = null;

  /**
   * 启动任务
   */
  public static void start()
  {
    if (null != timer)
    {
      log.info("启动失败，任务已经启动");
      return;
    }
    log.info("开始启动任务");
    timer = new TimerResourcesLog();
    timer.schedule(new TimerTask()
    {
      @Override
      public void run()
      {
        log.info("任务执行开始");
        String osName = ToolOS.getOsName(); // 获取操作系统类型名称
        String ip = ToolOS.getOsLocalHostIp(); // 获取本机IP
        String hostName = ToolOS.getOsLocalHostName(); // 获取本机名称
        int cpuNumber = ToolOS.getOsCpuNumber(); // 获取CPU数量
        double cpuRatio = ToolOS.getOscpuRatio(); // cpu使用率
        if (cpuRatio < 0)
        {
          cpuRatio = 0;
        }

        long phyMemory = ToolOS.getOsPhysicalMemory(); // 物理内存，总的可使用的
        long phyFreeMemory = ToolOS.getOsPhysicalFreeMemory(); // 物理内存，剩余

        long jvmTotalMemory = ToolOS.getJvmTotalMemory(); // JVM内存，内存总量
        long jvmFreeMemory = ToolOS.getJvmFreeMemory(); // JVM内存，空闲内存量
        long jvmMaxMemory = ToolOS.getJvmMaxMemory(); // JVM内存，最大内存量
        long gcCount = ToolOS.getJvmGcCount(); // 获取JVM GC次数
        long diskMemory = ToolOS.getOsDiskMemory();// c盘总量
        long diskFreeMemory = ToolOS.getOsDiskFreeMemory();// c盘空闲大小
        Resourceslog resourceslog = new Resourceslog();
        resourceslog.set("osname", osName);
        resourceslog.set("ip", ip);
        resourceslog.set("hostname", hostName);
        resourceslog.set("cpunumber", cpuNumber);
        resourceslog.set("cpuratio", cpuRatio * 100.00);
        resourceslog.set("phymemory", phyMemory);
        resourceslog.set("phyfreememory", phyFreeMemory);
        resourceslog.set("diskmemory", diskMemory);
        resourceslog.set("diskfreememory", diskFreeMemory);
        resourceslog.set("jvmtotalmemory", jvmTotalMemory);
        resourceslog.set("jvmfreememory", jvmFreeMemory);
        resourceslog.set("jvmmaxmemory", jvmMaxMemory);
        resourceslog.set("gccount", gcCount);
        resourceslog.set("create_time", new Date());
        resourceslog.save();
        log.info("任务执行结束");
      }
    }, 1000, 1000 * 60 * 2);// 启动项目一秒后执行，然后每次间隔2分钟
    log.info("启动任务完成");
  }

  /**
   * 停止任务
   */
  public static void stop()
  {
    if (null != timer)
    {
      log.info("任务退出开始");
      timer.cancel();
      log.info("任务退出成功");
    }
    else
    {
      log.info("任务退出失败，任务为空");
    }
  }

}
