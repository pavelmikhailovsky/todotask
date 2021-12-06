***
### Project description
***This is the backend part of the todotask project.***
***Todotask is a project for creating tasks for a day / week or any other time.***
***

### Launch the application:

#### 1) Download and install docker
***[Linux](https://docs.docker.com/engine/install/) / [Windows](https://docs.docker.com/desktop/windows/install/)***

#### 2) Download and install docker compose

***[Docker compose download link](https://docs.docker.com/compose/install/)***

#### 3) Clone repository:
    git clone https://github.com/pavelmikhailovsky/todotask.git

#### 4) Get permission to connect to the Docker for Linux
    sudo chmod 666 /var/run/docker.sock
    sudo usermod -aG docker ${USER}

#### 5) Create docker image
    docker-compose -f docker-compose.dev.yml build

#### 6) Run docker container
    docker-compose -f docker-compose.dev.yml up

#### 7) Open a browser and enter
    http://localhost:8080/api/v1/swagger-ui/index.html?url=/api/v1/v3/api-docs/

### Run tests:

#### Build docker image and run container
    docker build -t spring-tests --target test .
