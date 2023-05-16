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
