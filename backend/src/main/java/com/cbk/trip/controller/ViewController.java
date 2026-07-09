package com.cbk.trip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author sansintkyaw
 *
 */
@Controller
public class ViewController {

	/**
	 * redirect for frontend
	 * 
	 * @return
	 */
	@RequestMapping(value = { "{_:^(?!index\\.html|api|.*\\.svg$).*$}", "/{path:^(?!api).*}/{path:[^\\.]*}",
			"/{path:^(?!api).*}/**/{path:[^\\.]*}", "/{path:^(?!api).*}/**/**/{path:[^\\.]*}",
			"/{path:^(?!api).*}/**/**/**/{path:[^\\.]*}" })
	public String redirectPublic() {
		return "forward:/";
	}

}
