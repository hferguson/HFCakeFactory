package ca.hferguson.spring.misc;

public class NavLink {
	public String link;
	public String displayName;
	public boolean current = false;
	
	public NavLink() {}
	public NavLink(String link, String disp) {
		this.link = link;
		this.displayName = disp;
	}
}
