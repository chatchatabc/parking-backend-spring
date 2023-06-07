# build the project without testing
build:
  mvn -DskipTests clean package

# check dependencies updates
dependency-updates:
  mvn compile versions:display-dependency-updates > dependency-updates.txt

# display dependency tree
dependency-tree:
  mvn compile dependency:tree > dependency-tree.txt

# Redis cli for container
redis-cli:
  docker compose exec redis redis-cli

# PostgreSQL shell for container
psql:
  docker compose exec postgres psql -U postgres

# Flyway clean and migrate, and DBUnit operation
db-clean-migrate:
    mvn -f parking-domain/pom.xml flyway:clean
    mvn -f parking-domain/pom.xml flyway:migrate
    mvn -f parking-domain/pom.xml dbunit:operation

# Create a local pg dump and deploy to staging. Password is 'postgres'
pg-dump-staging:
    docker compose exec postgres pg_dump --clean -U postgres -d postgres -f /tmp/parking.sql
    docker compose cp postgres:/tmp/parking.sql /tmp/parking.sql
    scp -i ~/.ssh/id_rsa /tmp/parking.sql root@microservices.club:/tmp/parking.sql
    ssh -i ~/.ssh/id_rsa root@microservices.club "sudo psql -U postgres -h localhost -d postgres -f /tmp/parking.sql"


