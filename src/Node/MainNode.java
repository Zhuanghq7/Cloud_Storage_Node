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
	public static void saveProperties(){
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
	
	public static long getVolume(String s){
		String unit = s.substring(s.length()-2);
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
			prop = new Properties();//属性集合对象      
			FileInputStream fis;
			fis = new FileInputStream("Node.properties");
			prop.load(fis);//将属性文件流装载到Properties对象中    
			name = prop.getProperty("server");
			ip = prop.getProperty("ip");
			port = Integer.parseInt(prop.getProperty("port"));
			root_folder = prop.getProperty("root_folder");
			maxStorage = getVolume(prop.getProperty("volume"));
			leftStorage = getVolume(prop.getProperty("leftvolume"));
			System.out.println("初始化成功");
			ServerSocket ss = new ServerSocket(port);
			while(true){
				Socket s = ss.accept();
				new ClientServer(s).start();
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
