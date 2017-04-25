package SpamSMS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.basic.BasicViewportUI;

import MessageSMS.SMS;
import MessageSMS.Word;

public class CalculationSMS {
	
	private List<SMS> nonSpam = new ArrayList<SMS>();
	private List<SMS> Spam = new ArrayList<SMS>();
	private Integer totalSMS = 0;
	private Integer totalSMSSpam = 0;
	private Integer totalSMSNonSpam = 0;
	private Float  AverageLengthNonSpam = 0.0f;
	private Float baseNonSpam =0.0f;
	private Float AverageLengthSpam = 0.0f;
	private Float baseSpam = 0.0f;
	private Float averIntegerLengthSMS = 0.0f;
	private Float probSpam = 0.0f;
	private Float probNonSpam = 0.0f;
	private Float probLengthNonSpam = 0.0f;
	private Float probLengthSpam = 0.0f;
	private Float phi2Spam = 0.0f;
	private Float phi2NonSpam = 0.0f;
	
	private Dictionary dictionary; 
	
	
	public CalculationSMS(){
		
	}
	
	public CalculationSMS(String pathI,String pathII,String pathIII) {
		dictionary = new Dictionary(pathI, pathII, pathIII);
		SMStoNonSpamAndSpam(pathI, pathII, pathIII);
		calculationLengthSMS();
		calculationProbj();
		preCalculationPhi();
//		calculationBaseSMS();
	}
	
	public CalculationSMS(String path) {
		dictionary =  new Dictionary(path);
		SMStoNonSpamAndSpam(path);
		calculationLengthSMS();
		calculationProbj();
		preCalculationPhi();
	}
	
	public void preCalculationPhi() {
		/*
		 * calculation phi2Spam
		 */
		int sizeSpam = Spam.size();
		for(int i = 0;i < sizeSpam;i++) {
			float a = ((float)Spam.get(i).getLength() - AverageLengthSpam);
			phi2Spam += a*a;
		}
		phi2Spam = (float)phi2Spam/(sizeSpam - 1);
		/*
		 * calculation phi2NonSpam
		 */
		int sizeNonSpam = nonSpam.size();
		for(int i = 0;i < sizeNonSpam;i++) {
			float a = ((float)nonSpam.get(i).getLength() - AverageLengthNonSpam);
			phi2NonSpam += a*a;
		}
		phi2NonSpam = (float)phi2NonSpam/(sizeNonSpam - 1);
	}
	
	public float probSpamGaussian(int length) {
		float prob = (float) (1.0/(Math.sqrt(2*Math.PI*phi2Spam)));
		float a = (length - AverageLengthSpam);
		float express = (float)(-1)*(a*a)/(2*phi2Spam);
		float prob_e = (float)Math.exp(express);
		return (prob_e*prob);
	}
	
	public float probNonSpamGaussian(int length) {
		float prob = (float) (1.0/(Math.sqrt(2*Math.PI*phi2NonSpam)));
		float a = (length - AverageLengthNonSpam);
		float express = (float)(-1)*(a*a)/(2*phi2NonSpam);
		float prob_e = (float)Math.exp(express);
		return (prob_e*prob);
	}
	
	public void calculationGaussian(int length) {
		float a = probNonSpamGaussian(length);
		float b = probSpamGaussian(length);
		probLengthSpam = (float)(b*probSpam)/(b*probSpam + a*probNonSpam);
		probLengthNonSpam = (float)(a*probNonSpam)/(b*probSpam + a*probNonSpam);
	}
	
