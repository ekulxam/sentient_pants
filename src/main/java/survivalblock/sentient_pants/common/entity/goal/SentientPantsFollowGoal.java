package survivalblock.sentient_pants.common.entity.goal;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import survivalblock.sentient_pants.common.entity.SentientPantsEntity;

import java.util.EnumSet;

public class SentientPantsFollowGoal extends Goal {

    // FOLLOW OWNER GOALS
    public static final int TELEPORT_DISTANCE = 15;
    private static final int HORIZONTAL_RANGE = 2;
    private static final int HORIZONTAL_VARIATION = 4;
    private static final int VERTICAL_VARIATION = 2;
    private final SentientPantsEntity pants;
    private Entity owner;
    private final WorldView world;
    private final double speed;
    private final EntityNavigation navigation;
    private int updateCountdownTicks;
    private final float maxDistance;
    private final float minDistance;
    private float oldWaterPathfindingPenalty;
    private final boolean leavesAllowed;

    public SentientPantsFollowGoal(SentientPantsEntity pants, double speed, float minDistance, float maxDistance, boolean leavesAllowed) {
        this.pants = pants;
        this.world = pants.getWorld();
        this.speed = speed;
        this.navigation = pants.getNavigation();
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.leavesAllowed = leavesAllowed;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        if (!(pants.getNavigation() instanceof MobNavigation) && !(pants.getNavigation() instanceof BirdNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for SentientPantsFollowGoal");
        }
    }

    @Override
    public boolean canStart() {
        Entity entity = this.pants.getOwner();
        if (entity == null) {
            return false;
        }
        if (entity.isSpectator()) {
            return false;
        }
        if (this.cannotFollow()) {
            return false;
        }
        if (this.pants.squaredDistanceTo(entity) < (double)(this.minDistance * this.minDistance)) {
            return false;
        }
        this.owner = entity;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        if (this.navigation.isIdle()) {
            return false;
        }
        if (this.cannotFollow()) {
            return false;
        }
        return !(this.pants.squaredDistanceTo(this.owner) <= (double)(this.maxDistance * this.maxDistance));
    }

    private boolean cannotFollow() {
        return this.pants.hasVehicle() || this.pants.isLeashed();
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
        this.oldWaterPathfindingPenalty = this.pants.getPathfindingPenalty(PathNodeType.WATER);
        this.pants.setPathfindingPenalty(PathNodeType.WATER, 0.0f);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.pants.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty);
    }

    @Override
    public void tick() {
        this.pants.getLookControl().lookAt(this.owner, 10.0f, this.pants.getMaxLookPitchChange());
        if (--this.updateCountdownTicks > 0) {
            return;
        }
        this.updateCountdownTicks = this.getTickCount(10);
        if (this.pants.squaredDistanceTo(this.owner) >= TELEPORT_DISTANCE * TELEPORT_DISTANCE) {
            this.tryTeleport();
        } else {
            this.navigation.startMovingTo(this.owner, this.speed);
        }
    }

    private void tryTeleport() {
        BlockPos blockPos = this.owner.getBlockPos();
        for (int i = 0; i < 10; ++i) {
            int j = this.getRandomInt(-HORIZONTAL_VARIATION, HORIZONTAL_VARIATION);
            int k = this.getRandomInt(-VERTICAL_VARIATION, VERTICAL_VARIATION);
            int l = this.getRandomInt(-HORIZONTAL_VARIATION, HORIZONTAL_VARIATION);
            boolean bl = this.tryTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
            if (!bl) continue;
            return;
        }
    }

    private boolean tryTeleportTo(int x, int y, int z) {
        if (Math.abs((double)x - this.owner.getX()) < HORIZONTAL_RANGE && Math.abs((double)z - this.owner.getZ()) < HORIZONTAL_RANGE) {
            return false;
        }
        if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        }
        this.pants.refreshPositionAndAngles((double)x + 0.5, y, (double)z + 0.5, this.pants.getYaw(), this.pants.getPitch());
        this.navigation.stop();
        return true;
    }

    private boolean canTeleportTo(BlockPos pos) {
        PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(this.pants, pos.mutableCopy());
        if (pathNodeType != PathNodeType.WALKABLE) {
            return false;
        }
        BlockState blockState = this.world.getBlockState(pos.down());
        if (!this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock) {
            return false;
        }
        BlockPos blockPos = pos.subtract(this.pants.getBlockPos());
        return this.world.isSpaceEmpty(this.pants, this.pants.getBoundingBox().offset(blockPos));
    }

    private int getRandomInt(int min, int max) {
        return this.pants.getRandom().nextInt(max - min + 1) + min;
    }
}