package blackjack.exec;

import blackjack.service.BlackjackService;
import blackjack.service.BlackjackServiceV2;

public class BlackjackExec_02 {

	public static void main(String[] args) {
		BlackjackService bs = new BlackjackServiceV2();
		// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			bs.module();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
