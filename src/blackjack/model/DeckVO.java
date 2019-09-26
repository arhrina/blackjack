package blackjack.model;

// 리스트를 만들기 위한 VO
public class DeckVO {
	protected String pattern;
	protected String nums;
	
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getNums() {
		return nums;
	}

	public void setNums(String nums) {
		this.nums = nums;
	}

	public DeckVO(String pattern, String nums) {
		this.pattern = pattern;
		this.nums = nums;
	}
}