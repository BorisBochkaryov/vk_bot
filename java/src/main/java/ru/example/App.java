package ru.example;

import com.vk.api.sdk.actions.Messages;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class App {

    public static void main(String[] args) throws ClientException, ApiException, InterruptedException {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);

        Random random = new Random();

        Keyboard keyboard = new Keyboard();

        List<List<KeyboardButton>> allKey = new ArrayList<>();

        List<KeyboardButton> line1 = new ArrayList<>();
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Btn 1Btn 1Btn 1Btn 1Btn 1Btn 1Btn 1Btnbt").setType(KeyboardButtonActionType.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Btn 2").setType(KeyboardButtonActionType.TEXT)).setColor(KeyboardButtonColor.NEGATIVE));

        List<KeyboardButton> line2 = new ArrayList<>();
        line2.add(new KeyboardButton().setAction(new KeyboardButtonAction().setType(KeyboardButtonActionType.LOCATION)));

        allKey.add(line1);
        allKey.add(line2);
        keyboard.setButtons(allKey);

        GroupActor actor = new GroupActor(194736647, "8301df70265536a93edee972a184a06cec771764e2a0a274fea8d7b55f618972d6903ebef4bc4f377b86a");
        Integer ts = vk.messages().getLongPollServer(actor).execute().getTs();

        while (true) {
            MessagesGetLongPollHistoryQuery historyQuery = vk.messages().getLongPollHistory(actor).ts(ts);

            List<Message> messages = historyQuery.execute().getMessages().getItems();
            if (!messages.isEmpty()) {
                messages.forEach(message -> {
                    if (!message.isOut()) {
                        System.out.println(message.toString());
                        try {
                            vk.messages().send(actor).message("Test").userId(message.getFromId())
                                    .randomId(random.nextInt(10000))
                                    .keyboard(keyboard).execute();
                        } catch (ApiException | ClientException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
            Thread.sleep(500);
        }

    }

}
