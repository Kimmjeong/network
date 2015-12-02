package com.hanains.network.exam;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServerExam {

	private static final int PORT=5050;
	public static void main(String[] args) {
		
		ServerSocket serverSocket=null;
		
		try{
			// 서버 소켓 생성
			serverSocket=new ServerSocket();
			
			// 로컬호스트 얻어오기
			InetAddress inetAddress=InetAddress.getLocalHost();
			String localhost=inetAddress.getHostAddress();
			
			// 바인딩
			serverSocket.bind(new InetSocketAddress(localhost, PORT));
			System.out.println("[서버] 바인딩 " + localhost + ":" + PORT);
			
			System.out.println("[서버] 연결 기다림");
			while(true){
				
				// 연결 요청 대기
				Socket socket=serverSocket.accept();
				
				Thread thread = new EchoServerThread(socket);
				thread.start();
			}
			
		}catch (IOException e){
			e.printStackTrace();
		} finally{ // 서버 소켓 닫기
			if(serverSocket !=null && serverSocket.isClosed()==false){
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
