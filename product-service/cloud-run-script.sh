#!/bin/bash

# Load environment variables
source .env.cloudrun

# Deploy
gcloud run deploy product-service \
  --image gcr.io/${PROJECT_ID}/product-service:latest \
  --region ${REGION} \
  --allow-unauthenticated \
  --set-env-vars MONGODB_URI="${MONGODB_URI}"