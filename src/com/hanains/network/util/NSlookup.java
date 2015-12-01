package com.hanains.network.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSlookup {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.print("> ");
			String hostName = scanner.nextLine(); // hostName 입력받기

			if ("exit".equals(hostName)) { // exit 이 입력되면 종료
				break;
			}

			try {

				InetAddress[] inetAddress = InetAddress.getAllByName(hostName); // hostName에 해당하는 모든 IP Address 받기

				for (int i = 0; i < inetAddress.length; i++) {
					System.out.println(hostName + " : " + inetAddress[i].getHostAddress());
				}

			} catch (UnknownHostException e) { // 알 수 없는 host 입력 시 발생
				e.printStackTrace();
			}

		}
		
		scanner.close();

	}

}
