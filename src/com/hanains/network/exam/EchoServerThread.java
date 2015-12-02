package com.hanains.network.exam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EchoServerThread extends Thread {

	private Socket socket;

	public EchoServerThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {

			// 연결한 소켓 얻어오기
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			// 연결한 소켓의 IP 주소 얻기 // 주소에는 hostName + hostAddress가 들어있다.
			String remoteHostAddress = inetSocketAddress.getAddress().getHostAddress();
			// 연결한 소켓의 포트번호
			int remoteHostPort = inetSocketAddress.getPort();
			System.out.println("[서버] 연결됨 from " + remoteHostAddress + ":" + remoteHostPort);

			// IOStream 받아오기
			// socket으로부터 읽을 스트림
			InputStream inputStream = socket.getInputStream();
			// socket을 통해 보낼 스트림
			OutputStream outputStream = socket.getOutputStream();

			// 데이터 보낼 때
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
			// 데이터 읽을 때
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			String line = null;
			while ((line = bufferedReader.readLine()) != null) { // 데이터 읽기
				System.out.println("클라이언트로부터 전송받은 문자열 : " + line);

				// 데이터 보내기
				printWriter.println(line);
				printWriter.flush();
			}
			if (line == null) {
				System.out.println("[서버] 클라이언트로부터 연결 끊김");
			}
			
			printWriter.close();
			bufferedReader.close();

		} catch (IOException e) {
			System.out.println("[서버] 에러 : " + e);
		} finally {
			// 자원 정리
			if (socket.isClosed() == false) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
