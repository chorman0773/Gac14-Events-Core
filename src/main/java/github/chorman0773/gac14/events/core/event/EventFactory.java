package github.chorman0773.gac14.events.core.event;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid="gac14-events-core",bus=Mod.EventBusSubscriber.Bus.MOD)
@ParametersAreNonnullByDefault
public abstract class EventFactory extends ForgeRegistryEntry<EventFactory> implements NonNullSupplier<IEvent> {
	
	private EventFactory() {}
	
	private static IForgeRegistry<EventFactory> EventFactoryRegistry;

	public static IEvent createNewEvent(ResourceLocation loc){
		if(!EventFactoryRegistry.containsKey(loc))
			throw new NoSuchElementException("Cannot create Event for type "+loc+" no such factory");
		return EventFactoryRegistry.getValue(loc).get();
	}

	@SubscribeEvent
	public static void addRegistry(RegistryEvent.NewRegistry event) {
		EventFactoryRegistry = new RegistryBuilder<EventFactory>()
				.disableSync()
				.setName(new ResourceLocation("gac14:events/factories"))
				.setType(EventFactory.class)
				.create();
	}
	
	public static <T extends IEvent> void register(String id, Supplier<T> factory) {
		EventFactoryRegistry.register(new EventFactory() {
			@Override
			public IEvent get() {
				return factory.get();
			}
		}.setRegistryName(id));
	}
	
}
