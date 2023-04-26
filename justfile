# build the project without testing
build:
  mvn -DskipTests clean package

# check oudated dependencies
outdated:
  mvn compile versions:display-dependency-updates > outdated.txt

# display dependency tree
dependency-tree:
  mvn compile dependency:tree > dependency-tree.txt