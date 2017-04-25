package SpamSMS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import MessageSMS.SMS;

public class SeparatingData {
	
	public static final String PATH_DATA = "F:\\Java\\Spam\\data\\train.txt";
	public static final String PATH_DATA_I = "F:\\Java\\Spam\\data\\data_1.txt";
	public static final String PATH_DATA_II = "F:\\Java\\Spam\\data\\data_2.txt";
	public static final String PATH_DATA_III = "F:\\Java\\Spam\\data\\data_3.txt";
	public static final String PATH_DATA_IV = "F:\\Java\\Spam\\data\\data_4.txt";
	public static final String PATH_DATA_PREDICT = "F:\\Java\\Spam\\data\\predict.txt";
	
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	
	private List<SMS> dataTrain = new ArrayList<SMS>();
	private List<SMS> dataTest = new ArrayList<SMS>();
	private Map<String,String> convertChar = new HashMap<String, String>();
	
	private String listChar = "ạảâẩẳằắầấăáàẫãậặẠẢÂẨẲẰẮẦẤĂÁÀẪÃẬẶởỞôÔỏỎổỔơớỚƠóÓốỐớỚỗỖòÒồỒõÕọỌộỘợỢỡỠđĐêÊếẾệỆềỀẽẼễỄẹẸéÉèÈẻẺểỂịỊỉỈĩĨìÌíÍưƯụỤữỮựỰứỨừỪửỬùÙúÚũŨủỦýÝỳỲỷỶỹỸỵỴ";                                                                             
 	private String listConv = "aaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAoOoOoOoOooOOoOoOoOoOoOoOoOoOoOoOoOdDeEeEeEeEeEeEeEeEeEeEeEiIiIiIiIiIuUuUuUuUuUuUuUuUuUuUuUyYyYyYyYyY";
	
	public SeparatingData() {
		
	}
	
	public void mapConvert() {
		int len = listChar.length();
		for(int i = 0;i < len;i++) {
			Character key = new Character(listChar.charAt(i));
			Character value = new Character(listConv.charAt(i));
			convertChar.put(key.toString(), value.toString());
		}
	}
	
	public String convertStringNotSign(String strings) {
		int len = strings.length();
		StringBuilder stringbBuilder = new StringBuilder();
		for(int i = 0;i < len;i++) {
			Character key = strings.charAt(i);
			if(key.equals('\u0323') || key.equals('\u0301') || key.equals('\u0309') || key.equals('\u0300') || key.equals('\u0303')){
				continue;
			}
			if(convertChar.containsKey(key.toString())) {
				stringbBuilder.append(convertChar.get(key.toString()));
			}else {
				stringbBuilder.append(key.toString());
			}
		}
		return stringbBuilder.toString();
	}
	
	public void speratingData(Integer n) throws IOException {
		int row = n/4;
		bufferedReader = new BufferedReader(new FileReader(new File("data\\train_swap.txt")));
		bufferedWriter = new BufferedWriter(new FileWriter(new File(PATH_DATA_I)));
		int countLine = 1;
		int flag = 1;
		String line = bufferedReader.readLine();
		while(line != null){
			int div = countLine%row;
			if( div == 0 && flag < 4){
				flag++;
				bufferedWriter.write(line);
				line = bufferedReader.readLine();
				countLine++;
				bufferedWriter.close();
				if(flag == 2){
					bufferedWriter = new BufferedWriter(new FileWriter(new File(PATH_DATA_II)));
				}
				if(flag == 3){
					bufferedWriter = new BufferedWriter(new FileWriter(new File(PATH_DATA_III)));
				}
				if(flag == 4){
					bufferedWriter = new BufferedWriter(new FileWriter(new File(PATH_DATA_IV)));
				}
			}
			
			if(flag == 1){
				bufferedWriter.write(line + "\n");
				line = bufferedReader.readLine();
				countLine++;
				continue;
			}
			
			if(flag == 2) {
				bufferedWriter.write(line  + "\n");
				line = bufferedReader.readLine();
				countLine++;
				continue;
			}
			if(flag == 3) {
				bufferedWriter.write(line  + "\n");
				line = bufferedReader.readLine();
				countLine++;
				continue;
			}
			if(flag == 4) {
				bufferedWriter.write(line  + "\n");
				line = bufferedReader.readLine();
				countLine++;
			}
		}
		bufferedReader.close();
		bufferedWriter.close();
	}
	
	public String[] processMessage(String line) {
		String[] result = new String[2];
		String strings[] = line.split(" ");
		List<String> message = new ArrayList<String>();
		for(int i = 1;i < strings.length;i++) {
			message.add(strings[i]);
		}
		result[0] = strings[0];
		result[1] = String.join(" ",message);
		return result;
	}
	
