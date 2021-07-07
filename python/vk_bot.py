from vk_api import VkUpload
from vk_api.bot_longpoll import VkBotLongPoll, VkBotEventType
from vk_api.keyboard import VkKeyboard, VkKeyboardColor

import config
import requests
import vk_api
import random

session = requests.Session()
vk_session = vk_api.VkApi(token="TOKEN_API")

try:
    # vk_session.auth(token_only=True)

    vk = vk_session.get_api()
    long_poll = VkBotLongPoll(vk_session, 194736647)

    keyboard = VkKeyboard(one_time=True)
    keyboard.add_button("Кнопка 1", color=VkKeyboardColor.DEFAULT)
    keyboard.add_button("Кнопка 2", color=VkKeyboardColor.NEGATIVE)
    keyboard.add_button("Кнопка 3.1", color=VkKeyboardColor.POSITIVE)
    keyboard.add_line()
    keyboard.add_button("Кнопка 4", color=VkKeyboardColor.PRIMARY)
    keyboard.add_line()
    keyboard.add_location_button()
    keyboard.add_line()
    keyboard.add_vkpay_button(hash="action=pay-to-user&amount=100&user_id=23382988")
    keyboard.add_line()
    keyboard.add_vkpay_button(hash="action=transfer-to-group&group_id=194736647&aid=7439933")

    for event in long_poll.listen():
        if event.type == VkBotEventType.MESSAGE_NEW and event.obj['message']['from_id']:
            print(event)
            print(str(event.obj['message']['from_id']) + " " + event.obj['message']['text'])
            vk.messages.send(user_id=event.obj['message']['from_id'], message="Привет",
                             random_id=random.randint(1, 10000), keyboard=keyboard.get_keyboard())
        else:
            print(event)

    # friends = vk.friends.get()
    # print(friends)
    #
    # # 271958270
    # vk_upload = VkUpload(vk_session)
    #
    # result = vk_upload.photo("/Users/borisbockarev/Desktop/BeTryBot/Test Doc/test_image.png", album_id=271958270)
    # print(result)
    #
    # vk.wall.post(message="Test from bot", attachment='photo{}_{}'.format(result[0]['owner_id'], result[0]['id']))
except vk_api.AuthError as error:
    print(error)

# 8301df70265536a93edee972a184a06cec771764e2a0a274fea8d7b55f618972d6903ebef4bc4f377b86a
# https://oauth.vk.com/authorize?client_id=7439933&scope=messages,notifications&response_type=token
