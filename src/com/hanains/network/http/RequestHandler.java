package com.hanains.network.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class RequestHandler extends Thread {
	
	private Socket socket;
	
	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		BufferedReader bufferedReader = null;
		OutputStream outputStream = null;
		
		try {
			// get IOStream
			bufferedReader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
			outputStream = socket.getOutputStream();

			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			SimpleHttpServer.consolLog( "connected from " + inetSocketAddress.getHostName() + ":" + inetSocketAddress.getPort() );

			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
			
			//outputStream.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
			//outputStream.write( "Content-Type:text/html; charset=UTF-8\r\n".getBytes( "UTF-8" ) );
			//outputStream.write( "\r\n".getBytes() );
			//outputStream.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );
			
			String request="";
			SimpleHttpServer.consolLog("==================================request information ======================================");
			while(true){
				
				String line=bufferedReader.readLine();  // 브라우저가 보내는 Header부분을 읽어들임
				if(line==null || "".equals(line)){ // body 시작하는 부분에서 끝내기 : Header만 출력하도록 함
					break;
				}
				if("".equals(request)){ // 맨 첫줄일 때 : 첫줄 시작할 때 request 아무것도 없으므로 ""로 체크
					request=line; // 첫줄만 가져옴
				}
				
				SimpleHttpServer.consolLog(line);
				
			}
			SimpleHttpServer.consolLog("============================================================================================");
			
			
			// 요청 처리
			String[] tokens=request.split(" "); // 첫 째줄 분리

			if ("GET".equals(tokens[0])) { // GET 방식일 때에만
				responseStaticResource(outputStream, tokens[1], tokens[2]); // 예외 발생 하면 try-catch(Exception)문 안에 있으므로 처리됨
			} 
			else { // POST일 때에는 "잘못된 요청"
				response400Error(outputStream, tokens[2]);
			}
			
		} catch( Exception ex ) {
			SimpleHttpServer.consolLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( bufferedReader != null ) {
					bufferedReader.close();
				}
				
				if( outputStream != null ) {
					outputStream.close();
				}
				
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
				
			} catch( IOException ex ) {
				SimpleHttpServer.consolLog( "error:" + ex );
			}
		}
		
	}
	
	private void responseStaticResource(OutputStream outputStream, String url, String protocol) throws IOException{ // 이 메소드를 사용하는 것에서 예외 발생.
		
		// default html 처리 : url이 "/"이면 "/index.html"으로 변환하여 보여주도록하기
		if(url.equals("/")){
			url="/index.html";
		}
		
		// File 객체 생성
		File file = new File( "./webapp" + url ); // url이 webapp 하위에 있으므로 상대 경로를 통해 파일을 가져옴
		
		
		// file 존재 여부 체크
		if(file.exists()==false){ // 존재하지 않으면 "찾을 수 없는 페이지"
			response404Error(outputStream, protocol);
			return;
		}
		
		Path path = file.toPath(); // 파일에서 경로 추출 : Path 객체로 변환
		byte[] body=Files.readAllBytes(path); // 해당 경로 파일의 모든 byte를 읽어오면 그것이 body
		
		
		// 해당 path의 MIME타입을 구한다
		String mimeType=Files.probeContentType(path); 
		
		
		/* 
		 * html파일뿐만 아니라 이미지파일, css파일, ico파일 등의 요청에 정상 응답하지 않는 이유
		 * MIME타입을 통해 어떤 파일의 스트림인지 판별한다.
		 * html : text/html
		 * gif이미지 : image/gif
		 * css : text/css
		 * 그러므로 각 파일별로 MIME타입을 인식해야 하는데, text/html로 지정해놓으면 html로 인식되기 때문에 적용이 되지 않았다.
		 * favicon.ico(파비콘) : 작은 아이콘 
		 */
		
		// 브라우저에 보냄
		outputStream.write((protocol+"200 ok\r\n").getBytes("UTF-8"));
		outputStream.write(("Content-Type:"+mimeType+"\r\n").getBytes("UTF-8"));
		outputStream.write("\r\n".getBytes("UTF-8"));
		outputStream.write(body);
		

	}

	// File Not Found
	private void response404Error(OutputStream outputStream, String protocol) throws IOException{

		File file = new File("./webapp/error/404.html"); // 404 파일
		
		Path path = file.toPath();
		byte[] body = Files.readAllBytes(path); 

		// 브라우저에 응답
		outputStream.write((protocol + " 404 File Not Found\r\n").getBytes());
		outputStream.write("Content-Type:text/html\r\n".getBytes());
		outputStream.write("\r\n".getBytes("UTF-8"));
		outputStream.write(body);
		
	}
	
	// Bad Request
	private void response400Error(OutputStream outputStream, String protocol) throws IOException{

		
		File file = new File("./webapp/error/400.html"); // 400 파일
		
		Path path = file.toPath();
		byte[] body = Files.readAllBytes(path); 

		// 브라우저에 응답
		outputStream.write((protocol + " 400 Bad Request\r\n").getBytes());
		outputStream.write("Content-Type:text/html\r\n".getBytes());
		outputStream.write("\r\n".getBytes("UTF-8"));
		outputStream.write(body);
		
	}
	
}
