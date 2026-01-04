package com.example.connection_pool.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.connection_pool.dto.DataRow;
import com.example.connection_pool.service.TestService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class MainController {
        private final TestService testService;

    // MAIN_POOL로 SELECT
    @GetMapping("/data/{id}")
    public ResponseEntity<DataRow> getData(@PathVariable long id) {
        DataRow row = testService.fetchData(id);
        return row == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(row);
    }

    // LOG_POOL로 INSERT
    @PostMapping("/log")
    public ResponseEntity<String> writeLog(@RequestParam String message) {
        int inserted = testService.writeLog(message);
        return ResponseEntity.ok("inserted=" + inserted);
    }

    // MAIN_POOL SELECT + LOG_POOL INSERT
    @PostMapping("/data/{id}/fetch-and-log")
    public ResponseEntity<DataRow> fetchAndLog(@PathVariable long id) {
        DataRow row = testService.fetchAndLog(id);
        return row == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(row);
    }
    
}
