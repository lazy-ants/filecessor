# Filecessor
Upload and processing images for your web projects

## Description

- Uploading images via nginx upload progress module
- Progress of uploading using nginx upload progress module
- Dinamic resizing of images(configuration in nginx config), with caching
- After upload images save to async queue in rabbitmq for handling
- Handling images
    - fetch exif
    - search of dominant image colors
- Expose rest api for photo resources

## Api 

- http://docs.filecessor.apiary.io/

## Contributing

Filecessor is an open source project. If you find bugs or have proposal please create [issue](https://github.com/lazy-ants/filecessor/issues) or Pull Request
    
## License

All what you can find at this repo shared under the MIT License

## Deploy

To install app you need docker, docker-compose.


- create file docker-compose.override.yml to override basic configuration, example you can see in docker-compose.override.yml.dist
- expose nginx port 80; made volumes, if you need
- set mongodb password via environment variable in application containers and MONGO_PASSWD in db container
- you can configure front and handler container via such environment variables
    - MONGO_HOST
    - MONGO_PORT
    - MONGO_DATABASE
    - MONGO_AUTHENTICATION_DATABASE
    - MONGO_USERNAME
    - MONGO_PASSWORD
    - RABBIT_HOST
    - RABBIT_PORT
    - RABBIT_QUEUE_NAME
    - RABBIT_TOPIC_NAME
    - APPLICATION_MEDIA_DIRECTORY_PATH (path to save photos)
- you can configure front container via environment variables
    - APPLICATION_JWT_SECRET
    - USERS_FILE (path to file where json with users data is located)
- you can configure handler container via environment variables
    - RABBIT_CONCURRENCY (max quantity of concurrent consumers)
    - APPLICATION_EXIFTOOL_PATH (path to exiftool lib)
    
### Configure app users
By default app have 1 user with name and password "user1", you can override users configuration by that way:

1. create json file in such format:
```json
    [
      {
        "name": "user1",
        "password": "user1"
      },
      {
        "name": "user2",
        "password": "user2"
      }
    ]
```

2. add this file to front container and configure volume and application to use this file for example:
```
front:
    volumes:
        - ./users.json:/users.json
    environment:
        APPLICATION_USERS_FILE: "/users.json"
```
