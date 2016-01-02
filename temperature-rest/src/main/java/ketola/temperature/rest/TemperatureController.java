package ketola.temperature.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ketola.temperature.reader.Temperature;
import ketola.temperature.reader.Temperature.Source;
import ketola.temperature.reader.TemperatureObserver;
import ketola.temperature.reader.TemperatureReader;
import ketola.temperature.reader.TemperatureReaderSerialPortImpl;

@RestController
public class TemperatureController implements TemperatureObserver {

	private Temperature latestOutside = new Temperature();
	private Temperature latestInside = new Temperature();

	private TemperatureReader reader;

	public TemperatureController() {
		this.reader = new TemperatureReaderSerialPortImpl("COM3");
		this.reader.registerObserver(this);
	}

	@RequestMapping("/latest/outside")
	public Temperature getLatestReadingInside() {
		return latestOutside;
	}

	@RequestMapping("/latest/inside")
	public Temperature getLatestReadingOutside() {
		return latestInside;
	}

	@RequestMapping("/latest/sse")
	public SseEmitter getLatestReadingSse() {
		TemperatureEmitter sseEmitter = new TemperatureEmitter(reader);
		return sseEmitter;
	}

	@Override
	public void onTemperatureRead(Temperature temperature) {
		if (temperature.getSource() == Source.INSIDE) {
			this.latestInside = temperature;
		} else {
			this.latestOutside = temperature;
		}
	}

}
