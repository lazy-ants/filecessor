CC = gcc

all: up

reinstall:
	mvn -D skipTests=true install

up:
	docker-compose up -d

down:
	docker-compose down

permissions:
	sudo chmod a+w media volumes && sudo chmod a+s media volumes

push:
	mvn -D skipTests=true install && docker push lazyants/filecessor-handler && docker push lazyants/filecessor-front

deploy:
	docker pull lazyants/filecessor-handler && docker pull lazyants/filecessor-front && docker-compose up -d && docker rmi $(docker images -f "dangling=true" -q)