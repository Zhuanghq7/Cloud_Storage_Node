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
			public void windowClosing(WindowEvent e) {//���ڼ���
				if(!mainServer.isClosed()){
						mainServer.Close();
				}
				System.out.println("�رշ�����");
				System.exit(0);
			}
		});
		mainServer.run();
	}
	public void run(){
		while(retryTimes>0){
			try {
				
				ServerSocket ss = new ServerSocket(socketPort);
				System.out.println("Node�����������ɹ�");
				
				while(!close){
					Socket s = ss.accept();
					System.out.println("��������һ����������");
					new file_server(s).start();
					//����һ�����߳̽��д���ðɡ�_�����ڸ�Ϊ�̳߳�
				}
				if(!ss.isClosed()){
					ss.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("����Node������ʧ�ܣ���������,ʣ����������:"+--retryTimes);

			}	
		}
		//�򿪷��������˿�2016
	}
	public boolean isClosed(){
		return close;
	}
	public void Close(){
		close = true;
	}
}
