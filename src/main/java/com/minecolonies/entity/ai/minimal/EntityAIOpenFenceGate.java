package com.minecolonies.entity.ai.minimal;

import com.minecolonies.util.SoundUtils;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Used for automatic gate open/close.
 */
public class EntityAIOpenFenceGate extends EntityAIGateInteract
{
    /**
     * Close door after ... ticks.
     */
    private static final int TIME_TO_CLOSE_DOOR = 20;
    /**
     * Checks if the gate should be closed
     */
    private boolean closeDoor;
    /**
     * Ticks until the gate should be closed
     */
    private int     closeDoorTemporisation;

    /**
     * Constructor called to register the AI class with an entity
     *
     * @param entityLivingIn the registering entity
     * @param shouldClose    should the entity close the gate?
     */
    public EntityAIOpenFenceGate(@NotNull EntityLiving entityLivingIn, boolean shouldClose)
    {
        super(entityLivingIn);
        this.theEntity = entityLivingIn;
        this.closeDoor = shouldClose;
    }

    /**
     * Should the AI continue to execute?
     *
     * @return true or false
     */
    @Override
    public boolean continueExecuting()
    {
        return this.closeDoor && this.closeDoorTemporisation > 0 && super.continueExecuting();
    }

    /**
     * Start the execution
     */
    @Override
    public void startExecuting()
    {
        this.closeDoorTemporisation = TIME_TO_CLOSE_DOOR;
        toggleDoor(true);
    }

    /**
     * Toggles the door(Opens or closes)
     *
     * @param open if open or close
     */
    private void toggleDoor(boolean open)
    {
        IBlockState iblockstate = this.theEntity.worldObj.getBlockState(this.gatePosition);
        if (iblockstate.getBlock() == this.gateBlock && (iblockstate.getValue(BlockFenceGate.OPEN)) != open)
        {
            this.theEntity.worldObj.setBlockState(this.gatePosition, iblockstate.withProperty(BlockFenceGate.OPEN, open), 2);
            final SoundEvent openCloseSound = open ? SoundEvents.BLOCK_FENCE_GATE_OPEN : SoundEvents.BLOCK_FENCE_GATE_CLOSE;
            SoundUtils.playSoundAtCitizen(this.theEntity.worldObj, this.gatePosition, openCloseSound);
        }
    }

    /**
     * Updates the task.
     */
    @Override
    public void updateTask()
    {
        --this.closeDoorTemporisation;
        super.updateTask();
    }

    /**
     * Reset the action
     */
    @Override
    public void resetTask()
    {
        if (this.closeDoor)
        {
            toggleDoor(false);
        }
    }
}


