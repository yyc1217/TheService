## The Service
> A github issues' title and content update hook event service.
Github webhook has several issue events, i.e. list, create, create comments, but lack of issue title and content update events. This service is a github webhook proxy which has all of github webhook events **plus** issue title and content update event.

### Prerequisites
- java 1.8 or above.

### Usage

1. modify ``src/resources/github.properties``, add **client_id** and **client_secret**.
2. modify ``src/resources/application.properties``, replace **target.url** for your target system.
3. modify ``src/resources/repos.txt``, add repositories that your want to monitor, it's a pair of github username and github repository name split by comma at each line, for example 
```
username1,repository1
username2,repository2
```


#### Eclipse Project
``$ gradlew cleanEclipse eclipse``

#### War
``$ gradlew clean build``, **TheService-{version}-SNAPSHOT.war** will under ``build/libs``.

#### Running project in place
``$ gradlew bootRun``, default port is ``80``.
