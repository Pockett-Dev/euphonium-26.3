package folk.sisby.euphonium.sound;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SoundHandler<T extends ISoundInstance> {
	private final List<T> sounds = new ArrayList<>();
	private Player player;

	public SoundHandler(@NotNull Player player) {
		updatePlayer(player);
	}

	public void updatePlayer(@NotNull Player player) {
		this.player = player;
		sounds.forEach(s -> s.updatePlayer(this.player));
	}

	public List<T> getSounds() {
		return sounds;
	}

	public Player getPlayer() {
		return player;
	}

	public void tick() {
		if (player == null) return;
		sounds.forEach(T::tick);
	}

	public void stop() {
		sounds.forEach(T::stop);
	}
}
