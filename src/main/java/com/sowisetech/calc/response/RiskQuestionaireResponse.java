package com.sowisetech.calc.response;

import java.util.List;

public class RiskQuestionaireResponse {

	String questionId;
	String question;
	List<AnswerRes> answerRes;

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<AnswerRes> getAnswerRes() {
		return answerRes;
	}

	public void setAnswerRes(List<AnswerRes> answerRes) {
		this.answerRes = answerRes;
	}

}
