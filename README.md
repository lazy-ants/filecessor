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

## Deploy

To install app you need docker, docker-compose.

- Create file `docker-compose.override.yml` to override basic configuration, example you can see in `docker-compose.override.yml.dist`
- expose `nginx port 80`; made volumes, if you need
- Set mongodb password via environment variable `MONGO_PASSWD` in db container
- You can configure via environment variable in java container
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
    - `RABBIT_CONCURRENCY` (max quantity of concurrent consumers) 
    - `APPLICATION_MEDIA_DIRECTORY_PATH` (path to save photos in container)

## Contributing

Filecessor is an open source project. If you find bugs or have proposal please create [issue](https://github.com/lazy-ants/filecessor/issues) or Pull Request
    
## Lincense

All what you can find at this repo shared under the MIT License
