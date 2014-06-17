package com.wangyin.cds.server.modules;

import java.lang.management.ManagementFactory;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.wangyin.cds.server.container.sample.HelloConfig;
import com.wangyin.cds.server.container.sample.IHelloMBean;

public class JmxModule extends ServerModule implements INodeModule {
	private volatile boolean running;
	private Thread runningThread;
	public void configure(Map<String, Object> configuration) {
		
	}

	public void startup() throws Exception {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = new ObjectName("com.wangyin.cds.server:type=HelloConfig");
		IHelloMBean bean = new HelloConfig();
		mbs.registerMBean(bean, name);
		runningThread = Thread.currentThread();
		while (running){
			try{
				Thread.sleep(5000);
			}catch(InterruptedException e){
				break;
			}
		}
	}

	public void shutdown() throws Exception {
		runningThread.interrupt();
		running = false;
	}

}
