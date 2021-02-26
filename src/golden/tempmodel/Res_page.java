package golden.tempmodel;

import java.util.ArrayList;
import java.util.List;

import golden.model.Resource;

public class Res_page {
    private int total;
    private List<Resource> resource = new ArrayList<Resource>();
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<Resource> getResource() {
		return resource;
	}
	public void setResource(List<Resource> resource) {
		this.resource = resource;
	} 
}
