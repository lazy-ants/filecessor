# Filecessor [FC]

Upload and processing images for your web projects

## Description

- Uploading images and Progressing response with nginx upload progress module
- Dinamic resizing by specify actions urls, with caching
    - `original` - get original image
    - `resize`
    - `crop`
    - `coordinates` - crop image by given coordinates
- After upload images save to async queue in rabbitmq for handling
    - fetch exif
    - search of dominant image colors
- Expose REST API for photo resources

## Api

- Read API documentation about how to use the service http://docs.filecessor.apiary.io

## Install with Docker

- install [Docker](http://docker.com) and [Docker Compose](https://docs.docker.com/compose/)
- copy `docker-compose.override.yml.dist` to `docker-compose.override.yml` for override basic configuration
- set mongodb password `MONGO_PASSWD` environment variable in `docker-compose.override.yml`
- you can configure front and handler container via such environment variables
    - `MONGO_HOST`
    - `MONGO_PORT`
    - `MONGO_DATABASE`
    - `MONGO_AUTHENTICATION_DATABASE`
    - `MONGO_USERNAME`
    - `MONGO_PASSWORD`
    - `RABBIT_HOST`
    - `RABBIT_PORT`
    - `RABBIT_QUEUE_NAME`
    - `RABBIT_TOPIC_NAME`
    - `APPLICATION_MEDIA_DIRECTORY_PATH` (path to save photos)
- you can configure front container via environment variables
    - `APPLICATION_JWT_SECRET`
    - `USERS_FILE` (path to file where json with users data is located)
- you can configure handler container via environment variables
    - `RABBIT_CONCURRENCY` (max quantity of concurrent consumers)
    - `APPLICATION_EXIFTOOL_PATH` (path to exiftool lib)
    
### Configure app users

By default app have one user with login and password `user1`, you can override users configuration by:

- create `users.json` file in such format 
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

- update `docker-compose.override.yml` file `front` container with volumes and environment variable `APPLICATION_USERS_FILE` to use file with users

```yaml
front:
    volumes:
        - ./users.json:/users.json
    environment:
        APPLICATION_USERS_FILE: "/users.json"
```

## Known issues

- for Mac OS X users mondogDB volume do not works correctly, please comment out below line in `docker-compose.override.yml`

```yaml
db_data:
    volumes:
        # - ./volumes/mongodb:/data/db
```

- From the official Docker documentation https://hub.docker.com/_/mongo/

    **WARNING (Windows & OS X)**: The default Docker setup on Windows and OS X uses a VirtualBox VM to host the Docker daemon. Unfortunately, the mechanism VirtualBox uses to share folders between the host system and the Docker container is not compatible with the memory mapped files used by MongoDB (see [vbox bug](https://www.virtualbox.org/ticket/819), [docs.mongodb.org](https://docs.mongodb.org/manual/administration/production-notes/#fsync-on-directories) and related [jira.mongodb.org bug](https://jira.mongodb.org/browse/SERVER-8600)). This means that it is not possible to run a MongoDB container with the data directory mapped to the host.


## Contributing

Filecessor is an open source project. If you find bugs or have proposal please create [issue](https://github.com/lazy-ants/filecessor/issues) or Pull Request
    
## License

All what you can find at this repo shared under the MIT License
