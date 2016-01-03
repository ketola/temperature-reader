package ketola.temperature.reader;

import static java.util.Collections.synchronizedCollection;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import ketola.temperature.reader.Temperature.Source;

public class TemperatureReaderSerialPortImpl implements TemperatureReader {
	private static final Logger LOG = LoggerFactory.getLogger(TemperatureReaderSerialPortImpl.class);

	private static final String DEVICE_INSIDE = "002";
	private static final String DEVICE_OUTSIDE = "001";

	private Collection<TemperatureObserver> observers;

	public TemperatureReaderSerialPortImpl(String port) {
		LOG.info("Construct TemperatureReaderSerialPortImpl with serial port {}", port);
		this.observers = synchronizedCollection(new HashSet<TemperatureObserver>());
		try {
			createAndConfigureSerialPort(port);
		} catch (SerialPortException e) {
			throw new RuntimeException(e);
		}
	}

	protected SerialPort createAndConfigureSerialPort(String port) throws SerialPortException {
		SerialPort serialPort = new SerialPort(port);
		try {
			serialPort.openPort();
			serialPort.setParams(9600, 8, 1, 0);
		} catch (SerialPortException e) {
			LOG.error("SerialPortException caught", e);
			throw new RuntimeException(e);
		}

		serialPort.addEventListener(new SerialPortEventListener() {

			@Override
			public void serialEvent(SerialPortEvent event) {
				if (event.isRXCHAR()) {// If data is available
					if (event.getEventValue() > 0) {
						try {
							byte[] buffer = serialPort.readBytes(event.getEventValue());
							String message = new String(buffer);
							notifyObservers(toTemperature(message));
						} catch (SerialPortException ex) {
							LOG.error("SerialPortException caught", ex);
						} catch (Exception e) {
							LOG.error("Exception caught", e);
						}
					}
				}

			}
		});
		return serialPort;
	}

	private void notifyObservers(Temperature temperature) {
		synchronized (observers) {
			for (TemperatureObserver observer : observers) {
				observer.onTemperatureRead(temperature);
			}
		}
	}

	protected Temperature toTemperature(String message) {
		if (message == null || message.isEmpty() || message.length() < 17) {
			LOG.error("Message is not in a valid format ({})", message);
		}

		message = message.trim();
		LOG.debug("Convert message {}", message);

		message = message.replaceAll("\r\n|\n|\r", "");

		String system = message.substring(8, 11);
		String device = message.substring(12, 15);
		String value = message.substring(16, message.length()).trim();

		Temperature temperature = new Temperature();
		temperature.setSource(device.equals(DEVICE_INSIDE) ? Source.INSIDE : Source.OUTSIDE);
		temperature.setDate(new Date());
		temperature.setValue(Double.valueOf(value));

		return temperature;
	}

	@Override
	public void registerObserver(TemperatureObserver observer) {
		synchronized (observers) {
			observers.add(observer);
		}
	}

	@Override
	public void unregisterObserver(TemperatureObserver observer) {
		synchronized (observers) {
			if (observers.contains(observer))
				observers.remove(observer);
		}

	}
}
