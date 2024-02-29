# Static Imager
Provides a static link `http://localhost:7070/<service>` for images of the day for various service providers.
Supports bing and NASA APOD for now.  

### Compile
To build:
```shell
mvn clean package
```
To run (nasa api key optional, required only if using nasa apod)
```shell
java -jar static-imager.jar --nasa <nasa_apod_api_key> 
```
### Docker
Instructions at [docker hub](https://hub.docker.com/repository/docker/ramandeep89/static-imager/general)
