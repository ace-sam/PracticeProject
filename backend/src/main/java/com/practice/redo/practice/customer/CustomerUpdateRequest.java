package com.practice.redo.practice.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        int age
) { }
