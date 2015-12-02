package com.gunaatita.demo.chatserver.controller;

import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

	public String handleMessage(String message){
		return "'"+message+"' posted. status: SUCCESS";
	}
	
}
