package blackjack.service;

import java.util.List;

import blackjack.model.DeckVO;

// 블랙잭 내부 모듈
public interface BlackjackService {
	public void initDraw() throws Exception;
	public void draw() throws Exception;
	public void module() throws Exception;
	public void makeDeck(String[] pattern, String[] nums);
	// public int cal(List<DeckVO> listname);
	public int change(List<DeckVO> listname, boolean isOver);
	public void dealerDraw() throws Exception;
	public void storeResult(String wheter);
	void storeResult(String wheter, int playerCounter, int dealerCounter) throws Exception;
}