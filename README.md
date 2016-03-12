# Filecessor
Upload and processing images for your web projects

## Description
1. Uploading images via nginx upload progress module
2. Progress of uploading using nginx upload progress module
3. Dinamic resizing of images(configuration in nginx config), with caching
4. After upload images save to async queue in rabbitmq for handling
5. Handling images
    - fetch exif
    - search of dominant image colors
6. Expose rest api for photo resources

## Deploy

To install app you need docker, docker-compose.
### Create file docker-compose.override.yml to override basic configuration, example you can see in docker-compose.override.yml.dist
1. expose nginx port 80; made volumes, if you need
2. Set mongodb password via environment variable MONGO_PASSWD in db container
3. You can configure via environment variable in java container
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
    - RABBIT_CONCURRENCY (max quantity of concurrent consumers) 
    - APPLICATION_MEDIA_DIRECTORY_PATH (path to save photos in container)
    
