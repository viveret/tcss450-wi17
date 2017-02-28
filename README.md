Using Google Speech for Voice to Text, and Stanford-CoreNLP for language processing.

## Features Implemented
### 2/18/17
#### Android:
* Navigation drawer
* Settings -> able to change host address
* Can chat with pi
* PiLexa proxy interface to interact with pi through web api

#### Pi:
* PHP / Web interface (no current direct access through sockets)
* Speech recognition through Google Speech
* Natural Language Processing through Stanford CoreNLP
* Text to speech using MarryTTS
* Plugin based system for input methods (Google speech, Sphinx4, command line, socket / http )
* Plugin based system for skills
* Can easily add new skills + interact with pi
* PiLexa uses AI to direct what the user says to a skill
* Settings to run PiLexa on cssgate or on a machine with a speaker / microphone
* Flexible and extendible configuration service.

## User Stories Implemented
### 2/18/17
* Settings user story was touched on, not yet complete (can set host address, configuration file for pi).
* Timer user story touched on, not yet complete (can query for current time and date).
* These can be finished by next week

## How to build / run
1. Follow instructions [here](pi/README.md) to setup the pi / server.
2. Open up the android project with Android Studio.
3. Build & install onto device.
4. Open app and go to Settings > General and edit Host Address to where the pi package was published to.
5. Exit and restart app.
6. Try sending messages "tell me a joke" or "tell me the time".

## References
https://www.google.com/search?q=bb8+programmable&safe=off&tbm=isch&tbs=rimg:CWSCwxFzcHXPIjhdPwYA068ohAJvS_1keYlRXUKOEJOGKFf-v81Hoa3jIb9JAC3n18-BDdXoBpsMBtY6uzo95Zt9_12SoSCV0_1BgDTryiEEcCw4g1ihFOZKhIJAm9L-R5iVFcRYqzpaMtCTfMqEglQo4Qk4YoV_1xF3OuQB6dN31SoSCa_1zUehreMhvEZOEk0m34gf9KhIJ0kALefXz4EMRL5xFObyVTbsqEgl1egGmwwG1jhGCv9mtAaKuXioSCa7Oj3lm33_1ZERs-1rfVLTsx&tbo=u&sa=X&ved=0ahUKEwjE3K7Z0dnRAhUIs1QKHcR_BSoQ9C8ICQ&biw=1368&bih=645&dpr=1#imgrc=67GzdcPmB46drM%3A
