package ketola.temperature.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ketola.temperature.reader.Temperature;
import ketola.temperature.reader.TemperatureReader;
import ketola.temperature.reader.TemperatureReaderSerialPortImpl;

@RestController
public class TemperatureController {

	private Temperature latest = new Temperature();
	private TemperatureReader reader;

	public TemperatureController() {
		this.reader = new TemperatureReaderSerialPortImpl("COM3");
	}

	@RequestMapping("/latest")
	public Temperature getLatestReading() {
		return latest;
	}

	@RequestMapping("/latest/sse")
	public SseEmitter getLatestReadingSse() {
		TemperatureEmitter sseEmitter = new TemperatureEmitter(reader);
		return sseEmitter;
	}

}
