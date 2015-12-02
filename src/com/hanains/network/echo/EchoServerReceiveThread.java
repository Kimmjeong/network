package com.hanains.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EchoServerReceiveThread extends Thread {

	private Socket socket=null;
	
	public EchoServerReceiveThread(Socket socket) {
		this.socket=socket;
	}
	
	@Override
	public void run() {
		
		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;
		

		try{
					// 연결 성공
					InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
					String remoteHostAddress = inetSocketAddress.getAddress().getHostAddress();
					int remoteHostPort = inetSocketAddress.getPort();
					System.out.println("[서버] 연결됨 from " + remoteHostAddress + ":" + remoteHostPort);

					// IOStream 받아오기
					bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // inputStreamReader = new InputStreamReader(socket.getInputStream());
					printWriter = new PrintWriter(socket.getOutputStream());

					// 데이터 읽기
					try {
						char[] buffer = new char[256]; // byte[] buffer = new byte[256];
						while (true) {

							String data=bufferedReader.readLine();// int readCharCount = inputStreamReader.read(buffer);
							if(data==null){ //if (readCharCount < 0) {
								System.out.println("[서버] 클라이언트로부터 연결 끊김");
								break;
							}

							//String data = new String(buffer, 0, readCharCount); // byte -> String
							System.out.println("[서버] 데이터 수신 : " + data);
							
							// 데이터 보내기
							printWriter.println(data); //outputStream.write(data.getBytes("UTF-8")); // String -> byte : outputStream은 String을 byte로 바꿔줘야 했지만, printWriter는 자동으로 변환
							printWriter.flush();     //outputStream.flush(); // server socket의 outputStream buffer를 비워 전송 buffer에 넣어주는 것
							
						}
					} catch (IOException ex) {
						System.out.println("[서버] 에러 : " + ex);
					} finally {
						// 자원 정리
						bufferedReader.close();
						printWriter.close(); // outputStream.close();
						if (socket.isClosed() == false) {
							socket.close();
						}
					}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
