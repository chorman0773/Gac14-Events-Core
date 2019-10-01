package github.chorman0773.gac14.events.core.event;

import github.chorman0773.gac14.player.PlayerProfile;
import github.chorman0773.gac14.player.PlayerProfileEvent;
import net.minecraftforge.eventbus.api.Event;


public abstract class InGameEvent extends Event {
	public final IEvent event;
	public InGameEvent(IEvent e) {
		event = e;
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
	public static final class PlayerWins extends PlayerEvent{
		public PlayerWins(PlayerProfile player,IEvent e) {
			super(player,e);
		}
	}

}
