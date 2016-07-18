package Node;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;


public class MainNode {
	public static Properties prop;
	public static String name;
	public static String ip;
	public static int port;
	public static String root_folder;
	public static long maxStorage;
	public static long leftStorage;
	/*
	 * MainNode类负责储存整个程序初始化时从本地读取的信息
	 * prop：储存属性对象
	 * name：本地节点服务器的名称
	 * ip：本地节点服务器的ip地址
	 * port：本地节点服务器对应ip地址的对应端口
	 * root_folder：节点服务器接收文件的存储位置
	 * maxStorage:节点服务器最大储存容量
	 * leftStorage:节点服务器剩余容量
	 */
	
	
	public static void saveProperties(){//保存已有信息到本地文件中，防止意外丢失
		try {
			FileOutputStream fos = new FileOutputStream("Node.properties");
			prop.store(fos, null);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static long getVolume(String s){//输入一个大小字符串，从中获得大小并返回以byte为单位的long型
		String unit = s.substring(s.length()-2);
		//取出单位
		long result = 0;
		switch(unit){
		case "GB":
			result = Long.parseLong(s.substring(0, s.length()-2))*1024*1024*1024;
			return result;
		case "MB":
			result = Long.parseLong(s.substring(0,s.length()-2))*1024*1024;
			return result;
		case "KB":
			result = Long.parseLong(s.substring(0,s.length()-2))*1024;
			return result;
		}
		return 0;
	}
	
	public static void main(String[] args){
		try {
			MainNode MN = new MainNode();
			prop = new Properties(); 
			FileInputStream fis;
			fis = new FileInputStream("Node.properties");
			prop.load(fis);
			//将属性文件流装载到Properties对象中    
			name = prop.getProperty("server");
			ip = prop.getProperty("ip");
			port = Integer.parseInt(prop.getProperty("port"));
			root_folder = prop.getProperty("root_folder");
			maxStorage = getVolume(prop.getProperty("volume"));
			leftStorage = getVolume(prop.getProperty("leftvolume"));
			//通过properties初始化Node
			System.out.println("初始化成功");
			ServerSocket ss = new ServerSocket(port);
			while(true){
				Socket s = ss.accept();
				new ClientServer(s).start();
				//监听到了服务器发来的请求
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("初始化服务器失败！");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("初始化服务器失败！");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
