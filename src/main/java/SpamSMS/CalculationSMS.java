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

import MessageSMS.SMS;
import MessageSMS.Word;

public class CalculationSMS {
	
	private List<SMS> nonSpam = new ArrayList<SMS>();
	private List<SMS> Spam = new ArrayList<SMS>();
	private Integer totalSMS = 0;
	private Integer totalSMSSpam = 0;
	private Integer totalSMSNonSpam = 0;
	private Integer AverageLengthNonSpam = 0;
	private Integer AverageLengthSpam = 0;
	private Integer averIntegerLengthSMS = 0;
	private Float probSpam = 0.0f;
	private Float probNonSpam = 0.0f;
	private Float probjLenSMSSpamOne = 0.0f;
	private Float probjLenSMSSpamTwo = 0.0f;
	private Float probjLenSMSNonSpamOne = 0.0f;
	private Float probjLenSMSNonSpamTwo = 0.0f;
	
	
	private Dictionary dictionary; 
	
	
	public CalculationSMS(){
		
	}
	
	public CalculationSMS(String pathI,String pathII,String pathIII) {
		dictionary = new Dictionary(pathI, pathII, pathIII);
		SMStoNonSpamAndSpam(pathI, pathII, pathIII);
		calculationLengthSMS();
		calculationProbj();
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
	
	public void calculationLengthSMS() {
		int sizeNon = nonSpam.size();
		int sumLen = 0;
		for(int i = 0;i < sizeNon;i++) {
			sumLen += nonSpam.get(i).getLength();
		}
		AverageLengthNonSpam = sumLen/sizeNon;
		averIntegerLengthSMS += sumLen;
		sumLen = 0;
		int size = Spam.size();
		for(int i = 0;i < size;i++) {
			sumLen += Spam.get(i).getLength();
		}
		AverageLengthSpam = sumLen/size;
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
		float res = (float)(probjSpam + Math.log(probSpam));
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
		float res = (float)(probjNonSpam + Math.log(probNonSpam));
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
		  //if(count == 19){
			String data[] = a.processMessage(line);
			SMS sms = new SMS();
			sms.setLength(data[1].length());
			sms.setMessage(data[1]);
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
		//	break;
		 // }
		  line = bufferedReader.readLine();
		  count++;
		}
		bufferedReader.close();
		bufferedWriter.close();
		
	}
	

	public static void main(String args[]){
		CalculationSMS calculationSMS = new CalculationSMS(SeparatingData.PATH_DATA_II,SeparatingData.PATH_DATA_III,SeparatingData.PATH_DATA_I);
		System.out.println("Average Length Spam is: " + calculationSMS.getAverageLengthSpam());
		System.out.println("Average Length NonSpam is: " + calculationSMS.getAverageLengthNonSpam());
		System.out.println("Average Length SMS is: " + calculationSMS.getAverIntegerLengthSMS());
		try{
			calculationSMS.naiveBayes(SeparatingData.PATH_DATA_IV, SeparatingData.PATH_DATA_PREDICT);
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

	public Integer getAverageLengthNonSpam() {
		return AverageLengthNonSpam;
	}

	public void setAverageLengthNonSpam(Integer averageLengthNonSpam) {
		AverageLengthNonSpam = averageLengthNonSpam;
	}

	public Integer getAverageLengthSpam() {
		return AverageLengthSpam;
	}

	public void setAverageLengthSpam(Integer averageLengthSpam) {
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

	public Integer getAverIntegerLengthSMS() {
		return averIntegerLengthSMS;
	}

	public void setAverIntegerLengthSMS(Integer averIntegerLengthSMS) {
		this.averIntegerLengthSMS = averIntegerLengthSMS;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public Float getProbjLenSMSSpamOne() {
		return probjLenSMSSpamOne;
	}

	public void setProbjLenSMSSpamOne(Float probjLenSMSSpamOne) {
		this.probjLenSMSSpamOne = probjLenSMSSpamOne;
	}

	public Float getProbjLenSMSSpamTwo() {
		return probjLenSMSSpamTwo;
	}

	public void setProbjLenSMSSpamTwo(Float probjLenSMSSpamTwo) {
		this.probjLenSMSSpamTwo = probjLenSMSSpamTwo;
	}

	public Float getProbjLenSMSNonSpamOne() {
		return probjLenSMSNonSpamOne;
	}

	public void setProbjLenSMSNonSpamOne(Float probjLenSMSNonSpamOne) {
		this.probjLenSMSNonSpamOne = probjLenSMSNonSpamOne;
	}

	public Float getProbjLenSMSNonSpamTwo() {
		return probjLenSMSNonSpamTwo;
	}

	public void setProbjLenSMSNonSpamTwo(Float probjLenSMSNonSpamTwo) {
		this.probjLenSMSNonSpamTwo = probjLenSMSNonSpamTwo;
	}
	
}