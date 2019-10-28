package net.gongple.restapi.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
//	@MockBean
//	EventRepository eventRepository;
	
	@Test
	public void createEvent() throws Exception {
		EventDto eventDto = EventDto.builder()
				.name("Spring")
				.description("Rest Api Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2019, 10, 21, 9, 30))
				.closeEnrollmentDateTime(LocalDateTime.of(2019, 10, 21, 9, 30))
				.beginEventDateTime(LocalDateTime.of(2019, 10, 31, 9, 30))
				.endEventDateTime(LocalDateTime.of(2019, 10, 31, 9, 30))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남 D2 스타트업 팩토리")
				.build();
//		Mockito.when(eventRepository.save(event)).thenReturn(event);
		// Mockito? 뭐하는 놈이냐?
		
		this.mockMvc.perform(post("/api/events/")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaTypes.HAL_JSON)
						.content(objectMapper.writeValueAsString(eventDto)))
					.andDo(print())
					.andExpect(status().isCreated())
					.andExpect(jsonPath("id").exists())
					.andExpect(header().exists(HttpHeaders.LOCATION))
					.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
					.andExpect(jsonPath("id").value(Matchers.not(100)))
					.andExpect(jsonPath("free").value(Matchers.not(true)))
					.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
					;
	}
	
	
	@Test
	public void createEvent_BadRequest() throws Exception {
		Event event = Event.builder()
				.id(100)
				.name("Spring")
				.description("Rest Api Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2019, 10, 21, 9, 30))
				.closeEnrollmentDateTime(LocalDateTime.of(2019, 10, 21, 9, 30))
				.beginEventDateTime(LocalDateTime.of(2019, 10, 31, 9, 30))
				.endEventDateTime(LocalDateTime.of(2019, 10, 31, 9, 30))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남 D2 스타트업 팩토리")
				.free(true)
				.offline(false)
				.eventStatus(EventStatus.PUBLISHED)
				.build();
		
		this.mockMvc.perform(post("/api/events/")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaTypes.HAL_JSON)
						.content(objectMapper.writeValueAsString(event)))
					.andDo(print())
					.andExpect(status().isBadRequest())
					;
	}
	
	@Test
	public void createEvent_BadRequest_EmptyInput() throws Exception {
		EventDto eventDto = EventDto.builder().build();
		
		this.mockMvc.perform(post("/api/events/")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(this.objectMapper.writeValueAsString(eventDto)))
					.andExpect(status().isBadRequest())
					;
	}
}
