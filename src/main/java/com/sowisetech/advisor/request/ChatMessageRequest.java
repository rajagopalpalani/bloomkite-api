package com.sowisetech.advisor.request;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sowisetech.calc.request.ChatMessageReq;

@Component
public class ChatMessageRequest {

	List<ChatMessageReq> ChatMessageReqList;

	public List<ChatMessageReq> getChatMessageReqList() {
		return ChatMessageReqList;
	}

	public void setChatMessageReqList(List<ChatMessageReq> chatMessageReqList) {
		ChatMessageReqList = chatMessageReqList;
	}

}
