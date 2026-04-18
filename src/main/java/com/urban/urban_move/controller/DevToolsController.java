package com.urban.urban_move.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles Chrome DevTools and other browser-specific requests
 * that may not have corresponding resources in the application.
 * This prevents NoResourceFoundException errors in logs.
 */
@RestController
@RequestMapping("/.well-known")
public class DevToolsController {

    /**
     * Handle Chrome DevTools appspecific manifest request
     * Returns empty content with 204 No Content status
     */
    @GetMapping("/appspecific/com.chrome.devtools.json")
    public ResponseEntity<Void> chromeDevToolsManifest() {
        return ResponseEntity.noContent().build();
    }

    /**
     * Handle generic .well-known requests
     * Returns empty content with 204 No Content status
     */
    @GetMapping("/**")
    public ResponseEntity<Void> wellKnownFallback() {
        return ResponseEntity.noContent().build();
    }
}
