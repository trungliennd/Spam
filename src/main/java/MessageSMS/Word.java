package MessageSMS;

public class Word {
	
	private String word;	// the word ifselt 
	private int spamCount; 	// number of this word appearances in spam messages
	private int nonSpamCount; // number of this word appearances in non spam messages
	private float rateSpam;		// spamCount div total SMS spam count
	private float rateNonSpam;	// nonSpam div total SMS non spam count
	private float probOfSpam;	//	probability of word being spam
	
	
	public Word(){
		
	}
	
	public Word(String word){
		this.word = word;
		this.spamCount = 0;
		this.nonSpamCount = 0;
		rateSpam = 0.0f;
		rateNonSpam = 0.0f;
		probOfSpam = 0.0f;
	}
	
	//calculates the probability of spam, 
	//and gives the smallest and biggest probabilities more precedence
	public void calculateProbability(int totalSpam,int totalNonSpam){
		rateSpam = spamCount/(float)totalSpam;
		rateNonSpam = nonSpamCount/(float)totalNonSpam;
		
		if(rateSpam + rateNonSpam > 0){
			probOfSpam = rateSpam / (rateSpam + rateNonSpam);
		}
		if(probOfSpam < 0.01f){
			probOfSpam = 0.01f;
		}
		else if(probOfSpam > 0.99f){
			probOfSpam = 0.99f;
		}
		
	}
	
	
	public void countSpam(){
		spamCount++;
	}
	
	public void countNonSpam() {
		nonSpamCount++;
	}
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getSpamCount() {
		return spamCount;
	}
	public void setSpamCount(int spamCount) {
		this.spamCount = spamCount;
	}
	public int getNonSpamCount() {
		return nonSpamCount;
	}
	public void setNonSpamCount(int nonSpamCount) {
		this.nonSpamCount = nonSpamCount;
	}

	public float getRateSpam() {
		return rateSpam;
	}

	public void setRateSpam(float rateSpam) {
		this.rateSpam = rateSpam;
	}

	public float getRateNonSpam() {
		return rateNonSpam;
	}

	public void setRateNonSpam(float rateNonSpam) {
		this.rateNonSpam = rateNonSpam;
	}

	public float getProbOfSpam() {
		return probOfSpam;
	}

	public void setProbOfSpam(float probOfSpam) {
		this.probOfSpam = probOfSpam;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder strings = new StringBuilder();
		strings.append("Word is: ");
		strings.append(word);
		strings.append(" spamCount is: ");
		strings.append(spamCount);
		strings.append(" nonSpamCount is: ");
		strings.append(nonSpamCount);
		return strings.toString();
	}
}
