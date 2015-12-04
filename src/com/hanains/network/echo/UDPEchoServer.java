package com.hanains.network.echo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UDPEchoServer {

	private static final int PORT=9090;
	private static final int BUFFER_SIZE=1024;
	
	public static void main(String[] args) {

		DatagramSocket datagramSocket=null;
		
		try {
			// 1. UDP 소켓 생성
			datagramSocket=new DatagramSocket(PORT);
			
			// 2. 수신 대기
			log("수신 대기");
			DatagramPacket receivePacket=new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
			datagramSocket.receive(receivePacket);
			
			// 3. 데이터 확인
			String data=new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
			log("데이터 수신 :"+data);
			
			// 4. 데이터 전송
			DatagramPacket sendPacket
			=new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), receivePacket.getAddress(), receivePacket.getPort());
			
			datagramSocket.send(sendPacket);
			
			
		} catch (Exception e) {
			log("error : "+e);
		} finally{
			if(datagramSocket !=null){
				datagramSocket.close();
			}
		}
	}
	
	public static void log(String message){
		System.out.println("[UDP Echo Server] "+message);
	}

}
