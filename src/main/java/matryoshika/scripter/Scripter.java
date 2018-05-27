package matryoshika.scripter;

import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

import java.io.File;
import java.lang.reflect.Executable;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.logging.log4j.Logger;

@Mod(modid = Scripter.MODID, name = Scripter.NAME, version = Scripter.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
@Mod.EventBusSubscriber
public class Scripter {
	public static final String MODID = "scripter";
	public static final String NAME = "Scripter";
	public static final String VERSION = "1.0";

	protected static Logger logger;
	protected static File dir;
	protected static File jar;
	public static ObfuscationHelper helper = new ObfuscationHelper();
	public static ScriptEngine engine = new ScriptEngineManager(null).getEngineByName("nashorn");
	public static Invocable invocableEngine = (Invocable) engine;
	protected static int timer = 0;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		jar = event.getSourceFile();
		helper.verifyMappings();
		helper.readMappings();
		dir = event.getModConfigurationDirectory();
		
		Loader.loadScripts(new File(Scripter.dir, "Scripter"));
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}
	
	@EventHandler
	public static void onAction(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandScriptReload());
	}
	
	@EventHandler
	public static void onAction(FMLServerStartedEvent event) {
		ScripterEventHandler.execute(event, "FMLServerStartedEvent");
	}

	@EventHandler
	public static void onAction(FMLServerStoppingEvent event) {
		ScripterEventHandler.execute(event, "FMLServerStoppingEvent");
	}
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
		if(timer >= Integer.MAX_VALUE - 1)
			timer = 0;
		
		timer++;
	}
}
