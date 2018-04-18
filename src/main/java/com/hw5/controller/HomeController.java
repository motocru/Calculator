package com.hw5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/calculator")
public class HomeController {
	@RequestMapping(method = RequestMethod.GET)
	public String Index() {
		return "index.html";
	}
}