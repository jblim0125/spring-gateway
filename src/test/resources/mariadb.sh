docker run -itd --name gateway-mariadb \
    --restart=unless-stopped \
    --env MARIADB_USER=gateway \
    --env MARIADB_PASSWORD=gateway-secret \
    --env MARIADB_DATABASE=gateway-database \
    --env MARIADB_ROOT_PASSWORD=root-secret \
    -p 3306:3306 \
    -v ${PWD}/mariadb-data:/var/lib/mysql \
    mariadb:latest

