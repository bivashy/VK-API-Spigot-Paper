package com.ubivashka.vk.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.vk.api.sdk.objects.callback.MarketComment;

public class VKMarketCommentEditEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	private MarketComment comment;

	public VKMarketCommentEditEvent(MarketComment comment) {
		setComment(comment);
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}

	public MarketComment getComment() {
		return comment;
	}

	public void setComment(MarketComment comment) {
		this.comment = comment;
	}
}
