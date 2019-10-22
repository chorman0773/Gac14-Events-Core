package github.chorman0773.gac14.events.core.event;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import github.chorman0773.gac14.player.PlayerProfile;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;


@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface IEvent {
	/**
	 * Gets a variable by a given name, and returns the optional containing its value
	 * If there is not a variable with that name, returns an empty optional.
	 * @param key the name of the variable
	 * @param <T> the type of the variable
	 */
	public <T> Optional<T> getVariable(VariableKey<T> key);

	/**
	 * Sets the value of the variable with a given name.
	 * If there is not already a variable with that name, creates a new variable and assigns it that name.
	 *
	 * An Event "uses" an implementation-defined set of variables that, if provided, affect the event in some implementation-defined way.
	 * Such an implementation can impose additional restrictions, aside from just the type, upon values which may be stored.
	 *
	 * If called after the event has started, except during a callback registered by {@link #addStartCallback(Runnable)},
	 *  it is unspecified if the new value of the variable will be respected, if the implementation directly uses the variable.
	 *
	 * @param key the name of the variable being set
	 * @param value the value being set
	 * @param <T> The type of the variable
	 * @return this, for chaining
	 */
	public <T> IEvent setVariable(VariableKey<T> key,T value);

	/**
	 * Returns the name of the provider for this event
	 */
	public ResourceLocation getProviderName();

	/**
	 * Causes the given callback to be called when the event finishes,
	 * either by timing out, an explicit call to {@link #end()}, or some other unspecified event
	 * Callbacks are invoked in the order they are added.
	 * The Callbacks are additionally indeterminately sequenced to the firing of an {@link InGameEvent.End}
	 *  on the forge event bus.
	 *
	 * The callback accepts the list of players that joined the event and did not leave.
	 *
	 * If added after the event has finished, the call has no effect.
	 * If a callback is added during the execution of any callback previously added, or during the handling of an {@link InGameEvent.End}, the behavior is undefined.
	 * @return this, for chaining
	 */
	public IEvent addFinishCallback(Consumer<Stream<PlayerProfile>> finishCallback);

	/**
	 * Causes the given callback to be called when the event is started,
	 *  either by an explicit call to {@link #start()}, or some other unspecified cause.
	 *
	 * Callbacks are invoked in the order they are added.
	 * The Callbacks are additionally indeterminately sequenced to the firing of an {@link InGameEvent.Start}
	 *  on the forge event bus.
	 *
	 * The callback accepts no parameters.
	 *
	 * If added after the event has started, the call has no effect.
	 * If a callback is added during the execution of any callback previously added, or during the handling of an {@link InGameEvent.Start}, the behavior is undefined.
	 * @return this, for chaining
	 */
	public IEvent addStartCallback(Runnable startCallback);

	/**
	 * Causes the given callback to be called when a player joins the event,
	 *  either by an explicit call to {@link #addPlayer(PlayerProfile)}, or some other unspecified cause.
	 *
	 * Callbacks are invoked in the order they are added.
	 * The Callbacks are additionally indeterminately sequenced to the firing of an {@link InGameEvent.PlayerJoins}.
	 *
	 * The callback accepts the player that joined the event.
	 *
	 * If a callback is added during the execution of any callback previously added, or during the handling of an {@link InGameEvent.PlayerJoins}, the behavior is undefined.
	 * @return this, for chaining
	 */
	public IEvent addPlayerJoinedCallback(Consumer<PlayerProfile> joinedCallback);
	/**
	 * Causes the given callback to be called when a player leaves the event,
	 *  either by an explicit call to {@link #removePlayer(PlayerProfile)}, or some other unspecified cause.
	 *
	 * Callbacks are invoked in the order they are added.
	 * The Callbacks are additionally indeterminately sequenced to the firing of an {@link InGameEvent.PlayerLeaves}.
	 *
	 * The callback accepts the player that joined the event.
	 *
	 * If a callback is added during the execution of any callback previously added, or during the handling of an {@link InGameEvent.PlayerLeaves}, the behavior is undefined.
	 * @return this, for chaining
	 */
	public IEvent addPlayerLeavesCallback(Consumer<PlayerProfile> leavesCallback);

	/**
	 * Causes the given callback to be called when one or more players wins the event, during the ending sequence of the event.
	 *
	 * It is implementation-defined if players are able to "win" the event, and how such players are chosen, however only players that are members of the event are eligable to win that event.
	 *
	 * Callbacks are invoked in the order they are added.
	 * Additionally, if fired, the callbacks is indeterminately sequenced with any given callback added by {@link #addFinishCallback(Consumer)},
	 *  and with the firing of any given {@link InGameEvent.PlayerWins} event.
	 *
	 * In particular, the implementation may choose to interleave callbacks added by this method with callbacks added by {@link #addFinishCallback(Consumer)},
	 *  for example, by adding a modified version of the callback as a finishCallback (filtering it appropriately).
	 *
	 * @return this, for chaining
	 */
	public IEvent addPlayerWinsCallback(Consumer<Stream<PlayerProfile>> winsCallback);

	/**
	 * Explicitly causes the event to start.
	 * If called after the event has already started, and has not ended, the call has no effect.
	 *
	 * If the event has already completed, it is implementation-defined if it can be restarted.
	 * If an event is restartable, then the state after a call to {@link #end()},
	 * is the same as the state when created by the event Factory, except all callbacks previously registered remain registered.
	 *
	 * The time between when the event starts and when it finishes, is called the Lifetime of the event.
	 */
	public void start();

	/**
	 * Explicitly causes the event to end.
	 * If called after the event has already finished, or before it has been started, has no effect.
	 *
	 * As described in the contract for {@link #start()}, if the event is restartable,
	 *  then this call returns the state to the state it held prior to being started, except all subsequently added callbacks remain added.
	 */
	public void end();

	/**
	 * Adds the given player to the event explicitly. If the event has already been started, and has not ended, it is unspecified if the call has any effect.
	 *
	 * In particular, if:
	 * <ul>
	 *     <li>player has already joined the event and not yet left</li>
	 *     <li>the event has already started, has not ended, and the implementation chooses not to allow players to join the vent during its lifespan</li>
	 *     <li>the event has ended, and is not restartable</li>
	 * </ul>
	 * then the call has no effect.
	 *
	 * Otherwise, player "joins" the event. This causes all callbacks registered with {@link #addPlayerJoinedCallback(Consumer)} to be called,
	 *  and an {@link InGameEvent.PlayerJoins} event to be fired on the forge event bus.
	 *
	 * At the point which the callbacks are called, the event is fired, and immediate after the method returns, if the player "joins" the event,
	 *  then the stream returned by {@link #getMembers()} will contain player.
	 *  This holds for any case which causes player to join the event.
	 *
	 * If any callback called as a result of this method, or any handler for {@link InGameEvent.PlayerJoins} fired as a result of this method,
	 * calls this method, or {@link #removePlayer(PlayerProfile)} for player, the behavior is undefined.
	 */
	public void addPlayer(PlayerProfile player);

	/**
	 * Removes the given player to the event explicitly. If the event has already been started, and has not ended, it is unspecified if the call has any effect.
	 *
	 * In particular, if:
	 * <ul>
	 *     <li>player has not yet joined the event</li>
	 *     <li>player has already left the event</li>
	 *     <li>the event has already started, has not ended, and the implementation chooses not to allow players to join the vent during its lifespan</li>
	 *     <li>the event has ended, and is not restartable</li>
	 * </ul>
	 * then the call has no effect.
	 *
	 * Otherwise, player "leave" the event. This causes all callbacks registered with {@link #addPlayerLeavesCallback(Consumer)} to be called,
	 *  and an {@link InGameEvent.PlayerLeaves} event to be fired on the forge event bus.
	 *
	 * At the point which the callbacks are called, the event is fired, and immediate after the method returns, if the player "leaves" the event,
	 *  then the stream returned by {@link #getMembers()} will contain player.
	 *  This holds true for any case which causes a player to leave the event.
	 *
	 * If any callback called as a result of this method, or any handler for {@link InGameEvent.PlayerLeaves} fired as a result of this method
	 * calls this method, or {@link #removePlayer(PlayerProfile)} for player, the behavior is undefined
	 */
	public void removePlayer(PlayerProfile player);

	/**
	 * Gets the members of the event. The Stream is late-binding to the members of the event.
	 *
	 * The following guarantees are made at any given point, when traversing the stream:
	 * <ul>
	 *     <li>During the execution of a callback for {@link #addPlayerJoinedCallback(Consumer)}, that accepted the player p, p will be encountered by the unfiltered stream returned, and will be encountered exactly once</li>
	 *     <li>During execution of a callback for {@link #addPlayerLeavesCallback(Consumer)}, that accepted the player p, p will not be encountered by the stream.</li>
	 *     <li>Immediately after the end of any resettable event, the stream will traverse no elements (but not during the execution of an callback for its end)</li>
	 *     <li>Immediately after the creation of the event, the stream will traverse no elements</li>
	 * </ul>
	 */
	public Stream<PlayerProfile> getMembers();
}
