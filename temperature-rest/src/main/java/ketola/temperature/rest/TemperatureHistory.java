package ketola.temperature.rest;

import static java.util.Collections.synchronizedList;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import ketola.temperature.reader.Temperature;

public class TemperatureHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int HISTORY_MAX_SIZE = 10000;

	private List<Temperature> history = synchronizedList(new ArrayList<Temperature>());
	private Date latestSavedTemperature = new Date(0);

	public void saveToHistory(Temperature temperature) {

		if (DateUtils.round(temperature.getDate(), Calendar.MINUTE)
				.after(DateUtils.round(latestSavedTemperature, Calendar.MINUTE))) {
			synchronized (history) {
				if (history.size() > HISTORY_MAX_SIZE) {
					history.remove(0);
				}
				history.add(temperature);
				latestSavedTemperature = new Date();
			}
		}
	}

	public List<Temperature> getHistory() {
		return new ArrayList<Temperature>(history);
	}

	public List<Double> getHistoryValues() {
		List<Double> values = new ArrayList<Double>();

		for (Temperature temperature : getHistory()) {
			values.add(temperature.getValue());
		}

		return values;
	}

	public ValuesAndLabels getHistoryValuesAndLabels() {

		SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm d.M.");

		List<Double> values = new ArrayList<Double>();
		List<String> labels = new ArrayList<String>();

		for (Temperature temperature : getHistory()) {
			values.add(temperature.getValue());
			labels.add(dateFormat.format(temperature.getDate()));
		}

		return new ValuesAndLabels(values, labels);
	}

	public static class ValuesAndLabels {
		List<Double> values = new ArrayList<Double>();
		List<String> labels = new ArrayList<String>();

		public ValuesAndLabels(List<Double> values, List<String> labels) {
			super();
			this.values = values;
			this.labels = labels;
		}

		public List<Double> getValues() {
			return values;
		}

		public void setValues(List<Double> values) {
			this.values = values;
		}

		public List<String> getLabels() {
			return labels;
		}

		public void setLabels(List<String> labels) {
			this.labels = labels;
		}

	}
}
