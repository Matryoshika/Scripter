package matryoshika.scripter;

import java.io.File;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandScriptReload extends CommandBase{

	@Override
	public String getName() {
		return "scripter";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/scripter reload";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(!args[0].equals("reload"))
			return;
		
		if(sender.getCommandSenderEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
			if(!player.canUseCommand(server.getOpPermissionLevel(), getName()))
				player.sendMessage(new TextComponentTranslation("You do not have permissions to run this command."));
		}
		
		Loader.mappedScripts.clear();
		Loader.scriptNames.clear();
		Loader.scripts.clear();
		Loader.loadScripts(new File(Scripter.dir, "Scripter"));
		
	}

}
