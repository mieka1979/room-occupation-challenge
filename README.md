
room-occupation-challenge

To build project please run <br>
$./gradlew bootJar
<br><br>
To run application <br>
$java -jar build/libs/RoomOccupancy-1.0-SNAPSHOT.jar
<br><br>
Application stores test data in memory database. To test runing application please use exampled url.:
<br>
localhost:8080/room/occupancy?premium=2&economy=3
<br><br>
Currently project contains service tests in: 
https://github.com/mieka1979/room-occupation-challenge/blob/FirstDraft/src/test/java/com/roomoccupancy/service/OccupancyServiceTest.java

To run tests:
./gradlew test
