### [План автоматизации](docs/Plan.md)  

### [Отчет по тестированию](docs/Report.md)

### [Отчет по автоматизации](docs/Summary.md)

**Настройка окружения:**  
1. Открыть проект в IDEA
2. Запустить виртуальную машину на Docker (если нет Docker, то скачать и установить)
3. В терминале ввести команду `docker-compose up`  

**Запуск приложения:**
1. *Для запуска с подключением к СУБД MySQL:*  
открыть новый терминал и ввести команду `java -jar ./artifacts/aqa-shop.jar --spring.datasource.url=jdbc:mysql://192.168.99.100:3306/app`
2. *Для запуска с подключением к СУБД PostgreSQL:*  
открыть новый терминал и ввести команду `java -jar ./artifacts/aqa-shop.jar --spring.datasource.url=jdbc:postgresql://192.168.99.100:5432/app`  

**Запуск автотестов:**  
1. *Для запуска с подключением к СУБД MySQL:*  
открыть еще один терминал и запустить тесты командой `./gradlew "-Dspring.datasource.url=jdbc:mysql://192.168.99.100:3306/app" clean test`
2. *Для запуска с подключением к СУБД PostgreSQL:*  
открыть еще один терминал и запустить тесты командой `./gradlew "-Dspring.datasource.url=jdbc:postgresql://192.168.99.100:5432/app" clean test`