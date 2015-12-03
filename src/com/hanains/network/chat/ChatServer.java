package com.hanains.network.chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer {

	private static final int PORT = 9090;

	public static void main(String[] args) {

		ServerSocket serverSocket = null;

		try {
			// 서버 소켓 생성
			serverSocket = new ServerSocket();

			// 데이터 통신 스레드들이 공유하는 List
			List<Writer> listWriters = new ArrayList<Writer>();
			//닉네임
			Map<String,Writer> listNickNames=new HashMap<String,Writer>();

			// 바인딩
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(hostAddress,PORT));

			log("연결 기다림 " + hostAddress + ":" + PORT);

			while (true) {
				Socket socket = serverSocket.accept();
				new ChatServerThread(socket, listWriters, listNickNames).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 서버 소켓 닫기
			if (serverSocket != null && serverSocket.isClosed() == false) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					log("error: " + e);
				}
			}
		}

	}

	public static void log(String message) {
		System.out.println("[서버] " + message);
	}
}
