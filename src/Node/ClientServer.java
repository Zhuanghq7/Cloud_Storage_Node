package Node;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ClientServer extends Thread{
	private Socket s;
	private boolean isConnect = false;
	public ClientServer(Socket s){
		this.s = s;
		isConnect = true;
	}
	
	private void out(String ss) throws UnsupportedEncodingException, IOException{
		if(isConnect){
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(),"UTF-8"));//不管三七二十一咱先把这个文件名字写了是吧
			bw.write(ss+"\n");
			bw.flush();
		}
	}
	private void outS(String ss,Socket st)throws UnsupportedEncodingException,IOException{
		if(isConnect){
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(st.getOutputStream(),"UTF-8"));//不管三七二十一咱先把这个文件名字写了是吧
			bw.write(ss+"\n");
			bw.flush();
		}
	}
	private String inS(Socket st) throws IOException{
		if(isConnect){
			BufferedReader br = new BufferedReader(new InputStreamReader(st.getInputStream(),"UTF-8"));
			return br.readLine();
		}
		return null;
	}
	private String in() throws UnsupportedEncodingException, IOException{
		if(isConnect){
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream(),"UTF-8"));
			return br.readLine();
		}
		return null;
	}
	private boolean waitGet() throws UnsupportedEncodingException, IOException{
		String temp = in();
		if(temp.equals("get"))
			return true;
		return false;
	}
	private void delete(String file){
		File f = new File(file);
		if(f.exists()){
			f.delete();
		}
	}
	@Override
	public void run(){
		String fun;
		try {
			fun = in();
			switch(fun){
			case "first":
				out(MainNode.name);
				waitGet();
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeLong(MainNode.maxStorage);
				waitGet();
				dos.writeLong(MainNode.leftStorage);
				waitGet();
				break;
			case"second":
				out("get");
				String fun2 = in();
				switch(fun2){
				case"up":
					out("get");
					DataInputStream dis = new DataInputStream(s.getInputStream());
					long flength = dis.readLong();
					if(flength<=MainNode.leftStorage){
						out("get");
						String fileName = in();
						out("get");
						try{
							File f = new File(MainNode.root_folder);
							if(!f.exists()){
							f.mkdirs();
							} 
							File file = new File(f,fileName);
							if(!file.exists()){
								file.createNewFile();
							}
						//File f = new File(MainNode.root_folder+"\\"+fileName);

							FileOutputStream fos = new FileOutputStream(file);
	
							byte[] inputByte = new byte[1024];     
							System.out.println("开始下载文件："+fileName);  
							double sumL = 0;
							int length;
							while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {  
								fos.write(inputByte,0,inputByte.length);
								fos.flush();
								sumL+=length;
								System.out.println("已传输："+sumL/(flength/100)+"%");
								if(sumL>=flength){
									//System.out.println("1");
									out("get");
									fos.flush();
									fos.close();
									break;
								}  
							}  
						}catch(FileNotFoundException e3){
							System.out.println("传输中断，删除本地文件");
							delete(fileName);
						}
					}else{
						out("false");
					}
					break;
				case"down":
					out("get");
					String fileName = in();
					File f = new File(MainNode.root_folder);
					if(!f.exists()){
						f.mkdirs();
					} 
					File file = new File(f,fileName);
					if(!file.exists()){
						out("false");
					}else{
						FileInputStream fis = new FileInputStream(file);
						out("get");
						long l = file.length();
						DataOutputStream doss = new DataOutputStream(s.getOutputStream());
						waitGet();
						doss.writeLong(l);
						waitGet();
						//交涉完成
						byte[] inputByte = new byte[1024];     
						System.out.println("开始传送文件："+fileName);  
						double sumL = 0;
						int length;
						try{
							while ((length = fis.read(inputByte, 0, inputByte.length)) > 0) {  
								doss.write(inputByte,0,inputByte.length);
								doss.flush();
								sumL+=length;
								System.out.println("已传输："+sumL/(l/100)+"%");
								if(sumL>=l){
									//System.out.println("1");
									break;
								}  
							}  
						}
						catch(Exception e){
							
						}
					//File f = new File(MainNode.root_folder+"\\"+fileName);
					}
					if(s!=null){
						s.close();
					}
					break;
				case"delete":
					out("get");
					String ffff = in();
					File fffff = new File(ffff);
					long ffflength = fffff.length();
					MainNode.leftStorage-=ffflength;
					delete(in());
					break;
				}
				break;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
}
