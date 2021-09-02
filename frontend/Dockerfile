FROM node:12.14-alpine AS frontend-build-env
WORKDIR /app
RUN npm install -g @angular/cli@~9.0.2
COPY solarboat-app/package.json .
RUN npm install

FROM node:12.14-alpine AS frontend-build
WORKDIR /app
COPY --from=frontend-build-env /app .
RUN npm install -g @angular/cli@~9.0.2
COPY solarboat-app/ .
RUN ng build --prod

FROM nginx:alpine AS frontend
COPY --from=frontend-build /app/dist /usr/share/nginx/html