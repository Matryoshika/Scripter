package matryoshika.scripter;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingPackSizeEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.living.ZombieEvent;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ScripterEventHandler {

	private static ScriptContext ctx = new SimpleScriptContext();

	public static void execute(Event event, String name) {
		if (Loader.mappedScripts.get(name) == null)
			return;
		Loader.mappedScripts.get(name).forEach(script -> {
			try {
				Scripter.engine.eval(script);
				Scripter.invocableEngine.invokeFunction("onEvent", event);
			} catch (ScriptException | NoSuchMethodException e) {
				Scripter.logger.error("Error running " + Loader.getScriptName(script) + " during " + name + " : " + e.getCause().toString() + " : " + e.getStackTrace());
			}
		});
	}

	public static void execute(FMLStateEvent event, String name) {
		if (Loader.mappedScripts.get(name) == null)
			return;
		Loader.mappedScripts.get(name).forEach(script -> {
			try {
				Scripter.engine.eval(script);
				Scripter.invocableEngine.invokeFunction("onEvent", event);
			} catch (ScriptException | NoSuchMethodException e) {
				Scripter.logger.error("Error running " + Loader.getScriptName(script) + " during " + name + " : " + e.getCause().toString() + " : " + e.getStackTrace());
			}
		});
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(AnimalTameEvent event) {
		execute(event, "AnimalTameEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(AnvilRepairEvent event) {
		execute(event, "AnvilRepairEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(AnvilUpdateEvent event) {
		execute(event, "AnvilUpdateEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ArrowLooseEvent event) {
		execute(event, "ArrowLooseEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ArrowNockEvent event) {
		execute(event, "ArrowNockEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(AttackEntityEvent event) {
		execute(event, "AttackEntityEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(BabyEntitySpawnEvent event) {
		execute(event, "BabyEntitySpawnEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(BonemealEvent event) {
		execute(event, "BonemealEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(BlockEvent.BreakEvent event) {
		execute(event, "BlockEvent.BreakEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(BlockEvent.CreateFluidSourceEvent event) {
		execute(event, "BlockEvent.CreateFluidSourceEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(BlockEvent.HarvestDropsEvent event) {
		execute(event, "BlockEvent.HarvestDropsEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(BlockEvent.MultiPlaceEvent event) {
		execute(event, "BlockEvent.MultiPlaceEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(BlockEvent.NeighborNotifyEvent event) {
		execute(event, "BlockEvent.NeighborNotifyEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(BlockEvent.PlaceEvent event) {
		execute(event, "BlockEvent.PlaceEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ChunkDataEvent.Load event) {
		execute(event, "ChunkDataEvent.Load");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ChunkDataEvent.Save event) {
		execute(event, "ChunkDataEvent.Save");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ChunkWatchEvent.Watch event) {
		execute(event, "ChunkWatchEvent.Watch");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ChunkWatchEvent.UnWatch event) {
		execute(event, "ChunkWatchEvent.UnWatch");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(AdvancementEvent event) {
		execute(event, "AdvancementEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerEvent.BreakSpeed event) {
		execute(event, "PlayerEvent.BreakSpeed");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerEvent.Clone event) {
		execute(event, "PlayerEvent.Clone");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerEvent.HarvestCheck event) {
		execute(event, "PlayerEvent.HarvestCheck");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerEvent.LoadFromFile event) {
		execute(event, "PlayerEvent.LoadFromFile");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerEvent.NameFormat event) {
		execute(event, "PlayerEvent.NameFormat");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerEvent.SaveToFile event) {
		execute(event, "PlayerEvent.SaveToFile");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerEvent.StartTracking event) {
		execute(event, "PlayerEvent.StartTracking");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerEvent.StopTracking event) {
		execute(event, "PlayerEvent.StopTracking");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerEvent.Visibility event) {
		execute(event, "PlayerEvent.Visibility");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerContainerEvent.Close event) {
		execute(event, "PlayerContainerEvent.Close");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerContainerEvent.Open event) {
		execute(event, "PlayerContainerEvent.Open");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(CommandEvent event) {
		execute(event, "CommandEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(WorldEvent.CreateSpawnPosition event) {
		execute(event, "WorldEvent.CreateSpawnPosition");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(WorldEvent.Load event) {
		execute(event, "WorldEvent.Load");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(WorldEvent.Unload event) {
		execute(event, "WorldEvent.Unload");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(WorldEvent.Save event) {
		execute(event, "WorldEvent.Save");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(WorldEvent.PotentialSpawns event) {
		execute(event, "WorldEvent.PotentialSpawns");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(CriticalHitEvent event) {
		execute(event, "CriticalHitEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(CropGrowEvent.Pre event) {
		execute(event, "CropGrowEvent.Pre");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(CropGrowEvent.Post event) {
		execute(event, "CropGrowEvent.Post");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ExplosionEvent.Detonate event) {
		execute(event, "ExplosionEvent.Detonate");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ExplosionEvent.Start event) {
		execute(event, "ExplosionEvent.Start");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(DifficultyChangeEvent event) {
		execute(event, "DifficultyChangeEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(EnchantmentLevelSetEvent event) {
		execute(event, "EnchantmentLevelSetEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(EnderTeleportEvent event) {
		execute(event, "EnderTeleportEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(EntityEvent.EnteringChunk event) {
		execute(event, "EntityEvent.EnteringChunk");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(EntityEvent.CanUpdate event) {
		execute(event, "EntityEvent.CanUpdate");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(EntityEvent.EntityConstructing event) {
		execute(event, "EntityEvent.EntityConstructing");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerInteractEvent.EntityInteract event) {
		execute(event, "PlayerInteractEvent.EntityInteract");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerInteractEvent.EntityInteractSpecific event) {
		execute(event, "PlayerInteractEvent.EntityInteractSpecific");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerInteractEvent.LeftClickBlock event) {
		execute(event, "PlayerInteractEvent.LeftClickBlock");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerInteractEvent.LeftClickEmpty event) {
		execute(event, "PlayerInteractEvent.LeftClickEmpty");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerInteractEvent.RightClickBlock event) {
		execute(event, "PlayerInteractEvent.RightClickBlock");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerInteractEvent.RightClickEmpty event) {
		execute(event, "PlayerInteractEvent.RightClickEmpty");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerInteractEvent.RightClickItem event) {
		execute(event, "PlayerInteractEvent.RightClickItem");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(EntityItemPickupEvent event) {
		execute(event, "EntityItemPickupEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(EntityJoinWorldEvent event) {
		execute(event, "EntityJoinWorldEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(EntityMobGriefingEvent event) {
		execute(event, "EntityMobGriefingEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(EntityMountEvent event) {
		execute(event, "EntityMountEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(EntityStruckByLightningEvent event) {
		execute(event, "EntityStruckByLightningEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(EntityTravelToDimensionEvent event) {
		execute(event, "EntityTravelToDimensionEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(FillBucketEvent event) {
		execute(event, "FillBucketEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingEntityUseItemEvent.Finish event) {
		execute(event, "LivingEntityUseItemEvent.Finish");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingEntityUseItemEvent.Start event) {
		execute(event, "LivingEntityUseItemEvent.Start");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingEntityUseItemEvent.Stop event) {
		execute(event, "LivingEntityUseItemEvent.Stop");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingEntityUseItemEvent.Tick event) {
		execute(event, "LivingEntityUseItemEvent.Tick");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(FurnaceFuelBurnTimeEvent event) {
		execute(event, "FurnaceFuelBurnTimeEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ItemEvent event) {
		execute(event, "ItemEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ItemExpireEvent event) {
		execute(event, "ItemExpireEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ItemFishedEvent event) {
		execute(event, "ItemFishedEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ItemTossEvent event) {
		execute(event, "ItemTossEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingAttackEvent event) {
		execute(event, "LivingAttackEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingDamageEvent event) {
		execute(event, "LivingDamageEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingDeathEvent event) {
		execute(event, "LivingDeathEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingDestroyBlockEvent event) {
		execute(event, "LivingDestroyBlockEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingDropsEvent event) {
		execute(event, "LivingDropsEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingEquipmentChangeEvent event) {
		execute(event, "LivingEquipmentChangeEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingEvent.LivingJumpEvent event) {
		execute(event, "LivingEvent.LivingJumpEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingEvent.LivingUpdateEvent event) {
		execute(event, "LivingEvent.LivingUpdateEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingExperienceDropEvent event) {
		execute(event, "LivingExperienceDropEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingFallEvent event) {
		execute(event, "LivingFallEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingHealEvent event) {
		execute(event, "LivingHealEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingHurtEvent event) {
		execute(event, "LivingHurtEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingKnockBackEvent event) {
		execute(event, "LivingKnockBackEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingPackSizeEvent event) {
		execute(event, "LivingPackSizeEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingSetAttackTargetEvent event) {
		execute(event, "LivingSetAttackTargetEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingSpawnEvent.AllowDespawn event) {
		execute(event, "LivingSpawnEvent.AllowDespawn");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingSpawnEvent.CheckSpawn event) {
		execute(event, "LivingSpawnEvent.CheckSpawn");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LivingSpawnEvent.SpecialSpawn event) {
		execute(event, "LivingSpawnEvent.SpecialSpawn");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LootingLevelEvent event) {
		execute(event, "LootingLevelEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(LootTableLoadEvent event) {
		execute(event, "LootTableLoadEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(MinecartCollisionEvent event) {
		execute(event, "MinecartCollisionEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(MinecartInteractEvent event) {
		execute(event, "MinecartInteractEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(MinecartUpdateEvent event) {
		execute(event, "MinecartUpdateEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(NoteBlockEvent.Play event) {
		execute(event, "NoteBlockEvent.Play");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(NoteBlockEvent.Change event) {
		execute(event, "NoteBlockEvent.Change");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerBrewedPotionEvent event) {
		execute(event, "PlayerBrewedPotionEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerDestroyItemEvent event) {
		execute(event, "PlayerDestroyItemEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerDropsEvent event) {
		execute(event, "PlayerDropsEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerFlyableFallEvent event) {
		execute(event, "PlayerFlyableFallEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerPickupXpEvent event) {
		execute(event, "PlayerPickupXpEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerSetSpawnEvent event) {
		execute(event, "PlayerSetSpawnEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerSleepInBedEvent event) {
		execute(event, "PlayerSleepInBedEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PlayerWakeUpEvent event) {
		execute(event, "PlayerWakeUpEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PotionBrewEvent.Pre event) {
		execute(event, "PotionBrewEvent.Pre");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(PotionBrewEvent.Post event) {
		execute(event, "PotionBrewEvent.Post");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ProjectileImpactEvent.Throwable event) {
		execute(event, "ProjectileImpactEvent.Throwable");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ProjectileImpactEvent.Arrow event) {
		execute(event, "ProjectileImpactEvent.Arrow");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ProjectileImpactEvent.Fireball event) {
		execute(event, "ProjectileImpactEvent.Fireball");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(SaplingGrowTreeEvent event) {
		execute(event, "SaplingGrowTreeEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ServerChatEvent event) {
		execute(event, "ServerChatEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(SleepingLocationCheckEvent event) {
		execute(event, "SleepingLocationCheckEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(UseHoeEvent event) {
		execute(event, "UseHoeEvent");
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public static void onAction(ZombieEvent.SummonAidEvent event) {
		execute(event, "ZombieEvent.SummonAidEvent");
	}
}