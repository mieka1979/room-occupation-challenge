
room-occupation-challenge

To build project please run 
./gradlew bootJar

To run application 
java -jar <jar generated in build/libs folder like RoomOccupancy-1.0-SNAPSHOT.jar>

Application stores test data in memory database. To test ruuning application please use ex.:

localhost:8080/room/occupancy?premium=2&economy=3

Currently project contains service tests in: 
https://github.com/mieka1979/room-occupation-challenge/blob/FirstDraft/src/test/java/com/roomoccupancy/service/OccupancyServiceTest.java
