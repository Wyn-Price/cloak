package com.wynprice.brl.events;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This class is used as a superclass for all BRL events.
 * @author Wyn Price
 *
 */
public abstract class BRLBaseEvent<T extends BRLBaseEvent> extends Event
{
	public T post()
	{
		MinecraftForge.EVENT_BUS.post(this);
		return (T) this;
	}
}
