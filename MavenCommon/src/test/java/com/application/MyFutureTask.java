package com.application;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/8/4   18:34
 */
public class MyFutureTask<V> implements RunnableFuture<V> {

    /**
     * 多线程共享的状态码
     */
    private volatile int state;

    /**
     * 状态转换过程有：
     * NEW -> COMPLETING -> NORMAL
     * NEW -> COMPLETING -> EXCEPTIONAL
     * NEW -> CANCELLED
     * NEW -> INTERRUPTING -> INTERRUPTED
     */
    private static final int NEW = 0;

    private static final int COMPLETING = 1;

    private static final int NORMAL = 2;

    private static final int EXCEPTIONAL = 3;

    private static final int CANCELLED = 4;

    private static final int INTERRUPTING = 5;

    private static final int INTERRUPTED = 6;

    /**
     * 底层的可调用的；运行后被清空
     */
    private Callable<V> callable;

    /**
     * 运行可调用的线程
     */
    private Object outcome;

    /**
     * 正在运行的线程
     */
    private volatile Thread runner;

    /**
     * 等待的线程链
     */
    private volatile WaitNode waiters;


    public MyFutureTask(Callable<V> callable) {
        if (callable == null) {
            throw new NullPointerException();
        }
        this.callable = callable;
        // 确保可见性
        this.state = NEW;
    }

    public MyFutureTask(Runnable runnable, V result) {
        this.callable = Executors.callable(runnable, result);
        this.state = NEW;
    }

    private V report(int s) throws ExecutionException {
        // 返回的结果
        Object x = outcome;
        // 如果状态正常则返回正确的结果
        if (s == NORMAL)
            return (V) x;
        // 如果状态为CANCELED,INTERRUPTING,INTERRUPTED则跑出异常
        if (s >= CANCELLED)
            throw new CancellationException();
        // 否则就跑出运行时异常COMPLETING,EXCEPTIONAL
        throw new ExecutionException((Throwable) x);
    }

    @Override
    public void run() {
        // 如果当前线程不是新创建状态或者不能讲当前线程设置成功则直接返回
        if (state != NEW ||
                !UNSAFE.compareAndSwapObject(this, runnerOffset,
                        null, Thread.currentThread()))
            return;
        try {
            // 真正的线程调用方法
            Callable<V> c = callable;
            // 如果线程为新创建且调用主体存在则
            if (state == NEW && c != null) {
                V result;
                boolean ran;
                try {
                    // 调用主体方法
                    result = c.call();
                    // 设置当前线程为正在运行
                    ran = true;
                } catch (Throwable ex) {
                    // 如果抛出异常则返回结果为null
                    result = null;
                    // 同时设置运行时标志位false
                    ran = false;
                    // 将异常作为结果设置在当前线程中的返回结果集中
                    setException(ex);
                }
                // 如果运行正常则返回正确的结果集
                if (ran)
                    set(result);
            }
        } finally {
            // 将当前线程执行变量设置为弱引用
            runner = null;
            int s = state;
            // 如果当前线程状态为中断中则等待
            if (s >= INTERRUPTING)
                handlePossibleCancellationInterrupt(s);
        }
    }

    /**
     * 此方法多用于计算多次任务而没有返回值的场景
     *
     * @return
     */
    protected boolean runAndReset() {
        // 如果状态不是NEW 或者无法将当前线程设置进入实体中则直接返回false
        if (state != NEW ||
                !UNSAFE.compareAndSwapObject(this, runnerOffset,
                        null, Thread.currentThread()))
            return false;
        boolean ran = false;
        int s = state;
        try {
            Callable<V> c = callable;
            if (c != null && s == NEW) {
                try {
                    c.call(); // don't set result
                    ran = true;
                } catch (Throwable ex) {
                    setException(ex);
                }
            }
        } finally {
            // runner must be non-null until state is settled to
            // prevent concurrent calls to run()
            runner = null;
            // state must be re-read after nulling runner to prevent
            // leaked interrupts
            s = state;
            if (s >= INTERRUPTING)
                handlePossibleCancellationInterrupt(s);
        }
        // 如果正在运行中且状态为NEW 则返回true
        return ran && s == NEW;
    }

    private void handlePossibleCancellationInterrupt(int s) {
        // It is possible for our interrupter to stall before getting a
        // chance to interrupt us.  Let's spin-wait patiently.
        if (s == INTERRUPTING)
            while (state == INTERRUPTING)
                Thread.yield(); // wait out pending interrupt

        // assert state == INTERRUPTED;

        // We want to clear any interrupt we may have received from
        // cancel(true).  However, it is permissible to use interrupts
        // as an independent mechanism for a task to communicate with
        // its caller, and there is no way to clear only the
        // cancellation interrupt.
        //
        // Thread.interrupted();
    }

