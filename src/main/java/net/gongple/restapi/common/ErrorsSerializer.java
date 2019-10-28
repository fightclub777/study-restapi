package net.gongple.restapi.common;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

	@Override
	public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartArray();
//		errors.getFieldErrors().stream().forEach(e -> {
		errors.getFieldErrors().forEach(e -> { // Stream에서 forEach만 한다면 stream() 생략 가능.
			try {
				gen.writeStartObject();
				gen.writeStringField("field", e.getField());
				gen.writeStringField("objectName", e.getObjectName());
				gen.writeStringField("code", e.getCode());
				gen.writeStringField("defaultMessage", e.getDefaultMessage());
				Object rejectedValue = e.getRejectedValue();
				if (rejectedValue != null) {
					gen.writeStringField("rejectedValue", rejectedValue.toString());
				}
				gen.writeEndObject();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});
		
		errors.getGlobalErrors().forEach(e -> {
			try {
				gen.writeStartObject();
				gen.writeStringField("objectName", e.getObjectName());
				gen.writeStringField("code", e.getCode());
				gen.writeStringField("defaultMessage", e.getDefaultMessage());
				gen.writeEndObject();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});
		gen.writeEndArray();
	}

	
}
