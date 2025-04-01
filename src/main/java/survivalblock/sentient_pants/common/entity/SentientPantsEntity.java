package survivalblock.sentient_pants.common.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import survivalblock.atmosphere.atmospheric_api.not_mixin.util.BoxWithNoAtmosphere;
import survivalblock.sentient_pants.common.entity.goal.SentientPantsAttackGoal;
import survivalblock.sentient_pants.common.entity.goal.SentientPantsFollowGoal;
import survivalblock.sentient_pants.common.entity.goal.SentientPantsTrackingGoal;
import survivalblock.sentient_pants.common.init.SentientPantsEntityTypes;
import survivalblock.sentient_pants.common.init.SentientPantsGameRules;
import survivalblock.sentient_pants.mixin.LivingEntityAccessor;

import java.util.Objects;
import java.util.UUID;

public class SentientPantsEntity extends PathAwareEntity implements Ownable {

    public static final DefaultAttributeContainer OVERPOWERED = createPantsAttributes()
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.5)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0)
            .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 4.0)
            .add(EntityAttributes.GENERIC_STEP_HEIGHT, 10.0)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0).build();

    @Nullable
    private UUID ownerUuid;
    @Nullable
    private Entity owner;
    private final Box emptyBox = new BoxWithNoAtmosphere();
    private boolean shouldDrop;

    public SentientPantsEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setDefaultPantsStack();
        this.shouldDrop = false;
    }

    public SentientPantsEntity(World world, LivingEntity owner, ItemStack stack) {
        super(SentientPantsEntityTypes.SENTIENT_PANTS, world);
        this.setPosition(owner.getPos());
        this.initialize((ServerWorldAccess) world, world.getLocalDifficulty(this.getBlockPos()), SpawnReason.REINFORCEMENT, null);
        this.setEquipmentDropChance(EquipmentSlot.LEGS, 1f);
        this.setOwner(owner);
        this.shouldDrop = !(owner instanceof PlayerEntity player) || !player.isCreative();
        this.setPantsStack(stack.copyWithCount(1));
        this.setCustomName(stack.getName());
        if (!world.isClient() && world.getGameRules().getBoolean(SentientPantsGameRules.STRONGER_PANTS)) {
            ((LivingEntityAccessor) this).sentient_pants$setAttributes(new AttributeContainer(OVERPOWERED));
        }
    }

    @Override
    public ImmutableList<EntityPose> getPoses() {
        return ImmutableList.of(EntityPose.STANDING, EntityPose.CROUCHING, EntityPose.SITTING);
    }

    @Override
    public boolean isFireImmune() {
        return super.isFireImmune() || this.getEquippedStack(EquipmentSlot.LEGS).getItem().getComponents().contains(DataComponentTypes.FIRE_RESISTANT);
    }

    public static DefaultAttributeContainer.Builder createPantsAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
                .add(EntityAttributes.GENERIC_ARMOR, 6.0)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 2.0)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 10.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2.0)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.1);
    }

    @Override
    public Box getBoundingBox(EntityPose pose) {
        return this.isInSneakingPose() ? emptyBox : super.getBoundingBox(pose);
    }

    @Override
    public void takeKnockback(double strength, double x, double z) {
        if (this.isInSneakingPose()) {
            return;
        }
        super.takeKnockback(strength, x, z);
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();
        if (!world.isClient()) {
            this.equipmentChanges();
            LivingEntity living = this.getTarget();
            if (living != null) {
                if (living.isRemoved() || !living.isAlive()) {
                    this.setTarget(null);
                }
                if (living.squaredDistanceTo(this) < 4) {
                    if (!this.isInSneakingPose()) {
                        this.setPose(EntityPose.CROUCHING);
                    }
                } else if (this.isInSneakingPose()) {
                    this.setPose(EntityPose.STANDING);
                }
            } else if (this.isInSneakingPose()) {
                this.setPose(EntityPose.STANDING);
            }
            if (this.getTarget() == null && world.getGameRules().getBoolean(SentientPantsGameRules.PANTS_SEEK_OUT_TARGETS) && world.getTime() % 20 == 0) {
                Entity owner = this.getOwner();
                if (owner != null && owner.isAlive()) {
                    Box box = this.getBoundingBox().expand(2, 16, 2);
                    MobEntity mob = world.getClosestEntity(world.getEntitiesByClass(MobEntity.class, box, mobEntity -> {
                        if (!mobEntity.isAlive()) {
                            return false;
                        }
                        if (!box.intersects(mobEntity.getBoundingBox())) {
                            return false;
                        }
                        LivingEntity target = mobEntity.getTarget();
                        if (Objects.equals(owner, target)) {
                            return true;
                        }
                        if (!(target instanceof SentientPantsEntity sentientPants)) {
                            return false;
                        }
                        return Objects.equals(owner, sentientPants.getOwner());
                    }), TargetPredicate.createAttackable(), null, this.getX(), this.getY(), this.getZ());
                    if (mob != null) {
                        this.setTarget(mob);
                    }
                }
            }
        }
    }

    private void equipmentChanges() {
        ItemStack stack = this.getEquippedStack(EquipmentSlot.LEGS);
        if (stack == null || stack.isEmpty()) {
            setDefaultPantsStack();
        } else if (stack.getItem() instanceof ArmorItem armorItem && !armorItem.getSlotType().equals(EquipmentSlot.LEGS)) {
            setDefaultPantsStack();
        }
        removeStackInSlot(EquipmentSlot.MAINHAND);
        removeStackInSlot(EquipmentSlot.OFFHAND);
        removeStackInSlot(EquipmentSlot.HEAD);
        removeStackInSlot(EquipmentSlot.CHEST);
        removeStackInSlot(EquipmentSlot.FEET);
    }

    private void setDefaultPantsStack() {
        this.setPantsStack(Items.DIAMOND_LEGGINGS.getDefaultStack());
    }

    private void setPantsStack(ItemStack stack) {
        this.equipStack(EquipmentSlot.LEGS, stack);
        if (this.isDead() || !this.isAlive() || this.isRemoved()) {
            return;
        }
        float maxHealth = stack.getMaxDamage();
        float health = stack.getMaxDamage() - Math.max(0, stack.getDamage());
        final float DIVIDER = 5f;
        maxHealth /= DIVIDER;
        health /= DIVIDER;
        maxHealth = MathHelper.clamp(maxHealth, 15, 100);
        health = MathHelper.clamp(health, 20, 100);
        EntityAttributeInstance attributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (attributeInstance != null) attributeInstance.setBaseValue(maxHealth);
        this.setHealth(Math.max(this.getMaxHealth(), Math.max(health, this.getHealth())));
    }

    private void removeStackInSlot(EquipmentSlot slot) {
        ItemStack stack = this.getEquippedStack(slot);
        if (stack == null || stack.isEmpty()) {
            return;
        }
        this.equipStack(slot, ItemStack.EMPTY);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
        if (this.getTarget() != null) {
            nbt.putUuid("TargetEntity", this.getTarget().getUuid());
        }
        nbt.putBoolean("ShouldDropLeggings", this.shouldDrop);
    }

    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUuid = entity.getUuid();
            this.owner = entity;
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean success = super.tryAttack(target);
        World world = this.getWorld();
        if (success && !world.isClient() && world.getGameRules().getBoolean(SentientPantsGameRules.STRONGER_PANTS)) {
            this.heal(1);
        }
        return success;
    }

    @Override
    @Nullable
    public Entity getOwner() {
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        } else if (this.ownerUuid != null && this.getWorld() instanceof ServerWorld serverWorld) {
            this.owner = serverWorld.getEntity(this.ownerUuid);
            return this.owner;
        } else {
            return null;
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
        if (nbt.containsUuid("TargetEntity") && this.getWorld() instanceof ServerWorld serverWorld) {
            Entity entity = serverWorld.getEntity(nbt.getUuid("TargetEntity"));
            if (entity instanceof LivingEntity living) this.setTarget(living);
        }
        shouldDrop = nbt.getBoolean("ShouldDropLeggings");
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new WanderAroundGoal(this, 0.8));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(6, new SentientPantsFollowGoal(this, 1.0, 10.0f, 2.0f, false));
        this.targetSelector.add(1, new SentientPantsTrackingGoal(this));
        this.targetSelector.add(2, new SentientPantsAttackGoal(this));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!player.getUuid().equals(this.ownerUuid)) {
            return super.interactMob(player, hand);
        }
        if (!this.isAlive()) {
            return super.interactMob(player, hand);
        }
        final boolean client = this.getWorld().isClient();
        if (!this.shouldDrop) {
            if (!client) {
                this.discard();
            }
            return ActionResult.success(client);
        }
        ItemStack stackInHand = player.getStackInHand(hand);
        ItemStack stack = this.getEquippedStack(EquipmentSlot.LEGS);
        if (stackInHand == null || stackInHand.isEmpty()) {
            if (!client) {
                player.setStackInHand(hand, stack.copyWithCount(1));
                this.discard();
            }
            return ActionResult.success(client);
        }
        return super.interactMob(player, hand);
    }

    @Override
    protected void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer) {
    }

    @Override
    protected void drop(ServerWorld world, DamageSource damageSource) {
        super.drop(world, damageSource);
        if (!shouldDrop) {
            return;
        }
        ItemStack pants = this.getEquippedStack(EquipmentSlot.LEGS).copyWithCount(1);
        if (world.getGameRules().getBoolean(SentientPantsGameRules.RETURN_ON_DEATH)) {
            Entity owner = this.getOwner();
            if (owner instanceof PlayerEntity player) {
                player.getInventory().offerOrDrop(pants);
                return;
            }
        }
        if (world.getGameRules().getBoolean(SentientPantsGameRules.DROP_PANTS_ON_DEATH)) {
            this.dropStack(pants);
        }
    }

    @Override
    public double getAttributeValue(RegistryEntry<EntityAttribute> attribute) {
        if (EntityAttributes.GENERIC_ATTACK_KNOCKBACK.equals(attribute) && this.getWorld().getGameRules().getBoolean(SentientPantsGameRules.NO_KICKING)) {
            return 0;
        }
        return super.getAttributeValue(attribute);
    }
}
