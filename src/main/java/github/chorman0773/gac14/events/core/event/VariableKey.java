package github.chorman0773.gac14.events.core.event;

import java.time.Duration;
import java.util.Objects;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootTable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Represents the name of a Typed Variable in an IEvent object.
 * This is a Value-based class. 
 * Use of identity senstitive operations, such as reference equality, identity hash code, or synchronization make have unpredictable results.
 *
 *
 * Note that the type of the Variable is, by design, part of its name.
 * @author chorm
 *
 * @param <T>
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class VariableKey<T> {
	
	private final String name;
	private final Class<T> cl;
	
	private VariableKey(String name,Class<T> cl) {
		this.name = name;
		this.cl = cl;
	}
	
	public static <T> VariableKey<T> makeKey(String name, Class<T> cl){
		return new VariableKey<>(name,cl);
	}
	
	public static VariableKey<Integer> makeIntegerKey(String name){
		return new VariableKey<>(name,int.class);
	}
	
	public static VariableKey<Double> makeDoubleKey(String name){
		return new VariableKey<>(name,double.class);
	}
	
	public static VariableKey<Vec3d> makePositionKey(String name){
		return new VariableKey<>(name,Vec3d.class);
	}
	
	public static VariableKey<String> makeStringKey(String name){
		return new VariableKey<>(name,String.class);
	}
	
	public static VariableKey<ResourceLocation> makeResourceKey(String name){
		return new VariableKey<>(name,ResourceLocation.class);
	}
	
	public static VariableKey<ITextComponent> makeTextKey(String name){
		return new VariableKey<>(name,ITextComponent.class);
	}

	/**
	 * Contains a list of Well-known variable keys.
	 * These keys should be used for there intended purpose.
	 *
	 * Consumers of the Gac14 Core API are permitted assume that these keys, and not custom keys, are used for there provided purpose.
	 */
	@SuppressWarnings("unused")
	public static interface WellKnownKeys{
		/**
		 * Specifies the Readable event. If present, this key is used in an unspecified way when informing users about the event.
		 */
		VariableKey<ITextComponent> EventNameKey = makeTextKey("EventName");
		VariableKey<Duration> DurationKey = makeKey("Duration",Duration.class);
		VariableKey<LootTable> RewardKey = makeKey("Reward",LootTable.class);
		VariableKey<Vec3d> EventLocationKey = makePositionKey("EventLocation");
		VariableKey<ServerWorld> EventWorldKey = makeKey("EventWorld",ServerWorld.class);
	}
	
	public Class<T> getType(){
		return cl;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cl, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VariableKey<?> other = (VariableKey<?>) obj;
		return Objects.equals(cl, other.cl) && Objects.equals(name, other.name);
	}

}
