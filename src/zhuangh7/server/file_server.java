package zhuangh7.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Random;


public class file_server extends Thread{
	Socket socket;
	boolean stop = false;

	file_server(Socket s){
		socket = s;
	}
	public void run(){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			String fun = br.readLine();
			if(fun.equals("up")){
				upload();
			}
			else if(fun.equals("down")){
				download();
			}
			else if(fun.equals("delete")){
				
			}
			else if(fun.equals("rename")){
				
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
	public void delete(String path){
		File f = new File(path);
		if(f.exists()){
			f.delete();
		}
		System.out.println("������ֹ��ϣ�ɾ���ѱ����ļ�");
	}
	
	public void out(String s) throws IOException{
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
		bw.write(s+'\n');
	}
	
	public String in() throws UnsupportedEncodingException, IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
		return br.readLine();
	}
	public void download(){
		String filePath = "";
		try {
			//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
			//BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			filePath=filePath+"\\"+in();//�ӷ�������ȡ�ļ���
			
			File fff = new File(".");
			String nowPath = fff.getCanonicalPath()+filePath;
			
			File f = new File(nowPath);
			FileInputStream fis = new FileInputStream(f);
			
			
			double sumL = 0;
			long l = f.length();
			byte[] sendBytes = new byte[1024];  
			int length = 0;
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeLong(l);
			while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0 && !stop) {  
                sumL += length;    
               // System.out.println("�Ѵ��䣺"+((sumL/l)*100)+"%");  
                dos.write(sendBytes, 0, length);  
                dos.flush(); 
                if(!stop){
    	            
	            }
	            else{   
	            	System.out.println("������ֹ");
	            	break;
	            }
			}
			System.out.println("�������");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//�������߶�ʮһ���Ȱ�����ļ�����д���ǰ�
		
	}
	public void upload(){
		String filePath="";
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
                    if(!stop){
        	            
    	            }
    	            else{   
    	            	System.out.println("������ֹ");
    	            	break;
    	            }
                }  
                bw.write("close\n");
                bw.flush();
                bw.close();
                br.close();
                fos.close();
                if(!stop){
                	System.out.println("��ɽ��գ�"+filePath);
                }else{
                	System.out.println("���ܳ�������");
                }
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
	public void Stop(){
		stop = true;
	}
}
