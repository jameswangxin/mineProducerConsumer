package com.chipizz.Test;

import com.chipizz.Learning.consumer.AbstractConsumer;
import com.chipizz.Learning.consumer.Consumer;
import com.chipizz.Learning.model.Model;
import com.chipizz.Learning.producer.AbstractProducer;
import com.chipizz.Learning.producer.Producer;
import com.chipizz.Learning.task.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockingQueueModel implements Model {
    private final BlockingQueue<Task> queue;
    private final AtomicInteger increTaskNumber = new AtomicInteger(0);

    public BlockingQueueModel(int cap) {
        this.queue = new LinkedBlockingDeque<>(cap);
    }

    @Override
    public Runnable newRunnableConsumer() {
        return new ConsumerImpl();
    }

    @Override
    public Runnable newRunnableProducer() {
        return new ProducerImpl();
    }

    private class ConsumerImpl extends AbstractConsumer implements Consumer, Runnable {
        @Override
        public void consume() throws InterruptedException {
            Task task = queue.take();
            //固定时间范围内的消费，模拟相对稳定的服务器处理过程
            Thread.sleep(800 + (long)(Math.random()*500));
            System.out.println("consume: " + task.id);
        }
    }

    private class ProducerImpl extends AbstractProducer implements Producer, Runnable {
        @Override
        public void produce() throws InterruptedException {
            Thread.sleep((long) (Math.random() * 1000));
            Task task = new Task(increTaskNumber.getAndIncrement());
            System.out.println("produce: " + task.id);
            queue.put(task);
        }
    }

    public static void main(String[] args) {
        Model model = new BlockingQueueModel(3);
        for (int i = 0; i < 2; i++) {
            new Thread(model.newRunnableConsumer()).start();
        }
        for (int i = 0; i < 5; i++) {
            new Thread(model.newRunnableProducer()).start();
        }
    }
}
