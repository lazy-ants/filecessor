CC = gcc

all: up

rebuild:
	mvn -D skipTests=true package docker:build && make up

up:
	docker-compose up -d

down:
	docker-compose down

permissions:
	sudo chmod a+w media volumes && sudo chmod a+s media volumes