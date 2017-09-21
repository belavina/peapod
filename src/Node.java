import java.util.List;

public class Node {
	List<ESPDevice> esp;
	
	public Node (List <ESPDevice> esp) {
		this.esp = esp;
	}

	@Override
	public String toString() {
		return esp.toString();
	}

}
