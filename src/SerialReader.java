import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SerialReader {
	
	private static final String FILENAME = "/dev/ttyUSB0";

	public static List<ESPDevice> read(Boolean debug) {

		BufferedReader br = null;
		FileReader fr = null;
		List<String> output = new ArrayList<String>();
		List<ESPDevice> esp = new ArrayList<ESPDevice>();
		
		long timestamp;
		String MACAddress;
		String[] ssid;
		String[] mac;
		String[] signal;
		
		while (esp.size() == 0) {
			try {
				fr = new FileReader(FILENAME);
				br = new BufferedReader(fr);
				
				BaseFunc.sleep(1200);

				String sCurrentLine;

				output = new ArrayList<String>();

				while (br.ready()) {
					BaseFunc.sleep(100);

					sCurrentLine = br.readLine();
					if (!sCurrentLine.trim().isEmpty()) {
						output.add(sCurrentLine);
					}
					
					if (debug) {
						System.out.println(sCurrentLine);
					}
				}

				System.out.print("*");

				if (output.size() > 3) {
					
					timestamp = Long.parseLong(output.get(0));
					MACAddress = output.get(1);
					ssid = output.get(2).split(",");
					signal = output.get(3).split(",");
					mac = output.get(4).split(",");
					
					if (ssid.length != signal.length || ssid.length != signal.length) {
						System.out.println("WARN: Number of devices is not equal to number of MAC Addresses");
					}

					for (int i = 0; i < ssid.length && i < mac.length && i < signal.length; i++) {
						esp.add(new ESPDevice(mac[i], Integer.parseInt(signal[i]), ssid[i]));
					}
				}
				
			} catch (IOException e) {
				System.out.println("Error reading USB");
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();

					if (fr != null)
						fr.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
		}
		
		return esp;
		
	}
	
	
}