# AI gamme programming MQTT

## Installation

Java 17

### To launch locally

#### Logger

```sh
mvn install:install-file -Dfile='src/main/libs/logger-0.0.1-SNAPSHOT.jar' -DgroupId='fr.phenix333' -DartifactId=logger -Dversion='0.0.1-SNAPSHOT' -Dpackaging=jar
```

#### Launch

```
Run as you like from the command line or ide
```

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

<br>
<br>

# Tested in Java / Jar / Python / C++
