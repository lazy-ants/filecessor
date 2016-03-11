FROM dimka2014/nginx-upload-with-progress-modules

RUN mkdir -p /var/cache/nginx && chmod -R 777 /var/cache/nginx && rm -rf /etc/nginx/sites-enabled/*

ADD nginx.conf /etc/nginx/nginx.conf

RUN usermod -u 1000 www-data
