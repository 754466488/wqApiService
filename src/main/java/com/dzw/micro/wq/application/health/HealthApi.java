package com.dzw.micro.wq.application.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthApi {

	@GetMapping(value = "/api/health")
	public String health() {
		return "ok";
	}
}
