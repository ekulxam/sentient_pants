package survivalblock.sentient_pants.common.entity.goal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import survivalblock.sentient_pants.common.entity.SentientPantsEntity;

import java.util.EnumSet;

public class SentientPantsAttackGoal extends TrackTargetGoal {
    private final SentientPantsEntity pants;
    private LivingEntity attacking;
    private int lastAttackTime;

    public SentientPantsAttackGoal(SentientPantsEntity pants) {
        super(pants, false);
        this.pants = pants;
        this.setControls(EnumSet.of(Control.TARGET));
    }

    @Override
    public boolean canStart() {
        Entity entity = this.pants.getOwner();
        if (!(entity instanceof LivingEntity living)) {
            return false;
        }
        this.attacking = living.getAttacking();
        int i = living.getLastAttackTime();
        return i != this.lastAttackTime && this.canTrack(this.attacking, TargetPredicate.DEFAULT);
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacking);
        Entity entity = this.pants.getOwner();
        if (!(entity instanceof LivingEntity living)) {
            return;
        }
        this.lastAttackTime = living.getLastAttackTime();
        super.start();
    }
}
