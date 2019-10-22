package github.chorman0773.gac14.events.core.event;

import github.chorman0773.gac14.player.PlayerProfile;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

@MethodsReturnNonnullByDefault
public abstract class EventImpl implements IEvent {

    private final Map<VariableKey<?>,Object> variables;
    private final ResourceLocation provider;

    private final Set<PlayerProfile> players;

    private Consumer<PlayerProfile> joinCallback;
    private Consumer<PlayerProfile> leaveCallback;
    private Runnable startCallback;
    private Consumer<Stream<PlayerProfile>> finishCallback;
    private Consumer<Stream<PlayerProfile>> winCallback;
    private boolean running;

    private Instant endTime;



    public EventImpl(ResourceLocation provider){
        this.provider = provider;
        this.variables = new HashMap<>();
        this.players = new HashSet<>();
        this.joinCallback = p-> MinecraftForge.EVENT_BUS.post(new InGameEvent.PlayerJoins(p,this));
        this.leaveCallback = p->MinecraftForge.EVENT_BUS.post(new InGameEvent.PlayerLeaves(p,this));
        this.startCallback = ()->MinecraftForge.EVENT_BUS.post(new InGameEvent.Start(this));
        this.finishCallback = s->MinecraftForge.EVENT_BUS.post(new InGameEvent.End(this));
        this.winCallback = s->s.forEach(p->MinecraftForge.EVENT_BUS.post(new InGameEvent.PlayerWins(p,this)));
    }
    @Override
    public <T> Optional<T> getVariable(VariableKey<T> key) {
        return Optional.ofNullable((T)variables.get(key));
    }

    @Override
    public <T> IEvent setVariable(VariableKey<T> key, T value) {
        variables.put(key,value);
        return this;
    }

    @Override
    public ResourceLocation getProviderName() {
        return provider;
    }

    @Override
    public IEvent addFinishCallback(Consumer<Stream<PlayerProfile>> finishCallback) {
        this.finishCallback = this.finishCallback.andThen(finishCallback);
        return this;
    }

    @Override
    public IEvent addStartCallback(Runnable startCallback) {
        this.startCallback = ()->{this.startCallback.run();startCallback.run();};
        return this;
    }

    @Override
    public IEvent addPlayerJoinedCallback(Consumer<PlayerProfile> joinedCallback) {
        this.joinCallback = this.joinCallback.andThen(joinedCallback);
        return this;
    }

    @Override
    public IEvent addPlayerLeavesCallback(Consumer<PlayerProfile> leavesCallback) {
        this.leaveCallback = this.leaveCallback.andThen(leavesCallback);
        return this;
    }

    @Override
    public IEvent addPlayerWinsCallback(Consumer<Stream<PlayerProfile>> winsCallback) {
        this.winCallback = this.winCallback.andThen(winsCallback);
        return this;
    }

    protected final void handleWinningPlayers(Stream<PlayerProfile> winners){
        this.winCallback.accept(winners);
    }

    public void end(){
        if(running) {
            running = false;
            MinecraftForge.EVENT_BUS.unregister(endChecker);
            EventImpl.this.finishCallback.accept(EventImpl.this.getMembers());
        }
    }

    public void addPlayer(PlayerProfile player){
        this.players.add(player);
        this.joinCallback.accept(player);
    }

    public void removePlayer(PlayerProfile player){
        this.players.remove(player);
        this.leaveCallback.accept(player);
    }

    private Object endChecker;

    @Override
    public void start() {
        if(!running) {
            startCallback.run();
            MinecraftForge.EVENT_BUS.register(endChecker = new Object() {
                @SubscribeEvent
                public void tickEventEnd(TickEvent.ServerTickEvent event) {
                    if (running && Instant.now().isAfter(endTime))
                        end();
                }
            });
            this.endTime = this.getVariable(VariableKey.WellKnownKeys.DurationKey).map(d -> Instant.now().plus(d)).orElse(Instant.MAX);
            this.running = true;
        }
    }

    @Override
    public Stream<PlayerProfile> getMembers() {
        return players.stream();
    }
}
