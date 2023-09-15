package dev.ljcaliwan.cmbackend.request;

public record CustomerUpdateRequest(String name, String email, Integer age) { }