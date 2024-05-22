package it.racinghub.fe;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import it.racinghub.fe.costants.RHCostants;

public class StartBotApplication {

	public static void main(String[] args) throws TelegramApiException {
		
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		botsApi.registerBot(new RacingHubBot(RHCostants.BOTUSERNAME));
		
		System.out.println("#           ____             _             __  __      __       ____        __      \r\n"
					   	+  "#          / __ \\____ ______(_)___  ____ _/ / / /_  __/ /_     / __ )____  / /_     \r\n"
						+  "#   ______/ /_/ / __ `/ ___/ / __ \\/ __ `/ /_/ / / / / __ \\   / __  / __ \\/ __/_____\r\n"
						+  "#  /_____/ _, _/ /_/ / /__/ / / / / /_/ / __  / /_/ / /_/ /  / /_/ / /_/ / /_/_____/\r\n"
						+  "#       /_/ |_|\\__,_/\\___/_/_/ /_/\\__, /_/ /_/\\__,_/_.___/  /_____/\\____/\\__/       \r\n"
						+  "#                                /____/                                             ");
		
		System.out.println("Started Application with success!");
	}

}
