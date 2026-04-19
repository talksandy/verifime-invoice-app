#!/bin/bash

echo "Starting backend..."
cd backend && mvn quarkus:dev &

echo "Starting frontend..."
cd ../frontend && npm run dev