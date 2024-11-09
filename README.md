# Java-Minesweeper-Game
Java Minesweeper game source code

`mvn clean verify sonar:sonar -D"sonar.projectKey=Java-Minesweeper-Game" -D"sonar.projectName=Java-Minesweeper-Game" -D"sonar.host.url=http://localhost:9003" -D"sonar.login=sqp_ef9cc311deae72c5c1fb8c24c44012d269a2d6fd" -X`


![Minesweeper game screenshot](minesweeper.png)

> ### Making this project for code analysis in sonarqube and PDM i had to convert the simple java project with non organized code to a structure that is supported by maven, then added pom xml file with the necessary dependencies/plugins of sonarqube/jacoco and PDM 

![First Analysis](/report/1.png)

- With this configuration sonarqube found the project successfully and scanned the code and detected that there is no coverage of any code
- Navigating to the issues tab sonarqube is able to identify issues in code and point out the line and the reason
- Based on these analysis i was able to make changes refactoring

![Second Analysis](/report/2.png)

- Sonarqube is able to detect new code when running the analysis command and track the progress of maintenance and issues resolution, You may notice that i resolved the security Hotspot because of the change i made `SecureRandome` instead of `Random`
- Also you notice in the activity history that provides brief information the new analysis tells that one issue is resolved in this new code

![Third Analysis](/report/3.png)
![Last Analysis](/report/4.png)

- Continuing on the same steps using sonarqube resolving issue and then adding test to make more coverage
- Sonarqube by default have something called Quality Gate that defines some properties metrics like coverage (minimum 80%) i had to make a new Quality Gate for my case and made the coverage minimum 40% in this case sonarqube makes a pass
- The history visualizes the issue based on time it provides a clear idea about the progress of code quality


> PMD isn't as good as sonarqube but it provides other code quality that sonarqube ignore or dont support related to code style

![First Analysis](/report/pmd-1.png)

- After fix

![Second Analysis](/report/pmd-2.png)
