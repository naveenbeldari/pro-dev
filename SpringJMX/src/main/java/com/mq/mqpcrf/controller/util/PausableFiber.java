package com.mq.mqpcrf.controller.util;

/**
 * <p>
 * This interface defines an extension to the core {@link Fiber Fiber}interface
 * and adds the methods for pausing and resuming an executable context. The base
 * interface class defines an execution environment, this interface extends the
 * core interface to provide methods for suspending and resuming the
 * <code>Fiber</code>.
 * </p>
 
 */
public interface PausableFiber extends Fiber {
    /**
     * This state is used to define when a <code>Fiber</code> has begun the
     * process of pausing its operations. This is the intermedate period where
     * the thread is no longer in the <code>RUNNING</code> status, but not yet
     * to a <code>PAUSED</code> status.
     */
    public static final int PAUSE_PENDING = 5;

    /**
     * This state is used to denote a paused, or otherwise suspended
     * <code>Fiber</code>. When a <code>Fiber</code> is in this state it
     * should not be preforming any work.
     */
    public static final int PAUSED = 6;

    /**
     * This state is used to denote a <code>Fiber</code> recovering from a
     * paused state to a running status. During this status the
     * <code>Fiber</code> is reinitializing any necessary internal elements to
     * re-enter the <code>RUNNING</code> state.
     */
    public static final int RESUME_PENDING = 7;

    /**
     * This method is used to suspend a currently running <code>Fiber<code>.
     * When invoked the <code>Fiber</code> will begin the transition to
     * a <code>PAUSED</code> status after changing its internal state, if 
     * applicable.
     */
    public void pause();

    /**
     * This method is used to resume a suspeneded <code>Fiber</code>. If the
     * thread is already running then this method should have no effect on the
     * current <code>Fiber</code>.
     */
    public void resume();
}
