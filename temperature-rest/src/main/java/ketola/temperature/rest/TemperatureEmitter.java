package ketola.temperature.rest;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ketola.temperature.reader.Temperature;
import ketola.temperature.reader.TemperatureObserver;
import ketola.temperature.reader.TemperatureReader;

public class TemperatureEmitter extends SseEmitter implements TemperatureObserver {

	private static final Logger LOG = LoggerFactory.getLogger(TemperatureEmitter.class);

	private TemperatureReader reader;

	public TemperatureEmitter(TemperatureReader reader) {
		this.reader = reader;
		this.reader.registerObserver(this);
		onCompletion(new Runnable() {
			@Override
			public void run() {
				LOG.debug("TemperatureEmitter completed");
				TemperatureEmitter.this.reader.unregisterObserver(TemperatureEmitter.this);
			}
		});
	}

	@Override
	public void onTemperatureRead(Temperature temperature) {
		try {
			LOG.debug("Send temperature {}", temperature);
			send(temperature);
		} catch (IOException e) {
			LOG.error("IOException caught while sending temperature", e);
		} catch (IllegalStateException e) {
			LOG.error(
					"IllegalStateException caught while sending temperature. Unregistering this emitter from observing further readings",
					e);
			this.reader.unregisterObserver(this);
		}
	}

}
