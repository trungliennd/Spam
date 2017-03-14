package MessageSMS;

public class SMS {
	
	private String message;
	private Integer length;
	private Integer checked;
	private Integer predict;
	private Integer numberTerm;
	
	public SMS() {
		// TODO Auto-generated constructor stub
	}
	
	public SMS(String message,Integer length,Integer checked) {
		this.message = message;
		this.length = length;
		this.checked = checked;
	}
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getChecked() {
		return checked;
	}

	public void setChecked(Integer checked) {
		this.checked = checked;
	}
	
	
	public Integer getNumberTerm() {
		return numberTerm;
	}

	public void setNumberTerm(Integer numberTerm) {
		this.numberTerm = numberTerm;
	}
	
	

	public Integer getPredict() {
		return predict;
	}

	public void setPredict(Integer predict) {
		this.predict = predict;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder strings = new StringBuilder();
		strings.append("Message is: ");
		strings.append(message);
		strings.append("\n");
		strings.append("Length is: ");
		strings.append(length);
		strings.append("\n");
		strings.append("Checked is: ");
		strings.append(checked);
		return strings.toString(); 
	}
	
}
