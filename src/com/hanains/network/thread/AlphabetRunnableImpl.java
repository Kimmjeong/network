package com.hanains.network.thread;

public class AlphabetRunnableImpl extends Alphabet implements Runnable {

	// Alphabet을 수정하지 못한다는 조건
	// Alphabet의 메소드를 스레드에 태우고자 한다.
	
	@Override
	public void run() {
		print();
	}

}
