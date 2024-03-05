package com.practice.redo.practice.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        int age
) { }
