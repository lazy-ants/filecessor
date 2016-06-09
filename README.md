# Filecessor [FC]

Upload and processing images for your web projects

## Description

- Uploading images and Progressing response with nginx upload progress module
- Dinamic resizing by specify actions urls, with caching
    - `original` - get original image
    - `resize`
    - `crop`
    - `coordinates` - crop image by given coordinates
    - [Transformations documention](https://github.com/lazy-ants/filecessor-transformation)
- After upload images save to async queue in rabbitmq for handling
    - fetch exif
    - search of dominant image colors
    - create pre-cache image for quick resize small images(less 1500px width)
- Expose REST API for photo resources

## Api

- Read API documentation about how to use the service http://docs.filecessor.apiary.io

## Install with Docker

- install [Docker](http://docker.com) and [Docker Compose](https://docs.docker.com/compose/)
- `git clone` this repo `somewhere/filecessor`
- copy `docker-compose.override.yml.dist` to `docker-compose.override.yml` for override basic configuration
- set mongodb password `MONGO_PASSWD` environment variable in `docker-compose.override.yml`
- [configuration additional variables](https://github.com/lazy-ants/filecessor/doc/configuration.md)
- run containers with `docker-composer up -d`
- open in browser [http://localhost/api/photos](http://localhost/api/photos) the Api endpoint with list of all uploaded photos (it's empty by default)
- read more from the [Api documentaion](http://docs.filecessor.apiary.io/#reference/get-photos/all-photos/show-all-paginated-photos)

## Known issues

- for Mac OS X users mondogDB volume do not works correctly, please comment out below line in `docker-compose.override.yml`

```yaml
db_data:
    volumes:
        # - ./volumes/mongodb:/data/db
```

- From the official Docker documentation https://hub.docker.com/_/mongo/

    **WARNING (Windows & OS X)**: The default Docker setup on Windows and OS X uses a VirtualBox VM to host the Docker daemon. Unfortunately, the mechanism VirtualBox uses to share folders between the host system and the Docker container is not compatible with the memory mapped files used by MongoDB (see [vbox bug](https://www.virtualbox.org/ticket/819), [docs.mongodb.org](https://docs.mongodb.org/manual/administration/production-notes/#fsync-on-directories) and related [jira.mongodb.org bug](https://jira.mongodb.org/browse/SERVER-8600)). This means that it is not possible to run a MongoDB container with the data directory mapped to the host.

- Solved in the Docker native for Mac users, in beta now [https://beta.docker.com](https://beta.docker.com/)

## Contributing

Filecessor is an open source project. If you find bugs or have proposal please create [issue](https://github.com/lazy-ants/filecessor/issues) or Pull Request
    
## License

All what you can find at this repo shared under the MIT License
