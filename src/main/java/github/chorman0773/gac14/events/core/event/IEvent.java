package github.chorman0773.gac14.events.core.event;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import github.chorman0773.gac14.player.PlayerProfile;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface IEvent {
	public <T> Optional<T> getVariable(VariableKey<T> key);
	public <T> IEvent setVariable(VariableKey<T> key,T value);
	public ResourceLocation getProviderName();
	public IEvent addFinishCallback(Consumer<Stream<PlayerProfile>> finishCallback);
	public IEvent addStartCallback(Runnable startCallback);
	public IEvent addPlayerJoinedCallback(Consumer<PlayerProfile> joinedCallback);
	public IEvent addPlayerLeavesCallback(Consumer<PlayerProfile> leavesCallback);
	public void start();
	public Stream<PlayerProfile> getMembers();
}
