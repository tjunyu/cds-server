package com.wangyin.cds.server;

import static com.wangyin.cds.server.Predefined.NODE_TYPE_MASTER;
import static com.wangyin.cds.server.Predefined.NODE_TYPE_SLAVE;

import java.nio.channels.ShutdownChannelGroupException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;

import ch.qos.logback.classic.Logger;

/**
 * Master/Slave节点启动器
 * 
 * @author david
 */
public class Bootstrap {
	enum Action{START,STOP};
	@Option(name = "-t", aliases = { "-type" }, metaVar = "master|slave", required = true, usage = "Server Type")
	private String nodeType;

	@Option(name = "-h", aliases = { "-home" }, metaVar = "/var/tmp", usage = "Server Home")
	private String serverHome;
	
	@Argument
	private List<String> arguments = new ArrayList<String>();

	private Action action= Action.START;
	
	private ServerNode activeNode;

	private Bootstrap() {

	}

	public static void main(String[] args) {
		Bootstrap boot = new Bootstrap();
		boot.execMain(args);
	}

	private void execMain(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);
		parser.setUsageWidth(80);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			System.err.println("java Bootstrap [options...] arguments...");
			parser.printUsage(System.err);
			System.err.println();
			System.err.println("Example: java Bootstrap "
					+ parser.printExample(OptionHandlerFilter.ALL));
			return;
		}
		if (arguments.size() > 0 && arguments.get(0).equals("stop")){
			action = Action.STOP;
		}
		if (action==Action.START){
			System.out.println("try to start server");
			listenForClose();
		}else{
			System.out.println("try to stop server");
			shutdownLocalNode();
			return;
		}
		
		if (nodeType.equals(NODE_TYPE_MASTER) && nodeType.equals(NODE_TYPE_SLAVE)) {
			System.err.println("node type must be 'master' or 'slave'");
			System.exit(2);
		}
		if (null == serverHome) {
			serverHome = System.getProperty(Predefined.PROP_CDS_SERVER_HOME);
		}
		if (null == serverHome || !validHome(serverHome)) {
			System.err.println("the home path of server "+serverHome+" is not an valid directory");
			System.exit(3);
		}
		activeNode = nodeType.equals(NODE_TYPE_MASTER) ? new MasterNode()
				: new SlaveNode();
		activeNode.setHome(serverHome);
		try {
			activeNode.initialize();
		} catch (Exception e) {
			System.err.println("failed to initialize server");
			e.printStackTrace();
			try {
				activeNode.shutdown();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.exit(4);
		}
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				try {
					activeNode.shutdown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try{
			activeNode.startup();
		}catch(Exception e){
			System.err.print("failed to start server");
			e.printStackTrace();
			try {
				activeNode.shutdown();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.exit(5);
		}
	}
	//TODO 启动一个本地监听关闭
	private void listenForClose() {
		
	}
	//TODO 往本地发送一个关闭命令
	private void shutdownLocalNode() {
		
	}

	private boolean validHome(String serverHome2) {
		// TODO:验证home的目录和文件
		return true;
	}

}
