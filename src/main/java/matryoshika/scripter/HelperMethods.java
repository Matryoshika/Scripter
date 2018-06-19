package matryoshika.scripter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.server.FMLServerHandler;

public class HelperMethods {

	public static int getTimer() {
		return Scripter.timer;
	}

	public static void executeCommand(ICommandSender sender, String rawCommand) {
		MinecraftServer server = getServer();
		ICommandManager manager = server.commandManager;

		if (sender != null)
			manager.executeCommand(sender, rawCommand);
		else
			manager.executeCommand(server, rawCommand);
	}

	public static void registerCommandAlias(String originalName, String[] alias) {
		CommandHandler handler = (CommandHandler) getServer().getCommandManager();
		Map<String, ICommand> commands = handler.getCommands();
		ICommand original = commands.get(originalName);

		for (String a : alias) {
			ICommand icommand = commands.get(a);

			if (icommand == null || !icommand.getName().equals(a))
				commands.put(a, original);
		}
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

	public static double[] getPosition(Entity entity) {
		return new double[] { entity.posX, entity.posY, entity.posZ };
	}

	public static void updatePlayerInventory(EntityPlayerMP player) {
		player.sendContainerToPlayer(player.inventoryContainer);
	}

	public static void updateTileInventory(TileEntity tile) {
		World world = tile.getWorld();
		List players = world.playerEntities;
		if (!world.isRemote) {
			for (Object player : players) {
				if (player instanceof EntityPlayerMP) {
					if (pointDistancePlane(((EntityPlayerMP) player).posX, ((EntityPlayerMP) player).posZ, tile.getPos().getX() + 0.5, tile.getPos().getZ() + 0.5) < 64) {
						final SPacketUpdateTileEntity updatePacket = tile.getUpdatePacket();
						if (updatePacket != null)
							((EntityPlayerMP) player).connection.sendPacket(tile.getUpdatePacket());
					}
				}
			}
		}
	}

	public static float pointDistancePlane(double x1, double y1, double x2, double y2) {
		return (float) Math.hypot(x1 - x2, y1 - y2);
	}

	public static BlockPos navigate(BlockPos pos, String direction, int steps) {
		return pos.offset(EnumFacing.byName(direction.toUpperCase()), steps);
	}

	public static List<BlockPos> getPosBox(BlockPos corner1, BlockPos corner2) {
		return Lists.newArrayList(BlockPos.getAllInBox(corner1, corner1));
	}

	public static IBlockState getBlockState(World world, BlockPos pos) {
		return world.getBlockState(pos);
	}
}
