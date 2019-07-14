package h.uniview.smarthouse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@RequestMapping("pages/{url}.html")
	public String page(@PathVariable("url") String url){
		return "pages/" + url + ".html";
	}
}