	public void SMStoNonSpamAndSpam(String pathI,String pathII,String pathIII) {
		SeparatingData a = new SeparatingData();
		try {
			a.readDataTrain(pathI,pathII,pathIII);
			List<SMS> listSMS = a.getDataTrain();
			int size = listSMS.size();
			for(int i = 0;i <size;i++) {
				SMS sms = listSMS.get(i);
				if(sms.getChecked() == 1){
					nonSpam.add(sms);
					totalSMSNonSpam++;
				}else {
					Spam.add(sms);
					totalSMSSpam++;
				}
			}
			totalSMS = totalSMSNonSpam + totalSMSSpam;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void SMStoNonSpamAndSpam(String path) {
		SeparatingData a = new SeparatingData();
		try {
			a.readDataTrain(path);
			List<SMS> listSMS = a.getDataTrain();
			int size = listSMS.size();
			for(int i = 0;i <size;i++) {
				SMS sms = listSMS.get(i);
				if(sms.getChecked() == 1){
					nonSpam.add(sms);
					totalSMSNonSpam++;
				}else {
					Spam.add(sms);
					totalSMSSpam++;
				}
			}
			totalSMS = totalSMSNonSpam + totalSMSSpam;
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void calculationLengthSMS() {
		int sizeNon = nonSpam.size();
		int sumLen = 0;
		for(int i = 0;i < sizeNon;i++) {
			sumLen += nonSpam.get(i).getLength();
		}
		AverageLengthNonSpam = (float)sumLen/sizeNon;
		averIntegerLengthSMS += sumLen;
		sumLen = 0;
		int size = Spam.size();
		for(int i = 0;i < size;i++) {
			sumLen += Spam.get(i).getLength();
		}
		AverageLengthSpam = (float)sumLen/size;
		averIntegerLengthSMS += sumLen;
		averIntegerLengthSMS = averIntegerLengthSMS/(sizeNon + size);
	}
	
	public float getProbj(int count,int size){
		if(count == 0) {
			return (float)1/size;
		}else {
			return (float)count/size;
		}
	}
	
	
	public void calculationProbj() {
		probSpam = (float)totalSMSSpam/totalSMS;
		probNonSpam = (float)totalSMSNonSpam/totalSMS;
	}
	
	/*public void calculationBaseSMS() {
		int len = nonSpam.size();
		float sum = 0.0f;
		for(int i = 0;i < len;i++) {
			float a = (nonSpam.get(i).getLength() - AverageLengthNonSpam);
			sum += a*a;		
		}
		baseNonSpam = (float)Math.sqrt(sum)/len;
		
		sum = 0.0f;
		len = Spam.size();
		for(int i = 0;i < len;i++) {
			float a = (Spam.get(i).getLength() - AverageLengthSpam);
			sum += a*a;		
		}
		baseSpam = (float)Math.sqrt(sum)/len;
	}*/
	
	/*public void calculationLength(int x) {
		
		 * NonSpam
		 
		probLengthNonSpam = (float) (1.0/(baseNonSpam*Math.sqrt(Math.PI*2)));
		probLengthNonSpam = (float) Math.log((probLengthNonSpam*Math.exp((AverageLengthNonSpam - x)*(x - AverageLengthNonSpam)/(2*baseNonSpam*baseNonSpam))));
		System.out.println("res is: " + probLengthNonSpam);
		
		 * Spam
		 
		probLengthSpam = (float) (1.0/(baseSpam)*Math.sqrt(Math.PI*2));
		probLengthSpam = (float) (probLengthSpam*Math.exp((AverageLengthSpam - x)*(x - AverageLengthSpam)/(2*baseSpam*baseSpam)));
		probLengthSpam = (float) Math.log(probLengthSpam);
	}*/
	
	public float calculationSpam(SMS strings){
		String words[] = dictionary.getListNGramToMessages(strings);
		Map<String,Word> mapWords = dictionary.getDictionary();
		int countWordIntoSpam = 0;
		float probjSpam = 0.0f;
		for(int i = 0;i < words.length;i++) {
			if(mapWords.containsKey(words[i])) {
				countWordIntoSpam = mapWords.get(words[i]).getSpamCount();  
				System.out.println("Word is: " + words[i] + " | count is: " + countWordIntoSpam);
				probjSpam += Math.log((float)(countWordIntoSpam + 1)/(dictionary.getTotalTermSpam() + dictionary.getSizeDictionary()));
			}
		}
		System.out.println("(Spam) count: " + countWordIntoSpam + " term: " + dictionary.getTotalTermSpam() +" length: " + words.length);
		System.out.println("probjSpam is: " + probjSpam + " and probSpam is: " + probSpam);
		calculationGaussian(strings.getLength());
		float res = (float)(probjSpam + Math.log(probSpam) + probLengthSpam + Math.log(probLengthSpam));
		return res; 
	}
	
	public float calculationNonSpam(SMS strings) {
		String words[] = dictionary.getListNGramToMessages(strings);
		Map<String,Word> mapWords = dictionary.getDictionary();
		int countWordIntoNonSpam = 0;
		float probjNonSpam = 0.0f;
		for(int i = 0;i < words.length;i++) {
			if(mapWords.containsKey(words[i])) {
				countWordIntoNonSpam = mapWords.get(words[i]).getNonSpamCount();  
				System.out.println("Word is: " + words[i] + " | count is: " + countWordIntoNonSpam);
				probjNonSpam += Math.log((float)(countWordIntoNonSpam + 1)/(dictionary.getTotalTermNonSpam() + dictionary.getSizeDictionary()));
			}
		}
		System.out.println("(NonSpam) count: " + countWordIntoNonSpam + " term: " + dictionary.getTotalTermNonSpam() +" length: " + words.length);
		System.out.println("probjNonSpam is: " + probjNonSpam + " and probNonSpam is: " + probNonSpam);
		calculationGaussian(strings.getLength());
		float res = (float)(probjNonSpam + Math.log(probNonSpam) + probLengthNonSpam + Math.log(probLengthNonSpam));
		return res;
	}
	
	public String ConvertSMS(SMS sms) {
		StringBuilder strings = new StringBuilder();
		strings.append(sms.getChecked());
		strings.append(" ");
		strings.append(sms.getMessage());
		return strings.toString();
	}
	
	public void naiveBayes(String fileInput,String FileOutput) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileInput)));
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(FileOutput)));
		String line = bufferedReader.readLine();
		int count = 1;
		SeparatingData a = new SeparatingData();
		while(line != null) {
//		  if(count == 34){
			String data[] = a.processMessage(line);
			SMS sms = new SMS();
			sms.setLength(data[1].length());
			sms.setMessage(data[1]);
			//calculationLength(sms.getLength());
			System.out.println("prob NonSpam is: " + probLengthNonSpam);
			System.out.println("prob Spam is: " + probLengthSpam);
			float probjSpam = calculationSpam(sms);
			float probjNonSpam = calculationNonSpam(sms);
			System.out.println("STT " + count + " probjSpam is: " +probjSpam +" and probjNonSpam is: " + probjNonSpam);
			if(probjNonSpam > probjSpam){ 
				sms.setChecked(1);
			}else {
				sms.setChecked(-1);
			}
			bufferedWriter.write(ConvertSMS(sms));
			bufferedWriter.write("\n");
//			break;
//		  }
		  line = bufferedReader.readLine();
		  count++;
		}
		bufferedReader.close();
		bufferedWriter.close();
		
	}
	

	public static void main(String args[]){
//		CalculationSMS calculationSMS = new CalculationSMS(SeparatingData.PATH_DATA_II,SeparatingData.PATH_DATA_I,SeparatingData.PATH_DATA_IV);
		CalculationSMS calculationSMS = new CalculationSMS("F:\\Java\\Spam\\data\\sms_one.txt");
		System.out.println("Average Length Spam is: " + calculationSMS.getAverageLengthSpam());
		System.out.println("Average Length NonSpam is: " + calculationSMS.getAverageLengthNonSpam());
		System.out.println("Average Length SMS is: " + calculationSMS.getAverIntegerLengthSMS());
		try{
			calculationSMS.naiveBayes("F:\\Java\\Spam\\data\\test_new.txt", SeparatingData.PATH_DATA_PREDICT);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public List<SMS> getNonSpam() {
		return nonSpam;
	}


	public void setNonSpam(List<SMS> nonSpam) {
		this.nonSpam = nonSpam;
	}


	public List<SMS> getSpam() {
		return Spam;
	}


	public void setSpam(List<SMS> spam) {
		Spam = spam;
	}

	public Integer getTotalSMS() {
		return totalSMS;
	}

	public void setTotalSMS(Integer totalSMS) {
		this.totalSMS = totalSMS;
	}

	public Integer getTotalSMSSpam() {
		return totalSMSSpam;
	}

	public void setTotalSMSSpam(Integer totalSMSSpam) {
		this.totalSMSSpam = totalSMSSpam;
	}

	public Integer getTotalSMSNonSpam() {
		return totalSMSNonSpam;
	}

	public void setTotalSMSNonSpam(Integer totalSMSNonSpam) {
		this.totalSMSNonSpam = totalSMSNonSpam;
	}

	public Float getAverageLengthNonSpam() {
		return AverageLengthNonSpam;
	}

	public void setAverageLengthNonSpam(Float averageLengthNonSpam) {
		AverageLengthNonSpam = averageLengthNonSpam;
	}

	public Float getAverageLengthSpam() {
		return AverageLengthSpam;
	}

	public void setAverageLengthSpam(Float averageLengthSpam) {
		AverageLengthSpam = averageLengthSpam;
	}

	public Float getProbSpam() {
		return probSpam;
	}

	public void setProbSpam(Float probSpam) {
		this.probSpam = probSpam;
	}

	public Float getProbNonSpam() {
		return probNonSpam;
	}

	public void setProbNonSpam(Float probNonSpam) {
		this.probNonSpam = probNonSpam;
	}

	public Float getAverIntegerLengthSMS() {
		return averIntegerLengthSMS;
	}

	public void setAverIntegerLengthSMS(Float averIntegerLengthSMS) {
		this.averIntegerLengthSMS = averIntegerLengthSMS;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public Float getBaseNonSpam() {
		return baseNonSpam;
	}

	public void setBaseNonSpam(Float baseNonSpam) {
		this.baseNonSpam = baseNonSpam;
	}

	public Float getBaseSpam() {
		return baseSpam;
	}

	public void setBaseSpam(Float baseSpam) {
		this.baseSpam = baseSpam;
	}

	public Float getProbLengthNonSpam() {
		return probLengthNonSpam;
	}

	public void setProbLengthNonSpam(Float probLengthNonSpam) {
		this.probLengthNonSpam = probLengthNonSpam;
	}

	public Float getProbLengthSpam() {
		return probLengthSpam;
	}

	public void setProbLengthSpam(Float probLengthSpam) {
		this.probLengthSpam = probLengthSpam;
	}
	
}