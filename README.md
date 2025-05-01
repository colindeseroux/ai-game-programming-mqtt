# AI gamme programming MQTT

The aim of this project is just to create a wrapper so that we can use mqtt without having to implement it in the AI game programming course. This will avoid having to play every move in the game by hand and fully automate the games and run hundreds of simulations. To do this, the wrapper simply reads the standard output of the executed code and sends it to the mqtt broker, then writes what it receives to the standard input.

You can take inspiration from this and implement it directly in your code to save a few 10ths of a millisecond, but bearing in mind that you have 3 seconds to play a move, you'd better think about optimizing your mini-max.

## Installation

Java 17

---

### To launch locally

#### Logger

```sh
mvn install:install-file -Dfile='src/main/libs/logger-0.0.1-SNAPSHOT.jar' -DgroupId='fr.phenix333' -DartifactId=logger -Dversion='0.0.1-SNAPSHOT' -Dpackaging=jar
```

#### Launch

```
Run as you like from the command line or ide
```

---

### To launch the .jar

Arguments:

- mqtt.host=<test.mosquitto.org>
- mqtt.port=<1883>
- mqtt.user=\<Colin>
- mqtt.opponent=\<Jake>
- command=\<python3 test.py>

```sh
java -jar ai-game-programming-mqtt-0.0.1-SNAPSHOT.jar --host.port='test.mosquitto.org' --mqtt.port=1883 --mqtt.user=Colin --mqtt.opponent=Jake --command='java -jar src/test/test.jar'
```

All you have to do is send a message on the broker to **_awale/<mqtt.user>_** for it to receive it (in our case, it will return the date in nanoseconds on **_awale/<mqtt.opponent>_**).

You can either launch your code and wait for the message you're player 1 so start, or the other way round you're player 2 and wait for your opponent's move.

#### Debug

All logs are stored in files in Logs.
If you want to see your error output, go directly to Logs/debug.log; it will be all lines marked **_DEBUG ExecuteCommand lambda$executeCommand$1 -> YOUR DEBUG :_**.

<br>
<br>

---

# Tested in Java / Jar / Python / C++
