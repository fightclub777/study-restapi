package net.gongple.restapi.events;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class EventResource extends Resource<Event> {

	public EventResource(Event event, Link... links) {
		super(event, links);
		add(linkTo(EventController.class).slash(event.getId()).withSelfRel()); // Self Rel 추가.
//		add(new Link("http://localhost:8080/api/events/"+ event.getId())); //위 줄은 이와같이 표현할 수도 있다. 하지만 윗줄이 타입 세이프한 코드.
	}
}
 