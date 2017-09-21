public class ESPDevice implements Comparable<ESPDevice> {
	
	String macAddrs;
	int signalStrength;
	String SSID;
	
	public ESPDevice( String macAddrs, int signalStrength, String SSID) {
		this.macAddrs = macAddrs;
		this.signalStrength = signalStrength;
		this.SSID = SSID;
	}
	 
	@Override
	public String toString() {
		return "Mac Address: " + macAddrs + "\n"
			  +"NodeSignal : " + signalStrength + "\n"
		      +"      SSID : " + SSID + "\n";
	}
	
	@Override
	public boolean equals(Object c) {
	    if((c instanceof ESPDevice) == false) {
	        return false;
	    }

	    ESPDevice that = (ESPDevice)c;
	    return this.macAddrs.equals(that.macAddrs) && this.macAddrs.equals(that.macAddrs);
	}
	
	@Override
	public int hashCode(){
		return SSID.hashCode() + macAddrs.hashCode();
	}
	
	@Override
	public int compareTo(ESPDevice anotherDevice)  {
		if (!(anotherDevice instanceof ESPDevice))
			throw new ClassCastException("A Person object expected.");
	  
		return this.signalStrength - anotherDevice.signalStrength;    
	}

}