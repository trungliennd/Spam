package SpamSMS;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import MessageSMS.SMS;
import MessageSMS.Word;

public class Dictionary {
	
	private Map<String,Word> dictionary = new HashMap<String,Word>();
	private SeparatingData data;
	private Integer sizeDictionary = 0;
	private Integer totalTermSpam = 0;
	private Integer totalTermNonSpam = 0;
	
	public Dictionary(){
		
	}
	
	public Dictionary(String pathI,String pathII,String pathIII){
		data = new SeparatingData();
		/*
		 * Tinh total into Span and NonSpam
		 */
		// NonSpam
		try {
			data.readDataTrain(pathI, pathII, pathIII);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		makeDictionary();
	}
	
	public Dictionary(String path) {
		
		data = new SeparatingData();
		try{
			data.readDataTrain(path);
		}catch(IOException e) {
			e.printStackTrace();
		}
		makeDictionary();
	}
	
	public String[] getListNGramToMessages(SMS sms){
		String string = data.processDataMessages(sms.getMessage());
		int len = string.length();
		String result[] = new String[len - 2];
		for(int i = 0;i < len - 2;i++ ){
			result[i] = string.substring(i,i+3);
		}
		return result;
	}
	
	public String[] getListNGramToMessages(String sms){
		String string = sms;
		int len = string.length();
		String result[] = new String[len - 2];
		for(int i = 0;i < len - 2;i++ ){
			result[i] = string.substring(i,i+3);
		}
		return result;
	}
	
	public void makeDictionary() {
		List<SMS> listSMS = data.getDataTrain();
		int size = listSMS.size();
		for(int i = 0 ;i < size;i++) {
			String[] n_grams = getListNGramToMessages(listSMS.get(i));
			for(int j = 0;j < n_grams.length;j++){
				if(!dictionary.containsKey(n_grams[j])) {
					sizeDictionary++;
					Word word = new Word(n_grams[j]);
					if(listSMS.get(i).getChecked() == 1){
						word.countNonSpam();
					}else {
						word.countSpam();
					}
					dictionary.put(n_grams[j],word);
				}else {
					if(listSMS.get(i).getChecked() == 1){
						dictionary.get(n_grams[j]).countNonSpam();
					}else {
						dictionary.get(n_grams[j]).countSpam();
					}
				}
			}
			if(listSMS.get(i).getChecked() == 1) {
				totalTermNonSpam += n_grams.length;
			}else {
				totalTermSpam += n_grams.length;
			}
		}
	}

	public static void main(String args[]) {
		Dictionary dictionary = new Dictionary(SeparatingData.PATH_DATA_I,SeparatingData.PATH_DATA_II,SeparatingData.PATH_DATA_III);
		dictionary.makeDictionary();
		String s[] = dictionary.getListNGramToMessages("");
		System.out.println("length is: " + s.length);
		for(int i = 0;i< s.length;i++) {
			System.out.println(s[i]);
		}
		//System.out.println(dictionary.getSizeDictionary());
		//System.out.println(dictionary);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder strings = new StringBuilder();
		Set<Entry<String, Word>> set = dictionary.entrySet();
		Iterator<Entry<String, Word>> it = set.iterator();
		System.out.println("Size is: " +set.size());
		while(it.hasNext()) {
			Entry<String, Word> s = it.next();
			strings.append("Word is:");
			strings.append(s.getKey());
			strings.append("\nTotal Spam is: ");
			strings.append(s.getValue().getSpamCount());
			strings.append("\nTotal Non Spam is: ");
			strings.append(s.getValue().getNonSpamCount());
			strings.append("\n\n");
 		}
		return strings.toString();
	}
	

	public Map<String,Word> getDictionary() {
		return dictionary;
	}

	public void setDictionary(Map<String,Word> dictionary) {
		this.dictionary = dictionary;
	}

	public SeparatingData getData() {
		return data;
	}

	public void setData(SeparatingData data) {
		this.data = data;
	}

	public Integer getSizeDictionary() {
		return sizeDictionary;
	}

	public void setSizeDictionary(Integer sizeDictionary) {
		this.sizeDictionary = sizeDictionary;
	}

	public Integer getTotalTermSpam() {
		return totalTermSpam;
	}

	public void setTotalTermSpam(Integer totalTermSpam) {
		this.totalTermSpam = totalTermSpam;
	}

	public Integer getTotalTermNonSpam() {
		return totalTermNonSpam;
	}

	public void setTotalTermNonSpam(Integer totalTermNonSpam) {
		this.totalTermNonSpam = totalTermNonSpam;
	}
	
}
