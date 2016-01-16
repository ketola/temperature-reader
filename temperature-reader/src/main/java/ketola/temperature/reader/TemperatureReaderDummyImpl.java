package ketola.temperature.reader;

import static java.util.Collections.synchronizedCollection;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import ketola.temperature.reader.Temperature.Source;

public class TemperatureReaderDummyImpl implements TemperatureReader {

	private Collection<TemperatureObserver> observers;
	private boolean inside = true;

	public TemperatureReaderDummyImpl() {
		this.observers = synchronizedCollection(new HashSet<TemperatureObserver>());

		new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Temperature temperature = new Temperature();
					temperature.setSource(inside ? Source.INSIDE : Source.OUTSIDE);
					temperature.setDate(new Date());
					inside = !inside;
					temperature.setValue(Math.round(10 * Math.random()) / 10d);
					TemperatureReaderDummyImpl.this.notifyObservers(temperature);
				}

			}
		}).start();
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

	private void notifyObservers(Temperature temperature) {
		synchronized (observers) {
			for (TemperatureObserver observer : observers) {
				observer.onTemperatureRead(temperature);
			}
		}
	}

}
