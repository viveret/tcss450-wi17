# PiLexa Pi Module
## Skills
Skills are collections of intents. They are essentially a feature set that PiLexa understands. To create a new Skill, extend "AbstractSkill". Create a static constructor that calls "ConcretePiLexaService.registerSkill" with a new instance of your skill.

## Intents
Intents are individual features or functions the skill can perform. For instance, a Clock skill has timer and tell time intents. They are sent the same arguments every time.

## Invocations
Invocations are essentially the different ways to invoke an intent. For instance, one could ask "Set an alarm X minutes from now" or "Start a timer for X minutes" and the same operation will be performed. Invocations have the same amount and types of arguments as the Intent they invoke.

To create an invocation, pass an invocation pattern to "InvocationPattern.parse".

### Invocation patterns
Invocation patterns are how PiLexa defines invocations and how they should be decided on. The basics are the following:
* Words and phrases that should be matched are kept as normal.
* Spots to ignore are marked by using %% (two percent signs).
* Spots to capture arguments are marked by %label:type(s)%. The label is the name of the argument when accessed later, and the type(s) is a pipe separated list of types to accept. For a single type, no pipe is needed, but for multiple, each must be separated by a '|'.

The following types are currently supported:
* string - capture any string / text data. Ends only at punctuation or when a following match or argument matches.
* date - date and time formats, including months and holidays. Some holidays aren't included, so if any holiday is needed use 'date|string'.
* int - integers.
