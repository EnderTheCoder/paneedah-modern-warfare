package com.paneedah.weaponlib.grenade;

import com.paneedah.weaponlib.CommonModContext;
import com.paneedah.weaponlib.Explosion;
import com.paneedah.weaponlib.ModContext;
import com.paneedah.weaponlib.grenade.ItemGrenade.Type;
import com.paneedah.weaponlib.state.Aspect;
import com.paneedah.weaponlib.state.PermitManager;
import com.paneedah.weaponlib.state.StateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static com.paneedah.weaponlib.compatibility.CompatibilityProvider.compatibility;


/*
 * On a client side this class is used from within a separate client "ticker" thread
 */
public class GrenadeAttackAspect implements Aspect<GrenadeState, PlayerGrenadeInstance> {

    private static final Logger logger = LogManager.getLogger(GrenadeAttackAspect.class);

    @SuppressWarnings("unused")
    private static final long ALERT_TIMEOUT = 300;

    private Predicate<PlayerGrenadeInstance> hasSafetyPin = instance -> instance.getWeapon().hasSafetyPin();

    private static Predicate<PlayerGrenadeInstance> reequipTimeoutExpired =
            instance -> System.currentTimeMillis() >  instance.getStateUpdateTimestamp()
                + instance.getWeapon().getReequipTimeout();

//    private static Predicate<PlayerGrenadeInstance> isSmokeGrenade =
//                    instance -> instance.getWeapon().isSmokeOnly();

//    private static Predicate<PlayerGrenadeInstance> takingOffSafetyPinCompleted = instance ->
//            System.currentTimeMillis() >= instance.getStateUpdateTimestamp()
//                + instance.getWeapon().getTotalTakeSafetyPinOffDuration() * 1.1;

    private static Predicate<PlayerGrenadeInstance> throwingCompleted = instance ->
            System.currentTimeMillis() >= instance.getStateUpdateTimestamp()
                + instance.getWeapon().getTotalThrowingDuration() * 1.1;

    private static Predicate<PlayerGrenadeInstance> explosionTimeoutExpired = instance ->
            System.currentTimeMillis() >= instance.getStateUpdateTimestamp()
                + instance.getWeapon().getExplosionTimeout();

    private static final Set<GrenadeState> allowedAttackFromStates = new HashSet<>(
            Arrays.asList(GrenadeState.READY, GrenadeState.STRIKER_LEVER_RELEASED));

    private static final Set<GrenadeState> allowedPinOffFromStates = new HashSet<>(
            Arrays.asList(GrenadeState.SAFETY_PING_OFF));

    private static final Set<GrenadeState> allowedUpdateFromStates = new HashSet<>(
            Arrays.asList(GrenadeState.STRIKER_LEVER_RELEASED, GrenadeState.THROWING, GrenadeState.THROWN,
                    GrenadeState.EXPLODED_IN_HANDS));

    private static final int SAFETY_IN_ALERT_TIMEOUT = 1000;

    private ModContext modContext;

    private StateManager<GrenadeState, ? super PlayerGrenadeInstance> stateManager;

    public GrenadeAttackAspect(CommonModContext modContext) {
        this.modContext = modContext;
    }

    @Override
    public void setPermitManager(PermitManager permitManager) {}

    @Override
    public void setStateManager(StateManager<GrenadeState, ? super PlayerGrenadeInstance> stateManager) {
        this.stateManager = stateManager;

        stateManager

        .in(this)
        .change(GrenadeState.READY).to(GrenadeState.SAFETY_PING_OFF)
        .withAction(i -> takeSafetyPinOff(i))
        .when(hasSafetyPin)
        .manual()

        .in(this).change(GrenadeState.SAFETY_PING_OFF).to(GrenadeState.STRIKER_LEVER_RELEASED)
        .withAction(i -> releaseStrikerLever(i))
        //.when(takingOffSafetyPinCompleted)
        .manual()

        .in(this).change(GrenadeState.STRIKER_LEVER_RELEASED).to(GrenadeState.EXPLODED_IN_HANDS)
        .withAction(i -> explode(i))
        .when(explosionTimeoutExpired.and(i -> i.getWeapon().getType() == Type.REGULAR))
        .automatic()

        .in(this).change(GrenadeState.READY).to(GrenadeState.THROWING)
        .when(hasSafetyPin.negate())
        .manual()

        .in(this).change(GrenadeState.THROWING).to(GrenadeState.THROWN)
        .withAction(i -> throwIt(i))
        .when(throwingCompleted)
        .automatic()

        .in(this).change(GrenadeState.STRIKER_LEVER_RELEASED).to(GrenadeState.THROWING)
        .manual()

        .in(this).change(GrenadeState.THROWN).to(GrenadeState.READY)
        .withAction(i -> reequip(i))
        .when(reequipTimeoutExpired)
        .automatic()

        .in(this).change(GrenadeState.EXPLODED_IN_HANDS).to(GrenadeState.READY)
        .withAction(i -> reequip(i))
        .when(reequipTimeoutExpired)
        .automatic()
        ;
    }

