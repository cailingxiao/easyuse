package com.cai.easyuse.base;

import com.cai.easyuse.util.MainThreadUtils;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基本的模型层类，进行业务逻辑处理，提供给controller调用 这里面的线程使用的一个自定义实现,{@link #getThreadPool}可以修改，
 * 使用异步回调
 *
 * @author cailingxiao
 */
public abstract class BasePresenter implements IPresenter {
    private static final String TAG = "BuiPresenter";
    private static final boolean DEBUG = true;
    // 核心线程数等于核心数的个数+1，核心线程数指线程池中长存的线程（#allowCoreThreadTimeOut设置true后变成普通线程）
    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
    // 考虑到网络情况，一般2个同时下载
    private static final int CORE_POOL_SIZE_DOWNLOAD = 2;
    // 线程池最大的容量=核心数+普通数,当等待队列满了后，启用
    private static final int MAX_POOL_SIZE = CORE_POOL_SIZE * 2;
    // 考虑到网络情况，一般最多5个同时下载
    private static final int MAX_POOL_SIZE_DOWNLOAD = 5;
    // 普通线程完成任务后最大的停留时间，1分钟
    private static final int KEEP_ALIVE_TIME = 60;
    // 最大的任务等待队列长度,指定为池的最大容量的十倍
    private static final int MAX_BLOCKING_QUEUE_SIZE = MAX_POOL_SIZE * 10;
    // 下载任务可能较多，所以设置较大的体量
    private static final int MAX_BLOCKING_QUEUE_SIZE_DOWNLOAD = 200;
    // 锁，并发处理线程池初始化
    private static Object sLocked = new Object();
    /**
     * 通用任务线程池,并发执行
     */
    private static ExecutorService sExecutorService = null;

    /**
     * 顺序线程执行池,1个1个执行
     */
    private static ExecutorService sSingleExcutorService = null;

    /**
     * 文件下载线程池
     */
    private static ExecutorService sDownloadingExcutorService = null;

    /**
     * cached executor
     */
    private static ExecutorService sCachedExecutorService = null;

    /**
     * 定时循环任务
     */
    private static ScheduledExecutorService sScheduleExecutor = null;

    /**
     * 前台任务，伴随页面消失而停止任务的.调用release时取消
     */
    private List<Future> mFrontTasks = new CopyOnWriteArrayList<Future>();

    public BasePresenter() {

    }

    /**
     * 返回bui的实现
     *
     * @return
     */
    public static ThreadPoolExecutor getDefault() {
        // 超出的任务将被丢弃
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(MAX_BLOCKING_QUEUE_SIZE), new CpuThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    /**
     * 取消执行的任务
     *
     * @param future
     */
    public static void cancel(Future<?> future) {
        if (null == future) {
            return;
        }
        try {
            if (!future.isDone()) {
                future.cancel(true);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 关闭线程池，建议在程序退出时使用
     */
    public static void shutDown() {
        if (null != sExecutorService && !sExecutorService.isShutdown()) {
            sExecutorService.shutdown();
        }
        if (null != sSingleExcutorService && !sSingleExcutorService.isShutdown()) {
            sSingleExcutorService.shutdown();
        }
        if (null != sDownloadingExcutorService && !sDownloadingExcutorService.isShutdown()) {
            sDownloadingExcutorService.shutdown();
        }
        if (null != sCachedExecutorService && !sCachedExecutorService.isShutdown()) {
            sCachedExecutorService.shutdown();
        }
    }

    /**
     * 初始化线程池
     */
//    public static void initThreadPool() {
//        initTaskPool();
//        initSinglePool();
//        initDownloadPool();
//        initMaxTaskPool();
//        initSchedulePool();
//
//    }
    private static void initSchedulePool() {
        if (null == sScheduleExecutor) {
            synchronized (sLocked) {
                if (null == sScheduleExecutor) {
                    sScheduleExecutor = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
                }
            }
        }
    }

    private static void initMaxTaskPool() {
        if (null == sCachedExecutorService) {
            synchronized (sLocked) {
                if (null == sCachedExecutorService) {
                    sCachedExecutorService = Executors.newCachedThreadPool();
                }
            }
        }
    }

    private static void initDownloadPool() {
        if (null == sDownloadingExcutorService) {
            synchronized (sLocked) {
                if (null == sDownloadingExcutorService) {
                    sDownloadingExcutorService = new ThreadPoolExecutor(CORE_POOL_SIZE_DOWNLOAD,
                            MAX_POOL_SIZE_DOWNLOAD, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>(MAX_BLOCKING_QUEUE_SIZE_DOWNLOAD),
                            new IoThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());
                }
            }
        }
    }

    private static void initSinglePool() {
        if (null == sSingleExcutorService) {
            synchronized (sLocked) {
                if (null == sSingleExcutorService) {
                    sSingleExcutorService = Executors.newSingleThreadExecutor();
                }
            }
        }
    }

    private static void initTaskPool() {
        if (null == sExecutorService) {
            synchronized (sLocked) {
                if (null == sExecutorService) {
                    sExecutorService = getDefault();
                }
            }
        }
    }

    public static ExecutorService getExecutorService() {
        initTaskPool();
        return sExecutorService;
    }

    public static ExecutorService getSingleExcutorService() {
        initSinglePool();
        return sSingleExcutorService;
    }

    public static ExecutorService getDownloadingExcutorService() {
        initDownloadPool();
        return sDownloadingExcutorService;
    }

    public static ExecutorService getCachedExecutorService() {
        initMaxTaskPool();
        return sCachedExecutorService;
    }

    public static ScheduledExecutorService getScheduleExecutor() {
        initSchedulePool();
        return sScheduleExecutor;
    }

    /**
     * 下载任务专用线程
     *
     * @param task
     */
    protected void executeDownloadTask(Runnable task) {
        initDownloadPool();
        sDownloadingExcutorService.execute(task);
    }

    /**
     * 重复任务。在任务开始的同时开始下一次任务的间隔计时
     *
     * @param task
     * @param millSchedule 毫秒
     * @return
     */
    protected Future scheduleTask(Runnable task, long millSchedule) {
        return getScheduleExecutor().scheduleAtFixedRate(task, 0, millSchedule, TimeUnit
                .MILLISECONDS);
    }

    /**
     * 重复任务。在任务结束后开始下一次任务的间隔计时
     *
     * @param task
     * @param millSchedule
     * @return
     */
    protected Future scheduleWithTask(Runnable task, long millSchedule) {
        return getScheduleExecutor().scheduleWithFixedDelay(task, 0, millSchedule, TimeUnit
                .MILLISECONDS);
    }

    /**
     * 下载任务专用线程
     *
     * @param runnable
     * @return
     */
    protected Future<?> submitDownloadTask(Runnable runnable) {
        initDownloadPool();
        return sDownloadingExcutorService.submit(runnable);
    }

    /**
     * 新线程任务，对响应要求高的线程使用。不推荐大量使用
     *
     * @param runnable
     */
    protected void quickThreadTask(Runnable runnable) {
        initMaxTaskPool();
        getCachedExecutorService().execute(runnable);
    }

    /**
     * 顺序执行
     *
     * @param runnable
     */
    protected void executeInQueue(Runnable runnable) {
        initSinglePool();
        sSingleExcutorService.execute(runnable);
    }

    /**
     * 延时任务顺序执行
     *
     * @param runnable
     * @param delayedMillions
     */
    protected void executeDelayedInQueue(final Runnable runnable, long delayedMillions) {
        MainThreadUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                executeInQueue(runnable);
            }
        }, delayedMillions);
    }

    /**
     * 顺序执行任务提交
     *
     * @param runnable
     * @return
     */
    protected Future<?> submitInQueue(Runnable runnable) {
        return submitInQueue(runnable, false);
    }

    /**
     * 顺序执行任务提交,使用第二个参数表示页面退出，任务是否停止
     *
     * @param runnable
     * @param isFrontTask true表示停止
     * @return
     */
    protected Future<?> submitInQueue(Runnable runnable, boolean isFrontTask) {
        initSinglePool();
        Future future = sSingleExcutorService.submit(runnable);
        if (isFrontTask) {
            mFrontTasks.add(future);
        }
        return future;
    }

    /**
     * 顺序执行任务提交
     *
     * @param callable
     * @return
     */
    protected Future<?> submitInQueue(Callable callable) {
        initSinglePool();
        return sSingleExcutorService.submit(callable);
    }

    /**
     * 执行任务于线程池中
     *
     * @param runnable
     */
    protected void execute(Runnable runnable) {
        initTaskPool();
        sExecutorService.execute(runnable);
    }

    /**
     * 延时执行任务
     *
     * @param runnable
     * @param delayed
     */
    protected void executeDelayed(final Runnable runnable, long delayed) {
        MainThreadUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                execute(runnable);
            }
        }, delayed);
    }

    /**
     * 执行任务于主线程中
     *
     * @param runnable
     */
    public void executeInMainThread(Runnable runnable) {
        MainThreadUtils.post(runnable);
    }

    /**
     * 延时发送一个任务到主线程中
     *
     * @param runnable
     * @param millions
     */
    public void executeInMainThreadDelayed(Runnable runnable, long millions) {
        MainThreadUtils.postDelayed(runnable, millions);
    }

    /**
     * 取消运行
     *
     * @param runnable
     */
    public void cancelRunnable(Runnable runnable) {
        MainThreadUtils.cancel(runnable);
    }

    /**
     * 提交一个任务执行，返回一个future
     *
     * @param runnable
     * @return
     */
    protected Future<?> submit(Runnable runnable) {
        return submit(runnable, false);
    }

    /**
     * 提交一个任务执行，返回一个future。使用第二个参数表示页面退出，任务是否停止
     *
     * @param runnable
     * @param isFrontTask true表示停止
     * @return
     */
    protected Future<?> submit(Runnable runnable, boolean isFrontTask) {
        initTaskPool();
        Future future = sExecutorService.submit(runnable);
        if (isFrontTask) {
            mFrontTasks.add(future);
        }
        return future;
    }

    /**
     * 提交一个callable
     *
     * @param callable
     * @return
     */
    protected <V> Future<V> submit(Callable<V> callable) {
        initTaskPool();
        return sExecutorService.submit(callable);
    }

    @Override
    public void release() {
        if (0 < mFrontTasks.size()) {
            for (Future future : mFrontTasks) {
                cancel(future);
            }
            mFrontTasks.clear();
        }
    }

    /**
     * 基本
     */
    static abstract class BaseThreadFactory implements ThreadFactory {
        protected static final AtomicInteger poolNumber = new AtomicInteger(1);
        protected final ThreadGroup group;
        protected final AtomicInteger threadNumber = new AtomicInteger(1);
        protected String namePrefix;

        public BaseThreadFactory(String namePrefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = namePrefix + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            int priority = getThreadPriority();
            if (t.getPriority() != priority) {
                t.setPriority(priority);
            }
            return t;
        }

        protected abstract int getThreadPriority();
    }

    /**
     * cpu线程工厂实现
     *
     * @author cailingxiao
     * @date 2016年2月29日
     */
    static class CpuThreadFactory extends BaseThreadFactory {

        public CpuThreadFactory() {
            super("cpuWorkThread");
        }


        @Override
        protected int getThreadPriority() {
            return 3;
        }
    }

    /**
     * io线程工厂实现
     */
    static class IoThreadFactory extends BaseThreadFactory {

        public IoThreadFactory() {
            super("ioWorkThread");
        }

        @Override
        protected int getThreadPriority() {
            return 5;
        }
    }

    /**
     * 通用线程工厂实现
     */
    static class DefThreadFactory extends BaseThreadFactory {

        public DefThreadFactory() {
            super("normalThread");
        }

        @Override
        protected int getThreadPriority() {
            return Thread.NORM_PRIORITY;
        }
    }

}
