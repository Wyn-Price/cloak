package com.wynprice.brl.handlers;

import java.util.List;

import com.google.common.collect.Lists;
import com.wynprice.brl.addons.plastic.BufferedPlastic;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.event.CommandEvent;

public class PlasticCommand implements IClientCommand
{
	@Override
	public String getName() {
		return "brl";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public List<String> getAliases() {
		return Lists.newArrayList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException 
	{
		if(args.length > 0 && args[0].equals("plastic"))
		{
			BufferedPlastic.plastic = !BufferedPlastic.plastic;
			Minecraft.getMinecraft().renderGlobal.loadRenderers();
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) 
	{
		if(args.length == 1)
			return Lists.newArrayList("plastic");
		return Lists.newArrayList();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
		return false;
	}
}
