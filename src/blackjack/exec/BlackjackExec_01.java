package blackjack.exec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import blackjack.service.BlackjackService;
import blackjack.service.BlackjackServiceV1;

public class BlackjackExec_01 {
	public static void main(String[] args) {
		BlackjackService bs = new BlackjackServiceV1();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			
			System.out.println("==========================");
			System.out.println("1.게임시작\n종료하려면 아무키나 입력하세요");
			System.out.print("입력 >> ");
			
			boolean boolMenu = false;
			String strMenu;


			try {
				strMenu = br.readLine();
				if (strMenu.equals("1"))
					boolMenu = true;

				if (!boolMenu) {
					System.out.println("블랙잭을 종료합니다");
					break;
				}
				bs.module();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
