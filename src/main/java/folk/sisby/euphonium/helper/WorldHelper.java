package folk.sisby.euphonium.helper;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @version 1.0.0-charmonium
 */
@SuppressWarnings("unused")
public class WorldHelper {
	public static Optional<BlockPos> findClosest(BlockPos origin, int horizontalRange, int verticalRange, Predicate<BlockPos> predicate) {
		for (BlockPos pos : BlockPos.withinBoxByManhattanDistance(origin, horizontalRange, verticalRange, horizontalRange)) {
			if (predicate.test(pos)) return Optional.of(pos.immutable());
		}
		return Optional.empty();
	}

	public static boolean isDay(Player player) {
		long dayTime = player.level().getOverworldClockTime() % 24000;
		return dayTime >= 0 && dayTime < 12700;
	}

	public static boolean isNight(Player player) {
		long dayTime = player.level().getOverworldClockTime() % 24000;
		return dayTime >= 12700;
	}

	public static boolean isThundering(Player player) {
		return player.level().isThundering();
	}

	public static boolean isOutside(Player player) {
		if (player.isUnderWater()) return false;

		int blocks = 24;
		int start = 1;

		BlockPos playerPos = player.blockPosition();

		if (player.level().canSeeSky(playerPos)) return true;
		if (player.level().canSeeSkyFromBelowWater(playerPos)) return true;

		for (int i = start; i < start + blocks; i++) {
			BlockPos check = new BlockPos(playerPos.getX(), playerPos.getY() + i, playerPos.getZ());
			BlockState state = player.level().getBlockState(check);
			Block block = state.getBlock();

			if (player.level().isEmptyBlock(check)) continue;

			if (!state.canOcclude()) continue;

			if (player.level().canSeeSky(check)) return true;
			if (player.level().canSeeSkyFromBelowWater(check)) return true;
			if (state.canOcclude()) return false;
		}

		return player.level().canSeeSky(playerPos.above(blocks));
	}

	public static float distanceFromGround(Player player, int check) {
		var level = player.level();
		var pos = player.blockPosition();
		var playerHeight = pos.getY();

		// Sample points.
		var samples = List.of(
			pos.east(check),
			pos.west(check),
			pos.north(check),
			pos.south(check)
		);

		int avg = 0;
		for (BlockPos sample : samples) {
			avg += level.getHeight(Heightmap.Types.WORLD_SURFACE, sample.getX(), sample.getZ());
		}
		avg /= samples.size();
		return Math.max(0.0F, playerHeight - avg);
	}

	public static boolean isBelowSeaLevel(Player player) {
		return player.blockPosition().getY() < player.level().getSeaLevel();
	}

	public static double getDistanceSquared(BlockPos pos1, BlockPos pos2) {
		double d0 = pos1.getX();
		double d1 = pos1.getZ();
		double d2 = d0 - pos2.getX();
		double d3 = d1 - pos2.getZ();
		return d2 * d2 + d3 * d3;
	}
}
