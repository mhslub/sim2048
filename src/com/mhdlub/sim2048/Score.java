package com.mhdlub.sim2048;

/*
 * The Score is a class with one property that we need to pass by reference.
 */
public class Score {
	private int sc;
	public Score(){
		setSc(0);
	}
	public int getSc() {
		return sc;
	}
	public void setSc(int sc) {
		this.sc = sc;
	}
	
	public void increase(int i)
	{
		sc+=i;
	}
}