	public void readDataTrain(String pathI,String pathII,String pathIII) throws IOException{
		for(int i = 1;i <=3;i++){	
			if(i == 1){
				bufferedReader = new BufferedReader(new FileReader(new File(pathI)));
			}else if(i == 2) {
				bufferedReader = new BufferedReader(new FileReader(new File(pathII)));
			}else {
				bufferedReader = new BufferedReader(new FileReader(new File(pathIII)));
			}
			String line = bufferedReader.readLine();
			while(line != null){
				String data[] = processMessage(line);
				SMS sms = new SMS(data[1],data[1].length(),Integer.parseInt(data[0]));
				dataTrain.add(sms);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
		}
	}
	
	/*public void readDataTrain(String Path) throws IOException {
		bufferedReader = new BufferedReader(new FileReader(new File(Path)));
		String line = bufferedReader.readLine();
		while(line != null) {
			String data[] = processMessage(line);
			SMS sms = new SMS(data[1],data[1].length(),Integer.parseInt(data[0]));
			dataTrain.add(sms);
			line = bufferedReader.readLine();
		}
		bufferedReader.close();
	}
	*/
	public void ConvertToFile(String fileIn,String fileOut) throws IOException {
		bufferedReader = new BufferedReader(new FileReader(new File(fileIn)));
		bufferedWriter = new BufferedWriter(new FileWriter(new File(fileOut)));
		String line = bufferedReader.readLine();
		//int count = 1;
		while(line != null) {
			String strings = convertStringNotSign(line);
			bufferedWriter.write(strings);
			bufferedWriter.write("\n");
			line = bufferedReader.readLine();
			//count++;
			//if(count == 3) System.out.println(line);
		}
		
		bufferedReader.close();
		bufferedWriter.close();
		
	}
	
	public void readDataTrain(String pathI) throws IOException{
			bufferedReader = new BufferedReader(new FileReader(new File(pathI)));
			String line = bufferedReader.readLine();
			while(line != null){
				String data[] = processMessage(line);
				SMS sms = new SMS(data[1],data[1].length(),Integer.parseInt(data[0]));
				dataTrain.add(sms);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
	}
	
	public String processDataMessages(String line){
		String strings = line.replaceAll("\\.\\.\\.","???");
		String rex = "[,.\"()\\[\\]]";
		strings = strings.replaceAll(rex," ");
		strings = strings.replaceAll("\\?\\?\\?","...");
		strings = strings.replaceAll("\\s+"," ");
		strings = strings.toLowerCase();
		strings = strings.trim();
		return strings;
	}
	
	public void checkedPredict(String filepredict,String filetest) throws IOException {
		BufferedReader bufferedReaderPredict = new BufferedReader(new FileReader(new File(filepredict)));
		BufferedReader bufferedReaderTest = new BufferedReader(new FileReader(new File(filetest)));
		String LineOne = bufferedReaderPredict.readLine();
		String LineTwo = bufferedReaderTest.readLine();
		int count = 0;
		int numberTest = 0;
		while(LineOne != null && LineTwo != null) {
			numberTest++;
			Integer predict = Integer.parseInt(LineOne.split(" ")[0]);
			Integer test = Integer.parseInt(LineTwo.split(" ")[0]);
			if(test.equals(predict)) {
				count++;
			}
			LineOne = bufferedReaderPredict.readLine();
			LineTwo = bufferedReaderTest.readLine();
		}
		bufferedReaderPredict.close();
		bufferedReaderTest.close();
		System.out.println("Predict successfully is: " + ((float)count/numberTest)*100 + "% (" + count + "/" + numberTest +")");
	}
	
	public void swapData(int len) {
		int size = dataTrain.size();
		Random r = new Random();
		for(int i = 0;i < len;i++){
			int r1 = r.nextInt(size);
			int r2 = r.nextInt(size);
			if(r1 != r2) {
				SMS sms1 = dataTrain.get(r1);
				SMS sms2 = dataTrain.get(r2);
				dataTrain.set(r1, sms2);
				dataTrain.set(r2, sms1);
			}
		}
	}
	public void writeFile(String filename) throws IOException {
		bufferedWriter = new BufferedWriter(new FileWriter(new File(filename)));
		int size = dataTrain.size();
		for(int i = 0;i < size;i++) {
			StringBuilder strings = new StringBuilder();
			strings.append(dataTrain.get(i).getChecked());
			strings.append(" ");
			strings.append(dataTrain.get(i).getMessage());
			strings.append("\n");
			bufferedWriter.write(strings.toString());
		}
		bufferedWriter.close();
	}
	
	public static void main(String args[]) throws IOException {
		SeparatingData s = new SeparatingData();
		//System.out.println(s.processDataMessages("[QC] DV CHUYEN TIEN cua Viettel, Quy khach co the chuyen tien va nhan tien tai Nha hoac cac Diem chuyen tien cua Viettel tren toan quoc.LH 18008098 (mien phi).Tu choi QC, soan TC3 gui 199."));
//		String e = s.processDataMessages("QC Ban da nhan duoc LOI CHUC NAM MOI tu mot nguoi ban ( yeu cau giau ten ) Loi chuc kem theo qua tang goi DV");
//		System.out.println(e);
//		System.out.println(e.length());
		
	//	s.speratingData(300);
	/*	s.readDataTrain("data\\sms_one.txt");
		s.swapData(1000);
		s.writeFile("data\\train_swap.txt");*/
		s.checkedPredict("F:\\Java\\Spam\\data\\result.txt", "F:\\Java\\Spam\\data\\test_new.txt");
		//s.readDataTrain(SeparatingData.PATH_DATA_I, SeparatingData.PATH_DATA_II, SeparatingData.PATH_DATA_III);
		/*List<SMS> listSMS = s.getDataTrain();
		int size = listSMS.size();
		for(int i = 0;i < size;i++) {
			System.out.println("TT" + i +":");
			System.out.println(listSMS.get(i));
		}*/
		//s.processDataMessages("QC.Mot nguoi da goi truc tiep den tong dai yeu cau gui tang ban BAI HAT & ghi am LOI NHAN thoai gui den Ban.De nhan BAI HAT & nghe LOI NHAN soan: DK gui 7775");
	}

	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	public BufferedWriter getBufferedWriter() {
		return bufferedWriter;
	}

	public void setBufferedWriter(BufferedWriter bufferedWriter) {
		this.bufferedWriter = bufferedWriter;
	}

	public List<SMS> getDataTrain() {
		return dataTrain;
	}

	public void setDataTrain(List<SMS> dataTrain) {
		this.dataTrain = dataTrain;
	}
	
}