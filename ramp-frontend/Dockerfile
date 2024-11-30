# Stage 1: Build
FROM node:20.13.1 AS build

WORKDIR /app

# Copy required files
COPY . .

RUN npm install

RUN npm run build

# Stage 2: Run
FROM node:20.13.1-alpine

WORKDIR /app

# Copy only the necessary files from the build stage
COPY --from=build /app/packages/web/build ./build

# Install a simple web server to serve the built files
RUN npm install -g serve

# Expose the port on which the app runs
EXPOSE 3000

# Command to run the app
CMD ["serve", "-s", "build"]
