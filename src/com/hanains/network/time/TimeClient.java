package com.hanains.network.time;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class TimeClient {

	private static final String HOST_ADDRESS = "127.0.0.1";
	private static final int PORT = 9090;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		DatagramSocket datagramSocket = null;

		// 소켓 생성
		try {

			datagramSocket = new DatagramSocket();

			while (true) {
				
				System.out.println("Just press Enterkey...(if you want to exit, then enter 'exit'.) ");
				
				String data=scanner.nextLine();
				
				if(data.equals("exit")){
					break;
				}
				else if(!data.equals("")){
					continue;					
				}
				
				byte[] sendData = data.getBytes("UTF-8");

				// 서버에 데이터 전송
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						new InetSocketAddress(HOST_ADDRESS, PORT));
				datagramSocket.send(sendPacket);

				// 서버로부터 데이터 수신
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				datagramSocket.receive(receivePacket);

				// 콘솔에 데이터 출력
				data = new String(receivePacket.getData(),0, receivePacket.getLength(), "UTF-8");
				System.out.println(data+"\n");
			}

		} catch (Exception e) {
			log("error:" + e);
		} finally {
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}

		scanner.close();

	}

	private static void log(String message) {
		System.out.println("[UDP Client]" + message);
	}

}
