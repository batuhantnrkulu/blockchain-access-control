package com.blockchain.accesscontrol.access_control_system.utils;

import java.math.BigInteger;

public class AtomicBigInteger {
    private BigInteger value;

    public AtomicBigInteger(BigInteger initialValue) {
        this.value = initialValue;
    }

    public synchronized BigInteger get() {
        return value;
    }

    public synchronized BigInteger getAndIncrement() {
        BigInteger current = value;
        value = value.add(BigInteger.ONE);
        return current;
    }

    public synchronized void set(BigInteger newValue) {
        this.value = newValue;
    }
}