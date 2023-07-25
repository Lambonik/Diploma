1. Открыть проект в IDEA
2. Запустить виртуальную машину на Docker (если нет Docker, то скачать и установить)
3. В терминале ввести команду `docker-compose up`
4. Открыть новый терминал и ввести команду `java -jar ./artifacts/aqa-shop.jar --spring.datasource.url=jdbc:mysql://192.168.99.100:3306/app`
5. Открыть еще один терминал и запустить тесты командой `./gradlew clean test --info`
6. По-умолчанию настроено подклбчение к СУБД MySQL. Для подключения к PostgreSQL необходимо в пункте 4 использовать компнду `java -jar ./artifacts/aqa-shop.jar --spring.datasource.url=jdbc:postgresql://192.168.99.100:5432/app`
7. После подключения к PostgreSQL, снова запустить тесты командой `./gradlew clean test --info`