package com.chipizz.Learning.model;

public interface Model {
    Runnable newRunnableConsumer();
    Runnable newRunnableProducer();
}
