package github.chorman0773.gac14.events.core.event;

import java.time.Duration;
import java.util.Objects;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.storage.loot.LootTable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Represents the name of a Typed Variable in an IEvent object.
 * This is a Value-based class. 
 * Use of identity senstitive operations, such as reference equality, identity hash code, or synchronization make have unpredictable results.
 * 
 * @author chorm
 *
 * @param <T>
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class VariableKey<T> {
	
	private String name;
	private Class<T> cl;
	
	private VariableKey(String name,Class<T> cl) {
		// TODO Auto-generated constructor stub
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
	
	public static interface CommonKeys{
		VariableKey<ITextComponent> EventNameKey = makeTextKey("EventName");
		VariableKey<Duration> DurationKey = makeKey("Duration",Duration.class);
		VariableKey<LootTable> RewardKey = makeKey("Reward",LootTable.class);
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
