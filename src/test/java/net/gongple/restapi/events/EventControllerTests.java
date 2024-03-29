package net.gongple.restapi.events;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.gongple.restapi.common.RestDocsConfiguration;
import net.gongple.restapi.common.TestDescription;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventControllerTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
//	@MockBean
//	EventRepository eventRepository;
	
	@Test
	@TestDescription("정상적으로 이벤트를 생성하는 테스트")
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
					.andExpect(jsonPath("free").value(false))
					.andExpect(jsonPath("offline").value(true))
					.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
					
					.andExpect(jsonPath("_links.self").exists())
					.andExpect(jsonPath("_links.query-events").exists())
					.andExpect(jsonPath("_links.update-event").exists())
					
					.andDo(document("create-event",
							links(
									linkWithRel("self").description("link to self"),
									linkWithRel("query-events").description("link to query"),
									linkWithRel("update-event").description("link to update")
							),
							requestHeaders(
									headerWithName(HttpHeaders.ACCEPT).description("accept header"),
									headerWithName(HttpHeaders.CONTENT_TYPE).description("content Type header")
							),
							requestFields(
									fieldWithPath("name").description("Name of new event"),
									fieldWithPath("description").description("description of new event"),
									fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
									fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
									fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
									fieldWithPath("endEventDateTime").description("date time of end of new event"),
									fieldWithPath("location").description("location of new event"),
									fieldWithPath("basePrice").description("base price of new event"),
									fieldWithPath("maxPrice").description("max price of new event"),
									fieldWithPath("limitOfEnrollment").description("limit of enrollment")
							),
							responseHeaders(
									headerWithName(HttpHeaders.LOCATION).description("Location header"),
									headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type : HAL JSON")
							),
							responseFields(
									fieldWithPath("id").description("Identifier of new event"),
									fieldWithPath("name").description("Name of new event"),
									fieldWithPath("description").description("description of new event"),
									fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
									fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
									fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
									fieldWithPath("endEventDateTime").description("date time of end of new event"),
									fieldWithPath("location").description("location of new event"),
									fieldWithPath("basePrice").description("base price of new event"),
									fieldWithPath("maxPrice").description("max price of new event"),
									fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
									fieldWithPath("free").description("it tells if this event is free or not"),
									fieldWithPath("offline").description("it tells if this event is offline event or not"),
									fieldWithPath("eventStatus").description("event status"),
									fieldWithPath("_links.self.href").description("link to self"),
									fieldWithPath("_links.query-events.href").description("link to query event list"),
									fieldWithPath("_links.update-event.href").description("link to update existing event")
							)
					))
					;
	}
	
	
	@Test
	@TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
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
	@TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
	public void createEvent_BadRequest_EmptyInput() throws Exception {
		EventDto eventDto = EventDto.builder().build();
		
		this.mockMvc.perform(post("/api/events/")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(this.objectMapper.writeValueAsString(eventDto)))
					.andExpect(status().isBadRequest())
					;
	}
	
	@Test
	@TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
	public void createEvent_BadRequest_EmptyWrong() throws Exception {
		EventDto eventDto = EventDto.builder()
				.name("Spring")
				.description("Rest Api Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2019, 11, 26, 9, 30))
				.closeEnrollmentDateTime(LocalDateTime.of(2019, 11, 25, 9, 30))
				.beginEventDateTime(LocalDateTime.of(2019, 11, 24, 9, 30))
				.endEventDateTime(LocalDateTime.of(2019, 11, 23, 9, 30))
				.basePrice(10000)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남 D2 스타트업 팩토리")
				.build();
		
		this.mockMvc.perform(post("/api/events/")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(this.objectMapper.writeValueAsString(eventDto)))
					.andExpect(status().isBadRequest())
					.andDo(print())
					.andExpect(jsonPath("$[0].objectName").exists())
//					.andExpect(jsonPath("$[0].field").exists())
					.andExpect(jsonPath("$[0].defaultMessage").exists())
					.andExpect(jsonPath("$[0].code").exists())
//					.andExpect(jsonPath("$[0].rejectedValue").exists())
					;
	}
}
