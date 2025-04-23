package se.autocorrect.springexample.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import se.autocorrect.springexample.model.MagicStuff;
import se.autocorrect.springexample.services.MagicService;

@Controller
public class MagicGraphQLController {
	
	private final MagicService magicService;
	
	public MagicGraphQLController(@Qualifier("magicService")  MagicService magicService) {
		this.magicService = magicService;
	}

	@QueryMapping
	public Optional<MagicStuff> magicByKey(@Argument("key") String key) {
		return magicService.getMagicStuffByKey(key);
	}
}
