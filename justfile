# build the project without testing
build:
  mvn -DskipTests clean package

# check oudated dependencies
outdated:
  mvn compile versions:display-dependency-updates > outdated.txt