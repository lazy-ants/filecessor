## Configure additional environment variables

- front and handler container via such environment variables
    - `MONGO_HOST`
    - `MONGO_PORT`
    - `MONGO_DATABASE`
    - `MONGO_AUTHENTICATION_DATABASE`
    - `MONGO_USERNAME`
    - `MONGO_PASSWD`
    - `RABBIT_HOST`
    - `RABBIT_PORT`
    - `RABBIT_QUEUE_NAME`
    - `RABBIT_TOPIC_NAME`
    - `APPLICATION_MEDIA_ORIGINAL` (path to save photos)
- front container via environment variables
    - `APPLICATION_JWT_SECRET`
- handler container via environment variables
    - `RABBIT_CONCURRENCY` (max quantity of concurrent consumers)
    - `APPLICATION_EXIFTOOL_PATH` (path to exiftool lib)
    - `APPLICATION_MEDIA_REGULAR` (path to save pre-cache)
