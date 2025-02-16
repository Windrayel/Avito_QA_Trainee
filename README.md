# Maven в командной строке
1. Склонируйте репозиторий с проектом тестового задания
```
git clone https://github.com/Windrayel/Avito_QA_Trainee
```

2. Убедитесь, что на вашем компьютере установлена java. 
```
java -version
```
Если java отсутвует на компьютере, установите её с официального [сайта oracle](https://www.oracle.com/java/technologies/downloads/).

3. Убедитесь, что на вашем компьютере установлен maven
```
mvn -v
```
Если maven отсутвует на компьютере, установите его с официального [сайта](https://maven.apache.org/download.cgi). Не забудьте добавить папку bin в перемунную окружения PATH.

4. Откройте каталог с проектом в командной строке
```
cd */Avito_QA_Trainee-master
```

5. Запустите тесты в проекте
```
mvn test
```
Maven самостоятельно установит все необходимые зависимости.

# Intellij Idea
1. Склонируйте репозиторий с проектом тестового задания
```
git clone https://github.com/Windrayel/Avito_QA_Trainee
```

2. Откройте репозиторий в Intellij Idea
3. Дождитесь загрузки всех зависимостей
4. Запустите тесты
