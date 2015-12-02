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
import java.util.Scanner;

public class EchoClientExam {

	private static final String SERVER_IP = "192.168.56.1";
	private static final int PORT = 5050;

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		Socket socket = null;

		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			// 소켓생성
			socket = new Socket();
			// 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, PORT));
			System.out.println("[클라이언트] 서버 연결 성공");

			// IOStream 받아오기
			// 연결된 상대방과 통신할 수 있도록 도와줌

			// 데이터 받을 때
			inputStream = socket.getInputStream();
			// 데이터 보낼 때
			outputStream = socket.getOutputStream();

			// 데이터 보낼 때
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream));
			// 데이터 받을 때
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			while (true) {

				// 클라이언트 입력
				System.out.print(">> ");
				String data = sc.nextLine();

				// exit 입력되면 종료
				if (data.equals("exit")) {
					break;
				}

				// 데이터 보내기
				pw.println(data);
				pw.flush();

				// 데이터 받기
				String echo = br.readLine();
				System.out.println("<<" + echo);
			}

			pw.close();
			br.close();

		} catch (IOException e) {
			System.out.println("[클라이언트] 에러 : " + e);
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
