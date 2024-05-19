# Static Imager
Provides a static link `http://localhost:7070/<service>` for images of the day for various service providers.
Supports bing and NASA APOD for now.  

## Compile
To build:
```shell
mvn clean package
```
To run (nasa api key optional, required only if using nasa apod)
```shell
java -jar static-imager.jar --nasa <nasa_apod_api_key> 
```
## Docker
```shell
docker run -d \
  --name static-imager \
  -e PUID=1000 \
  -e PGID=1000 \
  -p 7070:7070 \
  -e nasa=<your nasa api token> `#optional, if you want to use nasa apod` \
  --restart unless-stopped \
  ramandeep89/static-imager
```

## Usage
* To redirect to nasa apod (must provide your own nasa api key):

  `http://static-imager:7070/nasa`


* To redirect to bing image of the day:

  `http://static-imager:7070/bing`


* To generate own landscape (inspired by https://tyrellrummage.github.io/landscape/):

  `http://static-imager:7070/landscape`
  
  ###### Optional Query Params for landscape generator:
  * `width`: custom width, default: 1920
  * `height`: custom height, default: 1080
  * `hue`: custom hue, float (-1 or 0 or 0 < hue < 360)
    * -1 : random hue `http://static-imager:7070/landscape?hue=-1`
    * 0 : default hue `http://static-imager:7070/landscape`
    * 0 < hue < 360: specific hue `http://static-imager:7070/landscape?hue=2.34`
