package com.application.common;

import org.openjdk.jol.info.ClassLayout;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/7/24   21:23
 */
public class Main {
    public static void main(String[] args) {
        /*FutureTask<Integer> task = new FutureTask<>((Callable<Integer>) () -> {
            int i = 0;
            for (; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + " 的循环变量i的值：" + i);
            }
            Thread.sleep(10000);
            return i;
        });
        for (int i = 0; i < 100; i++) {
            System.out.println("主线程：" + Thread.currentThread().getName() + i);
            if (i == 20) {
                new Thread(task, "线程1").start();
            }
        }

        try {
            System.out.println(task.get());
        } catch (Exception e) {

        }
        System.out.println("主线程完成");*/
        Object o = new Object();

        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }
}
