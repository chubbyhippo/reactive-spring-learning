package com.example.webflux.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SseController {
	@GetMapping("/sse.php")
	String initialView() {
		return "sse";
	}
}
