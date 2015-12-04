package com.hanains.network.time;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {

	private static final int PORT = 9090;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {

		DatagramSocket datagramSocket = null;
		SimpleDateFormat format=new SimpleDateFormat("yyy-MM-dd HH:mm:ss a");

		try {
			// 소켓 생성
			datagramSocket = new DatagramSocket(PORT);

			log("수신 대기");
			while (true) {
				// 클라이언트로부터 데이터 수신
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				datagramSocket.receive(receivePacket);
				
				// 클라이언트에 보낼 데이터
				String data=format.format(new Date());
				byte[] time=data.getBytes("UTF-8");
				
				// 클라이언트에 데이터 전송
				DatagramPacket sendPacket = new DatagramPacket(time,time.length,receivePacket.getAddress(), receivePacket.getPort());
				datagramSocket.send(sendPacket);
				
			}
		} catch (Exception e) {
			log("error:" + e);
		} finally{
			if(datagramSocket !=null){
				datagramSocket.close();
			}
		}
	}

	private static void log(String message) {
		System.out.println("[UDP Server] " + message);
	}

}
