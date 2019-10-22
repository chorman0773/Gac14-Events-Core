package github.chorman0773.gac14.events.core.event;

import github.chorman0773.gac14.player.PlayerProfile;
import github.chorman0773.gac14.player.PlayerProfileEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;


public abstract class InGameEvent extends Event {
	public final IEvent event;
	public InGameEvent(IEvent e) {
		event = e;
	}

	public static final class Create extends InGameEvent{
		public Create(IEvent e){super(e);}
	}

	public static final class Start extends InGameEvent{
		public Start(IEvent e) {
			super(e);
		}
	}
	
	public static final class End extends InGameEvent{
		public End(IEvent e) {
			super(e);
		}
	}
	
	static abstract class PlayerEvent extends PlayerProfileEvent{
		public final IEvent event;
		public PlayerEvent(PlayerProfile player,IEvent e) {
			super(player);
			event = e;
		}	
	}
	
	public static final class PlayerJoins extends PlayerEvent{
		public PlayerJoins(PlayerProfile player, IEvent e) {
			super(player, e);
		}
	}
	public static final class PlayerLeaves extends PlayerEvent{
		public PlayerLeaves(PlayerProfile player,IEvent e) {
			super(player,e);
		}
	}

	/**
	 * Fires on the {@link MinecraftForge#EVENT_BUS} whenever a player wins an event.
	 *
	 * If multiple players win, an event is fired for each of those players, in an unspecified order.
	 *
	 * The events are indeterminately sequenced.
	 *
	 * Additionally, this event is indeterminately sequenced with the {@link InGameEvent.End} event, and callbacks handling it.
	 */
	public static final class PlayerWins extends PlayerEvent{
		public PlayerWins(PlayerProfile player,IEvent e) {
			super(player,e);
		}
	}

}
