# Initial Setup
[//]: # (TODO: Update Readme: order and correct information)
## Create version for raspi
### Jar
1. Use `mvn clean install` to generate correct jar-file in target folder

### Docker
1. Generate the jar file
2. Use `docker build --tag=language-generator:latest .` to generate the runnable docker-file
3. Use `docker run -p8573:2345 language-generator:latest` to start the application in a docker container

### Docker database
**The docker database needs to be running before the application can be started**
- Run this command to start the docker-container for the database: `docker run --name mysql --env MYSQL_DATABASE=languages --env MYSQL_PASSWORD=122 --env MYSQL_ROOT_PASSWORD=1234 --env MYSQL_USER=dummyUser --publish 3306:3306 --restart unless-stopped -v languages-volume:/var/lib/mysql mysql:latest`
  - Container name: `mysql`
  - Environment variables: *If you change any of them, make sure to adjust your `application.properties` file accordingly before compiling your jar*
    - MYSQL_DATABASE=languages
    - MYSQL_PASSWORD=123
    - MYSQL_ROOT_PASSWORD=1234
    - MYSQL_USER=dummyUser
  - Run options:
    - --publish 3306:3306
    - -v languages-volume:/var/lib/mysql
  - Image-id: `mysql:latest`
- Run the command to start the database in a docker-container
  - If you already have done this previously, you can use `docker start` instead

## Spring-Boot version
The spring-boot-version of this tool is the newer one with added functionality. It provides a web-ui that can be
accessed through your browser. The user therefore does not need to install anything new on their machine, and can just
connect to a server. The only disadvantage this version has, is that the setup is not as automated as the java-client
setup.

### Setup before build
Before running the application the first time, you need to have a database ready, where spring can connect to. The files
are set up to use a mysql-database in a docker-container, but you can change the properties to point to a different
database as well. But this guide is designed, to work with the provided setup, so if you change anything, you might need
to adjust a few steps. Additionally, this guide requires the use of IntelliJ for some steps.
#### Setup docker
This only needs to be done once before the first launch of the application.
1. Install docker if not already done
2. Download the mysql:latest release
3. Edit your IntelliJ run-configurations and make sure, that the "Docker Image" configuration has the correct docker server selected
4. Run the "Docker Image" configuration
#### Database pre-configuration
This only needs to be done once before the first launch of the application, if you keep your database. But if you reset
or change your database at some point, this step needs to be executed again.
1. Set up a database in IntelliJ that points to your docker-container
2. Execute the schema.sql file in the resources directory.

### Start the application
Launch the "Main Application" run-configuration. This is the only step that needs to be repeated on every restart of the
application. If you run the docker container and application locally, make sure that the docker container is started
before this step, otherwise the application will have no database to connect to. To simplify this double-launch, you can
use the "Spring with Docker" run-configuration, which will automatically launch your docker-container in addition to the
application.
#### Fill in default data
At the first launch, the database will be completely empty. If you want to add a default-language, you can execute the
data.sql file in the resources directory. This will create one default language that will be accessible by every user,
with the owner set to user "fi0x".
### Connect to your web-ui
With the default configuration, the web-ui is reachable on localhost:2345. If you don't change the config, an admin-user
"fi0x" with the password "123" will be created by the launch of the application. You can use it to log in to the web-ui.

## Runnable java-client - No longer developed
The java client is the original program that runs locally on your computer. It uses its own folder in your APPDATA
path to save individual language-files in a .json format. These files are compatible with the web-version of this tool.
The client however does not support a visual option like the web-ui to modify or add languages. This can only be done,
by changing the .json files directly.

### Build the client
The client version is no longer developed. If you want to compile it, use the code from
[v-1.0.1](https://github.com/Fi0x/LanguageGenerator/tree/0.0.1)

## RaspberryPi setup
- Install docker `sudo apt install docker`
- Add user to docker-group `sudo usermod -a -G docker ${USER}`
- Add docker to autostart `sudo systemctl enable docker`
- Run docker-command to start mysql container
- Run `schema.sql` to initialize the database
- In the jar-directory, create a `config` folder
- Copy the `application.properties` into the `config` folder
- Adjust the settings for the database in the `applications.properties` file
- Run the jar-file (not the one with dependencies, just the regular one)
- !!! Instead of the jar-file, you can also use mvn clean package to generate a war file, this might be working better