package zhuangh7.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Random;

import zhuangh7.tools.timeTool;

public class file_server extends Thread{
	Socket socket;
	String filePath = "";
	file_server(Socket s){
		socket = s;
	}
	public void run(){
		byte[] inputByte = null;  
        int length = 0;  
        DataInputStream dis = null;  
        FileOutputStream fos = null;  
        String nowPath = null;
        long flength= 0;
        File fff = new File(".");
        try {
			nowPath = fff.getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        //String filePath = nowPath+timeTool.getDate()+"SJ"+new Random().nextInt(10000)+".zip";  
        filePath = nowPath;
        try {  
            try {  
            	BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            	filePath=filePath+"\\"+br.readLine();//�ӷ�������ȡ�ļ���
            	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
            	bw.write("get\n");//�ظ��������ļ����Ѿ����
            	bw.flush();
                dis = new DataInputStream(socket.getInputStream());
                flength = dis.readLong();//�ӷ�������ȡ�ļ���С
                File f = new File(filePath);
                fos = new FileOutputStream(f);      
                inputByte = new byte[1024];     
                System.out.println("��ʼ��������...");  
                double sumL = 0;
                while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {  
                    fos.write(inputByte, 0, length);  
                    fos.flush();  
                    sumL+=length;
                    System.out.println("�Ѵ��䣺"+sumL/(flength/100)+"%");
                    if(sumL>=flength){
                    	//System.out.println("1");
                    	break;
                    }  
                }  
                bw.write("close\n");
                bw.flush();
                bw.close();
                br.close();
                fos.close();
                System.out.println("��ɽ��գ�"+filePath);  
            } finally {  
                if (fos != null)  
                    fos.close();
                if (dis != null)  
                    dis.close();  
                if (socket != null)  
                    socket.close();   
            }  
        } catch (IOException e) {  
            e.printStackTrace();
            delete(filePath);
        }  
    }  
	public void delete(String path){
		File f = new File(path);
		if(f.exists()){
			f.delete();
		}
		System.out.println("������ֹ��ϣ�ɾ���ѱ����ļ�");
	}
}
