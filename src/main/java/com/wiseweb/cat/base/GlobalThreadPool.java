package com.wiseweb.cat.base;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public enum GlobalThreadPool {

	instance;
	private final ExecutorService service = Executors.newFixedThreadPool(10);

	public <T> Future<T> submit(Callable<T> task) {
		return service.submit(task);
	}

}
