package matryoshika.scripter;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.server.FMLServerHandler;

public class HelperMethods {
	
	public static int getTimer() {
		return Scripter.timer;
	}
	
	public static void executeCommand(ICommandSender sender, String rawCommand) {
		MinecraftServer server = getServer();
		ICommandManager manager = server.commandManager;
		
		if(sender != null)
			manager.executeCommand(sender, rawCommand);
		else
			manager.executeCommand(server, rawCommand);
	}
	
	public static MinecraftServer getServer() {
		return FMLServerHandler.instance().getServer();
	}
	
	public static Item getItem(String name) {
		return ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
	}
	
	public static Block getBlock(String name) {
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
	}
}
