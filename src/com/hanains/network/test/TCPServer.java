package com.hanains.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

			// 5. IOStream 받아오기
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			// 6. 데이터 읽기
			try {
				byte[] buffer = new byte[256];
				while (true) {

					int readByteCount = inputStream.read(buffer);
					if (readByteCount < 0) {
						System.out.println("[서버] 클라이언트로부터 연결 끊김");
						break;
					}

					String data = new String(buffer, 0, readByteCount); // byte -> String
					System.out.println("[서버] 수신 데이터 : " + data);
					
					// 7. 데이터 보내기
					outputStream.write(data.getBytes("UTF-8")); // String -> byte
					outputStream.flush(); // server socket의 outputStream buffer를 비워 전송 buffer에 넣어주는 것
					
				}
			} catch (IOException ex) {
				System.out.println("[서버] 에러 : " + ex);
			} finally {

				// 8. 자원 정리
				inputStream.close();
				outputStream.close();
				if (socket.isClosed() == false) {
					socket.close();
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
			return;

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

	}

}
