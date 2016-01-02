package ketola.temperature.rest;

import java.io.IOException;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ketola.temperature.reader.Temperature;
import ketola.temperature.reader.TemperatureObserver;
import ketola.temperature.reader.TemperatureReader;

public class TemperatureEmitter extends SseEmitter implements TemperatureObserver {

	public TemperatureEmitter(TemperatureReader reader) {
		reader.registerObserver(this);
		onCompletion(new Runnable() {
			@Override
			public void run() {
				reader.unregisterObserver(TemperatureEmitter.this);
			}
		});
	}

	@Override
	public void onTemperatureRead(Temperature temperature) {
		try {
			send(temperature);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