    private void explode(PlayerGrenadeInstance instance) {
        logger.debug("Exploding!");
        modContext.getChannel().getChannel().sendToServer(new GrenadeMessage(instance, 0));
    }

    private void throwIt(PlayerGrenadeInstance instance) {
        logger.debug("Throwing with state " + instance.getState());
        long activationTimestamp;
        if(instance.getWeapon().getType() != Type.REGULAR) {
            activationTimestamp = System.currentTimeMillis();
        } else if(instance.getWeapon().getExplosionTimeout() > 0) {
            activationTimestamp = instance.getActivationTimestamp();
        } else {
            activationTimestamp = ItemGrenade.EXPLODE_ON_IMPACT;
        }
        compatibility.playSound(instance.getPlayer(), instance.getWeapon().getThrowSound(), 1.0F, 1.0F);
        modContext.getChannel().getChannel().sendToServer(new GrenadeMessage(instance, activationTimestamp));
    }

    private void reequip(PlayerGrenadeInstance instance) {
        logger.debug("Reequipping");
    }

    private void takeSafetyPinOff(PlayerGrenadeInstance instance) {
        compatibility.playSound(instance.getPlayer(), instance.getWeapon().getSafetyPinOffSound(), 1.0F, 1.0F);
        logger.debug("Taking safety pin off");
    }

    private void releaseStrikerLever(PlayerGrenadeInstance instance) {
        logger.debug("Safety pin is off");
        instance.setActivationTimestamp(System.currentTimeMillis());
    }

    void onAttackButtonClick(EntityPlayer player, boolean throwingFar) {
        PlayerGrenadeInstance grenadeInstance = modContext.getPlayerItemInstanceRegistry().getMainHandItemInstance(player, PlayerGrenadeInstance.class);
        if(grenadeInstance != null) {
            grenadeInstance.setThrowingFar(throwingFar);
            stateManager.changeStateFromAnyOf(this, grenadeInstance, allowedAttackFromStates,
                    GrenadeState.SAFETY_PING_OFF, GrenadeState.THROWING);
        }
    }

    void onAttackButtonUp(EntityPlayer player, boolean throwingFar) {
        PlayerGrenadeInstance grenadeInstance = modContext.getPlayerItemInstanceRegistry().getMainHandItemInstance(player, PlayerGrenadeInstance.class);
        if(grenadeInstance != null) {
            grenadeInstance.setThrowingFar(throwingFar);
            stateManager.changeStateFromAnyOf(this, grenadeInstance, allowedPinOffFromStates, GrenadeState.STRIKER_LEVER_RELEASED);
        }
    }

    void onUpdate(EntityPlayer player) {
        PlayerGrenadeInstance grenadeInstance = modContext.getPlayerItemInstanceRegistry().getMainHandItemInstance(player, PlayerGrenadeInstance.class);
        if(grenadeInstance != null) {
            if(grenadeInstance.getState() == GrenadeState.STRIKER_LEVER_RELEASED
                    && grenadeInstance.getWeapon().getType() == Type.REGULAR
                    && System.currentTimeMillis() > grenadeInstance.getLastSafetyPinAlertTimestamp() + SAFETY_IN_ALERT_TIMEOUT) {
                long remainingTimeUntilExplosion = grenadeInstance.getWeapon().getExplosionTimeout() - (
                                System.currentTimeMillis() - grenadeInstance.getActivationTimestamp());

                if(remainingTimeUntilExplosion < 0) {
                    remainingTimeUntilExplosion = 0;
                }
                
                
                String message = compatibility.getLocalizedString("gui.grenadeExplodes",
                        Math.round(remainingTimeUntilExplosion / 1000f));
                modContext.getStatusMessageCenter().addAlertMessage(message, 1, 1000, 0);
                grenadeInstance.setLastSafetyPinAlertTimestamp(System.currentTimeMillis());
            }
            stateManager.changeStateFromAnyOf(this, grenadeInstance, allowedUpdateFromStates);
        }
    }

    public void serverThrowGrenade(EntityPlayer player, PlayerGrenadeInstance instance, long activationTimestamp) {
        logger.debug("Throwing grenade");
       
        //boolean isSmokeGrenade = instance.getWeapon().isSmokeOnly();
        
        serverThrowGrenade(modContext, player, instance, activationTimestamp);

        compatibility.consumeInventoryItemFromSlot(player, instance.getItemInventoryIndex());
    }

