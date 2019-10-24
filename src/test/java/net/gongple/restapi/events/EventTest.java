package net.gongple.restapi.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class EventTest {

	@Test
	public void builder() {
		Event event = Event.builder()
				.name("Gongple Spring Rest Api")
				.description("RestApi development with Spring.")
				.build();
		assertThat(event).isNotNull();
	}
	
	@Test
	public void javaBean() {
		// Given
		String name = "Event";
		String description = "Spring";
		
		// When
		Event event = new Event();
		event.setName(name);
		event.setDescription(description);
		
		// Then
		assertThat(event.getName()).isEqualTo(name);
		assertThat(event.getDescription()).isEqualTo(description);
	}
	
}
