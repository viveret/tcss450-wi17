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

### 3/8/17
#### Android:
* Setup wizard (manual and easy mode)
* PiLexa finder searches local network
* User login and registration
* Can logout or close connection

#### Pi:
* Socket / daemon interface
* Added clock, weather, play music, send text, and search skills
* Added user authentication and registration
* Added even queue and poll system
* Improved and fixed bugs in invocation system
* Refactored to use standard org.json instead of Google's simple json library.

## User Stories Implemented
### 2/18/17
* Settings user story was touched on, not yet complete (can set host address, configuration file for pi).
* Timer user story touched on, not yet complete (can query for current time and date).
* These can be finished by next week

### 3/8/17
* Timer implemented
* Play music implemented
* Text implemented
* Look up ingredients (search) implemented
* Simple configuration implemented through setup wizard

## 1. Bugs / Issues fixed
### Phase I Rubric:
* For part 1, we now use the pi to process invocations as well as send back data if
need be. We also store user data on the pi. We download information including pi responses
from the pi service. I would consider the pi part to be a web service since we
use it very similarly.
* File extension: we can package it in a .txt as well. the .MD extension is for
markdown files, a standard way to store readme files.

### Peer reviews:
* I implemented a user registration and login system for both the app and pi.
* We also have now most user stories implemented.
* The app crashing has been fixed in most instances.
* I'll also make sure to provide more detailed steps into testing and running.
* I've incorporated these changed by making error messages better and making
and almost automated setup.

## 2. Unimplemented user stories
* Building block interface to create composite intents
* New skills can be added if compiled java files and json manifest files are put
in the right place, but I don't think that's what a user would want to do.
* Right now there isn't any kind of restriction per user but I think it would
be easy to add that feature.
* Adding events to a calendar is really complex with the different amount of
ways it could be invoked (remind me later..., make an event for...).

## 3. Storing data
Messages are saved using Sqlite in the MessageCache class, and user settings
like the connection information are stored using preferences in the AppHelper
class. We also store user data like a hashed password on the pi.

## 4. Web Services
The app uses the PiLexaProxyConnection as a sort of web service to the pi,
and the pi uses Google Speech Cloud services. A few of the skills also use
public APIs to retrive data (specifically the joke and weather skill).

## 5. Content sharing
I think that including the texting skill qualifies because the user uses
Pilexa to send text messages to other people.


## How to build / run
1. Follow instructions [here](pi/README.md) to setup and run the PiLexa instance.
2. Open up the android project with Android Studio.
3. Build, install onto device, and open the app.
4. If you know the IP of the PiLexa instance, choose manual and don't edit the port. Otherwise choose easy.
5. If you chose easy, wait for the PiLexa instance to appear. If it doesn't appear within 2 minutes, go back and 
add it manually. You will need the IP adress of the device that is running the PiLexa instance.
6. You can register a new account or login with u: viveret, p: test
7. Try sending messages "tell me a joke" or "play Frank Ocean".
