package com.example.webflux.views;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.example.webflux.utils.IntervalMessageProducer;

@Controller
public class TickerSseController {

	@GetMapping("/ticker.php")
	String initialView() {
		return "ticker";
	}

	@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/ticker-stream")
	String streamingUpdates(Model model) {
		var producer = IntervalMessageProducer.produce();
		var updates = new ReactiveDataDriverContextVariable(producer, 1);
		model.addAttribute("updates", updates);
		return "ticker :: #updateBlock";
	}

}
