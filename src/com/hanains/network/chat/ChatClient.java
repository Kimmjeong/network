package com.hanains.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClient {

	private static final String SERVER_IP = "192.168.56.1";
	//private static final String SERVER_IP = "192.168.1.20";
	private static final int SERVER_PORT = 9090;

	public static void main(String[] args) {

		Scanner scanner = null;
		Socket socket = null;

		try {

			BufferedReader bufferedReader;
			PrintWriter printWriter;
			
			// 키보드 연결
			scanner = new Scanner(System.in);

			// 소켓 생성
			socket = new Socket();

			// 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			// reader/writer 생성
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

			// join 프로토콜
			while (true) {
				System.out.print("닉네임 등록: ");
				String nickName = scanner.nextLine();

				printWriter.println("join:" + nickName); // 서버에 보냄
				printWriter.flush();

				// 서버로부터의 응답
				String response = bufferedReader.readLine();
				if (response.equals("ok")) {
					System.out.println("닉네임 [ " + nickName + " ]이 등록되었습니다.");
					break;
				} else if (response.equals("fail")) {
					System.out.println("* 이미 등록된 닉네임입니다 *");
				}
			}

			// 스레드 시작 : 읽어오는 메세지 처리
			new ChatClientReceiveThread(bufferedReader).start();			

			// 키보드 입력 처리
			while (true) {

				// 메세지 입력
				String input = scanner.nextLine();

				if ("quit".equals(input)==true) {
					System.out.println("종료합니다.");
					printWriter.println("quit");
					printWriter.flush();
					break;
				} else {
					// 메시지 처리
					printWriter.println("message:" + input); // message프로토콜을 서버에
					printWriter.flush();
				}
			}
			
			bufferedReader.close();
			printWriter.close();

		} catch (IOException e) {
			log("error: " + e);
		} finally {
			try {
				if (socket != null && socket.isClosed() == false) {

					socket.close();
				}
			} catch (IOException e1) {
				log("error: " + e1);
			}

		}
		scanner.close();
	}

	public static void log(String message) {
		System.out.println("[클라이언트] " + message);
	}

}