    protected void set(V v) {
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            outcome = v;
            UNSAFE.putOrderedInt(this, stateOffset, NORMAL); // final state
            finishCompletion();
        }
    }

    protected void setException(Throwable t) {
        // 预期当前线程是NEW 状态同时又能从切换到运行状态则将当前结果设置为异常,并清除所有等待的线程,并将当前线程设置为异常状态
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            outcome = t;
            UNSAFE.putOrderedInt(this, stateOffset, EXCEPTIONAL); // final state
            finishCompletion();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        // 如果线程的状态是新建状态,并且从预期的新建状态更改为中断中或已取消状态,则进入下一个流程
        // 否则就返回false表示取消操作失败
        if (!(state == NEW && UNSAFE.compareAndSwapInt(this, stateOffset, NEW,
                mayInterruptIfRunning ? INTERRUPTING : CANCELLED))) {
            return false;
        }
        // 针对中断中的线程处理
        try {
            if (mayInterruptIfRunning) {
                try {
                    Thread t = runner;
                    if (t != null) {
                        t.interrupt();
                    }
                } catch (Exception e) {
                    // 最终将该线程状态改为已中断
                    UNSAFE.putOrderedInt(this, stateOffset, INTERRUPTED);
                }
            }
        } finally {
            finishCompletion();
        }
        return true;
    }

    private void finishCompletion() {
        // TODO 将所有等待的线程唤醒并清除引用
        for (WaitNode q; (q = waiters) != null; ) {
            // 如果能将当前对象的线程等待链属性的偏移量设置为null
            if (UNSAFE.compareAndSwapObject(this, waitersOffset, q, null)) {
                for (; ; ) {

                    // TODO 清除线程引用并唤醒线程
                    Thread t = q.thread;
                    if (t != null) {
                        // 唤醒对象
                        LockSupport.unpark(t);
                        // 清除当前线程的引用
                        q.thread = null;
                    }

                    // TODO 清除链表的下一个节点
                    WaitNode next = q.next;
                    if (next == null) {
                        break;
                    }
                    q = next;
                }
                break;// 跳出内层循坏等待waitNode值被修改为null
            }
        }

        // TODO 执行done方法
        done();

        // TODO 将callable 设置为null
        callable = null; // 释放空间
    }

    protected void done() {

    }

    /**
     * 判断是否可以取消(状态只要是NEW,COMPLETING,CANCELLED就能取消线程)
     *
     * @return true表示可以取消
     */
    @Override
    public boolean isCancelled() {
        return state >= CANCELLED;
    }

    /**
     * 判断是否在运行(只要不是新建状态的都表示在执行)
     *
     * @return true表示在运行
     */
    @Override
    public boolean isDone() {
        return state != NEW;
    }


    @Override
    public V get() throws InterruptedException, ExecutionException {
        int s = state;
        if (s <= COMPLETING)
            s = awaitDone(false, 0L);
        return report(s);
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (unit == null)
            throw new NullPointerException();
        int s = state;
        // 如果线程在单位时间内仍然处于NEW或者运行时并且等待了timeout的时间且还处于运行时则抛出超时异常
        if (s <= COMPLETING &&
                (s = awaitDone(true, unit.toNanos(timeout))) <= COMPLETING)
            throw new TimeoutException();
        return report(s);
    }

    private int awaitDone(boolean timed, long nanos)
            throws InterruptedException {
        // 如果使用时间限制,则返回当前系统的原子时间并加上锁给的纳秒数
        final long deadline = timed ? System.nanoTime() + nanos : 0L;
        MyFutureTask.WaitNode q = null;
        boolean queued = false;
        // 自旋
        for (; ; ) {
            // 如果当前调用线程成功被中断
            if (Thread.interrupted()) {
                // 删除线程等待链,同时抛出中断异常
                removeWaiter(q);
                throw new InterruptedException();
            }

            int s = state;
            // 如果当前线程不是NEW 状态则
            if (s > COMPLETING) {
                // 如果等待线程不为null则将其下的线程设置为null,然后返回状态
                if (q != null)
                    q.thread = null;
                return s;
                // 如果当前线程正在运行则再一次调用调度管理器，重新争取线程优先执行权
            } else if (s == COMPLETING) // cannot time out yet
                Thread.yield();
                // 如果当前调用链为null则创建一个新的调用链
            else if (q == null)
                q = new MyFutureTask.WaitNode();
                // 如果队列尚未满则将当前调用链的下一位也设置为当前调用链
            else if (!queued)
                queued = UNSAFE.compareAndSwapObject(this, waitersOffset,
                        q.next = waiters, q);
            else if (timed) {
                // 如果线程执行的时间在设定的纳秒之内则返回当前线程状态
                nanos = deadline - System.nanoTime();
                if (nanos <= 0L) {
                    removeWaiter(q);
                    return state;
                }
                // 否则多少纳秒后阻塞当前线程
                LockSupport.parkNanos(this, nanos);
            } else
                // 直接阻塞
                LockSupport.park(this);
        }
    }

    private void removeWaiter(MyFutureTask.WaitNode node) {
        if (node != null) {
            node.thread = null;
            retry:
            for (; ; ) {          // restart on removeWaiter race
                for (MyFutureTask.WaitNode pred = null, q = waiters, s; q != null; q = s) {
                    s = q.next;
                    if (q.thread != null)
                        pred = q;
                    else if (pred != null) {
                        pred.next = s;
                        if (pred.thread == null) // check for race
                            continue retry;
                    } else if (!UNSAFE.compareAndSwapObject(this, waitersOffset,
                            q, s))
                        continue retry;
                }
                break;
            }
        }
    }

    static final class WaitNode {
        volatile Thread thread;
        volatile WaitNode next;

        WaitNode() {
            thread = Thread.currentThread();
        }
    }

    //  内存操作
    private static final sun.misc.Unsafe UNSAFE;
    private static final long stateOffset;
    private static final long runnerOffset;
    private static final long waitersOffset;

    static {
        try {
            // 获取内存操作器
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = FutureTask.class;
            // 利用反射加内存操作器获取属性的内存地址偏移量
            stateOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("state"));
            runnerOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("runner"));
            waitersOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("waiters"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
