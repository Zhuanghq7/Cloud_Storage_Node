package zhuangh7.server;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

public class socket_to_Server extends Thread{
	static int socketPort = 2016;
	static int retryTimes = 3;
	static boolean close = false;
	public static void main(String args[]){
		socket_to_Server mainServer = new socket_to_Server();
		JFrame jf = new JFrame("Node");
		jf.setSize(300, 200);
		jf.setVisible(true);
		Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
		jf.setLocation(screenSize.width/2-150,screenSize.height/2-100);
		jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jf.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {//窗口监听
				if(!mainServer.isClosed()){
						mainServer.Close();
				}
				System.out.println("关闭服务器");
				System.exit(0);
			}
		});
		mainServer.run();
	}
	public void run(){
		while(retryTimes>0){
			try {
				
				ServerSocket ss = new ServerSocket(socketPort);
				System.out.println("Node服务器创建成功");
				
				while(!close){
					Socket s = ss.accept();
					System.out.println("监听到了一个服务请求");
					new file_server(s).start();
					//创建一个新线程进行处理好吧→_→后期改为线程池
				}
				if(!ss.isClosed()){
					ss.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("创建Node服务器失败！尝试重启,剩余重启次数:"+--retryTimes);

			}	
		}
		//打开服务器，端口2016
	}
	public boolean isClosed(){
		return close;
	}
	public void Close(){
		close = true;
	}
}
