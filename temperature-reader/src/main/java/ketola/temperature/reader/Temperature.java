package ketola.temperature.reader;

import java.io.Serializable;
import java.util.Date;

public class Temperature implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum Source {
		INSIDE, OUTSIDE
	}

	private Source source;
	private Double value;
	private Date date;

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Temperature [source=" + source + ", value=" + value + ", date=" + date + "]";
	}

}
