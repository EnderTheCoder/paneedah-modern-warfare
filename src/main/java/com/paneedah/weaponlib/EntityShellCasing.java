package com.paneedah.weaponlib;

import com.paneedah.weaponlib.Weapon.ShellCasingEjectDirection;
import com.paneedah.weaponlib.compatibility.CompatibleExtraEntityFlags;
import com.paneedah.weaponlib.compatibility.CompatibleMathHelper;
import com.paneedah.weaponlib.compatibility.CompatibleRayTraceResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static com.paneedah.weaponlib.compatibility.CompatibilityProvider.compatibility;

public class EntityShellCasing extends EntityProjectile {

    @SuppressWarnings("unused")
    private static final Logger logger = LogManager.getLogger(EntityShellCasing.class);

    private static final String TAG_ENTITY_ITEM = "entity_item";

    static final float DEFAULT_INACCURACY = 1f;

    private Random random = new Random();

    private Weapon weapon;
    private PlayerWeaponInstance weaponInstance;

    private float initialYaw;
    private float initialPitch;
    private float xRotation;
    private float yRotation;
    private float zRotation;
    private float xRotationChange;
    private float yRotationChange;
    private float zRotationChange;

    private float rotationSlowdownFactor = 0.95f;
    private float maxRotationChange = 30f;

    public EntityShellCasing(World world) {
        super(world);
        setRotations();
    }

    public EntityShellCasing(PlayerWeaponInstance weaponInstance, World world, EntityLivingBase player, float velocity,
            float gravityVelocity, float inaccuracy) {
        super(world, player, velocity, gravityVelocity, inaccuracy);
       
        this.weapon = weaponInstance.getWeapon();
        this.weaponInstance = weaponInstance;
    }

    @Override
    public void setPositionAndDirection() {
        this.setSize(0.001f, 0.001f);
        float forwardOffset = compatibility.getCompatibleShellCasingForwardOffset()
                + weapon.getShellCasingForwardOffset(); // 0.1f;

        float sideOffset;
        if (weapon.getShellCasingEjectDirection() == ShellCasingEjectDirection.RIGHT) {
            sideOffset = weaponInstance.isAimed() ? weapon.getShellCasingSideOffsetAimed()
                    : weapon.getShellCasingSideOffset();
        } else {
            sideOffset = weaponInstance.isAimed() ? -0.1f : 0f;
        }

        float yOffset = weapon.getShellCasingVerticalOffset();
        if (thrower.isSneaking()) {
            yOffset -= 0.1f;
        } else if (thrower instanceof EntityPlayer && 
                (CompatibleExtraEntityFlags.getFlags(thrower) & CompatibleExtraEntityFlags.PRONING) != 0) {
            yOffset -= 0.0f;
        }

        this.setLocationAndAngles(thrower.posX, thrower.posY + (double) thrower.getEyeHeight() + yOffset, thrower.posZ,
                thrower.rotationYaw, thrower.rotationPitch);

        this.posX -= (double) (CompatibleMathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * sideOffset)
                + CompatibleMathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI)
                        * CompatibleMathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * forwardOffset;

        // float verticalOffset = ;
        this.posY += (double) (-CompatibleMathHelper.sin((this.rotationPitch) / 180.0F * (float) Math.PI)
                * forwardOffset);

        this.posZ -= (double) (CompatibleMathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * sideOffset)
                - CompatibleMathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI)
                        * CompatibleMathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * forwardOffset;

        this.setPosition(this.posX, this.posY, this.posZ);

        float f = velocity;

        float adjustedRotationYaw;
        if (weapon.getShellCasingEjectDirection() == ShellCasingEjectDirection.RIGHT) {
            adjustedRotationYaw = this.rotationYaw + (weaponInstance.isAimed() ? -10f : -30f);
        } else {
            adjustedRotationYaw = this.rotationYaw + 0f;
        }

        int directionSignum = weapon.getShellCasingEjectDirection() == ShellCasingEjectDirection.RIGHT ? 1 : -1;

        this.motionX = directionSignum
                * -(double) (CompatibleMathHelper.cos(adjustedRotationYaw / 180.0F * (float) Math.PI) * f);

        this.motionZ = directionSignum
                * (double) (-CompatibleMathHelper.sin(adjustedRotationYaw / 180.0F * (float) Math.PI) * f);

        this.motionY = 0;
        
        this.motionX *= 0.1;
        this.motionY *= 0.1;
        this.motionZ *= 0.1;

        this.initialYaw = this.rotationYaw;
        this.initialPitch = this.rotationPitch;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, velocity, inaccuracy);
    }

    private void setRotations() {
        xRotationChange = maxRotationChange * (float) random.nextGaussian();
        yRotationChange = maxRotationChange * (float) random.nextGaussian();
        zRotationChange = maxRotationChange * (float) random.nextGaussian();
    }

    @Override
    public void onUpdate() {
//        if(true) {
//            this.setDead();
//        }
        super.onUpdate();
        // /*
        // logger.trace("Before {} {} {}, velocity: {}, {}, {}, gravity: {}",
        // this.posX, this.posY, this.posZ,
        // this.motionX, this.motionY, this.motionZ, this.gravityVelocity);
        // */

        xRotation += xRotationChange;
        yRotation += yRotationChange;
        zRotation += zRotationChange;

        xRotationChange *= rotationSlowdownFactor;
        yRotationChange *= rotationSlowdownFactor;
        zRotationChange *= rotationSlowdownFactor;

        // /*
        //
        // logger.debug("After {} {} {}, velocity: {}, {}, {}, gravity: {}",
        // this.posX, this.posY, this.posZ,
        // this.motionX, this.motionY, this.motionZ, this.gravityVelocity);
        // */
    }

    /**
     * @see net.minecraft.entity.projectile.EntityThrowable#onImpact(net.minecraft.util.MovingObjectPosition)
     */
    @Override
    protected void onImpact(CompatibleRayTraceResult position) {
        if (!compatibility.world(this).isRemote) {
            setDead();
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        super.writeSpawnData(buffer);
        buffer.writeInt(Item.getIdFromItem(weapon));
        buffer.writeFloat(initialYaw);
        buffer.writeFloat(initialPitch);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        super.readSpawnData(buffer);
        weapon = (Weapon) Item.getItemById(buffer.readInt());
        initialYaw = buffer.readFloat();
        initialPitch = buffer.readFloat();
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);
        Item item = Item.getItemById(tagCompound.getInteger(TAG_ENTITY_ITEM));
        if (item instanceof Weapon) {
            weapon = (Weapon) item;
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger(TAG_ENTITY_ITEM, Item.getIdFromItem(weapon));
    }

    Weapon getWeapon() {
        return weapon;
    }

    boolean isDamageableEntity(Entity entity) {
        return false;
    }

    public float getInitialYaw() {
        return initialYaw;
    }

    public float getInitialPitch() {
        return initialPitch;
    }

    public float getXRotation() {
        return initialPitch - xRotation;
    }

    public float getYRotation() {
        return yRotation - initialYaw - 90f;
    }

    public float getZRotation() {
        return zRotation;
    }

    @Override
    public void setDead() {
        super.setDead();
        // logger.debug("{} despawned", this);
    }

}