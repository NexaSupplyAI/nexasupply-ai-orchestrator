package com.nexasupplyai.ai.exception;
public class FoundryUnavailableException extends RuntimeException {
    public FoundryUnavailableException() {
        super("Microsoft Foundry no está configurado; establezca FOUNDRY_ENABLED=true y sus parámetros Azure");
    }
}
