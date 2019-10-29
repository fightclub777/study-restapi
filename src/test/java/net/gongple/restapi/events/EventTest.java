package net.gongple.restapi.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class) 
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
	
	@Test
//	@Parameters({
//		"0, 0, true",
//		"100, 0, false",
//		"0, 100, false"
//	})
//	@Parameters(method = "parametersForTestFree")
	@Parameters
	public void testFree(int basePrice, int maxPrice, boolean isFree) {
		// Given
		Event event = Event.builder()
				.basePrice(basePrice)
				.maxPrice(maxPrice)
				.build();
		// When
		event.update();
		// Then
		assertThat(event.isFree()).isTrue();
		
		// Given
		event = Event.builder()
				.basePrice(basePrice)
				.maxPrice(maxPrice)
				.build();
		// When
		event.update();
		// Then
		assertThat(event.isFree()).isFalse();
		
		// Given
		event = Event.builder()
				.basePrice(basePrice)
				.maxPrice(maxPrice)
				.build();
		// When
		event.update();
		// Then
		assertThat(event.isFree()).isEqualTo(isFree);
	}
	
	/*
	 * parametersFor까지 적고(컨벤션), 
	 * 적용할 메소드 이름을 적어준 뒤, 
	 * 적용 메소드에서 @Parameters 애노테이션만 붙이면 자동 적용.
	 * 당연히 적용 메소드에는 값이 자동 적용될 수 있도록 파라메터를 만들어줘야 한다.
	 */
	private Object[] parametersForTestFree() {
		return new Object[] {
			new Object[] { 0, 0, true},
			new Object[] { 100, 0, false},
			new Object[] { 0, 100, false},
			new Object[] { 100, 200, false},
		};
	}
	
	@Test
	@Parameters
	public void testOffline(String location, boolean isOffline) {
		// Given
		Event event = Event.builder()
				.location(location)
				.build();
		// When
		event.update();
		// Then
		assertThat(event.isOffline()).isEqualTo(isOffline);
		
		// Given
		event = Event.builder()
				.build();
		// When
		event.update();
		// Then
		assertThat(event.isOffline()).isEqualTo(isOffline);
	}
	
	private Object[] parametersForTestOffline() {
		return new Object[] {
			new Object[] { "강남", true},
			new Object[] { null, false},
			new Object[] { "", false}
		};
	}
}
