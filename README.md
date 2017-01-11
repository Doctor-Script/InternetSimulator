# InternetSimulator

InternetSimulator is simple tool for emulation transport layer protocols issues on localhost, such as long ping or splitting/merging of packets.

## Long ping emulation

Sometime you need to check how your application will work with perceptible ping, or debug with specified ping value.
In long ping mode application works as mediator which receives messages from one side (client or server), waits specified time (ping/2) and sends it to other side.

For the beginning you need to set server address and port in [Config](https://github.com/Doctor-Script/InternetSimulator/blob/master/src/tcp/config/Config.java).
Then client can connect to InternetSimulator. After accepting connection InternetSimulator connects to server and starts two threads: one for listening messages from client, other for messages from server.

Supported generation strategies:
- [SleepPingGenerator](https://github.com/Doctor-Script/InternetSimulator/blob/master/src/tcp/mediator/generators/SleepPingGenerator.java) use the simplest way to make delay - Thread.sleep(). Because of this, actual delay will will continuously growing. **(Deprecated)**.
- [FixedPingGenerator](https://github.com/Doctor-Script/InternetSimulator/blob/master/src/tcp/mediator/generators/FixedPingGenerator.java) puts received messages to queue and sends it further after specified time will expire. Can use only fixed delay.

## Emulation splitting/merging of packets (echo)

In a stream-based transport such as TCP/IP, received data is stored into a socket receive buffer. Unfortunately, the buffer of a stream-based transport is not a queue of packets but a queue of bytes. It means, even if you sent two messages as two independent packets, an operating system will not treat them as two messages but as just a bunch of bytes. Therefore, there is no guarantee that what you read is exactly what your remote peer wrote.

This is rare problem because most of network APIs has solved this problem, but sometimes you may need such tool.

In this [mode](https://github.com/Doctor-Script/InternetSimulator/blob/master/src/tcp/echo/RaggedEchoHandler.java) InternetSimulator works as server which receives message from client, multiplies (for emulation merged messages) it and sends back as pairs of messages: first part (some number of bytes) and rest. The size of "first part" varies from 1 to multiplied message size. So if receiving system works fine the message processor will see only complete packets.
