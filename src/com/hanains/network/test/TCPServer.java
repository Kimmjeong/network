package com.hanains.network.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	private static final int PORT = 5050;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;

		try {

			// 1. 서버 소켓 생성
			serverSocket = new ServerSocket();

			// 2. 바인딩
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhost = inetAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhost, PORT));
			System.out.println("[서버] 바인딩 " + localhost + ":" + PORT);

			// 3. 연결 요청 대기(accept)
			Socket socket = serverSocket.accept();

			// 4. 연결 성공
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			String remoteHostAddress = inetSocketAddress.getAddress().getHostAddress();
			int remoteHostPort = inetSocketAddress.getPort();
			System.out.println("[서버] 연결됨 from " + remoteHostAddress + ":" + remoteHostPort);

			// 7. 소켓 닫기
			if (socket.isClosed()==false) {
				socket.close();
			}

		} catch (IOException e) {

			e.printStackTrace();
			return;

		} finally { // return 되기전에 불려진다
			// 서버 소켓 닫기
			if (serverSocket != null && serverSocket.isClosed()==false) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
