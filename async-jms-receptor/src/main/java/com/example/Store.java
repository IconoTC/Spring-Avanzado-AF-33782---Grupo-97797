package com.example;

import java.util.ArrayList;
import java.util.List;

public class Store {
	private static List<Message> queue = new ArrayList<Message>();
	private static List<Message> topic = new ArrayList<Message>();
	
	public static void addQueue(Message msg) {
		queue.add(msg);
	}
	public static List<Message> getQueue() {
		return queue;
	}
	public static void clearQueue() {
		queue.clear();
	}
	public static void addTopic(Message msg) {
		topic.add(msg);
	}
	public static List<Message> getTopic() {
		return topic;
	}
	public static void clearTopic() {
		topic.clear();
	}
}
