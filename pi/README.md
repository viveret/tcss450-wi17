# PiLexa Pi Module

## Building
1. Run `gradle build` inside directory.
    This will also install dependencies.
3. Before the next step, look over the [configuration file](res/pilexa-config.json) and edit to fit your needs.
4. For instance, if you want to add voice recognition, add
`"com.viveret.pilexa.pi.inputmethods.GoogleSpeechPiLexaProxy"` to the input methods.

### Running locally
1. Just run `gradle run` on Windows and Linux machines (or `gradle runMac` on iOS).
2. Wait for `Done starting inputs` to appear in the console.
3. You can now use the app or do the following step.
4. Maybe add some of the input methods available in the [configuration file](res/pilexa-config.json) if you want voice input or a verbal response.

### Testing locally without app
Run `echo '{"op":"interpret", "msg":"tell me the time"}' | netcat 127.0.0.1 <port, default is 11823>` to test if PiLexa
gets network messages. 

## Definitions
### Skills
Skills are collections of intents. They are essentially a feature set that PiLexa understands. To create a new Skill, copy and modify an existing [manifest file](res/skills/com/viveret/pilexa/pi/defaultskills/clockskill/manifest.json) in a directory matching the class path of your Java code. To implement the invocations, follow [some sample intents](src/com/viveret/pilexa/pi/defaultskills/clockskill/) that implement `InvocationProcessor`.

### Intents
Intents are individual features or functions the skill can perform. For instance, a Clock skill has timer and tell time intents. They are sent the same arguments every time.

### Invocations
Invocations are essentially the different ways to invoke an intent. For instance, one could ask "Set an alarm X minutes from now" or "Start a timer for X minutes" and the same operation will be performed. Invocations have the same amount and types of arguments as the Intent they invoke.

#### Invocation patterns
Invocation patterns are how PiLexa defines invocations and how they should be decided on. The basics are the following:
* Words and phrases that should be matched are kept as normal.
* Spots to ignore are marked by using `%%` (two percent signs).
* Spots to capture arguments are marked by `%label:type(s)%`. The label is the name of the argument when accessed later, and the type(s) is a pipe separated list of types to accept. For a single type, no pipe is needed, but for multiple, each must be separated by a `|`.

The following types are currently supported:
* `string` - capture any string / text data. Ends only at punctuation or when a following match or argument matches.
* `date` - date and time formats, including months and holidays. Some holidays aren't included, so if any holiday is needed use `date|string`.
* `int` - integers.
