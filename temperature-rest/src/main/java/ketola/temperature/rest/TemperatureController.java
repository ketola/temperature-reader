package ketola.temperature.rest;

import static java.util.Collections.synchronizedList;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
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

	private static final int HISTORY_MAX_SIZE = 100;
	private Temperature latestOutside = createDefaultTemperature();
	private Temperature latestInside = createDefaultTemperature();

	private List<Temperature> historyInside = synchronizedList(new ArrayList<Temperature>());
	private List<Temperature> historyOutside = synchronizedList(new ArrayList<Temperature>());

	private TemperatureReader reader;

	@Value("${serial.port}")
	private String serialPort;

	@PostConstruct
	private void init() {
		this.reader = new TemperatureReaderSerialPortImpl(serialPort);
		this.reader.registerObserver(this);
	}

	private Temperature createDefaultTemperature() {
		Temperature temperature = new Temperature();
		temperature.setValue(100d);
		return temperature;
	}

	@RequestMapping("/latest/outside")
	public Temperature getLatestReadingInside() {
		return latestOutside;
	}

	@RequestMapping("/latest/inside")
	public Temperature getLatestReadingOutside() {
		return latestInside;
	}

	@RequestMapping("/history/outside")
	public List<Temperature> getHistoryOutside() {
		return historyOutside;
	}

	@RequestMapping("/history/inside")
	public List<Temperature> getHistoryInside() {
		return historyInside;
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
			saveToHistory(temperature, this.historyInside);
		} else {
			this.latestOutside = temperature;
			saveToHistory(temperature, this.historyOutside);
		}
	}

	private void saveToHistory(Temperature temperature, List<Temperature> history) {
		synchronized (history) {
			if (history.size() > HISTORY_MAX_SIZE) {
				history.remove(0);
			}
			history.add(temperature);
		}
	}

}