    public static void serverThrowGrenade(ModContext modContext, EntityLivingBase player, PlayerGrenadeInstance instance,
            long activationTimestamp) {
    	
    
        if(activationTimestamp == 0 && instance.getWeapon().getType() == Type.REGULAR) {
            // explode immediately
        	player.attackEntityFrom(DamageSource.causeExplosionDamage(player), 500f);
            Explosion.createServerSideExplosion(modContext, compatibility.world(player), null,
                    player.posX, player.posY, player.posZ, instance.getWeapon().getExplosionStrength(), false, true,
                    instance.getWeapon().isDestroyingBlocks(), 1f, 1f, 1.5f, 1f, null, null, modContext.getExplosionSound());

        } else if(instance.getWeapon().getType() == Type.SMOKE) {
            float velocity = instance.isThrowingFar() ? instance.getWeapon().getFarVelocity() : instance.getWeapon().getVelocity();
            EntitySmokeGrenade entityGrenade = new EntitySmokeGrenade.Builder()
                    .withThrower(player)
                    .withActivationTimestamp(activationTimestamp)
                    .withGrenade(instance.getWeapon())
                    .withSmokeAmount(instance.getWeapon().getExplosionStrength())
                    .withActivationDelay(0) //instance.getWeapon().getExplosionTimeout())
                    .withActiveDuration(instance.getWeapon().getActiveDuration())
                    .withVelocity(velocity)
                    .withGravityVelocity(instance.getWeapon().getGravityVelocity())
                    .withRotationSlowdownFactor(instance.getWeapon().getRotationSlowdownFactor())
                    .build(modContext);
            logger.debug("Throwing velocity {} ", velocity);
            compatibility.spawnEntity(player, entityGrenade);
        } else if(instance.getWeapon().getType() == Type.GAS) {
            float velocity = instance.isThrowingFar() ? instance.getWeapon().getFarVelocity() : instance.getWeapon().getVelocity();
            EntityGasGrenade entityGrenade = new EntityGasGrenade.Builder()
                    .withThrower(player)
                    .withActivationTimestamp(activationTimestamp)
                    .withGrenade(instance.getWeapon())
                    .withSmokeAmount(instance.getWeapon().getExplosionStrength())
                    .withActivationDelay(0)
                    .withActiveDuration(instance.getWeapon().getActiveDuration())
                    .withVelocity(velocity)
                    .withGravityVelocity(instance.getWeapon().getGravityVelocity())
                    .withRotationSlowdownFactor(instance.getWeapon().getRotationSlowdownFactor())
                    .build(modContext);
            logger.debug("Throwing velocity {} ", velocity);
            compatibility.spawnEntity(player, entityGrenade);
        } else if (instance.getWeapon().getType() == Type.FLASH) {
            float velocity = instance.isThrowingFar() ? instance.getWeapon().getFarVelocity() : instance.getWeapon().getVelocity();
            EntityFlashGrenade entityGrenade = new EntityFlashGrenade.Builder()
                    .withThrower(player)
                    .withActivationTimestamp(activationTimestamp)
                    .withGrenade(instance.getWeapon())
                    .withExplosionStrength(instance.getWeapon().getExplosionStrength())
                    .withExplosionTimeout(instance.getWeapon().getExplosionTimeout())
                    .withVelocity(velocity)
                    .withGravityVelocity(instance.getWeapon().getGravityVelocity())
                    .withRotationSlowdownFactor(instance.getWeapon().getRotationSlowdownFactor())
                    .withDestroyingBlocks(false)
                    .build(modContext);
            logger.debug("Throwing velocity {} ", velocity);
            compatibility.spawnEntity(player, entityGrenade);
        } else {
            float velocity = instance.isThrowingFar() ? instance.getWeapon().getFarVelocity() : instance.getWeapon().getVelocity();
            EntityGrenade entityGrenade = new EntityGrenade.Builder()
                    .withThrower(player)
                    .withActivationTimestamp(activationTimestamp)
                    .withGrenade(instance.getWeapon())
                    .withExplosionStrength(instance.getWeapon().getExplosionStrength())
                    .withExplosionTimeout(instance.getWeapon().getExplosionTimeout())
                    .withVelocity(velocity)
                    .withGravityVelocity(instance.getWeapon().getGravityVelocity())
                    .withRotationSlowdownFactor(instance.getWeapon().getRotationSlowdownFactor())
                    .withDestroyingBlocks(instance.getWeapon().isDestroyingBlocks())
                    .build(modContext);
            logger.debug("Throwing velocity {} ", velocity);
            compatibility.spawnEntity(player, entityGrenade);
        }
    }

    int getParticleCount(float damage) {
        return (int) (-0.11 * (damage - 30) * (damage - 30) + 100);
    }
}
