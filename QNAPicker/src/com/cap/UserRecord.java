package com.cap;

public class UserRecord {
	
	private String uid;
	private String SecQuestion;
	private String SecAnswer;
	public String getMemberID() {
		return uid;
	}
	public void setMemberID(String memberID) {
		this.uid = memberID;
	}
	public String getSecQuestion() {
		return SecQuestion;
	}
	public void setSecQuestion(String secQuestion) {
		SecQuestion = secQuestion;
	}
	public String getSecAnswer() {
		return SecAnswer;
	}
	public void setSecAnswer(String secAnswer) {
		SecAnswer = secAnswer;
	}
	
	public String toString() {
	    return uid+"::"+SecQuestion+"::"+SecAnswer;
	}

}
