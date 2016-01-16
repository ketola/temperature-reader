package ketola.temperature.rest;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ketola.temperature.reader.Temperature;
import ketola.temperature.reader.Temperature.Source;
import ketola.temperature.reader.TemperatureObserver;
import ketola.temperature.reader.TemperatureReader;
import ketola.temperature.reader.TemperatureReaderSerialPortImpl;
import ketola.temperature.rest.TemperatureHistory.ValuesAndLabels;

@RestController
public class TemperatureController implements TemperatureObserver {

	private static final Logger LOG = LoggerFactory.getLogger(TemperatureController.class);

	private Temperature latestOutside = createDefaultTemperature();
	private Temperature latestInside = createDefaultTemperature();

	private TemperatureHistory historyInside = new TemperatureHistory();
	private TemperatureHistory historyOutside = new TemperatureHistory();

	private TemperatureReader reader;

	@Value("${serial.port}")
	private String serialPort;

	@PostConstruct
	private void init() {
		this.reader = new TemperatureReaderSerialPortImpl(serialPort);
		// this.reader = new TemperatureReaderDummyImpl();
		this.reader.registerObserver(this);
	}

	private Temperature createDefaultTemperature() {
		Temperature temperature = new Temperature();
		temperature.setValue(100d);
		return temperature;
	}

	@RequestMapping("/latest/outside")
	public Temperature getLatestReadingInside() {
		LOG.debug("GET /latest/outside");
		return latestOutside;
	}

	@RequestMapping("/latest/inside")
	public Temperature getLatestReadingOutside() {
		LOG.debug("GET /latest/inside");
		return latestInside;
	}

	@RequestMapping("/history/outside")
	public List<Temperature> getHistoryOutside() {
		LOG.debug("GET /history/outside");
		return historyOutside.getHistory();
	}

	@RequestMapping("/history/outside/values")
	public List<Double> getHistoryOutsideValues() {
		LOG.debug("GET /history/outside/values");
		return historyOutside.getHistoryValues();
	}

	@RequestMapping("/history/outside/values-and-labels")
	public ValuesAndLabels getHistoryOutsideValuesAndLabels() {
		LOG.debug("GET /history/outside/values-and-labels");
		return historyOutside.getHistoryValuesAndLabels();
	}

	@RequestMapping("/history/inside")
	public List<Temperature> getHistoryInside() {
		LOG.debug("GET /history/inside/values");
		return historyInside.getHistory();
	}

	@RequestMapping("/history/inside/values")
	public List<Double> getHistoryInsideValues() {
		LOG.debug("GET /history/inside/values");
		return historyInside.getHistoryValues();
	}

	@RequestMapping("/history/inside/values-and-labels")
	public ValuesAndLabels getHistoryinsideValuesAndLabels() {
		LOG.debug("GET /history/inside/values-and-labels");
		return historyInside.getHistoryValuesAndLabels();
	}

	@RequestMapping("/latest/sse")
	public SseEmitter getLatestReadingSse() {
		LOG.debug("GET /latest/sse");
		TemperatureEmitter sseEmitter = new TemperatureEmitter(reader);
		return sseEmitter;
	}

	@Override
	public void onTemperatureRead(Temperature temperature) {
		LOG.debug("New temperature {}", temperature);

		if (temperature.getSource() == Source.INSIDE) {
			this.latestInside = temperature;
			historyInside.saveToHistory(temperature);
		} else {
			this.latestOutside = temperature;
			historyOutside.saveToHistory(temperature);
		}
	}
}
