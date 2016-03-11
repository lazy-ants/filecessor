CC = gcc

all: up

rebuild:
	mvn -D skipTests=true package docker:build && make up

up:
	docker-compose up -d

down:
	docker-compose down
