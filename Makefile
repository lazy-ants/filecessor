all: install

install:
	sudo chmod a+w media && mvn -D skipTests=true package docker:build && docker-compose up -d

up:
	mvn -D skipTests=true package docker:build && docker-compose up -d

down:
	docker-compose down