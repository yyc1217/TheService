## The Service

### Prerequisites
- java 1.8 or above.

### Usage

1. modify ``src/resources/github.properties``, add **client_id** and **client_secret**.
2. modify ``src/resources/repos.txt``, add repositories that your want to monitor, it's a pair of github username and github repository name split by comma at each line.  For example 
```
username1,repository1
username2,repository2
```


#### Eclipse Project
``$ gradlew cleanEclipse eclipse``

#### War
``$ gradlew clean build``, **TheService-{version}-SNAPSHOT.war** will under build/libs.