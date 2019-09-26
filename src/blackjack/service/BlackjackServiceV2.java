package blackjack.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import blackjack.model.DeckVO;

// 딜러는 컴퓨터, 플레이어는 플레이어로서 게임 참여

// 추가할만한 사항으로는 플레이어 수를 게임 시작시에 사용자로부터 입력받고(max 8, min 2),
// 플레이어 수만큼 카드를 분배, 게임. AI 플레이어의 hit 여부 알고리즘 만들기,
// 사용자도 딜러를 할 수 있는 모드(혹은 랜덤으로)
// 로그에 점수 추가

public class BlackjackServiceV2 extends BlackjackServiceV1 {

	List<DeckVO> card; // 두 개의 값(문양, A~K까지의 카드 숫자)로 카드 한장을 만드는 리스트
	BufferedReader br;
	List<DeckVO> dealer;
	List<DeckVO> player;
	private final int INITDRAW = 2;
	private int sum;
	private final String strFileName = "src/blackjack/result.txt";
	FileWriter fw;
	PrintWriter pw;
	private int playerDrawCounter;
	private int dealerDrawCounter;

	public BlackjackServiceV2() {
		br = new BufferedReader(new InputStreamReader(System.in));
		try {
			fw = new FileWriter(strFileName, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initDraw() throws Exception {
		// TODO Auto-generated method stub
		String path = "src/blackjack/init.txt";
		PrintWriter make = new PrintWriter(path);
		make.println("DqHqCqS");
		make.println("Aq2q3q4q5q6q7q8q9q10qJqQqK");
		make.close();
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);

		String[] pattern = br.readLine().split("q");
		String[] nums = br.readLine().split("q");
		this.makeDeck(pattern, nums);
		br.close();
		fr.close();
		playerDrawCounter = 0;
		dealerDrawCounter = 0;
		/*
		 * for(deckVO dVO : card) { card 리스트 안에 들어가있는 모양, 숫자 확인
		 * System.out.printf("%s %s\n", dVO.getPattern(), dVO.getNums()); }
		 */
		Collections.shuffle(card);
		for (int i = 0; i < INITDRAW; i++) {
			// 딜러 2장, 플레이어 2장 분배하고 리스트에서 분배한 카드 삭제
			dealer.add(card.get(0));
			card.remove(0);
			player.add(card.get(0));
			card.remove(0);
		}
		DeckVO dVO = dealer.get(1);
		Thread.sleep(1000);
		System.out.printf("딜러의 카드 한장은 %s문양 %s입니다\n", dVO.getPattern(), dVO.getNums());
		for (DeckVO pVO : player) {
			Thread.sleep(1000);
			System.out.printf("당신의 카드는 %s문양 %s입니다\n", pVO.getPattern(), pVO.getNums());
		}
		if (this.change(player, false) == 21) {
			Thread.sleep(1000);
			System.out.println("블랙잭입니다! 이겼습니다!");
			this.storeResult("승리", playerDrawCounter, dealerDrawCounter);
			this.module();
		} else if (this.change(dealer, false) == 21) {
			Thread.sleep(1000);
			System.out.println("딜러가 블랙잭입니다. 패배하였습니다");
			this.storeResult("패배", playerDrawCounter, dealerDrawCounter);
			this.module();
		}
	}

	@Override
	public void draw() throws Exception {
		// TODO Auto-generated method stub
		if (this.change(player, false) < 21) {
			System.out.println(card.get(0).getPattern() + card.get(0).getNums());
			player.add(card.get(0));
			card.remove(0);
			System.out.println("남은 덱의 카드수 : " + card.size());
			Thread.sleep(1000);
			System.out.println("당신의 점수 : " + change(player, false));
			Thread.sleep(1000);
		} else if (this.change(player, true) < 21) {
			System.out.println(card.get(0).getPattern() + card.get(0).getNums());
			player.add(card.get(0));
			card.remove(0);
			System.out.println("남은 덱의 카드수 : " + card.size());
			Thread.sleep(1000);
			System.out.println("당신의 점수 : " + change(player, true));
			Thread.sleep(1000);
		}

		if (change(player, false) > 21) {
			if (change(player, true) > 21) {
				Thread.sleep(1000);
				System.out.println("21점을 초과하여 패배하였습니다");
				try {
					this.storeResult("패배", playerDrawCounter, dealerDrawCounter);
					this.module();
				} catch (IOException e) { // TODO Auto-generated catch
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void dealerDraw() throws Exception { // 딜러 점수 16 이하면 넘을 때까지 드로우, 21 넘으면 A 여부 확인
		while ((this.change(dealer, false) <= 16) || (this.change(dealer, true) <= 16)) {
			Thread.sleep(1000);
			System.out.println("딜러의 점수가 16점 이하이므로 드로우를 합니다");
			System.out.println(card.get(0).getPattern() + card.get(0).getNums());
			dealer.add(card.get(0));
			card.remove(0);
			dealerDrawCounter++;
			if ((this.change(dealer, false) < 21)) {
				Thread.sleep(1000);
				System.out.println("남은 덱의 카드수 : " + card.size());
				Thread.sleep(1000);
				System.out.println("딜러의 점수 : " + change(dealer, false));
			} else if ((this.change(dealer, true) < 21)) {
				Thread.sleep(1000);
				System.out.println("남은 덱의 카드수 : " + card.size());
				Thread.sleep(1000);
				System.out.println("딜러의 점수 : " + change(dealer, true));
			}
		}

		if (this.change(dealer, false) > 21) {
			if (this.change(dealer, true) > 21) {
				Thread.sleep(1000);
				System.out.println("딜러가 21점을 초과하여 당신이 승리하였습니다");
				try {
					this.storeResult("승리", playerDrawCounter, dealerDrawCounter);
					this.module();
				} catch (IOException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public int change(List<DeckVO> listname, boolean isOver) { // 문자열 변환해서 합 리턴. isOver는 21 초과값일 경우 A 처리
		int rt = 0;
		sum = 0;
		for (int i = 0; i < listname.size(); i++) {
			DeckVO dVO = listname.get(i);
			if (dVO.getNums().equals("A")) {
				if (isOver)
					rt = 1;
				else
					rt = 11;
			} else if (dVO.getNums().equals("2"))
				rt = 2;
			else if (dVO.getNums().equals("3"))
				rt = 3;
			else if (dVO.getNums().equals("4"))
				rt = 4;
			else if (dVO.getNums().equals("5"))
				rt = 5;
			else if (dVO.getNums().equals("6"))
				rt = 6;
			else if (dVO.getNums().equals("7"))
				rt = 7;
			else if (dVO.getNums().equals("8"))
				rt = 8;
			else if (dVO.getNums().equals("9"))
				rt = 9;
			else if (dVO.getNums().equals("10"))
				rt = 10;
			else if (dVO.getNums().equalsIgnoreCase("J"))
				rt = 10;
			else if (dVO.getNums().equalsIgnoreCase("Q"))
				rt = 10;
			else if (dVO.getNums().equalsIgnoreCase("K"))
				rt = 10;
			sum += rt;
		}
		return sum;
	}

	/*
	 * @Override public int cal(List<DeckVO> listname) { // 21점 이상일 때 A처리법 if (sum >
	 * 21) { DeckVO dVO; for (int i = 0; i < listname.size(); i++) { dVO =
	 * listname.get(i); if (dVO.getNums().equalsIgnoreCase("A")) { sum -= 10; } } }
	 * return sum; }
	 */

	@Override
	public void makeDeck(String[] pattern, String[] nums) { // 덱 52장을 만드는 메소드
		// TODO Auto-generated method stub
		for (int i = 0; i < pattern.length; i++) {
			for (int j = 0; j < nums.length; j++) {
				DeckVO dVO = new DeckVO(pattern[i], nums[j]);
				card.add(dVO);
			}
		}
	}

	@Override
	public void storeResult(String wheter, int playerCounter, int dealerCounter) throws Exception {
		// TODO Auto-generated method stub
		pw = new PrintWriter(fw);
		Date date = new Date();
		String s = date.toString();
		this.cls();
		pw.printf("플레이시각 : %s\n승패여부 : %s\n플레이어 드로우횟수 : %d\n딜러 드로우횟수 : %d\n\n\n", s, wheter, playerCounter,
				dealerCounter);
		pw.flush();
		pw.close();
		System.out.println("간단한 게임로그는 " + strFileName +"경로에 저장됩니다");
		System.out.println("5초 뒤 새로운 게임이 자동으로 시작됩니다");
		Thread.sleep(2000);
		for(int i = 0; i < 3; i++) {
			this.cls();
			System.out.println(3 - i);
			Thread.sleep(1000);
		}
		
	} // storeResult

	@Override
	public void module() throws Exception {
		// TODO Auto-generated method stub

		while (true) {

			this.cls();
			System.out.println("B");
			Thread.sleep(200);
			this.cls();
			System.out.println("B L");
			Thread.sleep(200);
			this.cls();
			System.out.println("B L A");
			Thread.sleep(200);
			this.cls();
			System.out.println("B L A C");
			Thread.sleep(200);
			this.cls();
			System.out.println("B L A C K");
			Thread.sleep(200);
			this.cls();
			System.out.println("B L A C K J");
			Thread.sleep(200);
			this.cls();
			System.out.println("B L A C K J A");
			Thread.sleep(200);
			this.cls();
			System.out.println("B L A C K J A C");
			Thread.sleep(200);
			this.cls();
			System.out.println("B L A C K J A C K");
			Thread.sleep(200);
			System.out.println("게임시작은 1만 입력\n종료하려면 아무키나 입력하세요");
			Thread.sleep(1000);
			System.out.print("입력 >> ");

			boolean boolMenu = false;
			String strMenu;

			strMenu = br.readLine();
			if (strMenu.equals("1"))
				boolMenu = true;

			if (!boolMenu) {
				Thread.sleep(1000);
				System.out.println("블랙잭을 종료합니다");
				break;
			}

			card = new ArrayList<DeckVO>();
			dealer = new ArrayList<DeckVO>();
			player = new ArrayList<DeckVO>();

			System.out.println("덱을 만들어 카드를 분배합니다");
			Thread.sleep(1000);
			this.cls();
			System.out.println("덱 섞는 중");
			Thread.sleep(500);
			this.cls();
			System.out.println("덱 섞는 중 .");
			Thread.sleep(500);
			this.cls();
			System.out.println("덱 섞는 중 . .");
			Thread.sleep(500);
			this.cls();
			System.out.println("덱 섞는 중 . . .");
			this.initDraw();
			System.out.println("남은 덱의 카드수 : " + card.size());
			Thread.sleep(1000);
			System.out.println("딜러의 점수 : " + change(dealer, false));
			Thread.sleep(1000);
			System.out.println("당신의 점수 : " + change(player, false));
			Thread.sleep(1000);
			while (true) {
				System.out.println("1.추가드로우 2.추가드로우 안함");
				Thread.sleep(1000);
				System.out.print("입력 >> ");
				String strDraw;
				strDraw = br.readLine();
				if (strDraw.equals("1")) {
					this.draw();
					playerDrawCounter++;
				} else if (strDraw.equals("2")) {
					break;
				}
			}
			this.dealerDraw();
			if ((this.change(player, false) <= 21) && (this.change(dealer, false) <= 21)) {
				if ((21 - (this.change(player, false))) > (21 - this.change(dealer, false))) {
					Thread.sleep(1000);
					System.out.println("딜러의 점수 : " + this.change(dealer, false));
					Thread.sleep(1000);
					System.out.println("당신의 점수 : " + this.change(player, false));
					Thread.sleep(1000);
					System.out.println("딜러가 당신보다 21에 가까우므로 승리했습니다");
					this.storeResult("패배", playerDrawCounter, dealerDrawCounter);
					this.module();
				} else {
					Thread.sleep(1000);
					System.out.println("딜러의 점수 : " + this.change(dealer, false));
					Thread.sleep(1000);
					System.out.println("당신의 점수 : " + this.change(player, false));
					Thread.sleep(1000);
					System.out.println("당신이 딜러보다 21에 가까우므로 승리했습니다");
					this.storeResult("승리", playerDrawCounter, dealerDrawCounter);
					this.module();
				}
			} else if ((this.change(player, false) <= 21) && (this.change(dealer, true) <= 21)) {
				if ((21 - (this.change(player, false))) > (21 - this.change(dealer, true))) {
					Thread.sleep(1000);
					System.out.println("딜러의 점수 : " + this.change(dealer, true));
					Thread.sleep(1000);
					System.out.println("당신의 점수 : " + this.change(player, false));
					Thread.sleep(1000);
					System.out.println("딜러가 당신보다 21에 가까우므로 승리했습니다");
					this.storeResult("패배", playerDrawCounter, dealerDrawCounter);
					this.module();
				} else {
					Thread.sleep(1000);
					System.out.println("딜러의 점수 : " + this.change(dealer, true));
					Thread.sleep(1000);
					System.out.println("당신의 점수 : " + this.change(player, false));
					Thread.sleep(1000);
					System.out.println("당신이 딜러보다 21에 가까우므로 승리했습니다");
					this.storeResult("승리", playerDrawCounter, dealerDrawCounter);
					this.module();
				}
			} else if ((this.change(player, true) <= 21) && (this.change(dealer, false) <= 21)) {
				if ((21 - (this.change(player, true))) > (21 - this.change(dealer, false))) {
					Thread.sleep(1000);
					System.out.println("딜러의 점수 : " + this.change(dealer, true));
					Thread.sleep(1000);
					System.out.println("당신의 점수 : " + this.change(player, false));
					Thread.sleep(1000);
					System.out.println("딜러가 당신보다 21에 가까우므로 승리했습니다");
					this.storeResult("패배", playerDrawCounter, dealerDrawCounter);
					this.module();
				} else {
					Thread.sleep(1000);
					System.out.println("딜러의 점수 : " + this.change(dealer, true));
					Thread.sleep(1000);
					System.out.println("당신의 점수 : " + this.change(player, false));
					Thread.sleep(1000);
					System.out.println("당신이 딜러보다 21에 가까우므로 승리했습니다");
					this.storeResult("승리", playerDrawCounter, dealerDrawCounter);
					this.module();
				}
			} else if ((this.change(player, true) <= 21) && (this.change(dealer, true) <= 21)) {
				if ((21 - (this.change(player, true))) > (21 - this.change(dealer, true))) {
					Thread.sleep(1000);
					System.out.println("딜러의 점수 : " + this.change(dealer, true));
					Thread.sleep(1000);
					System.out.println("당신의 점수 : " + this.change(player, true));
					Thread.sleep(1000);
					System.out.println("딜러가 당신보다 21에 가까우므로 승리했습니다");
					this.storeResult("패배", playerDrawCounter, dealerDrawCounter);
					this.module();
				} else {
					Thread.sleep(1000);
					System.out.println("딜러의 점수 : " + this.change(dealer, true));
					Thread.sleep(1000);
					System.out.println("당신의 점수 : " + this.change(player, true));
					Thread.sleep(1000);
					System.out.println("당신이 딜러보다 21에 가까우므로 승리했습니다");
					this.storeResult("승리", playerDrawCounter, dealerDrawCounter);
					this.module();
				}
			}
		}
	} // out of module method
	
	public void cls() {
		// new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		for (int i = 0; i < 50; ++i) System.out.println();
	}
}
