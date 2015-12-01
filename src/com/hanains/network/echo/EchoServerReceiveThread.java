package com.hanains.network.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EchoServerReceiveThread extends Thread {

	private Socket socket=null;
	
	public EchoServerReceiveThread(Socket socket) {
		this.socket=socket;
	}
	
	@Override
	public void run() {

		try{
					// 연결 성공
					InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
					String remoteHostAddress = inetSocketAddress.getAddress().getHostAddress();
					int remoteHostPort = inetSocketAddress.getPort();
					System.out.println("[서버] 연결됨 from " + remoteHostAddress + ":" + remoteHostPort);

					// IOStream 받아오기
					InputStream inputStream = socket.getInputStream();
					OutputStream outputStream = socket.getOutputStream();

					// 데이터 읽기
					try {
						byte[] buffer = new byte[256];
						while (true) {

							int readByteCount = inputStream.read(buffer);
							if (readByteCount < 0) {
								System.out.println("[서버] 클라이언트로부터 연결 끊김");
								break;
							}

							String data = new String(buffer, 0, readByteCount); // byte -> String
							System.out.println("[서버] 데이터 수신 : " + data);
							
							// 데이터 보내기
							outputStream.write(data.getBytes("UTF-8")); // String -> byte
							outputStream.flush(); // server socket의 outputStream buffer를 비워 전송 buffer에 넣어주는 것
							
						}
					} catch (IOException ex) {
						System.out.println("[서버] 에러 : " + ex);
					} finally {
						// 자원 정리
						inputStream.close();
						outputStream.close();
						if (socket.isClosed() == false) {
							socket.close();
						}
					}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
