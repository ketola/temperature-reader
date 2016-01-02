package ketola.temperature.reader;

public interface TemperatureReader {
	void registerObserver(TemperatureObserver observer);

	void unregisterObserver(TemperatureObserver observer);
}
