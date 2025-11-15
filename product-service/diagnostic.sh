#!/bin/bash

# Maven Build Diagnostic Script
# This script checks Java and Maven configuration



echo "=========================================="
echo "DIAGNOSTIC REPORT"
echo "=========================================="

echo -e "\n1. System Java:"
java -version

echo -e "\n2. JAVA_HOME:"
echo $JAVA_HOME
ls -la $JAVA_HOME 2>/dev/null || echo "JAVA_HOME path doesn't exist!"

echo -e "\n3. Maven Wrapper Java:"
./mvnw -version 2>&1

echo -e "\n4. Available Java versions:"
update-alternatives --list java

echo -e "\n5. Maven wrapper config:"
[ -f .mvn/jvm.config ] && cat .mvn/jvm.config || echo "No jvm.config"

echo -e "\n6. pom.xml Java version:"
grep -A 1 "<java.version>" pom.xml

echo -e "\n7. Maven compiler plugin:"
grep -A 20 "maven-compiler-plugin" pom.xml

echo "=========================================="
echo "END OF REPORT"
echo "=========================================="