cd docker
docker-compose stop hyl_db
docker-compose rm -f hyl_db
docker-compose up -d hyl_db
cd ..