# Event Analyzer

This project analyzes log file with several events occurrences and alert events with duration longer than 4 ms.
It is built using spring batch framework along spring boot and using HSQL file based db to store events.

## Compile
```
mvn clean install
```
Assembled jar will be generated on the standard folder `target`

## Run 
To execute and process a series of event logs run the following command:
```
java -Dinput.file.path="<FILE_PATH>" -jar target/eventanalyzer-1.0-SNAPSHOT.jar 

Optional parameters with default value
1. batch.size 1000
2. event.duration.threshold 4
```
### Output
* Generate no of events has taken more than 4 ms.
* Log event information if event is invalid.


## Next Steps
* Database parameters can be configurable.
* Add test cases to test job start to finish.
* Add test cases to ignore unformatted records.
* Add multithreading support to support parallel processing of large file.
 
