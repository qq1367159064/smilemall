package cn.smile.smilemall.search.thread;

import java.util.concurrent.*;

/**
 * <p>线程测试</p>
 *
 * @author smile
 * @date 2021-02-21
 */
public class ThreadTest {
	static ExecutorService executorService = Executors.newFixedThreadPool(10);
	
	public static void mains(String[] args) {
		// 线程池
		// 构建一个固定线程数的线程池
		// 一个系统线程池只有一个或者两个,每个异步任务都提交个线程池
		// submit() 给线程添加异步任务,这个可以有返回值
		//
		executorService.submit(() -> {
			System.out.println("hello submit");
		});
		//submit() 给线程添加异步任务,这个没有返回值
		executorService.execute(() -> {
			System.out.println("hello execute");
		});
		// 原生线程池
		/**
		 * 线程七大参数
		 * int corePoolSize: 核心线程数,线程池，创建好以后就准备就绪的线程数量,就等待来接收任务执行， 只有线程池销毁就会一直纯在,除非设置了线程超时
		 * int maximumPoolSize: 线程池中允许的最大线程数量
		 * long keepAliveTime: 存活时间，如果当前线程数量大于核心数量的时候。如果线程空闲的线程，空闲时间大于这个设定的时间就会销毁线程，但是核心线程是不会被销毁
		 * TimeUnit unit: 指定时间单位
		 * BlockingQueue<Runnable> workQueue：阻塞队列,如果任务过多就会把多的任务放在队列中,等待空闲线程处理
		 * threadFactory: 线程的创建工厂
		 * RejectedExecutionHandler handler: 如果队列满了，不能够在保存任务的时候，按照我们自己定义的处理策略来处理
		 *
		 * 工作顺序:
		 *    - 线程池创建，准备好core数量核心线程，准备接收任务
		 *    - 如果核心线程么有空闲的时候，就会把任务放到阻塞队列中
		 *    - 如果阻塞队列都满了，就会开启新的线程，但是不会超过自己定义的最大线程数量
		 *    - 如果创建的线程数量也达到了最大线程数量,就会使用自己定义的拒绝策略来处理,如果没有满，等到线程的空闲时间达到，自己指定的最大空闲时间
		 *    的时候就会进行线程销毁
		 *
		 *
		 */
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
				10,
				200,
				10,
				TimeUnit.SECONDS,
				new LinkedBlockingDeque<>(1000),
				Executors.defaultThreadFactory(),
				new ThreadPoolExecutor.AbortPolicy());
		
		
	}
	
	/**
	 * CompletableFuture 异步编排
	 * Future: 可以获取异步结果
	 */
	public static void mainb(String[] args) throws ExecutionException, InterruptedException {
		// 指定线程运行
		System.out.println("main...start");
		
		/*CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
			System.out.println("单前线程" + Thread.currentThread().getName());
			int i = 10 / 2;
			System.out.println("运行结果" + i);
		}, executorService);*/
		/*CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
			System.out.println("单前线程" + Thread.currentThread().getName());
			int i = 10 / 2;
			System.out.println("运行结果" + i);
			return i;
		}, executorService).whenComplete((res, err) -> {
			// 这个方法 whenComplete 可以感知异常，但是不能处理
			System.out.println("异步任务执行成功....." + res + "异常..." + err);
		}).exceptionally(err -> {
			// 这个exceptionally可以感知异常也可以是处理异常，同时返回一个默认值，返回值的类型需要看CompletableFuture接收的泛型
			return 1;
		});*/
		
		/**
		 * 方法完成后的处理
		 */
		CompletableFuture<Integer> handleFuture = CompletableFuture.supplyAsync(() -> {
			System.out.println("单前线程" + Thread.currentThread().getName());
			int i = 10 / 2;
			System.out.println("运行结果" + i);
			return i;
		}, executorService).handle((res, err) -> {
			if(res != null) {
				return  res * 2;
			}
			if(err != null) {
				return 0;
			}
			return 0;
		});
		System.out.println(handleFuture.get());
		
		System.out.println("main....end");
	}
	
	/**
	 * CompletableFuture 异步编排
	 * Future: 可以获取异步结果
	 */
	public static void mainc(String[] args) {
		/**
		 * thenApply: 当一个线程依赖另一个线程时，获取上一个任务返回的结果，并返回当前任务的返回值
		 * thenAccept； 消费处理结果，接收任务的处理结果，并消费处理，没有返回结果
		 * thenRun: 只要上面的任务执行完成，就开始执行thenRun，只是处理完任务后，执行thenRun的后续操作
		 */
		/*CompletableFuture<Void> future1 = CompletableFuture.supplyAsync(() -> {
			System.out.println("单前线程" + Thread.currentThread().getName());
			int i = 10 / 2;
			System.out.println("运行结果" + i);
			return i;
		}, executorService).thenRunAsync(() -> {
			System.out.println("任务2启动了");
		}, executorService);*/
		// thenAcceptAsync可以使用的上一次任务的返回值，但是自身没有返回值
		/*CompletableFuture<Void> future1 = CompletableFuture.supplyAsync(() -> {
			System.out.println("单前线程" + Thread.currentThread().getName());
			int i = 10 / 2;
			System.out.println("运行结果" + i);
			return i;
		}, executorService).thenAcceptAsync(res -> {
			System.out.println("任务2启动了" + res);
		}, executorService);*/
		// thenApplyAsync可以感知到上一次线程的返回值，自身也会有返回值
		CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
			System.out.println("单前线程" + Thread.currentThread().getName());
			int i = 10 / 2;
			System.out.println("运行结果" + i);
			return i;
		}, executorService).thenApplyAsync((res) -> {
			return res * 2;
		}, executorService);
	}
	
	
	/**
	 * <p>两个任务组合</p>
	 * @author smile
	 * @date 2021/2/21/021
	 * @param args 1
	 * @return void
	 */
	public static void main(String[] args) {
		CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(() -> {
			int i = 10 / 2;
			System.out.println("运行结果" + i);
			return i;
		}, executorService);
		CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
			System.out.println("运行结果2" + "hello");
			return "hello";
		}, executorService);
		
		task1.runAfterBothAsync(task2, () -> {
			System.out.println("任务三开启");
		},executorService);
		
	}
	
	public static class Thread01 extends Thread {
		@Override
		public void run() {
			System.out.println("单前线程" + Thread.currentThread().getName());
			int i = 10 / 2;
			System.out.println("运行结果" + i);
		}
	}
	
	public static class Runnable01 implements Runnable {
		@Override
		public void run() {
			System.out.println("单前线程" + Thread.currentThread().getName());
			int i = 10 / 2;
			System.out.println("运行结果" + i);
		}
	}
	
	/**
	 * <p>Callable可以有返回值</p>
	 *
	 * @author smile
	 * @date 2021/2/21/021
	 * @return
	 */
	public static class Callable01 implements Callable<String> {
		@Override
		public String call() throws Exception {
			return "你好";
		}
	}
}
