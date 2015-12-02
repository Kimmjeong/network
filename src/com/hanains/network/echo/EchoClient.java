package com.hanains.network.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

	private static final String SERVER_IP = "192.168.56.1";
	private static final int SERVER_PORT = 5050;

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		Socket socket = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			// 소켓 생성
			socket = new Socket();

			// 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[클라이언트] 서버 연결 성공");

			// IOStream 받아오기
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();

			while (true) {
				// 쓰기/읽기
				
				// 클라이언트 입력
				System.out.print(">> ");
				String data = sc.nextLine();
				
				// exit 입력되면 종료
				if (data.equals("exit")){
					break;
				}
				
				// server에서 readline으로 읽을 때 \r\n을 기준으로 끊어 읽기 때문에, println이라면 \r\n 할 필요 없음.
				outputStream.write((data+"\r\n").getBytes("UTF-8")); // 서버에 문자열 전달하기
				
				byte[] buffer = new byte[256];
				int readByteCount = inputStream.read(buffer); // 서버로부터 문자열 전달받기

				data = new String(buffer, 0, readByteCount, "UTF-8");
				System.out.println("<< " + data);

			}

		} catch (IOException ex) {
			System.out.println("[클라이언트] 에러 : " + ex);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		sc.close();
	}
}
