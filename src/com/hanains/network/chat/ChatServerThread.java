package com.hanains.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatServerThread extends Thread {

	private String nickName; // 닉네임
	private Socket socket;
	private List<Writer> listWriters;
	private List<String> listNickNames;

	public ChatServerThread(Socket socket, List<Writer> listWriters, List<String> listNickNames) {
		this.socket = socket;
		this.listWriters = listWriters;
		this.listNickNames = listNickNames;
	}

	@Override
	public void run() {

		try {
			
			BufferedReader bufferedReader = null;
			PrintWriter printWriter = null;
			
			// 스트림 얻기
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

			// 요청처리
			while (true) {

				try {
					
					String request = bufferedReader.readLine(); // 클라이언트로부터 읽어온 (join, message, quit을 포함한) 요청

					// 클라이언트가 quit을 하지 않고 소켓을 닫은 경우
					if (request == null) {
						ChatServer.log("클라이언트로부터 연결 끊김");
						doQuit(printWriter);
						break;
					}

					// 프로토콜 분석
					String[] tokens = request.split(":");
					if ("join".equals(tokens[0])) { // 닉네임 등록
						doJoin(tokens[1], printWriter);
					} else if ("message".equals(tokens[0])) { // 메세지

						doMessage(tokens[1]);

					} else if ("quit".equals(tokens[0])) { // 종료
						doQuit(printWriter);
						break;

					} else {
						ChatServer.log("에러: 알수없는 요청(" + tokens[0] + ")");
					}

				} catch (SocketException e) {
					ChatServer.log("클라이언트로부터 연결 끊김");
					doQuit(printWriter);
					break;
				}
			}
			bufferedReader.close();
			printWriter.close();

		} catch (IOException e) {
			ChatServer.log("error: " + e);
		} finally {
			
			try {
				
				if (socket != null && socket.isClosed() == false) {

					socket.close();
				}
			} catch (IOException e1) {
				ChatServer.log("error: " + e1);
			}
		}
	}

	// 동기화된 리스트에 있는 writer의 데이터를 모두 내보냄
	private void broadcast(String data) {
		synchronized (listWriters) {
			for (Writer writer : listWriters) { // listWriters를 공유하고 있는 모든
												// 클라이언트의 writer에 접근해 data를 보냄
				PrintWriter printWriter = (PrintWriter) writer;
				printWriter.println(data);
				printWriter.flush();
			}

		}
	}

	// 참여
	private void doJoin(String nickName, Writer writer) {
		this.nickName = nickName;
		PrintWriter printWriter = (PrintWriter) writer;

		if (!isNickName(nickName)) { // 입력된 닉네임이 중복되는지 확인

			String data = nickName + "님이 참여하였습니다.";
			System.out.println(data);
			// listWriters를 공유하고 있는 다른 모든 클라이언트에 data를 보냄
			broadcast(data);
			// 자신의 클라이언트는 listWriters에 추가하지 않았기 때문에 자신한테는 이 메세지가 보여지지 않음.

			/* writer pool에 저장 */
			// 자기자신(클라이언트)를 listWriters에 추가함
			addWriter(writer);

			// ack
			printWriter.println("ok"); // 해당 writer를 가진 클라이언트에 메세지 보냄
			printWriter.flush();

		} else {
			// ack
			printWriter.println("error");
			printWriter.flush();
		}

	}

	// 메세지 보내기
	private void doMessage(String message) {
		String data = nickName + " >> " + message;
		broadcast(data);
	}

	// 나가기
	private void doQuit(Writer writer) {
		removeWriter(writer);
		String data = nickName + "님이 퇴장 하였습니다.";
		listNickNames.remove(nickName);
		broadcast(data);
	}

	// writer pool에 추가
	private void addWriter(Writer writer) {
		synchronized (listWriters) {
			listWriters.add(writer); // 동기화하기 위한 리스트에 자기 자신의 writer 추가
		}

	}

	// writer pool에서 제거
	private void removeWriter(Writer writer) {
		synchronized (listWriters) {
			listWriters.remove(writer); // 동기화 리스트에서 자기 자신의 writer 제거
		}
	}

	// 닉네임 중복 확인
	private boolean isNickName(String nname) {

		for (String listnickname : listNickNames) {
			if (listnickname.equals(nname)) {
				return true;
			}
		}

		listNickNames.add(nname);
		return false;
	}

}
