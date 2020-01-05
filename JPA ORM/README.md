To setup the postgres database in a postgres container:

>sudo apt-get install docker.io #Install docker
>docker run --name $container -e POSTGRES_PASSWORD=$password -p 5432:5432 -d postgres:alpine #Create container
>docker exec -it $container bash #Run your container
>bash-5.0# psql -U postgres #Log into psql as user "postgres"
>postgres=# CREATE DATABASE bankdb; #Create the database "bankdb"
>bash-5.0# psql \q #Exit psql
>exit #Exit bash