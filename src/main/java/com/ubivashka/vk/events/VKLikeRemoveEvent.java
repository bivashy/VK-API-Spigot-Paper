package com.ubivashka.vk.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.vk.api.sdk.objects.callback.LikeAddRemove;

public class VKLikeRemoveEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	private LikeAddRemove likeAdd;

	public VKLikeRemoveEvent(LikeAddRemove likeAdd) {
		setLikeAdd(likeAdd);
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}
	public LikeAddRemove getLikeAdd() {
		return likeAdd;
	}

	public void setLikeAdd(LikeAddRemove likeAdd) {
		this.likeAdd = likeAdd;
	}
}
