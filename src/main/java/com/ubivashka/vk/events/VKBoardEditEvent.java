package com.ubivashka.vk.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.vk.api.sdk.objects.board.TopicComment;

public class VKBoardEditEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	private TopicComment topicComment;

	public VKBoardEditEvent(TopicComment topicComment) {
		setTopicComment(topicComment);
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}

	public TopicComment getTopicComment() {
		return topicComment;
	}

	public void setTopicComment(TopicComment topicComment) {
		this.topicComment = topicComment;
	}
}
