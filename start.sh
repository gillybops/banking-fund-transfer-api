#!/bin/bash

# Banking API Quick Start Script
# This script sets up and runs the Banking Fund Transfer API

set -e

echo "🏦 Banking Fund Transfer API - Quick Start"
echo "=========================================="
echo ""

# Check Java version
echo "📋 Checking prerequisites..."
if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Please install Java 21 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "❌ Java version $JAVA_VERSION found. Please install Java 21 or higher."
    exit 1
fi

echo "✅ Java $JAVA_VERSION detected"
echo ""

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven not found. Please install Maven 3.6+."
    exit 1
fi

echo "✅ Maven detected"
echo ""

# Build the project
echo "🔨 Building the project..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed. Please check the error messages above."
    exit 1
fi

echo "✅ Build successful!"
echo ""

# Run tests
echo "🧪 Running tests..."
mvn test

if [ $? -ne 0 ]; then
    echo "⚠️  Some tests failed. Check the output above."
    read -p "Continue anyway? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo "✅ Tests passed!"
echo ""

# Start the application
echo "🚀 Starting the Banking API..."
echo ""
echo "The application will be available at:"
echo "  🌐 API: http://localhost:8080"
echo "  📚 Swagger UI: http://localhost:8080/swagger-ui.html"
echo "  💾 H2 Console: http://localhost:8080/h2-console"
echo ""
echo "🔐 Default credentials:"
echo "  Admin: admin / admin"
echo "  User: user / password"
echo ""
echo "Press Ctrl+C to stop the server"
echo "=========================================="
echo ""

mvn spring-boot:run
