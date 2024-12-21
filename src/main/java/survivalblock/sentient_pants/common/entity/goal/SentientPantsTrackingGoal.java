package survivalblock.sentient_pants.common.entity.goal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import survivalblock.sentient_pants.common.entity.SentientPantsEntity;

import java.util.EnumSet;

public class SentientPantsTrackingGoal extends TrackTargetGoal {

    // TRACK OWNER ATTACKER GOAL
    private final SentientPantsEntity pants;
    private LivingEntity attacker;
    private int lastAttackedTime;

    public SentientPantsTrackingGoal(SentientPantsEntity pants) {
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
        this.attacker = living.getAttacker();
        int i = living.getLastAttackedTime();
        return i != this.lastAttackedTime && this.canTrack(this.attacker, TargetPredicate.DEFAULT);
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacker);
        Entity entity = this.pants.getOwner();
        if (entity instanceof LivingEntity livingEntity) {
            this.lastAttackedTime = livingEntity.getLastAttackedTime();
        }
        super.start();
    }
}