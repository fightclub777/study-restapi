package net.gongple.restapi.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockBean
	EventRepository eventRepository;
	
	@Test
	public void createEvent() throws Exception {
		Event event = Event.builder()
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
		event.setId(10);
		
		Mockito.when(eventRepository.save(event)).thenReturn(event);
		// Mockito? 뭐하는 놈이냐?
		
		mockMvc.perform(post("/api/events/")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaTypes.HAL_JSON)
					.content(objectMapper.writeValueAsString(event)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").exists())
				;
	}
	
}
