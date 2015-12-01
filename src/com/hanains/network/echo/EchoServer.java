package com.hanains.network.echo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {

	private static final int PORT = 5050;

	public static void main(String[] args) {

		Scanner sc=new Scanner(System.in);
		ServerSocket serverSocket = null;

		
		try {
			// 서버 소켓 생성
			serverSocket = new ServerSocket();

			// 바인딩
			InetAddress inetAddress = InetAddress.getLocalHost();

			String localhost = inetAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhost, PORT));
			
			System.out.println("[서버] 바인딩 " + localhost + ":" + PORT);

			System.out.println("[서버] 연결 기다림");
			
			while (true) {
				
				// 연결 요청 대기(accept)
				Socket socket = serverSocket.accept();

				Thread thread = new EchoServerReceiveThread(socket);
				thread.start();
								
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // return 되기전에 불려진다
			// 서버 소켓 닫기
			if (serverSocket != null && serverSocket.isClosed() == false) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		sc.close();
	}
}
