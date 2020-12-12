
public class ParseToken {
	private char token;
	private int numVal;
	private int currStep;
	
	
	public ParseToken() {
		token = ' ';
		numVal=0;
		currStep=0;
	}
	
	public ParseToken(char tok, int numV) {
		token = tok;
		numVal=numV;
		currStep=0;
	}


	public char getToken() {
		return token;
	}


	public void setToken(char token) {
		this.token = token;
	}


	public int getNumVal() {
		return numVal;
	}


	public void setNumVal(int numVal) {
		this.numVal = numVal;
	}


	public int getCurrStep() {
		return currStep;
	}


	public void setCurrStep(int currStep) {
		this.currStep = currStep;
	}
	
	public String toString() {
		return token + "=" + numVal + ":" + currStep;
	}

}
