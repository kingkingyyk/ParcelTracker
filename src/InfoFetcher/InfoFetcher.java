package InfoFetcher;

public abstract class InfoFetcher {
	public static class NoTrackingException extends Exception {
		private static final long serialVersionUID = -5412427050254182301L;

		public NoTrackingException (String msg) {super(msg);}
	}
	
	public static boolean hasWord(String s) {
		boolean contains=false;
		for (char c : s.toCharArray()) contains|=(Character.isAlphabetic(c) || Character.isDigit(c));
		return contains;
	}
	
	private String trackingNumber="";
	
	public InfoFetcher (String n) {
		this.trackingNumber=n;
	}

	public void setTrackingNumber (String t) {this.trackingNumber=t;}
	public String getTrackingNumber () {return this.trackingNumber;}
	public abstract String getType();
	public abstract void fetchInfo ()  throws NoTrackingException, Exception;
	
	public String toString() {
		return this.getType();
	}
}