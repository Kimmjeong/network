package com.hanains.network.exam;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHostExam {

	public static void main(String[] args) {
		
		try {
			InetAddress inetAddress=InetAddress.getLocalHost();// 자신의 로컬호스트 가져옴.
			System.out.println(inetAddress.getHostName()); // host 이름
			System.out.println(inetAddress.getHostAddress()); // ip address를 문자열로 가져옴
			
			byte[] addresses=inetAddress.getAddress(); // ip address를 바이트 단위로 잘라서 가져옴
			
			for(int i=0;i<addresses.length;i++){
				System.out.print(addresses[i]&0xff); // byte를 int로 바꿈
				if(i<addresses.length-1){
					System.out.print(".");
				}
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
	}

}
