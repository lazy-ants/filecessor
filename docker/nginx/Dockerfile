FROM dimka2014/nginx-upload-with-progress-modules

ADD nginx.conf /etc/nginx/nginx.conf

RUN usermod -u 1000 www-data

VOLUME ["/media", "/var/tmp/uploads", "/var/cache/nginx", "/var/log/nginx"]