package com.ubivashka.vk.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.vk.api.sdk.objects.callback.GroupLeave;

public class VKUserGroupLeaveEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	private GroupLeave leave;

	public VKUserGroupLeaveEvent(GroupLeave leave) {
		setLeave(leave);
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}

	public GroupLeave getLeave() {
		return leave;
	}

	public void setLeave(GroupLeave leave) {
		this.leave = leave;
	}

	
}
