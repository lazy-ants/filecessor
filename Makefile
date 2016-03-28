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